package io.beam.exp.core;

import io.beam.exp.core.factory.AbstractCryptoMarketDataServiceFactory;
import io.beam.exp.core.service.GCP_CryptoMarketDataServiceFactory;
import io.beam.exp.core.service.CryptoMarketDataService;
import io.beam.exp.cryptorealtime.model.Quote;
import io.beam.exp.cryptorealtime.model.TradeEx;

public class Main {
    static String quoteTable = "QuoteTest";
    static String tradeTable = "TradeExTest";

    static AbstractCryptoMarketDataServiceFactory cryptoSubscriberServiceFactory=null;
    static{
        cryptoSubscriberServiceFactory = new GCP_CryptoMarketDataServiceFactory(quoteTable, tradeTable);
    }
    public static void main(String args[]) throws Exception{

        CryptoMarketDataService<Quote> quoteCryptoMarketDataService = cryptoSubscriberServiceFactory.createQuoteService();
        quoteCryptoMarketDataService.startSubscription("hitbtc","BTC","USD");

        CryptoMarketDataService<TradeEx> tradeExCryptoMarketDataService = cryptoSubscriberServiceFactory.createTradeService();
        tradeExCryptoMarketDataService.startSubscription("hitbtc","BTC","USD");
    }
}
