package io.beam.exp.core.service;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.beam.exp.core.observe.Observer;
import io.beam.exp.core.service.model.Subscription;
import io.beam.exp.core.service.model.SubscriptionStatus;
import io.beam.exp.cryptorealtime.ExchangeInterface;
import io.beam.exp.cryptorealtime.XChangeStreamCoreQuoteService;
import lombok.extern.slf4j.Slf4j;
import model.Quote;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

@Slf4j
public abstract class AbstractCryptoMarketDataService<T> implements CryptoSubscriberService<T> {

    private class ExchangeMarketData {
        Subscription<T> subscription;
        ExchangeInterface exchangeInterface;
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

    public void injectQuoteObserver(Observer<T> observer) {
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
            try {
                exchangeMarketData.exchangeInterface.unsubscribe();
                exchangeMarketData.subscription.setSubscriptionStatus(SubscriptionStatus.STOP);
            }catch(Exception ex){
                log.error(ex.getMessage());
            }
        }
    }

    @Override
    public List<Map<String, String>> listSubscription() {
        return null;
    }

    @Override
    public Map<String, String> getSubscription(String exchange, String baseCcy, String counterCcy) {
        return null;
    }

    abstract void subscribe(ExchangeInterface exchangeInterface, Subscription subscription);

    private void createSubscription(String exchange, String baseCcy, String counterCcy) {
        try {
            ExchangeMarketData exchangeMarketData = new ExchangeMarketData();
            String key = getExchangeKey(exchange, baseCcy, counterCcy);
            ExchangeInterface exchangeInterface = XChangeStreamCoreQuoteService.of(exchange, baseCcy, counterCcy);

            exchangeMarketData.exchangeInterface = exchangeInterface;
            exchangeMarketData.subscription = new Subscription(exchange, baseCcy, counterCcy);
            ;

            if (observerSet.size() == 0) {
                throw new IllegalStateException("No observer inserted!");
            }

            observerSet.forEach(
                    observer -> {
                        subscribe(exchangeInterface, exchangeMarketData.subscription);
                        exchangeMarketData.subscription.registerObserver(observer);
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
