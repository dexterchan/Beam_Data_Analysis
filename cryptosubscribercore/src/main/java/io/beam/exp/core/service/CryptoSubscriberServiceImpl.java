package io.beam.exp.core.service;

import com.google.common.collect.Maps;
import io.beam.exp.core.observe.Observer;
import io.beam.exp.core.service.model.MarketData;
import io.beam.exp.core.service.model.Subscription;
import io.beam.exp.cryptorealtime.ExchangeInterface;
import io.beam.exp.cryptorealtime.XChangeStreamCoreQuoteService;
import lombok.extern.slf4j.Slf4j;
import model.Quote;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Slf4j
public class CryptoSubscriberServiceImpl implements  CryptoSubscriberService<Object> {
    private class ExchangeMarketData{
        MarketData marketData;
        ExchangeInterface exchangeInterface;
    }
    private final Map<String, ExchangeMarketData> exchangeMarketDataMap = Maps.newConcurrentMap();
    private Map<Class, Set<Observer<Quote>>> observerSetMap = Maps.newConcurrentMap();

    private static String getExchangeKey(String exchange, String baseCcy, String counterCcy){
        exchange = filterExchange(exchange);
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

        String key = getExchangeKey(exchange,baseCcy,counterCcy);
        if (exchangeMarketDataMap.containsKey(key)){
            throw new IllegalStateException(String.format("%s was in service", key));
        }
        createSubscription( exchange,  baseCcy,  counterCcy);
    }

    @Override
    public void stopSubscription(String exchange, String baseCcy, String counterCcy) {
        log.info("Stop service");
        exchange = filterExchange(exchange);
        /*
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
                });*/
    }

    @Override
    public List<Map<String,String>> listSubscription() {
        List<Map<String,String>> lst =null;
/*
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
        ).collect(Collectors.toList());*/

        return lst;
    }
    @Override
    public Map<String, String> getSubscription(String exchange, String baseCcy, String counterCcy){
        String key = getExchangeKey(exchange,baseCcy,counterCcy);
/*
        return Optional.ofNullable(exchangeStatusMap.get(key)).map(stub->ImmutableMap.of(
                "key", key,
                "TurnOn", Boolean.toString(stub.TurnOn),
                "QuoteStatus", stub.QuoteStatus,
                "TradeExStatus", stub.TradeExStatus
        )).orElse(ImmutableMap.<String, String>builder().build());*/
        return null;
    }


    public void injectQuoteObserver(Class c, Observer<Quote> observer){
        this.observerSetMap.putIfAbsent(c, new HashSet<>());
        Set<Observer<Quote>> observerSet = this.observerSetMap.get(c);
        observerSet.add(observer);
    }

    @Override
    public void injectQuoteObserver(Observer<Object> observer) {

    }

    private void createSubscription(String exchange, String baseCcy, String counterCcy){

        try{
            ExchangeMarketData exchangeMarketData = new ExchangeMarketData();
            String key = getExchangeKey(exchange,baseCcy,counterCcy);
            ExchangeInterface exchangeInterface= XChangeStreamCoreQuoteService.of(exchange, baseCcy, counterCcy);
            MarketData marketData = MarketData.createMarketData(exchange, baseCcy, counterCcy);

            exchangeMarketData.exchangeInterface = exchangeInterface;
            exchangeMarketData.marketData = marketData;

            observerSetMap.forEach(
                    (objectClass, observerSet)->{
                        marketData.addSubscription(objectClass);
                        Subscription subscription = marketData.getSubscription(objectClass);
                        observerSet.forEach(
                                observer -> {
                                    exchangeInterface.subscribeQuote(
                                            quote->{
                                                observer.update(quote);
                                            },
                                            ex->{
                                                observer.throwError(ex);
                                            }
                                    );
                                    subscription.registerObserver(observer);
                                }
                        );
                    }
            );
            exchangeMarketDataMap.put(key, exchangeMarketData);

        }catch (Exception ex) {
            log.error(String.format("%s-%s/%s:%s",exchange, baseCcy,counterCcy,ex.getMessage()));
        }
        return ;
    }


}
