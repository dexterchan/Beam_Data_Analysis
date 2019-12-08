package io.beam.exp.core.service;

import com.google.common.collect.Lists;
import io.beam.exp.core.observe.Observer;
import lombok.extern.slf4j.Slf4j;
import io.beam.exp.cryptorealtime.model.TradeEx;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class TradeCryptoMarketDataServiceTest {
    List<TradeEx> tradeList = Lists.newLinkedList();
    List<Throwable> errLst = Lists.newLinkedList();
    Observer<TradeEx> observer= new Observer<TradeEx>(){
        @Override
        public void update(TradeEx msg) {
            tradeList.add(msg);
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
            return "Test TradeEx";
        }
    };
    @Test
    void RunTradeExSubcription_HappyPath() throws Exception{
        CryptoSubscriberService cryptoSubscriberService = new TradeCryptoMarketDataService();
        cryptoSubscriberService.injectObserver(observer);
        cryptoSubscriberService.startSubscription("hitbtc","BTC","USD");

        synchronized (observer) {
            observer.wait();
        }

        assertThat(tradeList.size()).isGreaterThan(0);
    }
}