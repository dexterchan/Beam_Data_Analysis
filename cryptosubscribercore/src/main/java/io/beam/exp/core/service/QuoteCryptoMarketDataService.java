package io.beam.exp.core.service;

import io.beam.exp.core.service.model.Subscription;
import io.beam.exp.cryptorealtime.ExchangeInterface;
import model.Quote;

public class QuoteCryptoMarketDataService extends AbstractCryptoMarketDataService<Quote> {
    @Override
    void subscribe(ExchangeInterface exchangeInterface, Subscription subscription ) {
        exchangeInterface.subscribeQuote(
                quote -> {
                    subscription.notifyOservers(quote);
                },
                throwable ->{
                    subscription.notifyObservers(throwable);
                }
        );
    }
}
