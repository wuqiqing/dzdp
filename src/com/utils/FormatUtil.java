package com.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>
 * Title: 
 * </p>
 * <p>
 * Description:
 * </p>
 * @create 2009-7-4 by ASAP TEAM
 * @author <a href="mailto:shiming@ec.com.cn">shiming</a>
 * @version 1.0
 * @CopyRight www.ec.com.cn
 */

public class FormatUtil {
	
	public static Format _dateFormatyMd = new SimpleDateFormat("yyyy-MM-dd");
    public static Format _dateFormatMd = new SimpleDateFormat("MM/dd");
    public static Format _dateFormaty = new SimpleDateFormat("yyyy");
	public static Format _dateFormatyMdHms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static String NumberFormatStr ="#.###";
	/**
	 * 格式化数字、默认格式为小数后三位
	 * @param value
	 * @return
	 */
	public static String parseDouble3(double value){
		return parseDouble(value,NumberFormatStr);
	}
	
	/**
	 * 根据指定格式,格式化数字
	 * 格式串如:#;#.#;#.##以此类推
	 * @param value
	 * @param formatStr
	 * @return
	 */
	public static String parseDouble(double value,String formatStr){
		Format _numberFormat = new java.text.DecimalFormat(formatStr);
		return _numberFormat.format(value);
	}
	
	public static String parseDoubleExcel(String value){//add-20141203-excel导出数据的科学计数法转换
		Double v = Double.valueOf(value);
		return parseDouble(v,"#.####");
	}
	
	public static String dateFormatYMD(Date date){
		return _dateFormatyMd.format(date);
	}
	public static String dateFormatY(Date date){
		return _dateFormaty.format(date);
	}
	
	public static String dateFormatYMDHMS(Date date){
		return _dateFormatyMdHms.format(date);
	}
	public static String dateFormatyyyy(Date date){
	   return  _dateFormaty.format(date);	
	}
	
	public static Date parseStringToDate(String dateStr) throws ParseException{
		return (Date)_dateFormatyMd.parseObject(dateStr);
	}
	public static Date parseStringToDatey(String dateStr) throws ParseException{
		return (Date)_dateFormaty.parseObject(dateStr);
	}
	
	public static String encoder(String str){
		try {
			return URLEncoder.encode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;
	}
	
	public static String decoder(String str){
		try {
			return URLDecoder.decode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;
	}
}
