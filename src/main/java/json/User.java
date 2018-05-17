package json;

import io.vertx.core.json.JsonArray;

/**  
* <p>Title: User</p>  
* <p>Description: </p>  
* @author liuli  
* @date 2018年5月16日  
*/
public class User {

  private int id;
  private String name;
  /**
   * @return the id
   */
  public int getId() {
    return id;
  }
  /**
   * @param id the id to set
   */
  public void setId(int id) {
    this.id = id;
  }
  /**
   * @return the name
   */
  public String getName() {
    return name;
  }
  /**
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }
  
  
  public User() {};
  
  
  public User(int id,String name) {};
  
  public static void main(String[] args) {
    
    User user = new User(1001,"张三");

   
    //System.out.println( jsonArray ); 
  }
  
}
