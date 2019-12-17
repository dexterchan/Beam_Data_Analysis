package io.beam.exp.core.service;

import io.beam.exp.core.observe.Subject;
import io.beam.exp.cryptorealtime.ExchangeInterface;
import io.beam.exp.cryptorealtime.model.Quote;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class QuoteCryptoMarketDataService extends AbstractCryptoMarketDataService<Quote> {

    QuoteCryptoMarketDataService(){}

    @Override
    void subscribe(ExchangeInterface exchangeInterface, Subject subject ) {
        exchangeInterface.subscribeQuote(
                quote -> subject.notifyOservers(quote),
                throwable -> subject.notifyObservers(throwable)
        );
    }

    @Override
    void unsubscribe(ExchangeInterface exchangeInterface, Subject subject) {
        try {
            exchangeInterface.unsubscribe();
        }catch(Exception ex){
            log.error(ex.getMessage());
        }
    }

    @Override
    String getDataName() {
        return "QUOTE";
    }
}
