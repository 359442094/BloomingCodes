package cn.blooming.bep.crawler.model.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 统计爬取用时
 * */
public class TimeUtil {

    private final static Logger logger= LoggerFactory.getLogger(TimeUtil.class);

    public static String endTime(long endTime,long starTime){

        long totalMilliSeconds=endTime-starTime;

        long totalSeconds = totalMilliSeconds / 1000;

        //求出现在的秒
        long currentSecond = totalSeconds % 60;

        //求出现在的分
        long totalMinutes = totalSeconds / 60;
        long currentMinute = totalMinutes % 60;

        //求出现在的小时
        long totalHour = totalMinutes / 60;
        long currentHour = totalHour % 24;

        String log="用时:"+currentHour + " 时 " + currentMinute + " 分 " + currentSecond + " 秒 ";
        //显示时间
        logger.debug(log);
        return log;
    }
}
