package io.beam.exp.cryptorealtime;

import model.OrderBook;
import model.Quote;
import model.TradeEx;

import java.util.function.Consumer;

public interface ExchangeQuoteInterface extends AutoCloseable {

    public void subscribe(Consumer<TradeEx> handle,Consumer<Quote> ohandle);


    public void unsubscribe() throws Exception;
}
