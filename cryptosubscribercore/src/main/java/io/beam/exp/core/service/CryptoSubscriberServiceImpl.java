package io.beam.exp.core.service;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import io.beam.exp.core.outputStream.CryptoDataOutputStream;
import io.beam.exp.cryptorealtime.ExchangeQuoteInterface;
import io.beam.exp.cryptorealtime.XChangeStreamCoreQuoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.Quote;
import model.TradeEx;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
public class CryptoSubscriberServiceImpl implements CryptoSubscriberService {

    private final Map<String, ExchangeStub> exchangeStatusMap = Maps.newConcurrentMap();


    private final CryptoDataOutputStream<TradeEx> TradeExOutputStream;
    private final CryptoDataOutputStream<Quote> QuoteOutputStream;


    BlockingQueue<TradeEx> tradequeue = new LinkedBlockingQueue<>();
    BlockingQueue<Quote> quoteQueue = new LinkedBlockingQueue<>();

    ExecutorService executor = Executors.newCachedThreadPool();

    private static String getExchangeKey(String exchange, String baseCcy, String counterCcy){
        return String.format("%s_%s_%s",exchange,baseCcy,counterCcy);
    }

    private static String filterExchange(String exchange){
        if(exchange==null || exchange.length()==0){
            exchange="hitbtc";
        }
        return exchange;
    }

    @Override
    public void startSubscription(String exchange, String baseCcy, String counterCcy) {
        log.info("Start service");
        exchange = filterExchange(exchange);
        String key = getExchangeKey(exchange,baseCcy,counterCcy);
        if(exchangeStatusMap.containsKey(key)){
            ExchangeStub stub = exchangeStatusMap.get(key);
            if(stub.TurnOn){
                return;
            }else if(stub.TradeExStatus.equals("OK") && stub.QuoteStatus.equals("OK")){
                return;
            }
        }
        ExchangeStub exchStub = createSubscription(exchange, baseCcy, counterCcy);
        exchangeStatusMap.put(key, exchStub);
    }

    @Override
    public void stopSubscription(String exchange, String baseCcy, String counterCcy) {
        log.info("Stop service");
        exchange = filterExchange(exchange);
        Optional.of(exchangeStatusMap.get(getExchangeKey(exchange,baseCcy,counterCcy)))
                .ifPresent(exchangeStub->{
                    try {
                        exchangeStub.exInf.unsubscribe();
                        exchangeStub.TurnOn=false;
                        exchangeStub.TradeExStatus="STOP";
                        exchangeStub.QuoteStatus="STOP";
                    }catch(Exception ex){
                        log.error(ex.getMessage());
                    }
                });
    }

    @Override
    public List<Map<String,String>> listSubscription() {

        List<Map<String,String>> lst = exchangeStatusMap.keySet().stream().map(
                key->{
                    ExchangeStub stub = exchangeStatusMap.get(key);

                    return ImmutableMap.of(
                            "key", key,
                            "TurnOn", Boolean.toString(stub.TurnOn),
                            "QuoteStatus", stub.QuoteStatus,
                            "TradeExStatus", stub.TradeExStatus
                    );
                }
        ).collect(Collectors.toList());
        return lst;
    }
    @Override
    public Map<String, String> getSubscription(String exchange, String baseCcy, String counterCcy){
        exchange = filterExchange(exchange);
        String key = getExchangeKey(exchange,baseCcy,counterCcy);

        return Optional.ofNullable(exchangeStatusMap.get(key)).map(stub->ImmutableMap.of(
                "key", key,
                "TurnOn", Boolean.toString(stub.TurnOn),
                "QuoteStatus", stub.QuoteStatus,
                "TradeExStatus", stub.TradeExStatus
        )).orElse(ImmutableMap.<String, String>builder().build());
    }

    private class ExchangeStub{
        ExchangeQuoteInterface exInf;
        boolean TurnOn = true;
        String QuoteStatus = "WAIT";
        String TradeExStatus = "WAIT";
    }
    private ExchangeStub createSubscription(String exchange, String baseCcy, String counterCcy){
        ExchangeStub exchangeStub = new ExchangeStub();

        try{
            exchangeStub.exInf = XChangeStreamCoreQuoteService.of(exchange, baseCcy, counterCcy);
            exchangeStub.exInf.subscribe(
                    tradeex->{
                        try {
                            tradeex.setExchange(exchange);
                            if (exchangeStub.TurnOn)
                                tradequeue.put(tradeex);
                            log.info("TradeEx:"+tradeex.toString());
                        }catch(Exception ex) {
                            log.error(ex.getMessage());
                            exchangeStub.TradeExStatus = "FAIL_Subsribe";
                        }
                    },
                    q->{
                        try{
                            q.setExchange(exchange);
                            if (exchangeStub.TurnOn)
                                quoteQueue.put(q);
                            log.info("Quote:"+q.toString());
                        }catch(Exception ex){
                            log.error (ex.getMessage());
                            exchangeStub.QuoteStatus = "FAIL";
                        }
                    }
            );

            executor.execute(() -> {
                while (exchangeStub.TurnOn) {
                    try {
                        TradeEx t = tradequeue.take();
                        TradeExOutputStream.write(t);
                        exchangeStub.TradeExStatus = "OK";
                    } catch (Exception ex) {
                        log.error(ex.getMessage());
                        exchangeStub.TradeExStatus = "FAIL_Write";
                    }
                }
            });

            executor.execute(() -> {
                while (exchangeStub.TurnOn) {
                    try {
                        Quote q = quoteQueue.take();
                        QuoteOutputStream.write(q);
                        exchangeStub.QuoteStatus = "OK";
                    } catch (Exception ex) {
                        log.error(ex.getMessage());
                        exchangeStub.QuoteStatus = "FAIL_Write";
                    }
                }
            });

        }catch (Exception ex) {
            log.error(String.format("%s-%s/%s:%s",exchange, baseCcy,counterCcy,ex.getMessage()));
        }
        return exchangeStub;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        executor.shutdown();
        try {
            if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
    }
}
