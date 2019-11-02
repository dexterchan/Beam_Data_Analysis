package io.beam.exp.service;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import info.bitrich.xchangestream.hitbtc.HitbtcStreamingExchange;
import io.beam.exp.cryptorealtime.ExchangeQuoteInterface;
import io.beam.exp.cryptorealtime.XChangeStreamCoreQuoteService;
import io.beam.exp.outputstream.QuoteFireBaseOutputStream;
import io.beam.exp.outputstream.TradeExFireBaseOutputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.Quote;
import model.TradeEx;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.function.Consumer;

@Service
@Slf4j
@RequiredArgsConstructor
public class CryptoSubscriberServiceImpl implements CryptoSubscriberService {

    private final Map<String, ExchangeStub> exchangeStatusMap = Maps.newConcurrentMap();

    private final TradeExFireBaseOutputStream TradeExOutputStream;
    private final QuoteFireBaseOutputStream QuoteOutputStream;


    BlockingQueue<TradeEx> tradequeue = new LinkedBlockingQueue<>();
    BlockingQueue<Quote> quoteQueue = new LinkedBlockingQueue<>();

    ExecutorService executor = Executors.newCachedThreadPool();

    private static String getExchangeKey(String exchange, String baseCcy, String counterCcy){
        return String.format("%s_%s_%s",exchange,baseCcy,counterCcy);
    }

    private final Map<String, String> exchangeClassMap = ImmutableMap.of(
            "hitbtc",HitbtcStreamingExchange.class.getName());

    @Override
    public void startSubscription(String exchange, String baseCcy, String counterCcy) {
        log.info("Start service");

        ExchangeStub exchStub = createSubscription("hitbtc", baseCcy, counterCcy);
        exchangeStatusMap.put(getExchangeKey(exchange,baseCcy,counterCcy), exchStub);

    }

    @Override
    public void stopSubscription(String exchange, String baseCcy, String counterCcy) {
        log.info("Stop service");
        String exchName = Optional.of(exchangeClassMap.get("hitbtc")).get();

    }

    private class ExchangeStub{
        ExchangeQuoteInterface exInf;
        boolean TurnOn = true;
        String QuoteStatus = "WAIT";
        String TradeExStatus = "WAIT";
    }
    private ExchangeStub createSubscription(String exchange, String baseCcy, String counterCcy){
        ExchangeStub exchangeStub = new ExchangeStub();
        String exchName = Optional.of(exchangeClassMap.get(exchange)).get();
        try{
            exchangeStub.exInf = XChangeStreamCoreQuoteService.of(exchName, baseCcy, counterCcy);
            exchangeStub.exInf.subscribe(
                    tradeex->{
                        try {
                            tradeex.setExchange(exchange);
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
