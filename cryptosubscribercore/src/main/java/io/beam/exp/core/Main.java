package io.beam.exp.core;

import io.beam.exp.core.outputStream.QuoteFireBaseOutputStream;
import io.beam.exp.core.outputStream.TradeExFireBaseOutputStream;
import io.beam.exp.core.service.CryptoSubscriberService;
import io.beam.exp.core.service.CryptoSubscriberServiceImpl;

public class Main {
    public static void main(String args[]) throws Exception{

        CryptoSubscriberService cryptoSubscriberService = new CryptoSubscriberServiceImpl(
                new TradeExFireBaseOutputStream(),new QuoteFireBaseOutputStream());
        cryptoSubscriberService.startSubscription("","BTC","USD");
    }
}
