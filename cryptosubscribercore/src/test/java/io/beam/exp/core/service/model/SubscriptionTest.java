package io.beam.exp.core.service.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.beam.exp.core.observe.Observer;
import io.beam.exp.cryptorealtime.model.Quote;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SubscriptionTest {

    List<String> msgLst = new LinkedList<>();
    List<String> errLst = new LinkedList<>();

    Quote quote = null;
    Observer<Quote> observer=null;
    @BeforeEach
    void init(){
        String QuoteSample="{\"exchange\":\"hitbtc\",\"currencyPair\":\"BTC/USD\",\"open\":0.0,\"last\":8123.72,\"bid\":8121.71,\"ask\":8122.76,\"high\":8466.6,\"low\":8060.01,\"volume\":35528.56615,\"quoteVolume\":2.88624123404078E8,\"timestamp\":\"2019-11-19 06:53:00.883\"}";
        Gson g = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd hh:mm:ss.S")
                .create();
        quote = g.fromJson(QuoteSample, Quote.class);
        assertNotNull(quote);

        observer = new Observer<Quote>() {
            @Override
            public void update(Quote msg) {
                msgLst.add(msg.toString());
            }

            @Override
            public void throwError(Throwable ex) {
                errLst.add(ex.getMessage());
            }

            @Override
            public String getDescription() {
                return "testObserver";
            }
        };
    }



    @Test
    void notifyOservers() throws Exception{
        Exception exception = new Exception("Test exception");
        Subscription subscription = new Subscription("hitbtc","BTC", "USD");
        subscription.registerObserver(observer);
        subscription.notifyOservers(quote);
        subscription.notifyObservers(exception);
        Thread.sleep(100);
        assertThat(msgLst.size()).isEqualTo(1);
        assertThat(msgLst.get(0)).isEqualTo(quote.toString());
        assertThat(errLst.size()).isEqualTo(1);
        assertThat(errLst.get(0)).isEqualTo(exception.getMessage());
    }
}