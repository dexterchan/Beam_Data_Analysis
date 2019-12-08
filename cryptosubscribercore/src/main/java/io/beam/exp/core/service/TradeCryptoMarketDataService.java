package io.beam.exp.core.service;

import io.beam.exp.core.observe.Subject;
import io.beam.exp.cryptorealtime.ExchangeInterface;
import model.TradeEx;

public class TradeCryptoMarketDataService extends AbstractCryptoMarketDataService<TradeEx> {
    @Override
    void subscribe(ExchangeInterface exchangeInterface, Subject subject) {
        exchangeInterface.subscribeTrade(
                tradeEx -> subject.notifyOservers(tradeEx),
                throwable -> subject.notifyObservers(throwable)
        );
    }
}
