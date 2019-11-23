package io.beam.exp.core;

import io.beam.exp.core.outputStream.bigquery.CryptoDataBigQueryOutputStream;
import io.beam.exp.core.outputStream.firebase.QuoteFireBaseOutputStream;
import io.beam.exp.core.outputStream.firebase.TradeExFireBaseOutputStream;
import io.beam.exp.core.service.CryptoSubscriberService;
import io.beam.exp.core.service.CryptoSubscriberServiceImpl;
import model.Quote;
import model.TradeEx;

public class Main {
    public static void main(String args[]) throws Exception{

        String TradeTable = "CryptoTrade";
        String QuoteTable = "CryptoQuote";
        CryptoSubscriberService cryptoSubscriberService = new CryptoSubscriberServiceImpl(
                new CryptoDataBigQueryOutputStream<TradeEx>(TradeEx.class, TradeTable),
                new CryptoDataBigQueryOutputStream<Quote>(Quote.class, QuoteTable)
        );
        cryptoSubscriberService.startSubscription("","BTC","USD");
    }
}
