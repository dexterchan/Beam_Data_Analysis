package io.beam.exp.core.service;

import java.util.List;
import java.util.Map;

public interface CryptoSubscriberService {
    public void startSubscription(String exchange, String baseCcy, String counterCcy);
    public void stopSubscription(String exchange, String baseCcy, String counterCcy);
    public List<Map<String,String>> listSubscription();
}
