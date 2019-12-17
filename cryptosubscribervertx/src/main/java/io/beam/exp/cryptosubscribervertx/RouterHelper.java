package io.beam.exp.cryptosubscribervertx;

import io.beam.exp.core.service.CryptoMarketDataService;
import io.beam.exp.cryptosubscribervertx.handler.CreateSubscription;
import io.beam.exp.cryptosubscribervertx.handler.ListSubscriptionStatus;
import io.beam.exp.cryptosubscribervertx.handler.StopSubscription;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;

public class RouterHelper {


  public static void setGetCryptoSubscription(Router router){

  }
  public static Router createRouter(Vertx vertx){
    Router router = Router.router(vertx);

    Route route = router.route("/subscription/list");
    route.handler(
      new ListSubscriptionStatus()
      );

    route = router.route("/subscription/create");
    route.handler(new CreateSubscription());

    route = router.route("/subscription/stop");
    route.handler(
      new StopSubscription()
    );

    route = router.route("/");
    route.handler(
      routingContext->{
        HttpServerResponse response = routingContext.response();
        response.putHeader("content-type", "text");
        // enable chunked responses because we will be adding data as
        // we execute over other handlers. This is only required once and
        // only if several handlers do output.
        response.setChunked(true);
        response.write("OK").end();
      }
    );

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

  public static Router createTestRouter(Vertx vertx, CryptoMarketDataService cryptoMarketDataService){
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
