package test;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;

public class Encode {

	private String separator = "~";
	
	public String encode(String code) {
		if(StringUtils.isBlank(code))
			return "";
		
		
		code = new String(Base64.encodeBase64(code.getBytes()));
		
		code = sort(code);
		
		code = new String(Base64.encodeBase64(code.getBytes()));
		
		return code;
	}
	
	public String encode(String userCode, String psw) {
		if(null != userCode && null != psw)
			return encode(userCode + separator + psw);
		return "";
	}
	
	private  String sort(String code) {
		if(StringUtils.isBlank(code))
			return "";
		char[] datas = code.toCharArray();
		int len = datas.length;
		StringBuffer sb = new StringBuffer();
		for(int i = len - 1; i >= 0; i --) {
			sb.append(datas[i]);
		}
		return sb.toString();
	}
	
	public String decode(String code) {
		if(StringUtils.isBlank(code))
			return "";
		byte[] bs;
//		try {
			bs = Base64.decodeBase64(code.getBytes());
			String temp = new String(bs);
			temp = sort(temp);
			bs = Base64.decodeBase64(temp.getBytes());
			return new String(bs);
//		} catch (Base64DecodingException e) {
//			e.printStackTrace();
//		}
//		return "";
	}
	
	public String encodeString(String ...strings ) {
		StringBuffer sb = new StringBuffer() ;
		if(strings.length > 0) {
			sb.append(strings[0]);
			for(int i = 1; i < strings.length; i ++) {
				sb.append(separator);
				sb.append(strings[i]);
			}
			
			String code = new String(Base64.encodeBase64(sb.toString().getBytes()));
			
			code = sort(code);
			
			code = new String(Base64.encodeBase64(code.getBytes()));
			
			return code;
		}
		
		return null;
	}
	
	public String[] decodeString(String code) {
		if(StringUtils.isBlank(code))
			return null;
		byte[] bs;
		bs = Base64.decodeBase64(code.getBytes());
		String temp = new String(bs);
		temp = sort(temp);
		bs = Base64.decodeBase64(temp.getBytes());
		String tempResult =  new String(bs);
		String[] result = tempResult.split(separator);
		return  result;
	}
	
	
	public static void main(String[] str) {
		Encode encode=new Encode();
		String[]  str1=encode.decodeString("PVUyTTRnalp3SWpaM1VETWxaVE5sSm1ZaGxUTmhKV08wa3pNalJXWXdFVForRlRaNWxXYw==");
		
		System.out.println(str1[0]+" "+str1[1]);
		
	}

	
}
