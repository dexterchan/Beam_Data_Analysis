package io.beam.exp.core.outputStream.bigquery;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryOptions;
import com.google.cloud.bigquery.FieldValueList;
import com.google.cloud.bigquery.QueryJobConfiguration;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import model.Quote;
import model.TradeEx;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
@Tag("integration")
@Slf4j
class CryptoDataBigQueryOutputStreamTest {
    BigQuery bigquery =null;
    @BeforeEach
    void initBQ(){
        // Create a service instance
        bigquery = BigQueryOptions.getDefaultInstance().getService();
    }
    @Test
    void writeQuote() throws Exception {
        final String table = "QuoteTest";
        CryptoDataBigQueryOutputStream<Quote> bqQuoteStream = new CryptoDataBigQueryOutputStream<Quote>(Quote.class, table);

        String QuoteSample="{\"exchange\":\"hitbtc\",\"currencyPair\":\"BTC/USD\",\"open\":0.0,\"last\":8123.72,\"bid\":8121.71,\"ask\":8122.76,\"high\":8466.6,\"low\":8060.01,\"volume\":35528.56615,\"quoteVolume\":2.88624123404078E8,\"timestamp\":\"2019-11-19 06:53:00.883\"}";
        Gson g = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd hh:mm:ss.S")
                .create();
        Quote q = g.fromJson(QuoteSample, Quote.class);
        assertNotNull(q);


        bqQuoteStream.write(q);

        log.debug(String.format("SELECT * FROM Crypto.%s LIMIT 5",table));
        // Create a query request
        QueryJobConfiguration queryConfig =
                QueryJobConfiguration.newBuilder(String.format("SELECT * FROM Crypto.%s",table)).build();
        // Read rows
        int count =0;
        for (FieldValueList row : bigquery.query(queryConfig).iterateAll()) {
            count++;
        }
        assertTrue(count>0);
    }
    @Test
    void writeTradeEx() throws  Exception{
        final String table = "TradeExTest";
        CryptoDataBigQueryOutputStream<TradeEx> bqQuoteStream = new CryptoDataBigQueryOutputStream<TradeEx>(TradeEx.class, table);

        String TradeExSample = "{\"exchange\":\"hitbtc\",\"type\":\"BID\",\"originalAmount\":1.0E-5,\"currencyPair\":\"BTC/USD\",\"price\":8131.96,\"timestamp\":\"2019-11-19 06:48:44.919\"}";

        Gson g = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd hh:mm:ss.S")
                .create();
        TradeEx t = g.fromJson(TradeExSample, TradeEx.class);
        assertNotNull(t);

        bqQuoteStream.write(t);

        log.debug(String.format("SELECT * FROM Crypto.%s LIMIT 5",table));
        // Create a query request
        QueryJobConfiguration queryConfig =
                QueryJobConfiguration.newBuilder(String.format("SELECT * FROM Crypto.%s",table)).build();
        // Read rows
        int count =0;
        for (FieldValueList row : bigquery.query(queryConfig).iterateAll()) {
            count++;
        }
        assertTrue(count>0);
    }


}