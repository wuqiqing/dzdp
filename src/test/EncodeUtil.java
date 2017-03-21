package test;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

import org.apache.commons.lang.StringUtils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 编码工具类 1.将byte[]转为各种进制的字符串 2.base 64 encode 3.base 64 decode 4.获取byte[]的md5值
 * 5.获取字符串md5值 6.结合base64实现md5加密 7.AES加密 8.AES加密为base 64 code 9.AES解密 10.将base
 * 64 code AES解密
 * 
 * @author uikoo9
 * @version 0.0.7.20140601
 */
public class EncodeUtil {

	public static void main(String[] args) throws Exception
	{
/*		String content = "zyc_user001zhaold1zyc_user001";
		System.out.println("加密前：" + content);

		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String key = formatter.format(currentTime);

		System.out.println("加密密钥和解密密钥：" + key);

		String encrypt = aesEncrypt(content, key);
		System.out.println("加密后：" + encrypt);

		String decrypt = aesDecrypt("fgCo5kxDRKa6aEBD7P9ihw==", key);
		System.out.println("解密后：" + decrypt);*/
		
		String x = "R0lGODlhAQABAIAAAAAAAP";
		String y = "H5BAEAAAAALAAAAAABAAEAQAIBRAA7";
		String sx = new String(base64Decode(x),"GBK");
		System.out.println(sx);
		
	}

	/**
	 * 将byte[]转为各种进制的字符串
	 * 
	 * @param bytes
	 *            byte[]
	 * @param radix
	 *            可以转换进制的范围，从Character.MIN_RADIX到Character.MAX_RADIX，超出范围后变为10进制
	 * @return 转换后的字符串
	 */
	public static String binary(byte[] bytes, int radix)
	{
		return new BigInteger(1, bytes).toString(radix);// 这里的1代表正数
	}

	/**
	 * base 64 encode
	 * 
	 * @param bytes
	 *            待编码的byte[]
	 * @return 编码后的base 64 code
	 */
	public static String base64Encode(byte[] bytes)
	{
		return new BASE64Encoder().encode(bytes);
	}

	/**
	 * base 64 decode
	 * 
	 * @param base64Code
	 *            待解码的base 64 code
	 * @return 解码后的byte[]
	 * @throws Exception
	 */
	public static byte[] base64Decode(String base64Code) throws Exception
	{
		return isEmpty(base64Code) ? null : new BASE64Decoder()
				.decodeBuffer(base64Code);
	}

	/**
	 * 获取byte[]的md5值
	 * 
	 * @param bytes
	 *            byte[]
	 * @return md5
	 * @throws Exception
	 */
	public static byte[] md5(byte[] bytes) throws Exception
	{
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(bytes);

		return md.digest();
	}

	/**
	 * 获取字符串md5值
	 * 
	 * @param msg
	 * @return md5
	 * @throws Exception
	 */
	public static byte[] md5(String msg) throws Exception
	{
		return isEmpty(msg) ? null : md5(msg.getBytes());
	}

	/**
	 * 结合base64实现md5加密
	 * 
	 * @param msg
	 *            待加密字符串
	 * @return 获取md5后转为base64
	 * @throws Exception
	 */
	public static String md5Encrypt(String msg) throws Exception
	{
		return isEmpty(msg) ? null : base64Encode(md5(msg));
	}
	
	/**
	 * 生成KEY的方法
	 * 
	 * @param key 
	 * @return key
	 */
	public static Key getKey(String strKey) {
	    try {
	          if (strKey == null) {
	           strKey = "";
	         }
	         KeyGenerator _generator = KeyGenerator.getInstance("AES");
	         SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
	         secureRandom.setSeed(strKey.getBytes());
	         _generator.init(128, secureRandom);
	        return _generator.generateKey();
	        } catch (Exception e) {
	            throw new RuntimeException(" 初始化密钥出现异常 ");
	      }
	}
	/**
	 * AES加密
	 * 
	 * @param content
	 *            待加密的内容
	 * @param encryptKey
	 *            加密密钥
	 * @return 加密后的byte[]
	 * @throws Exception
	 */
	public static byte[] aesEncryptToBytes(String content, String encryptKey)
			throws Exception
	{
		SecureRandom sr = new SecureRandom();
        Key secureKey = getKey(encryptKey);
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secureKey, sr);

		return cipher.doFinal(content.getBytes("utf-8"));
	}

	/**
	 * AES加密为base 64 code
	 * 
	 * @param content
	 *            待加密的内容
	 * @param encryptKey
	 *            加密密钥
	 * @return 加密后的base 64 code
	 * @throws Exception
	 */
	public static String aesEncrypt(String content, String encryptKey)
			throws Exception
	{
		return base64Encode(aesEncryptToBytes(content, encryptKey));
	}

	/**
	 * AES解密
	 * 
	 * @param encryptBytes
	 *            待解密的byte[]
	 * @param decryptKey
	 *            解密密钥
	 * @return 解密后的String
	 * @throws Exception
	 */
	public static String aesDecryptByBytes(byte[] encryptBytes,
			String decryptKey) throws Exception
	{
		
		SecureRandom sr = new SecureRandom();
        Cipher cipher = Cipher.getInstance("AES");
        Key secureKey = getKey(decryptKey);
        cipher.init(Cipher.DECRYPT_MODE, secureKey, sr);
        
		byte[] decryptBytes = cipher.doFinal(encryptBytes);

		return new String(decryptBytes);
	}

	/**
	 * 将base 64 code AES解密
	 * 
	 * @param encryptStr
	 *            待解密的base 64 code
	 * @param decryptKey
	 *            解密密钥
	 * @return 解密后的string
	 * @throws Exception
	 */
	public static String aesDecrypt(String encryptStr, String decryptKey)
			throws Exception
	{
		return isEmpty(encryptStr) ? null : aesDecryptByBytes(
				base64Decode(encryptStr), decryptKey);
	}

	/**
	 * 判断字符串是否为空
	 * 
	 * @param str
	 * @return true or false
	 */
	public static boolean isEmpty(String str)
	{
		return (str == null || str.trim().length() == 0);
	}
	
	public static String encodeMd5(String str) {
		if (StringUtils.isBlank(str)) {
			return str;
		}
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(str.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			System.out.println("NoSuchAlgorithmException caught!");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		byte[] byteArray = messageDigest.digest();
		StringBuffer md5StrBuff = new StringBuffer();
		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
				md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
			} else {
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
			}
		}
		return md5StrBuff.toString();
	}

}