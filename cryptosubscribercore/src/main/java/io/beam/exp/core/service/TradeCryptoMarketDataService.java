package io.beam.exp.core.service;

import io.beam.exp.core.observe.Subject;
import io.beam.exp.cryptorealtime.ExchangeInterface;
import io.beam.exp.cryptorealtime.model.TradeEx;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TradeCryptoMarketDataService extends AbstractCryptoMarketDataService<TradeEx> {
    @Override
    void subscribe(ExchangeInterface exchangeInterface, Subject subject) {
        exchangeInterface.subscribeTrade(
                tradeEx -> subject.notifyOservers(tradeEx),
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
}
