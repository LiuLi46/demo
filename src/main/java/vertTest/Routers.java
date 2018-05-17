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
      // �������ǻ��ڲ�ͬ�Ĵ�������д����Ӧ�������Ҫ���÷ֿ鴫��
      // ������Ҫͨ����������������Ӧʱ����Ҫ
      response.setChunked(true);

      response.write("route1\n");

      // 5 ��������һ��������
      routingContext.vertx().setTimer(5000, tid -> routingContext.next());
    });

    Route route2 = router.route("/some/path/").handler(routingContext -> {

      HttpServerResponse response = routingContext.response();
      response.write("route2\n");

      // 5 ��������һ��������
      routingContext.vertx().setTimer(5000, tid ->  routingContext.next());
    });

    Route route3 = router.route("/some/path/").handler(routingContext -> {

      HttpServerResponse response = routingContext.response();
      response.write("route3");

      // ������Ӧ
      routingContext.response().end();
    });
  }
}
