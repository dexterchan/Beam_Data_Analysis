package io.beam.exp.cryptorealtime;

import info.bitrich.xchangestream.hitbtc.HitbtcStreamingExchange;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class XChangeStreamCoreQuoteServiceTest {
    Logger log= LoggerFactory.getLogger(XChangeStreamCoreQuoteServiceTest.class);

    @Test
    void testSubsribe() {
        String exchName= HitbtcStreamingExchange.class.getName();//"HitbtcExchange";//"hitBTC";
        log.debug(exchName);
        String baseCcy="BTC";
        String counterCcy="USD";
        try(ExchangeQuoteInterface exInf =XChangeStreamCoreQuoteService.of(exchName,baseCcy,counterCcy);){

            assertNotNull(exInf);
            assertThat(exInf).isNotNull();
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }
}