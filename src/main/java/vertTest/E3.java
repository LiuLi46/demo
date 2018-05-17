package vertTest;

/**  
* <p>Title: E3</p>  
* <p>Description: </p>  
* @author liuli  
* @date 2018Äê5ÔÂ3ÈÕ  
*/
public class E3 {

  public static void main(String[] args) {
    
    String decrypt = AESUtil.decrypt("Xlwoa4zL77tZUlMZeHZ1cQqDpjHyNC1PtwpwByovi8k=");
    System.out.println("aaaa"+decrypt);
    String[] split = decrypt.split(":");
    System.out.println(split[0]);
    Long unionID = Long.valueOf(split[0]);
    System.out.println(unionID);
    if(System.currentTimeMillis()<=Long.valueOf(split[1])) {
      
      System.out.println("666666666");
    }else {
      
      System.out.println("666");
    }
 
  }
}
