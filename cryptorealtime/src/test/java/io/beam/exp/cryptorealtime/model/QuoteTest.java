package io.beam.exp.cryptorealtime.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuoteTest {

    @Test
    void getKey() {
        String k = "BTC/\\USD";
        String nk = k.replaceAll("/|\\\\","");
        assertEquals(nk,"BTCUSD");
    }
}