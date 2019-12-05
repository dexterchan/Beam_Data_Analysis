package io.beam.exp.core.observe;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import model.Quote;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j


class AsyncObserverTest {
     List<String> msgLst = new LinkedList<>();
     List<String> errLst = new LinkedList<>();
    Quote q = null;
    @BeforeEach
    void init(){
        String QuoteSample="{\"exchange\":\"hitbtc\",\"currencyPair\":\"BTC/USD\",\"open\":0.0,\"last\":8123.72,\"bid\":8121.71,\"ask\":8122.76,\"high\":8466.6,\"low\":8060.01,\"volume\":35528.56615,\"quoteVolume\":2.88624123404078E8,\"timestamp\":\"2019-11-19 06:53:00.883\"}";
        Gson g = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd hh:mm:ss.S")
                .create();
        q = g.fromJson(QuoteSample, Quote.class);
        assertNotNull(q);
    }
    @Test
    void update() throws Exception{
        Observer<Quote> t = new TestObserver();
        t.update(q);
        Thread.sleep(100);
        assertEquals(1,msgLst.size() );
    }

    @Test
    void throwError() throws Exception{
        Observer<Quote> t = new TestObserver();
        t.throwError(new Exception("Testing error"));
        Thread.sleep(100);
        assertEquals(1,errLst.size() );
    }

    class TestObserver extends AsyncObserver<Quote>{

        @Override
        public void asyncUpdate(Quote msg) {
            log.info(msg.toString());
            msgLst.add(msg.toString());
        }

        @Override
        public void asyncThrowError(Throwable ex) {
            log.info(ex.getMessage());
            errLst.add(ex.getMessage());
        }

        @Override
        public String getDescription() {
            return "Testing only";
        }
    }
}