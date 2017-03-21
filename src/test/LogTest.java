package test;

import org.apache.log4j.Logger;

import com.main.access.DZDemo;

public class LogTest {
	
	private static Logger log = Logger.getLogger(LogTest.class);
    
	public static void main(String[] args) {
		//this 在web模块下
		log.info("webInfo-------------------- 信息-----end");
		log.error("webError-------------- 信息-----end");
        //用core模块中的类来获取log对象
        Logger core = Logger.getLogger(new DZDemo().getClass());
        core.info("coreInfo ------------------------信息--end");
        core.error("coreError  ----------------------信息--end");
	}
}
