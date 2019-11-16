package io.beam.exp.cryptosubscribervertx.handler;

import io.beam.exp.cryptosubscribervertx.domain.CryptoSubscriptionExecutor;
import io.beam.exp.cryptosubscribervertx.exception.InvalidInputParameter;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;

public class CreateSubscriptionStatus implements Handler<RoutingContext> {
  @Override
  public void handle(RoutingContext routingContext) {
    HttpServerRequest req = routingContext.request();
    String baseccy = req.getParam("baseccy");
    if(baseccy==null){
      throw new InvalidInputParameter("Base CCY not provided");
    }
    String counterccy = req.getParam("counterccy");
    if(counterccy==null){
      throw new InvalidInputParameter("Counter CCY not provided");
    }
    CryptoSubscriptionExecutor.startSubscription(baseccy, counterccy);
    HttpServerResponse response = routingContext.response();
    response.setChunked(true);
    response.write("OK");
    routingContext.response().end();
  }
}
