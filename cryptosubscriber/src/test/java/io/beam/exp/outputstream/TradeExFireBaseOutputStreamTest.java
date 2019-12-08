package io.beam.exp.outputstream;

import com.google.gson.Gson;
import io.beam.exp.cryptorealtime.model.TradeEx;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Tag("integration")
class TradeExFireBaseOutputStreamTest {

    TradeEx tradeex;
    TradeExFireBaseOutputStream outputStream;
    @BeforeEach
    void initialize() throws Exception{
        Gson gson = new Gson();
        String quoteString = "{\"type\":\"BID\",\"originalAmount\":0.05854,\"currencyPair\":\"BTC/USD\",\"price\":9211.43,\"timestamp\":\"Nov 2, 2019 6:26:38 PM\"}";
        tradeex = gson.fromJson(quoteString,TradeEx.class);
        outputStream= new TradeExFireBaseOutputStream();
    }
    @Test
    void write() throws Exception {
        outputStream.write(tradeex);
    }
}