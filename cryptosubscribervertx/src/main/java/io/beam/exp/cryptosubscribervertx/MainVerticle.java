package io.beam.exp.cryptosubscribervertx;

import io.beam.exp.cryptosubscribervertx.domain.CryptoSubscriptionExecutor;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

public class MainVerticle extends AbstractVerticle {

  //CryptoSubscriberService cryptoSubscriberService = null;
  //ExecutorService executor = Executors.newCachedThreadPool();

  private final static boolean RUN_SUBSCRIPTION_AT_START = true;

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    if (RUN_SUBSCRIPTION_AT_START)
      CryptoSubscriptionExecutor.startSubscription("BTC", "USD");

    vertx.createHttpServer().requestHandler(
      RouterHelper.createRouter(vertx)
    ).listen(8888, http -> {
      if (http.succeeded()) {
        startPromise.complete();
        System.out.println("HTTP server started on port 8888");
      } else {
        startPromise.fail(http.cause());
      }
    });

  }
}
