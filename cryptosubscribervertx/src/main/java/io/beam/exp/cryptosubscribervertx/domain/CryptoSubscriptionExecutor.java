package io.beam.exp.cryptosubscribervertx.domain;

import io.beam.exp.core.service.CryptoMarketDataService;
import io.beam.exp.cryptorealtime.model.Quote;
import io.beam.exp.cryptorealtime.model.TradeEx;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class CryptoSubscriptionExecutor {
  //static CryptoSubscriberService cryptoSubscriberService = null;
  static CryptoMarketDataService<Quote> quoteCryptoMarketDataService =null;
  static CryptoMarketDataService<TradeEx> tradeExCryptoMarketDataService =null;
  static ExecutorService executor = Executors.newCachedThreadPool();

  static{
    try {
      String TradeTable = "CryptoTrade";
      String QuoteTable = "CryptoQuote";
      /*
      cryptoSubscriberService = new CryptoSubscriberServiceImpl(
        new CryptoDataBigQueryOutputStream<TradeEx>(TradeEx.class, TradeTable),
        new CryptoDataBigQueryOutputStream<Quote>(Quote.class, QuoteTable)
      );*/
    }catch(Exception ex){
      log.error(ex.getMessage());
      System.exit(-1);
    }
  }

  public static void startSubscription(String baseCcy, String counterCcy){
    executor.execute(()->{
      quoteCryptoMarketDataService.startSubscription("",baseCcy,counterCcy);
    });
    executor.execute(()->{
      tradeExCryptoMarketDataService.startSubscription("",baseCcy,counterCcy);
    });
  }
  public static void stopSubscription(String baseCcy, String counterCcy){
    //cryptoSubscriberService.stopSubscription("", baseCcy, counterCcy);
  }
  public static List<Map<String,String>> listSubscription(){
    return null;
  }
  public static Map<String, String> getSubscription(String baseCcy, String counterCcy){
    return null;
  }
}
