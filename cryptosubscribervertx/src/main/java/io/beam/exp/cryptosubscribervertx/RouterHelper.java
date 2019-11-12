package io.beam.exp.cryptosubscribervertx;

import com.google.gson.Gson;
import io.beam.exp.core.service.CryptoSubscriberService;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;

import java.util.List;
import java.util.Map;

public class RouterHelper {


  public static void setGetCryptoSubscription(Router router){

  }
  public static Router createRouter(Vertx vertx, CryptoSubscriberService cryptoSubscriberService){
    Router router = Router.router(vertx);

    Route route = router.route("/subscription/list");

    route.handler(routingContext -> {

      List<Map<String,String>> subscriptionLst = cryptoSubscriberService.listSubscription();
      Gson gson = new Gson();
      String jsonString = gson.toJson(subscriptionLst);

      HttpServerResponse response = routingContext.response();
      // enable chunked responses because we will be adding data as
      // we execute over other handlers. This is only required once and
      // only if several handlers do output.
      response.setChunked(true);
      response.write(jsonString);
      routingContext.response().end();
    });
    /*
    route.handler(routingContext->{
      List<Map<String,String>> subscriptionLst = cryptoSubscriberService.listSubscription();
      Gson gson = new Gson();
      String jsonString = gson.toJson(subscriptionLst);
      HttpServerResponse response = routingContext.response();
      //response.putHeader("content-type", "application/json");
      jsonString = "{ \"name\":\"John\", \"age\":30}";
      response.write(jsonString);
      routingContext.response().end();
    });*/

    return router;
  }

  public static Router createTestRouter(Vertx vertx, CryptoSubscriberService cryptoSubscriberService){
    Router router = Router.router(vertx);

    Route route = router.route("/subscription/list");
    route.handler(routingContext -> {

      HttpServerResponse response = routingContext.response();
      // enable chunked responses because we will be adding data as
      // we execute over other handlers. This is only required once and
      // only if several handlers do output.
      response.setChunked(true);
      response.write("route1\n");

      // Call the next matching route after a 5 second delay
      routingContext.vertx().setTimer(5000, tid -> routingContext.next());
    });
    route.handler(routingContext -> {

      HttpServerResponse response = routingContext.response();
      response.write("route2\n");

      // Call the next matching route after a 5 second delay
      routingContext.vertx().setTimer(5000, tid -> routingContext.next());
    });
    route.handler(routingContext -> {

      HttpServerResponse response = routingContext.response();
      response.write("route3");

      // Now end the response
      routingContext.response().end();
    });

    return router;
  }
}
