package com.windcoder.common.utills;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Description:
 * User: WindCoder
 * Date: 2017-09-29
 * Time: 19:12 下午
 */
public class DateUtillZ {
    /**
     * yyyy-MM-dd HH:mm:ss 格式
     */
    public static final String DEFAULT_DATE_TIME_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";
    /**
     * yyyy-MM-dd HH:mm 格式
     */
    public static final String DEFAULT_DATE_TIME_HHmm_FORMAT_PATTERN = "yyyy-MM-dd HH:mm";
    /**
     * yyyy-MM-dd HH 格式
     */
    public static final String DEFAULT_DATE_TIME_HH_FORMAT_PATTERN = "yyyy-MM-dd HH";
    /**
     * yyyy-MM-dd 格式
     */
    public static final String DEFAULT_DATE_FORMAT_PATTERN = "yyyy-MM-dd";
    /**
     * HH:mm:ss 格式
     */
    public static final String DEFAULT_TIME_FORMAT_PATTERN = "HH:mm:ss";
    /**
     * HH:mm 格式
     */
    public static final String DEFAULT_TIME_HHmm_FORMAT_PATTERN = "HH:mm";


    /**
     * 获得当前时间的时间戳
     * @return 返回Timestamp类型的当前时间的时间戳
     */
    public static Timestamp getTimestampOfNow(){
        return new Timestamp(System.currentTimeMillis());
    }




    /**
     * 根据开始与结束日期获取相减得到的天数
     * @param beginDateStr
     * @param endDateStr
     * @return
     */
    public static long getDaySub(String beginDateStr,String endDateStr){
        long day=0;
        SimpleDateFormat format = new SimpleDateFormat(DEFAULT_DATE_FORMAT_PATTERN);
        Date beginDate;
        Date endDate;
        try{
            beginDate = format.parse(beginDateStr);
            endDate= format.parse(endDateStr);
            day=(endDate.getTime()-beginDate.getTime())/(24*60*60*1000);
            //System.out.println("相隔的天数="+day);
        } catch (ParseException e){
            // TODO 自动生成 catch 块
            e.printStackTrace();
        }
        return day;
    }

    /**
     * 日期 Date 转 String
     * @param date 要转的日期
     * @param type 要转成的格式
     * @return 转换的结果
     */
    public static String DateToString(Date date,String type){
        SimpleDateFormat dateFormat = new SimpleDateFormat(type);
        return dateFormat.format(date);
    }



    /***
     * 获得当前时间的时间戳 String 类型
     * @param type 时间戳的样式
     * @return 返回String类型的当前时间的时间戳
     */
    public static String getTimestampStrOfNow(String type){
        SimpleDateFormat dateFormat = new SimpleDateFormat(type);
        return dateFormat.format(System.currentTimeMillis());
    }

    public static long getDaySubOfTimestamp(Timestamp beginTime,Timestamp endTime){
        return  endTime.getTime()-beginTime.getTime();
    }



    /**
     * 获取今天的日期字符串
     * （短日期）
     * @return 日期字符串
     */
    public static String getTodayString(){
        return DateToString(new Date(), DEFAULT_DATE_FORMAT_PATTERN);
    }

    /**
     * 时间字符串转为新的type样式的时间字符串
     * @param dateString
     * @param type
     * @return
     */
    public static String StringToNewStringOfDate(String dateString,String type){
        SimpleDateFormat sdf = new SimpleDateFormat(type);
        Date date = null;
        try {
            date = sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sdf.format(date);
    }

    /**
     * 时间 String 转 Date
     * @param dateString
     * @param type
     * @return
     */
    public static Date StringToDate(String dateString,String type){
        SimpleDateFormat sdf = new SimpleDateFormat(type);
        Date date = null;
        try {
            date = sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 时间 String 转 Calendar
     * @param dateString
     * @param type
     * @return
     */
    public static Calendar StringtoCalendar(String dateString, String type){
        SimpleDateFormat sdf= new SimpleDateFormat(type);
        Date date = null;
        try {
            date = sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    /**
     * 在指定日期上加一天
     * @param date
     * @return
     */
    public static Date addDateOneDay(Date date) {
        if (null == date) {
            return date;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);   //设置当前日期
        c.add(Calendar.DATE, 1); //日期加1天
//     c.add(Calendar.DATE, -1); //日期减1天
        date = c.getTime();
        return date;
    }


    /**
     * // 获取当日期的前后日期 i为正数 向后推迟i天，负数时向前提前i天
     * @param i
     * @return
     */
    public static Date getNumdate(int i) {
        Calendar cd = Calendar.getInstance();
        cd.add(Calendar.DATE, i);
        return cd.getTime();
    }

    /**
     * 获取某月的第一天
     *  0当月
     *  -1 前一个月
     *  1 后一个月
     * @param i
     * @return
     */
    public static Date getNumMonth(int i){
        Calendar   cal_1=Calendar.getInstance();//获取当前日期
        cal_1.add(Calendar.MONTH, i);
        cal_1.set(Calendar.DAY_OF_MONTH,1);
        return cal_1.getTime();
    }
    /**
     * 获取本周日的日期
     * @return
     */
    public static Date getSundayOfThisWeek() {
        Calendar c = Calendar.getInstance();
        int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0)
            day_of_week = 7;
        c.add(Calendar.DATE, -day_of_week + 7);
        return c.getTime();
    }
    /**
     * 获取本周一的日期
     * @return
     */
    public static Date getMondayOfThisWeek() {
        Calendar c = Calendar.getInstance();
        int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0)
            day_of_week = 7;
        c.add(Calendar.DATE, -day_of_week + 1);
        return c.getTime();
    }
}
