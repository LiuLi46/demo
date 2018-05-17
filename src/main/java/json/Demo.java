package json;

import java.util.ArrayList;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**  
* <p>Title: Demo</p>  
* <p>Description: </p>  
* @author liuli  
* @date 2018年5月16日  
*/
public class Demo {

  public static void main(String[] args) {
  
    JsonObject Json=new JsonObject(); 
    JsonObject Json1=new JsonObject(); 
    JsonArray books=new JsonArray();  
    Json1.put("book1","java开发").put("BOOK2", "ANDROID");
    System.out.println(Json1);
    books.add(Json1);
    System.out.println(books);
    Json.put("id",1);
    Json.put("username","wp");
    Json.put("books",books);
    System.out.println(Json.encodePrettily().toString());
    
    ArrayList<String> list=new ArrayList<String>();
    
    list.add("java");
    list.add("android");
}
}
