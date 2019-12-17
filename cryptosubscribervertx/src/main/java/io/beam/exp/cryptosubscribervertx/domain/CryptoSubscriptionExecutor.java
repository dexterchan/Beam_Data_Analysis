package io.beam.exp.cryptosubscribervertx.domain;

import io.beam.exp.core.factory.AbstractCryptoMarketDataServiceFactory;
import io.beam.exp.core.observe.Observer;
import io.beam.exp.core.service.CryptoMarketDataService;
import io.beam.exp.core.service.GCP_CryptoMarketDataServiceFactory;
import io.beam.exp.core.service.QuoteCryptoMarketDataService;
import io.beam.exp.core.service.TradeCryptoMarketDataService;
import io.beam.exp.cryptorealtime.model.Quote;
import io.beam.exp.cryptorealtime.model.TradeEx;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class CryptoSubscriptionExecutor {
  static CryptoMarketDataService<Quote> quoteCryptoMarketDataService =null;
  static CryptoMarketDataService<TradeEx> tradeExCryptoMarketDataService =null;
  static ExecutorService executor = Executors.newCachedThreadPool();

  static String quoteTable = "CryptoQuote";
  static String tradeTable = "CryptoTrade";
  static AbstractCryptoMarketDataServiceFactory cryptoSubscriberServiceFactory = null;

  static{
    if (System.getenv("QUOTETABLE")!=null){
      quoteTable = System.getenv("QUOTETABLE");
    }
    if (System.getenv("TRADEEXTABLE")!=null){
      tradeTable = System.getenv("TRADEEXTABLE");
    }
    cryptoSubscriberServiceFactory = new GCP_CryptoMarketDataServiceFactory(quoteTable, tradeTable);

    quoteCryptoMarketDataService = cryptoSubscriberServiceFactory.createQuoteService();
    tradeExCryptoMarketDataService = cryptoSubscriberServiceFactory.createTradeService();

  }
  static String EXCHANGE = "hitbtc";

  public static void startSubscription(String baseCcy, String counterCcy){
    executor.execute(()->{
      quoteCryptoMarketDataService.startSubscription(EXCHANGE,baseCcy,counterCcy);
    });
    executor.execute(()->{
      tradeExCryptoMarketDataService.startSubscription(EXCHANGE,baseCcy,counterCcy);
    });
  }
  public static void stopSubscription(String baseCcy, String counterCcy){
    quoteCryptoMarketDataService.stopSubscription(EXCHANGE,baseCcy,counterCcy);
    tradeExCryptoMarketDataService.stopSubscription(EXCHANGE, baseCcy, counterCcy);
  }
  public static List<Map<String,String>> listSubscription(){
    List<Map<String,String>> mapList = new LinkedList<>();
    mapList.addAll( quoteCryptoMarketDataService.listSubscription() );
    mapList.addAll(tradeExCryptoMarketDataService.listSubscription());
    return mapList;
  }
  public static List<Map<String,String>>  getSubscription(String baseCcy, String counterCcy){
    List<Map<String,String>> mapList = new LinkedList<>();
    mapList.add(quoteCryptoMarketDataService.getSubscription(EXCHANGE, baseCcy, counterCcy));
    mapList.add(tradeExCryptoMarketDataService.getSubscription(EXCHANGE, baseCcy, counterCcy));
    return mapList;
  }

  static Observer<Quote> logQuoteObserver = new Observer<Quote>(){
    @Override
    public void update(Quote msg) {
      log.info(msg.toString());
      synchronized (this) {
        this.notifyAll();
      }
    }

    @Override
    public void throwError(Throwable ex) {
      log.error(ex.getMessage());
    }

    @Override
    public String getDescription() {
      return "Log Quote";
    }
  };
}
