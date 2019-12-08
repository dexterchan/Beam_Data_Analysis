package io.beam.exp.outputstream;

import io.beam.exp.cryptorealtime.model.Quote;

public interface CryptoDataOutputStream<T>{
    void write(T q) throws Exception;
}
