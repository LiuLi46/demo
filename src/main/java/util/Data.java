package util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Data {


  public static String getDate() {

  Date date = new Date();

  System.out.println(date);

  SimpleDateFormat slf = new SimpleDateFormat("yyyy-MM-dd");
  String format = slf.format(date);
  System.out.println(format);
  
  return format;
}

 
}
