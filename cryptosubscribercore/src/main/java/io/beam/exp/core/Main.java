package io.beam.exp.core;

import io.beam.exp.core.factory.AbstractCryptoMarketDataServiceFactory;
import io.beam.exp.core.service.GCP_CryptoMarketDataServiceFactory;
import io.beam.exp.core.service.CryptoMarketDataService;
import io.beam.exp.cryptorealtime.model.Quote;
import io.beam.exp.cryptorealtime.model.TradeEx;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {
    static String quoteTable = "CryptoQuote";
    static String tradeTable = "CryptoTrade";

    static AbstractCryptoMarketDataServiceFactory cryptoSubscriberServiceFactory=null;
    static{
        if (System.getenv("QUOTETABLE") != null) {
            quoteTable = System.getenv("QUOTETABLE");
        }
        if (System.getenv("TRADEEXTABLE") != null) {
            tradeTable = System.getenv("TRADEEXTABLE");
        }
        cryptoSubscriberServiceFactory = new GCP_CryptoMarketDataServiceFactory(quoteTable, tradeTable);
    }
    public static void main(String args[]) throws Exception{
        String ccy = "LTC";
        if (args.length>0){
            ccy = args[0];
        }
        log.info("Market data for {}", ccy);
        CryptoMarketDataService<Quote> quoteCryptoMarketDataService = cryptoSubscriberServiceFactory.createQuoteService();
        quoteCryptoMarketDataService.startSubscription("hitbtc",ccy,"USD");

        CryptoMarketDataService<TradeEx> tradeExCryptoMarketDataService = cryptoSubscriberServiceFactory.createTradeService();
        tradeExCryptoMarketDataService.startSubscription("hitbtc",ccy,"USD");
    }
}
