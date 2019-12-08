package io.beam.exp.core.service;

import io.beam.exp.core.observe.Subject;
import io.beam.exp.cryptorealtime.ExchangeInterface;
import io.beam.exp.cryptorealtime.model.Quote;

public class QuoteCryptoMarketDataService extends AbstractCryptoMarketDataService<Quote> {
    @Override
    void subscribe(ExchangeInterface exchangeInterface, Subject subject ) {
        exchangeInterface.subscribeQuote(
                quote -> subject.notifyOservers(quote),
                throwable -> subject.notifyObservers(throwable)
        );
    }
}
