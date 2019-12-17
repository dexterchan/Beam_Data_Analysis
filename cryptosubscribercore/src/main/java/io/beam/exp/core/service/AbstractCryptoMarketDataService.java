package io.beam.exp.core.service;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.beam.exp.core.observe.Observer;
import io.beam.exp.core.observe.Subject;
import io.beam.exp.core.service.model.Subscription;
import io.beam.exp.cryptorealtime.ExchangeInterface;
import io.beam.exp.cryptorealtime.XChangeStreamCoreQuoteService;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public abstract class AbstractCryptoMarketDataService<T> implements CryptoMarketDataService<T> {


    private class ExchangeMarketData {
        Subject<T> subject;
        ExchangeInterface exchangeInterface;

        void createSubject(String exchange, String baseCcy, String counterCcy, String dataName){
            this.subject = new Subscription(exchange, baseCcy, counterCcy, dataName);
            //exchangeMarketData.subject.
            this.subject.setActive(true);
        }
    }

    private final Map<String, ExchangeMarketData> exchangeMarketDataMap = Maps.newConcurrentMap();
    private Set<Observer<T>> observerSet = Sets.newConcurrentHashSet();

    protected String getExchangeKey(String exchange, String baseCcy, String counterCcy) {
        exchange = filterExchange(exchange);
        return String.format("%s_%s_%s", exchange, baseCcy, counterCcy);
    }

    private static String filterExchange(String exchange) {
        if (exchange == null || exchange.length() == 0) {
            exchange = "hitbtc";
        }
        return exchange;
    }

    @Override
    public void injectObserver(Observer<T> observer) {
        observerSet.add(observer);
    }

    @Override
    public void startSubscription(String exchange, String baseCcy, String counterCcy) {
        log.info("Start service");

        String key = getExchangeKey(exchange, baseCcy, counterCcy);
        if (exchangeMarketDataMap.containsKey(key)) {
            throw new IllegalStateException(String.format("%s was in service", key));
        }
        createSubscription(exchange, baseCcy, counterCcy);
    }

    @Override
    public void stopSubscription(String exchange, String baseCcy, String counterCcy) {
        log.info("Stop service");
        String key = getExchangeKey(exchange, baseCcy, counterCcy);
        if(exchangeMarketDataMap.containsKey(key)){
            ExchangeMarketData exchangeMarketData = exchangeMarketDataMap.get(key);
            this.unsubscribe(exchangeMarketData.exchangeInterface, exchangeMarketData.subject);
            exchangeMarketDataMap.remove(key);
        }
    }

    @Override
    public List<Map<String, String>> listSubscription() {
        List<Map<String, String>> res = exchangeMarketDataMap.keySet().stream().map(
                (key)->exchangeMarketDataMap.get(key).subject.getDescription()
        ).collect(Collectors.toList());
        return res;
    }

    @Override
    public Map<String, String> getSubscription(String exchange, String baseCcy, String counterCcy) {
        String key = getExchangeKey(exchange, baseCcy, counterCcy);
        if(exchangeMarketDataMap.containsKey(key)){
            ExchangeMarketData exchangeMarketData = exchangeMarketDataMap.get(key);
            return exchangeMarketData.subject.getDescription();
        }
        return new HashMap<>();
    }

    abstract void subscribe(ExchangeInterface exchangeInterface, Subject subject);
    abstract void unsubscribe(ExchangeInterface exchangeInterface, Subject subject);
    abstract String getDataName();

    private void createSubscription(String exchange, String baseCcy, String counterCcy) {
        try {
            ExchangeMarketData exchangeMarketData = new ExchangeMarketData();
            String key = getExchangeKey(exchange, baseCcy, counterCcy);
            ExchangeInterface exchangeInterface = XChangeStreamCoreQuoteService.of(exchange, baseCcy, counterCcy);

            exchangeMarketData.exchangeInterface = exchangeInterface;
            exchangeMarketData.createSubject(exchange, baseCcy, counterCcy, getDataName());

            if (observerSet.size() == 0) {
                throw new IllegalStateException("No observer inserted!");
            }

            observerSet.forEach(
                    observer -> {
                        subscribe(exchangeInterface, exchangeMarketData.subject);
                        exchangeMarketData.subject.registerObserver(observer);
                    }
            );

            exchangeMarketDataMap.put(key, exchangeMarketData);

        } catch (Exception ex) {
            log.error(String.format("%s-%s/%s:%s", exchange, baseCcy, counterCcy, ex.getMessage()));
            throw ex;
        }
        return;
    }
}
