package io.beam.exp.outputstream;

import model.Quote;

public interface CryptoDataOutputStream<T>{
    void write(T q) throws Exception;
}
