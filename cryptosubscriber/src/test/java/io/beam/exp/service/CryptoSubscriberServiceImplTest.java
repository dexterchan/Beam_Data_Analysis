package io.beam.exp.service;

import io.beam.exp.outputstream.QuoteFireBaseOutputStream;
import io.beam.exp.outputstream.TradeExFireBaseOutputStream;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

@Tag("integration")
class CryptoSubscriberServiceImplTest {
    Logger log = LoggerFactory.getLogger(CryptoSubscriberServiceImplTest.class);

    @Test
    void startSubscription() throws Exception{
        log.debug("Running SubsriptionService start");
        CryptoSubscriberService svc = new CryptoSubscriberServiceImpl(
                new TradeExFireBaseOutputStream(),
                new QuoteFireBaseOutputStream()
        );
        svc.startSubscription("","BTC","USD");
        Thread.sleep(5000);

    }

    @Test
    void stopSubscription() {
        log.debug("Running SubsriptionService stop");
    }
}