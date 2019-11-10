package io.beam.exp.cryptosubscribervertx;

import io.beam.exp.core.outputStream.QuoteFireBaseOutputStream;
import io.beam.exp.core.outputStream.TradeExFireBaseOutputStream;
import io.beam.exp.core.service.CryptoSubscriberService;
import io.beam.exp.core.service.CryptoSubscriberServiceImpl;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

public class MainVerticle extends AbstractVerticle {


  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    vertx.createHttpServer().requestHandler(req -> {
      req.response()
        .putHeader("content-type", "text/plain")
        .end("Hello from Vert.x!");
    }).listen(8888, http -> {
      if (http.succeeded()) {
        startPromise.complete();
        System.out.println("HTTP server started on port 8888");
      } else {
        startPromise.fail(http.cause());
      }
    });
    CryptoSubscriberService cryptoSubscriberService = new CryptoSubscriberServiceImpl(
      new TradeExFireBaseOutputStream(),new QuoteFireBaseOutputStream());
    cryptoSubscriberService.startSubscription("","BTC","USD");

  }
}
