package vertTest;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;

public class Routers extends AbstractVerticle {

  public static void main(String[] args) {
    
    Vertx vertx=Vertx.vertx();
    vertx.deployVerticle(Routers.class.getName());
  }
  public void start() {
    HttpServer server = vertx.createHttpServer();

    Router router = Router.router(vertx);

    Route route1 = router.route("/some/path/").handler(routingContext -> {

      HttpServerResponse response = routingContext.response();
      // 由于我们会在不同的处理器里写入响应，因此需要启用分块传输
      // 仅当需要通过多个处理器输出响应时才需要
      response.setChunked(true);

      response.write("route1\n");

      // 5 秒后调用下一个处理器
      routingContext.vertx().setTimer(5000, tid -> routingContext.next());
    });

    Route route2 = router.route("/some/path/").handler(routingContext -> {

      HttpServerResponse response = routingContext.response();
      response.write("route2\n");

      // 5 秒后调用下一个处理器
      routingContext.vertx().setTimer(5000, tid ->  routingContext.next());
    });

    Route route3 = router.route("/some/path/").handler(routingContext -> {

      HttpServerResponse response = routingContext.response();
      response.write("route3");

      // 结束响应
      routingContext.response().end();
    });
  }
}
