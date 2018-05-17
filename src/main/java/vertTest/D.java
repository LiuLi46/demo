package vertTest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import io.vertx.core.json.JsonObject;

/**  
* <p>Title: D</p>  
* <p>Description: </p>  
* @author liuli  
* @date 2018年5月7日  
*/
public class D {
 // static String chinese = ChineseName.getname();

  private static  Map<String,String> mappingMap;
  static {
    mappingMap=new HashMap<>();
   // mappingMap.put("openid","openid");
    mappingMap.put("nickname","nikename");
    mappingMap.put("sex","sex");
    mappingMap.put("province","province");
    mappingMap.put("city","city");
    mappingMap.put("country","country");
    mappingMap.put("headimgurl","headimgurl");
    mappingMap.put("privilege","privilege");
    mappingMap.put("unionid","unionid");
    mappingMap.put("create_time","create_time");
  }
  public static String getUpdateSql(JsonObject jsonObject) {
    StringBuffer setSql=new StringBuffer();

    Iterator<Map.Entry<String, Object>> iterable = jsonObject.iterator();
    while (iterable.hasNext()) {
      Map.Entry<String, Object> v = iterable.next();

      if(null==mappingMap.get(v.getKey())) continue;

      StringBuffer append = setSql.append(",").append(v.getKey()).append("=").append("'" + v.getValue()+"'");
    
     // System.out.println(append);
      System.out.println(v.getKey());
    }
    System.out.println(jsonObject);
    String sql="UPDATE  `t_user`  t SET "+setSql.toString().replaceFirst(",","")+" WHERE t.`unionid`='"+jsonObject.getString("unionid")+"'";
    System.out.println(sql);
    return sql;
  }
  public static String getInsertSql(JsonObject jsonObject) {
    //创建时间
    String createTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    jsonObject.put("create_time",createTime);

    StringBuffer condition = new StringBuffer();
    StringBuffer values = new StringBuffer();

    Iterator<Map.Entry<String, Object>> iterable = jsonObject.iterator();
    while (iterable.hasNext()) {
      Map.Entry<String, Object> v = iterable.next();

      if(null==mappingMap.get(v.getKey())) continue;

      condition.append("," + v.getKey());
      values.append(",'" + v.getValue()+"'");
    }

    String sql = "insert into `t_user` (" + condition + ") values(" + values + ");";
    return sql.replaceAll("\\(,","(");
  }
  public static void main(String[] args) {
    
    JsonObject userJsonFromWx = new JsonObject();
    userJsonFromWx.put("openid", "1234").put("nickname", "张三").put("sex", "1").put("unionid", "8494891812")
        .put("country", "CN").put("province", "浙江").put("city", "杭州").put("headimgurl",
            "http://wx.qlogo.cn/mmopen/g3MonUZtNHkdmzicIlibx6iaFqAc56vxLSUfpb6n5WKSYVY0ChQKkiaJSgQ1dZuTOgvLLrhJbERQQ4eMsv84eavHiaiceqxibJxCfHe/0");
       
    String updateSql = getUpdateSql(userJsonFromWx);
      System.out.println(updateSql);
      /*   String insertSql = getInsertSql(userJsonFromWx);
    System.out.println(insertSql);*/
          
  }
}

