package io.beam.exp.core.service;

import io.beam.exp.core.observe.Observer;

import java.util.List;
import java.util.Map;

public interface CryptoSubscriberService<T> {
    public void startSubscription(String exchange, String baseCcy, String counterCcy);
    public void stopSubscription(String exchange, String baseCcy, String counterCcy);
    public List<Map<String,String>> listSubscription();
    public Map<String, String> getSubscription(String exchange, String baseCcy, String counterCcy);

    public void injectObserver(Observer<T> observer);
}
