package io.beam.exp.core.outputStream;

public interface CryptoDataOutputStream<T>{
    void write(T q) throws Exception;
}
