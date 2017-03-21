package com.main.out;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;

import com.main.parseHTML.Process;
import com.utils.FormatUtil;
public class IORunnable implements Runnable {
	
	private static Logger log = Logger.getLogger(IORunnable.class);
	
	private  LinkedBlockingQueue<Map<String,String>> qp = null;
	
	public IORunnable(LinkedBlockingQueue<Map<String,String>> queue) {
		this.qp=queue;
	}
	public void dispatchTask() {
        while(true){
        	Map<String,String> map = (Map<String,String>)qp.poll();
       		try {
       			if(map!=null){
				   writer(excelUtils.path,map);
       			}
			} catch (IOException e) {
				e.printStackTrace();
			}
       	 }
	}
	@Override
	public void run() {
		this.dispatchTask();
	}

	
	 public  void writer(String path, Map<String, String> map) throws IOException {  
		    HSSFSheet sheet = excelUtils.getSheetSingle();
	        //循环写入行数据   
	            HSSFRow row=sheet.createRow((short)(sheet.getLastRowNum()+1)); //在现有行号后追加数据  
	            row.setHeight((short) 540); 
	            row.setHeight((short) 500); 
	            for(int j=0;j<15;j++){
	        	 String value = excelUtils.shopMap.get(j);
		        	 if(value==null){
		        		 continue;
		        	 }else{
		        		 HSSFCell cell = excelUtils.createCell(row, j);
		        		 cell.setCellValue(map.get(value));
		        	 }
	         }
	        //创建文件流   
	        OutputStream stream = new FileOutputStream(path);  
	        //写入数据   
	        excelUtils.getWorkBookSingle().write(stream);  
	        //关闭文件流   
	        stream.close();  
	        log.info("------时间：  "+FormatUtil.dateFormatYMDHMS(new Date())+" shopId:"+map.get("shop_id")+" 写入excel...第 "+(sheet.getLastRowNum()+1)+" 行");
	    } 


}
