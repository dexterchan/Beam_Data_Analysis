package io.beam.exp.blockchaininfo.outputstream;

@FunctionalInterface
public interface BlockchaininfoOutputStream<T>{
    void write(T q) throws Exception;
}
