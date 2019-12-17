package io.beam.exp.core.factory;

import com.google.common.collect.Lists;
import io.beam.exp.core.observe.Observer;
import io.beam.exp.core.service.CryptoMarketDataService;
import io.beam.exp.cryptorealtime.model.Quote;
import io.beam.exp.cryptorealtime.model.TradeEx;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("integration")
@Slf4j
class GCP_CryptoMarketDataServiceFactoryTest {

    static String quoteTable = "QuoteTest";
    static String tradeTable = "TradeExTest";

    List<Quote> quoteList = Lists.newLinkedList();
    List<TradeEx> tradeExList = Lists.newLinkedList();
    List<Throwable> errLst = Lists.newLinkedList();
    static AbstractCryptoMarketDataServiceFactory cryptoSubscriberServiceFactory = null;
    Observer<Quote> dummyQuoteObserver = new Observer<Quote>(){
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

    Observer<TradeEx> dummyTradeObserver = new Observer<TradeEx>(){
        @Override
        public void update(TradeEx msg) {
            tradeExList.add(msg);
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

    @BeforeAll
    static void init(){
        cryptoSubscriberServiceFactory = new GCP_CryptoMarketDataServiceFactory(quoteTable, tradeTable);
    }

    @Test
    void createQuoteService() throws Exception{

        CryptoMarketDataService<Quote> cryptoMarketDataService = cryptoSubscriberServiceFactory.createQuoteService();

        cryptoMarketDataService.injectObserver(dummyQuoteObserver);

        cryptoMarketDataService.startSubscription("hitbtc","BTC","USD");

        synchronized (dummyQuoteObserver) {
            dummyQuoteObserver.wait();
        }

        assertThat(quoteList.size()).isGreaterThan(0);
    }

    @Test
    void createTradeService() throws Exception{
       CryptoMarketDataService<TradeEx> cryptoMarketDataService = cryptoSubscriberServiceFactory.createTradeService();
       cryptoMarketDataService.injectObserver(dummyTradeObserver);
       cryptoMarketDataService.startSubscription("hitbtc","BTC","USD");
        synchronized (dummyTradeObserver) {
            dummyTradeObserver.wait();
        }

        assertThat(tradeExList.size()).isGreaterThan(0);
    }
}