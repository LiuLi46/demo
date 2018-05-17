package vertTest;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.asyncsql.MySQLClient;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.sql.UpdateResult;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.LoggerFormat;
import io.vertx.ext.web.handler.LoggerHandler;
import io.vertx.ext.web.handler.TimeoutHandler;
import io.vertx.redis.RedisClient;
import io.vertx.redis.RedisOptions;
import util.ChineseName;
import util.ConfReadUtils;
import util.Data;

/**
 * @author liu
 *
 */
public class RepositoryVerticle extends AbstractVerticle {
  private static int vertx_port;
  private static JsonObject serverConf;
  protected SQLClient mysqlclient;
  protected RedisClient redisClient;
  private static final Logger logger = LogManager.getLogger();

  public static void main(String[] args) {

    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(RepositoryVerticle.class.getName());
  }

  public void start() throws Exception {
    // 创建路由
    Router router = Router.router(vertx);

    router.route().handler(BodyHandler.create());

    // 初始化配置文件
    initConfig(router);
    // 请求地址
    router.get("/queryUser").handler(this::queryUser);
    router.get("/queryUsers").handler(this::queryUsers);
    router.post("/redisDemo/:key/:value").handler(this::redisDemo);
    router.get("/addUser").handler(this::addUser);
    router.post("/updateUser/:uuid/:name").handler(this::updateUser);
    // 配置项中告诉服务器监听指定的主机和端口
    HttpServer httpServer = vertx.createHttpServer();
    httpServer.requestHandler(router::accept).listen(vertx_port);
  }

  /**
   * @param context
   *          查询语句
   */
  private void queryUser(RoutingContext context) {
  
 //   JsonObject response = new JsonObject();
    
    //JsonObject tokenJson = context.getBodyAsJson();
    //System.out.println(tokenJson);
    
    StringBuffer sql = new StringBuffer(" SELECT * FROM T_USER");

    mysqlclient.query(sql.toString(), res -> {
      if (res.result().getRows().size() > 0) {
        // System.out.println(res.result().toJson().getJsonArray("rows").toString());
         System.out.println(res.result().getRows().get(2));
       
      }else {
     // 返回执行结果
        context.response().end(res.result().toJson().getJsonArray("rows").toString());
      }
      
     
    });

  }
  
  private void queryUsers(RoutingContext context) {
    JsonObject response = new JsonObject();
    mysqlclient.query("select name,create_time from t_user ", res ->{
      
      if(res.result().getRows().size()<0) {
        
      System.out.println("6666");
      System.out.println(res.result().getRows().size());
      }else {        
     //   context.response().end(res.result().toJson().getJsonArray("rows").toString());
       System.out.println("6666");
       JsonObject object = res.result().getRows().get(0);
       System.out.println(object.encodePrettily());
       context.response().setStatusCode(400).end();
       System.out.println(res.result().getRows().size());
       System.out.println("777");
       context.response().end(res.result().toJson().getJsonArray("rows").toString());
       
       // System.out.println(message.getStatusCode());

      }
      
    });
  }

  /**
   * 对Redis进行操作
   * 
   * @param context
   */
  private void redisDemo(RoutingContext context) {
    String key = context.request().getParam("key");
    String value = context.request().getParam("value");

    redisClient.set(key, value, request -> {
      context.response().end(String.valueOf(request.succeeded()));
    });
  }

  private void updateUser(RoutingContext context) {
    String uuid = context.request().getParam("uuid");
    String name = context.request().getParam("name");

    String sql = "update t_user set name=''" + name + "'' where uuid='" + uuid + "'";

    mysqlclient.update(sql, req -> {

      context.response().end("成功");
    });

  }

  private void addUser(RoutingContext context) {

    String date = Data.getDate();
    String chinese = ChineseName.getname();
    String sql = "insert into t_user set name=?,create_time=?";
    JsonArray params=new JsonArray().add(chinese).add(date);
    mysqlclient.updateWithParams(sql,params, request -> {

      if (request.succeeded()) {

        UpdateResult updateResult = request.result();

        System.out.println("No. of rows updated: " + updateResult.getUpdated());

      } else {

        // Failed!

      }
    });
    
    
  /*  String addUser = "insert into t_user set nikename=?,unionid=?,headimgurl=?";
    JsonArray params = new JsonArray().add(chinese).add((Math.random() * 10000)).add(
        "http://wx.qlogo.cn/mmopen/g3MonUZtNHkdmzicIlibx6iaFqAc56vxLSUfpb6n5WKSYVY0ChQKkiaJSgQ1dZuTOgvLLrhJbERQQ4eMsv84eavHiaiceqxibJxCfHe/0");
    mysqlclient.updateWithParams(addUser, params, res->{   
      if (res.succeeded()) {

        UpdateResult updateResult = res.result();

        System.out.println("No. of rows updated: " + updateResult.getUpdated());

      } else {

        // Failed!

      }
    });*/
  }

  /**
   * @param router
   * @throws Exception
   *           初始化mysql
   */
  public void initConfig(Router router) throws Exception {
    try {
      serverConf =ConfReadUtils.getServerConfByJson("conf.json");
    } catch (Exception e) {
      e.printStackTrace();
      logger.error(e);
    }
    vertx_port = serverConf.getJsonObject("vertx").getInteger("port");

    initMySqlConfig(router);
    initRedis(router);
  }

  private void initMySqlConfig(Router router) {
    JsonObject mysqlConf = serverConf.getJsonObject("mysql");
    mysqlclient = MySQLClient.createNonShared(vertx, mysqlConf);
    Handler<RoutingContext> mysqlHandler = routingContext -> mysqlclient.getConnection(res -> {
      if (res.failed()) {
        routingContext.fail(res.cause());
      } else {
        SQLConnection conn = res.result();
        routingContext.put("mysqlconn", conn);
        routingContext.addHeadersEndHandler(done -> conn.close(v -> {
        }));
        routingContext.next();
      }
    });

    Handler<RoutingContext> mysqlfailHandler = routingContext -> {
      SQLConnection conn = routingContext.get("mysqlconn");
      if (conn != null) {
        conn.close(v -> {
        });
      }
    };
    router.route("/").handler(mysqlHandler).handler(TimeoutHandler.create(3 * 1000)).failureHandler(mysqlfailHandler);
  }

 /* private JsonObject getServerConf(String filename) {
    Path path = Paths.get(filename);
    try {
      String data = new String(Files.readAllBytes(path));
      return new JsonObject(data);
    } catch (Exception e) {
      logger.error(e);
    }

    return null;
  }*/

  /**
   * @param router
   *          初始化redis
   */
  private void initRedis(Router router) {
    RedisOptions config = new RedisOptions().setHost("127.0.0.1");
    redisClient = RedisClient.create(vertx, config);
  }

}
