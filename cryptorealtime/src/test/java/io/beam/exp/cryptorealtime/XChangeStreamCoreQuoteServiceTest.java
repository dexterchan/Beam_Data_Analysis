package io.beam.exp.cryptorealtime;


import io.beam.exp.cryptorealtime.model.Quote;
import io.beam.exp.cryptorealtime.model.TradeEx;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Tag("conectiontest")
class XChangeStreamCoreQuoteServiceTest {
    Logger log = LoggerFactory.getLogger(XChangeStreamCoreQuoteServiceTest.class);

    static final int counterTicks = 10;

    int numOfTrade = 0;
    int numOfQuote = 0;

    @BeforeEach
    void init(){
        this.numOfQuote = 0;
        this.numOfTrade = 0;
    }

    //@Disabled
    @Test
    void testSubsribeHitbtcStreamingExchange() {
        String exchName = "hitbtc";
        log.debug(exchName);
        String baseCcy = "BTC";
        String counterCcy = "USD";
        BlockingQueue<TradeEx> tradequeue = new LinkedBlockingQueue<>();
        BlockingQueue<Quote> quoteQueue = new LinkedBlockingQueue<>();
        try (ExchangeInterface exInf = XChangeStreamCoreQuoteService.of(exchName, baseCcy, counterCcy);) {
            assertNotNull(exInf);
            assertThat(exInf).isNotNull();

            exInf.subscribeTrade(
                    t -> {
                        try {
                            tradequeue.put(t);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    },
                    ex -> {
                        ex.printStackTrace();
                    }
            );
            exInf.subscribeQuote(
                    q -> {
                        try {
                            quoteQueue.put(q);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    },
                    ex -> {
                        ex.printStackTrace();
                    }

            );


            long refTime = System.currentTimeMillis();



            ExecutorService executor = Executors.newFixedThreadPool(5);
            executor.execute(() -> {
                while ((System.currentTimeMillis() - refTime) < 100 * 5) {
                    try {
                        TradeEx q = tradequeue.take();
                        log.debug("TradeEx:" + q.toString());
                        assertThat(q.getPrice()).isNotNull();
                        this.numOfTrade++;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
            executor.execute(() -> {
                while ((System.currentTimeMillis() - refTime) < 100 * 5) {
                    try {
                        Quote q = quoteQueue.take();
                        log.debug("Quote:" + q.toString());
                        assertThat(q.getBid()).isNotNull();
                        this.numOfQuote++;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });

            executor.shutdown();
            try {
                if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
            }
            assertThat(this.numOfQuote).isGreaterThan(0);
            assertThat(this.numOfTrade).isGreaterThan(0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}