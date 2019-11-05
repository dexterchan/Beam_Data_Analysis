package io.beam.exp.core.service;

public interface CryptoSubscriberService {
    public void startSubscription(String exchange, String baseCcy, String counterCcy);
    public void stopSubscription(String exchange, String baseCcy, String counterCcy);

}
