package io.beam.exp.blockchaininfo.outputstream.bigquery;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.beam.exp.blockchaininfo.model.BitcoinState;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@Tag("integration")
class BitcoinBQBlockchaininfoOutputStreamTest {

    @Test
    void write() throws Exception{
        Gson g = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd hh:mm:ss.S")
                .create();

        String sample2 ="{\"difficulty\":1.2973235968799E13,\"blockcount\":605695,\"bcperblock\":1250000000,\"totalbc\":1807118700000000,\"probability\":1.7946998282759516E-23,\"hashestowin\":9223372036854775807,\"nextretarget\":605694,\"avgtxsize\":544.06424162,\"avgtxvalue\":5.012596492853484E9,\"interval\":538.9494,\"eta\":-350.05060000,\"avgtxnumber\":790.1973,\"timestamp\":\"2019-11-28 10:48:04.292\"}\n";
        String sample = "{\"difficulty\":1.2973235968799E13,\"blockcount\":605602,\"bcperblock\":1250000000,\"totalbc\":1807002500000000,\"probability\":1.7946998282759516E-23,\"hashestowin\":9223372036854775807,\"nextretarget\":605601,\"avgtxsize\":544.07679062,\"avgtxvalue\":5.013322269969029E9,\"interval\":662.2713,\"eta\":-287.7287,\"avgtxnumber\":790.0152,\"timestamp\":\"2019-11-27 10:33:24.417\"}";
        BitcoinState bitcoinState = g.fromJson(sample2, BitcoinState.class);
        BitcoinBQBlockchaininfoOutputStream bitcoinBQBlockchaininfoOutputStream = new BitcoinBQBlockchaininfoOutputStream();
        bitcoinBQBlockchaininfoOutputStream.write(bitcoinState);
    }
}