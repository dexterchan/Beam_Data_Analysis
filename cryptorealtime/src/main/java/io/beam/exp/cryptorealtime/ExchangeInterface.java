package io.beam.exp.cryptorealtime;

import io.beam.exp.cryptorealtime.model.Quote;
import io.beam.exp.cryptorealtime.model.TradeEx;

import java.util.function.Consumer;

public interface ExchangeInterface extends AutoCloseable {

    public void subscribeQuote(Consumer<Quote> quote, Consumer<Throwable> exception);
    public void subscribeTrade(Consumer<TradeEx> trade, Consumer<Throwable> exception);

    public void unsubscribe() throws Exception;
}
