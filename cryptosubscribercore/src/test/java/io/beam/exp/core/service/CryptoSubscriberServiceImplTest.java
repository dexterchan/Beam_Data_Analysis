package io.beam.exp.core.service;


import io.beam.exp.core.outputStream.CryptoDataOutputStream;
import model.Quote;
import model.TradeEx;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.mockito.Mock;

import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

//@Tag("integration")
class CryptoSubscriberServiceImplTest {

    @Mock
    CryptoDataOutputStream<TradeEx> tradeExRepository;
    @Mock
    CryptoDataOutputStream<Quote> quoteRepository;

    @BeforeEach
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @org.junit.jupiter.api.Test
    void listSubscription() throws Exception {
        tradeExRepository = (tradeex)->{
            System.out.println(String.format("TradeSubEX %s",tradeex.toString()));
        };
        quoteRepository = (quoteex)->{
            System.out.println(String.format("QuoteSubEx %s",quoteex.toString()));
        };
        TradeEx ex = new TradeEx();
        Quote q = new Quote();
        //when(tradeExRepository).write(ex);
        //doNothing().when(quoteRepository).write(q);
        CryptoSubscriberService cryptoSubscriberService = new CryptoSubscriberServiceImpl(tradeExRepository, quoteRepository);

        cryptoSubscriberService.startSubscription("hitbtc","BTC","USD");
        cryptoSubscriberService.startSubscription("hitbtc","ETH","USD");

        Thread.sleep(1000);
        List<Map<String,String>>  subscriptionLst= cryptoSubscriberService.listSubscription();

        assertThat(subscriptionLst.size()).isEqualTo(2);

        subscriptionLst.forEach(
                status->{
                    assertEquals(status.get("TurnOn"),"true");
                    assertEquals(status.get("QuoteStatus"),"OK");
                }
        );


        //verify(tradeExRepository).write(ex);
        //verify(quoteRepository).write(q);

    }
}