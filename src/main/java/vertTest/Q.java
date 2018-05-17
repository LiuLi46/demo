package vertTest;

import java.util.UUID;

import io.vertx.core.json.JsonObject;

/**
 * @author liu
 *
 */
public class Q {
  
  private static final String QRCODE_URL = "https://open.weixin.qq.com/connect/qrconnect?appid=wxbdc5610cc59c1631&redirect_uri=https%3A%2F%2Fpassport."
      + "yhd.com%2Fwechat%2Fcallback.do&response_type=code&scope=snsapi_login&state=?2#wechat_redirect";

  public static void main(String[] args) {
 

    String state = UUID.randomUUID().toString().replaceAll("-", "");
    
    String url=getStateUrl(state);
 
    JsonObject obj = new JsonObject();
    obj.put("qrcode_url", url); 
    System.out.println(obj);
    
    
    
  }
  
   private static String getStateUrl(String state) {
     return QRCODE_URL.replace("?2", state);
   }

   
}
