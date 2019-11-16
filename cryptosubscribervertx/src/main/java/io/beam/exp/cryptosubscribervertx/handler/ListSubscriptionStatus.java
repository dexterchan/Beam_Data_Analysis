package io.beam.exp.cryptosubscribervertx.handler;

import com.google.gson.Gson;
import io.beam.exp.core.service.CryptoSubscriberService;
import io.beam.exp.cryptosubscribervertx.domain.CryptoSubscriptionExecutor;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;

import java.util.List;
import java.util.Map;

public class ListSubscriptionStatus implements Handler<RoutingContext> {

  @Override
  public void handle(RoutingContext routingContext) {
    List<Map<String,String>> subscriptionLst = CryptoSubscriptionExecutor.listSubscription();
    Gson gson = new Gson();
    String jsonString = gson.toJson(subscriptionLst);

    HttpServerResponse response = routingContext.response();
    response.putHeader("content-type", "application/json");
    // enable chunked responses because we will be adding data as
    // we execute over other handlers. This is only required once and
    // only if several handlers do output.
    response.setChunked(true);
    response.write(jsonString);
    routingContext.response().end();
  }
}
