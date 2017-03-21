package com.utils;

import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Priority;

/**
 * Created by zhuqiang on 2015/5/12.
 */
public class MyDailyRollingFileAppender extends DailyRollingFileAppender {
    @Override
    public boolean isAsSevereAsThreshold(Priority priority) {
        //只判断相同的范围（层级）
    	 return this.getThreshold().equals(priority);
    }
}