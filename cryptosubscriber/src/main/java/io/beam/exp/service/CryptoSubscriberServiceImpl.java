package io.beam.exp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CryptoSubscriberServiceImpl implements CryptoSubscriberService {
    @Override
    public void startSubscription() {
        log.info("Start service");
    }

    @Override
    public void stopSubscription() {
        log.info("Stop service");
    }
}
