package vertTest;

/*
 * 
 * @author liuli
 * @date 2018年5月2日
 */
public class W {

  public static void main(String[] args) {
    String unionid = "888766";
    
    String beforeToken =unionid+":"+System.currentTimeMillis()+ ":"
        + (int) (Math.random() * 1000);
    System.out.println(beforeToken);
    //加密
    String token = AESUtil.encrypt(beforeToken);
    System.out.println(token);
    //解密
    String token1 = AESUtil.decrypt(token);
    System.out.println(token1);
    //拆分token
   String[] tokenArray = token1.split(":");
   System.out.println(Long.valueOf(tokenArray[0]));
   
   System.out.println(System.currentTimeMillis());
   
   if (System.currentTimeMillis() >= Long.valueOf(tokenArray[1])) {
     
     System.out.println("yes");
   }else {  System.out.println("no");}
 
  
  }
}
