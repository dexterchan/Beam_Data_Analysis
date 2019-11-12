package io.beam.exp.cryptosubscribervertx;

import io.beam.exp.core.outputStream.QuoteFireBaseOutputStream;
import io.beam.exp.core.outputStream.TradeExFireBaseOutputStream;
import io.beam.exp.core.service.CryptoSubscriberService;
import io.beam.exp.core.service.CryptoSubscriberServiceImpl;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainVerticle extends AbstractVerticle {

  CryptoSubscriberService cryptoSubscriberService = null;
  ExecutorService executor = Executors.newCachedThreadPool();

  private final static boolean RUN_SUBSCRIPTION_AT_START = false;
  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    vertx.createHttpServer().requestHandler(
        RouterHelper.createRouter(vertx,cryptoSubscriberService)
    ).listen(8888, http -> {
      if (http.succeeded()) {
        startPromise.complete();
        System.out.println("HTTP server started on port 8888");
      } else {
        startPromise.fail(http.cause());
      }
    });



    cryptoSubscriberService = new CryptoSubscriberServiceImpl(
      new TradeExFireBaseOutputStream(),new QuoteFireBaseOutputStream());

    executor.execute(()->{
      if(RUN_SUBSCRIPTION_AT_START)
        cryptoSubscriberService.startSubscription("","BTC","USD");
    });


  }
}
