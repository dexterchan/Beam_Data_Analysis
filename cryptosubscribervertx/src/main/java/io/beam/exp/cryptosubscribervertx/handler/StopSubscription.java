package io.beam.exp.cryptosubscribervertx.handler;

import com.google.gson.Gson;
import io.beam.exp.cryptosubscribervertx.domain.CryptoSubscriptionExecutor;
import io.beam.exp.cryptosubscribervertx.exception.InvalidInputParameter;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;

import java.util.List;
import java.util.Map;

public class StopSubscription implements Handler<RoutingContext> {
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
    CryptoSubscriptionExecutor.stopSubscription(baseccy, counterccy);

    List<Map<String, String>> status = CryptoSubscriptionExecutor.getSubscription(baseccy, counterccy);
    Gson gson = new Gson();
    String jsonString = gson.toJson(status);

    HttpServerResponse response = routingContext.response();
    response.putHeader("content-type", "application/json");
    response.setChunked(true);
    response.write(jsonString);
    routingContext.response().end();
  }
}
