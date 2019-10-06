package io.beam.exp.cryptorealtime;

import model.Quote;

import java.util.function.Supplier;

public interface ExchangeQuoteInterface extends AutoCloseable {

    public void subscribe(Supplier<Quote> handle);

}
