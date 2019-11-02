package io.beam.exp.outputstream;

import com.google.cloud.firestore.DocumentReference;
import com.google.gson.Gson;

import model.Quote;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Tag("integration")
class QuoteFireBaseOutputStreamTest {

    Quote quote;
    QuoteFireBaseOutputStream outputStream;
    @BeforeEach
    void initialize() throws Exception{
        Gson gson = new Gson();
        String quoteString = "{\"currencyPair\":\"BTC/USD\",\"open\":0.0,\"last\":9211.8,\"bid\":9209.84,\"ask\":9210.94,\"high\":9275.84,\"low\":9071.0,\"volume\":44752.71183,\"quoteVolume\":4.12253030835594E8,\"timestamp\":\"Nov 2, 2019 4:42:30 PM\"}";
        quote = gson.fromJson(quoteString,Quote.class);
        outputStream= new QuoteFireBaseOutputStream();
    }


    @Test
    void write() throws Exception{

        outputStream.write(quote);
    }



}
