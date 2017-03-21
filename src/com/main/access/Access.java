package com.main.access;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import com.main.out.IORunnable;
import com.utils.*;

import org.apache.log4j.Logger;

public class Access {
	
	private static LinkedBlockingQueue queue = new LinkedBlockingQueue();
	private static Logger log =Logger.getLogger(Access.class);
	public static void main(String[] args) {
		ExecutorService service = Executors.newSingleThreadExecutor();
		service.execute(new IORunnable(queue));
		DZDemo demo = new DZDemo();
	    log.info("  |||----时间：  "+FormatUtil.dateFormatYMDHMS(new Date())+"--------------启动-------------------------");
		demo.SearchKeyword(queue);
	}
}
