package com.main.out;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;

public class excelUtils {
	
	private static HSSFWorkbook wb=null;
	
	public static String path="E:\\out\\sampleDate.xls";
	public static List<Map<String, String>> res = new ArrayList<Map<String,String>>();
	
	public static int sequenceNumber=1;
	public static int tempS=1;
	
	public static Map<Integer, String> shopMap=new HashMap<Integer, String>();
	static{
		shopMap.put(0,"shop_id");
		shopMap.put(1,"shop_name");
		shopMap.put(2,"shop_categories");
		shopMap.put(3,"shop_rank");
		shopMap.put(4,"shop_address");
		shopMap.put(5,"latitude") ;
		shopMap.put(6,"longitude") ;
		shopMap.put(7,"shop_telephone") ;
		shopMap.put(8,"shop_spend");
		shopMap.put(9,"can_takeout");
		shopMap.put(10,"shop_scores");
		shopMap.put(11,"open_time") ;
		shopMap.put(12,"comment_tags") ;
		shopMap.put(13,"shop_comment_num");
		shopMap.put(14,"shop_views") ;			
		shopMap.put(15,"shop_branches");
	}
	
   public static HSSFWorkbook getWorkBookSingle() {
        if(wb==null){
            FileInputStream fs;
			try {
				fs = new FileInputStream(path);
				POIFSFileSystem ps=new POIFSFileSystem(fs);  //使用POI提供的方法得到excel的信息  
				 wb=new HSSFWorkbook(ps);
				return wb;
			} catch (Exception e) {
				e.printStackTrace();
			} 
        }
		return wb; 
   }
   public static HSSFSheet getSheetSingle() {
           wb = getWorkBookSingle();
           HSSFSheet sheet=wb.getSheetAt(0);
          /* int i=0;
           while(true){
        	   sheet=wb.getSheetAt(i);
        	   int num = sheet.getLastRowNum();
                if(num>5){
                	i++;
                }else if(num<5){
                   break;
                }else if(num==5){
                	 sheet = wb.createSheet();
                	 break;
                }
           }*/
           return sheet;
  }
   
   public static void main(String[] args) { 
	   HSSFSheet sheet = getSheetSingle();
	   System.out.println(sheet);
}
   
   public static HSSFCell createCell(HSSFRow row,int cellNum) {
	   HSSFCell cell = row.createCell(cellNum);
       row.setHeight((short) 540); 
       CellStyle style = wb.createCellStyle(); // 样式对象      
       // 设置单元格的背景颜色为淡蓝色  
       style.setFillForegroundColor(HSSFColor.PALE_BLUE.index); 
/*       style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);// 垂直      
       style.setAlignment(CellStyle.ALIGN_CENTER);// 水平   
*/       style.setWrapText(true);// 指定当单元格内容显示不下时自动换行
       cell.setCellStyle(style); // 样式，居中
       Font font = wb.createFont();  
       font.setFontName("宋体");  
       style.setFont(font);
	return cell;  
   }
   
   
}
