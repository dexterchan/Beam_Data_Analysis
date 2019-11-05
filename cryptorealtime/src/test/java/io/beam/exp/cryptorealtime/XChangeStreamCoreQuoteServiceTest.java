package io.beam.exp.cryptorealtime;


import info.bitrich.xchangestream.hitbtc.HitbtcStreamingExchange;
import model.Quote;
import model.TradeEx;
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

    //@Disabled
    @Test
    void testSubsribeHitbtcStreamingExchange() {
        String exchName = "hitbtc";
        log.debug(exchName);
        String baseCcy = "BTC";
        String counterCcy = "USD";
        BlockingQueue<TradeEx> tradequeue = new LinkedBlockingQueue<>();
        BlockingQueue<Quote> quoteQueue = new LinkedBlockingQueue<>();
        try (ExchangeQuoteInterface exInf = XChangeStreamCoreQuoteService.of(exchName, baseCcy, counterCcy);) {
            assertNotNull(exInf);
            assertThat(exInf).isNotNull();

            exInf.subscribe(t -> {
                try {
                    tradequeue.put(t);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }, q -> {
                try {
                    quoteQueue.put(q);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });

            long refTime = System.currentTimeMillis();


            ExecutorService executor = Executors.newCachedThreadPool();
            executor.execute(() -> {
                while ((System.currentTimeMillis() - refTime) < 100 * 5) {
                    try {
                        TradeEx q = tradequeue.take();
                        log.debug("TradeEx:"+q.toString());
                        assertThat(q.getPrice()).isNotNull();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
            executor.execute(()->{
                while ((System.currentTimeMillis() - refTime) < 100 * 5) {
                    try {
                        Quote q = quoteQueue.take();
                        log.debug("Quote:"+q.toString());
                        assertThat(q.getBid()).isNotNull();
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

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}