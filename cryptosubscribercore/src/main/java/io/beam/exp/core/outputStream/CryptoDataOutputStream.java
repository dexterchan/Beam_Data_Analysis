package io.beam.exp.core.outputStream;
@FunctionalInterface
public interface CryptoDataOutputStream<T>{
    void write(T q) throws Exception;
}
