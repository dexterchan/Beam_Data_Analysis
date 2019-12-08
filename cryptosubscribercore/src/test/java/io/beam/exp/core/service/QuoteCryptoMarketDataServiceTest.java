package io.beam.exp.core.service;

import com.google.common.collect.Lists;
import io.beam.exp.core.observe.Observer;
import lombok.extern.slf4j.Slf4j;
import io.beam.exp.cryptorealtime.model.Quote;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@Slf4j
class QuoteCryptoMarketDataServiceTest {
    List<Quote> quoteList = Lists.newLinkedList();
    List<Throwable> errLst = Lists.newLinkedList();
    Observer<Quote> observer= new Observer<Quote>(){
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
    @Test
    void RunQuoteSubcription_HappyPath() throws Exception{
        CryptoSubscriberService cryptoSubscriberService = new QuoteCryptoMarketDataService();

        cryptoSubscriberService.injectObserver(observer);
        cryptoSubscriberService.startSubscription("hitbtc","BTC","USD");

        synchronized (observer) {
            observer.wait();
        }

        assertThat(quoteList.size()).isGreaterThan(0);
        //quoteList.forEach(quote->log.debug(quote.toString()));

                //assertThat
        // assertThrows(ValueNotFoundException.class, () -> {
        //            controller.oopsHandler();
        //        });
    }
    @Test
    void RunQuoteSubcriptionWithoutObserver_ThrowException() throws Exception{
        CryptoSubscriberService cryptoSubscriberService = new QuoteCryptoMarketDataService();

        assertThrows(IllegalStateException.class, ()->{
            cryptoSubscriberService.startSubscription("hitbtc","BTC","USD");
        });

    }

}