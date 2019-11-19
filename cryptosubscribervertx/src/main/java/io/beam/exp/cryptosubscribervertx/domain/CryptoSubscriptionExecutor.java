package io.beam.exp.cryptosubscribervertx.domain;

import io.beam.exp.core.outputStream.firebase.QuoteFireBaseOutputStream;
import io.beam.exp.core.outputStream.firebase.TradeExFireBaseOutputStream;
import io.beam.exp.core.service.CryptoSubscriberService;
import io.beam.exp.core.service.CryptoSubscriberServiceImpl;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class CryptoSubscriptionExecutor {
  static CryptoSubscriberService cryptoSubscriberService = null;
  static ExecutorService executor = Executors.newCachedThreadPool();

  static{
    try {
      cryptoSubscriberService = new CryptoSubscriberServiceImpl(
        new TradeExFireBaseOutputStream(), new QuoteFireBaseOutputStream());
    }catch(Exception ex){
      log.error(ex.getMessage());
      System.exit(-1);
    }
  }
  public static CryptoSubscriberService getCryptoSubscriberService(){
    return cryptoSubscriberService;
  }

  public static void startSubscription(String baseCcy, String counterCcy){
    executor.execute(()->{
        cryptoSubscriberService.startSubscription("",baseCcy,counterCcy);
    });
  }
  public static void stopSubscription(String baseCcy, String counterCcy){
    cryptoSubscriberService.stopSubscription("", baseCcy, counterCcy);
  }
  public static List<Map<String,String>> listSubscription(){
    return cryptoSubscriberService.listSubscription();
  }
  public static Map<String, String> getSubscription(String baseCcy, String counterCcy){
    return cryptoSubscriberService.getSubscription("",baseCcy,counterCcy);
  }
}
