package io.beam.exp.core.service;

import com.google.common.collect.Lists;
import io.beam.exp.core.observe.Observer;
import lombok.extern.slf4j.Slf4j;
import io.beam.exp.cryptorealtime.model.Quote;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@Slf4j
class QuoteCryptoMarketDataServiceTest {


    static Observer<Quote> getDummyObserver(List<Quote> quoteList, List<Throwable> errLst){
        return new Observer<Quote>(){
            @Override
            public void update(Quote msg) {
                quoteList.add(msg);
                log.debug(msg.toString());
                synchronized (this) {
                    this.notifyAll();
                }
            }

            @Override
            public void throwError(Throwable ex) {
                errLst.add(ex);
            }

            @Override
            public String getDescription() {
                return "Test Quote";
            }
        };
    }

    @Test
    void RunQuoteSubcription_HappyPath() throws Exception{
        List<Quote> quoteList = Lists.newLinkedList();
        List<Throwable> errLst = Lists.newLinkedList();
        CryptoMarketDataService cryptoMarketDataService = new QuoteCryptoMarketDataService();

        Observer<Quote> observer = getDummyObserver(quoteList, errLst);
        cryptoMarketDataService.injectObserver(observer);
        cryptoMarketDataService.startSubscription("hitbtc","BTC","USD");

        synchronized (observer) {
            observer.wait();
        }
        Map<String, String> des=cryptoMarketDataService.getSubscription("hitbtc","BTC","USD");
        assertThat(des.size()).isGreaterThan(0);
        assertThat(quoteList.size()).isGreaterThan(0);
        assertThat(des.get("dataName")).isEqualTo("QUOTE");


    }
    @Test
    void RunQuoteSubcriptionWithoutObserver_ThrowException() throws Exception{
        CryptoMarketDataService cryptoMarketDataService = new QuoteCryptoMarketDataService();

        assertThrows(IllegalStateException.class, ()->{
            cryptoMarketDataService.startSubscription("hitbtc","BTC","USD");
        });

    }
    @Test
    void RunQuoteSubcriptionBTCETH_HappyPath() throws Exception{
        List<Quote> quoteList = Lists.newLinkedList();
        List<Throwable> errLst = Lists.newLinkedList();
        CryptoMarketDataService cryptoMarketDataService = new QuoteCryptoMarketDataService();

        Observer<Quote> observer = getDummyObserver(quoteList, errLst);
        cryptoMarketDataService.injectObserver(observer);
        cryptoMarketDataService.startSubscription("hitbtc","BTC","USD");
        cryptoMarketDataService.startSubscription("hitbtc","ETH","USD");

        synchronized (observer) {
            observer.wait();
        }
        List<Map<String, String>> subscriptionList = cryptoMarketDataService.listSubscription();
        assertThat(subscriptionList.size()).isEqualTo(2);

    }


}