package vertTest;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class AESUtil {  //初始向量
  public static final String VIPARA = "aabtjkddeeffgghh";   //AES �?16bytes. DES �?8bytes

  //编码方式
  public static final String bm = "UTF-8";

  //私钥
  private static final String ASE_KEY = "aabbchjgeeffgghh";   //AES固定格式�?128/192/256 bits.即：16/24/32bytes。DES固定格式�?128bits，即8bytes�?

  /**
   * 加密
   *
   * @param cleartext
   * @return
   */
  public static String encrypt(String cleartext) {
    //加密方式�? AES128(CBC/PKCS5Padding) + Base64, 私钥：aabbccddeeffgghh
    try {
      IvParameterSpec zeroIv = new IvParameterSpec(VIPARA.getBytes());
      //两个参数，第�?个为私钥字节数组�? 第二个为加密方式 AES或�?�DES
      SecretKeySpec key = new SecretKeySpec(ASE_KEY.getBytes(), "AES");
      //实例化加密类，参数为加密方式，要写全
      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding"); //PKCS5Padding比PKCS7Padding效率高，PKCS7Padding可支持IOS加解�?
      //初始化，此方法可以采用三种方式，按加密算法要求来添加。（1）无第三个参数（2）第三个参数为SecureRandom random = new SecureRandom();中random对象，随机数�?(AES不可采用这种方法)�?3）采用此代码中的IVParameterSpec
      cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
      //加密操作,返回加密后的字节数组，然后需要编码�?�主要编解码方式有Base64, HEX, UUE,7bit等等。此处看服务器需要什么编码方�?
      byte[] encryptedData = cipher.doFinal(cleartext.getBytes(bm));

      return new BASE64Encoder().encode(encryptedData);
    } catch (Exception e) {
      e.printStackTrace();
      return "";
    }
  }

  /**
   * 解密
   *
   * @param encrypted
   * @return
   */
  public static String decrypt(String encrypted) {
    try {
      byte[] byteMi = new BASE64Decoder().decodeBuffer(encrypted);
      IvParameterSpec zeroIv = new IvParameterSpec(VIPARA.getBytes());
      SecretKeySpec key = new SecretKeySpec(
              ASE_KEY.getBytes(), "AES");
      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
      //与加密时不同MODE:Cipher.DECRYPT_MODE
      cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
      byte[] decryptedData = cipher.doFinal(byteMi);
      return new String(decryptedData, bm);
    } catch (Exception e) {
      e.printStackTrace();
      return "";
    }
  }
}
