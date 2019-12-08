package io.beam.exp.core;

import io.beam.exp.core.factory.AbstractCryptoSubscriberServiceFactory;
import io.beam.exp.core.factory.GCP_CryptoSubscriberServiceFactory;
import io.beam.exp.core.service.CryptoSubscriberService;
import io.beam.exp.cryptorealtime.model.Quote;
import io.beam.exp.cryptorealtime.model.TradeEx;

public class Main {
    static String quoteTable = "QuoteTest";
    static String tradeTable = "TradeExTest";

    static AbstractCryptoSubscriberServiceFactory cryptoSubscriberServiceFactory=null;
    static{
        cryptoSubscriberServiceFactory = new GCP_CryptoSubscriberServiceFactory(quoteTable, tradeTable);
    }
    public static void main(String args[]) throws Exception{

        CryptoSubscriberService<Quote> quoteCryptoSubscriberService = cryptoSubscriberServiceFactory.createQuoteService();
        quoteCryptoSubscriberService.startSubscription("hitbtc","BTC","USD");

        CryptoSubscriberService<TradeEx> tradeExCryptoSubscriberService = cryptoSubscriberServiceFactory.createTradeService();
        tradeExCryptoSubscriberService.startSubscription("hitbtc","BTC","USD");
    }
}
