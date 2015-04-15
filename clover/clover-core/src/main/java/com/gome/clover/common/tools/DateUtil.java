package com.gome.clover.common.tools;

import org.quartz.CronExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ━━━━━━神兽出没━━━━━━
 * 　　　┏┓　　　┏┓
 * 　　┏┛┻━━━┛┻┓
 * 　　┃　　　　　　　┃
 * 　　┃　　　━　　　┃
 * 　　┃　┳┛　┗┳　┃
 * 　　┃　　　　　　　┃
 * 　　┃　　　┻　　　┃
 * 　　┃　　　　　　　┃
 * 　　┗━┓　　　┏━┛
 * 　　　　┃　　　┃神兽保佑, 永无BUG!
 * 　　　　┃　　　┃Code is far away from bug with the animal protecting
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 * ━━━━━━感觉萌萌哒━━━━━━
 * Module Desc:日期工具类
 * User: wangyue-ds6 || stark_summer@qq.com
 * Date: 2014/11/21
 * Time: 14:27
 */

public class DateUtil {

    private final static Logger logger = LoggerFactory.getLogger(DateUtil.class);

    private final static  String pattern = "yyyy-MM-dd HH:mm:ss";

    public static long getServerTime() {
        return System.currentTimeMillis();
    }

    /**
     * 格式化日期(String-->Date),默认返回yyyy-MM-dd HH:mm:ss
     * @param date 日期
     * @return
     */
    public static String formatWithDefaultPattern(Date date) {
        return format(date,pattern);
    }

    /**
     * 格式化显示当前日期
     * @param pattern 日期格式格式
     * @return String
     */
    public static String formatWithCurrentTime(String pattern) {
        return format(new Date(),pattern);
    }

    /**
     * 日期格式化(Date-->String)
     * @param date 日期
     * @param pattern 日期格式格式
     * @return
     */
    public static String format(Date date, String pattern) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            return sdf.format(date);
        } catch (Exception e) {
            logger.error("DateUtil-->>format(" + date + "," + pattern + ") error", e.getMessage());
            throw new RuntimeException("DateUtil-->>format("+date+","+pattern+") error,"+ e.getMessage());
        }
    }

    /**
     * 日期格式化(String-->Date),默认返回yyyy-MM-dd HH:mm:ss
     * @param dateStr 字符串格式日期
     * @return Date
     */
    public static Date formatWithDefaultPattern(String dateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            return sdf.parse(dateStr);
        } catch (Exception e) {
            logger.error("DateUtil-->>formatWithDefaultPattern("+dateStr+") error", e.getMessage());
             throw new RuntimeException("DateUtil-->>formatWithDefaultPattern("+dateStr+") error,"+ e.getMessage());
        }
    }

    /**
     * 日期格式化(String-->Date)
     * @param dateStr 字符串格式日期
     * @param pattern
     * @return Date
     */
    public static Date format(String dateStr, String pattern) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            return sdf.parse(dateStr);
        } catch (Exception e) {
            logger.error("DateUtil-->>format("+dateStr+","+pattern+") error", e.getMessage());
            throw new RuntimeException("DateUtil-->>format("+dateStr+","+pattern+") error,"+ e.getMessage());
        }
    }

    /**
     * 时间格式化， 传入毫秒
     * @param time
     * @return
     */
    public static String dateFormat(long time) {
        return format(new Date(time), "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 获取当前日期时间字符串
     * @return
     */
    public static String currentDateTime() {
        Date date = new Date();
        date.setTime(System.currentTimeMillis());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return df.format(date);
        } catch (Exception e) {
            logger.error("DateUtil-->>currentDateTime() error", e.getMessage());
            throw new RuntimeException("DateUtil-->>currentDateTime() error,"+ e.getMessage());
        }
    }

    public static  Date getNextValidTimeAfter(String cronExpressionStr, Date currentDate) throws ParseException {
        CronExpression cronExpression = new CronExpression(cronExpressionStr);
        return cronExpression.getNextValidTimeAfter(currentDate);
    }
}
