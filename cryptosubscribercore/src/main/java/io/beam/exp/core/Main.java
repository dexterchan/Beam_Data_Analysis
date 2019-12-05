package io.beam.exp.core;

import io.beam.exp.core.outputStream.bigquery.CryptoDataBigQueryOutputStream;

import io.beam.exp.core.service.CryptoSubscriberService;
import io.beam.exp.core.service.CryptoSubscriberServiceImpl;
import model.Quote;
import model.TradeEx;

public class Main {
    public static void main(String args[]) throws Exception{

        String TradeTable = "CryptoTrade";
        String QuoteTable = "CryptoQuote";

        //cryptoSubscriberService.startSubscription("","BTC","USD");
    }
}
