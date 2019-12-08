package io.beam.exp.core.factory;

import io.beam.exp.core.service.CryptoSubscriberService;
import io.beam.exp.cryptorealtime.model.Quote;
import io.beam.exp.cryptorealtime.model.TradeEx;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
@Tag("integration")
class GCP_CryptoSubscriberServiceFactoryTest {

    static String quoteTable = "QuoteTest";
    static String tradeTable = "TradeExTest";

    static AbstractCryptoSubscriberServiceFactory cryptoSubscriberServiceFactory = null;

    @BeforeAll
    static void init(){
        cryptoSubscriberServiceFactory = new GCP_CryptoSubscriberServiceFactory(quoteTable, tradeTable);
    }

    @Test
    void createQuoteService() throws Exception{

        CryptoSubscriberService<Quote>  cryptoSubscriberService = cryptoSubscriberServiceFactory.createQuoteService();
        cryptoSubscriberService.startSubscription("hitbtc","BTC","USD");

        Thread.sleep(1000*10);
    }

    @Test
    void createTradeService() throws Exception{
       CryptoSubscriberService<TradeEx> cryptoSubscriberService = cryptoSubscriberServiceFactory.createTradeService();
       cryptoSubscriberService.startSubscription("hitbtc","BTC","USD");
       Thread.sleep(1000*10);
    }
}