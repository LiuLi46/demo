package vertTest;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

/**  
* <p>Title: R</p>  
* <p>Description: </p>  
* @author liuli  
* @date 2018Äê5ÔÂ4ÈÕ  
*/
public class R {

  public static void main(String[] args) {
    JsonObject userJson = new JsonObject();
    userJson.put("1", "sad").put("2", "asdada");
    System.out.println(userJson.containsKey("2"));
    System.out.println(userJson.encode());
    System.out.println(userJson.encodePrettily());
    System.out.println(userJson.getString("1"));
 
  }
  
  private void toResponse(RoutingContext context, JsonObject result) {
    context.response().putHeader("content-type", "application/json").end(result.encodePrettily());
  }
  
}
