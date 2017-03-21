package test;
import java.util.List;  

import org.apache.log4j.Logger;
  
public class TestObservable {  
      
    static Logger chargeLogger = Logger.getLogger("charge_log");  
    static Logger goldLogger = Logger.getLogger("gold_log");  
      
      
    public void setSource(List<?> objectList,String loggerType) throws Exception {  
        if(objectList == null || objectList.isEmpty()) {  
            return;  
        }
        for(Object obj : objectList) {  
  
            switch(loggerType) {  
            case "CHARGE_LOG" :  
                chargeLogger.info(obj.toString());  
                break;  
            case "GOLD_LOG" :  
                goldLogger.info(obj.toString());  
                break;  
            default :  
                throw new Exception("Type not suitable.");  
            }  
        } 
    }  
  
    public void setSource(Object obj,String loggerType) throws Exception {  
        if(obj == null) {  
            return;  
        }  
        switch(loggerType) {  
        case "CHARGE_LOG" :  
            chargeLogger.info(obj.toString());  
            break;  
        case "GOLD_LOG" :  
            goldLogger.info(obj.toString());  
            break;  
        default :  
            throw new Exception("Type not suitable.");  
        }  
    }  
      
    public static void main(String[] args) {  
      TestObservable  abservable = new TestObservable();
      try {
    	   abservable.setSource("gole_log", goldLogger.getName());
           abservable.setSource("charge_log", chargeLogger.getName());
           } catch (Exception e) {
        	  e.printStackTrace();
           }
      }
 }
      
      