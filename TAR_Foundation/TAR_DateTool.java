package com.huanwei.TAR_UtilsForAndroid.TAR_Foundation;

import android.support.annotation.Nullable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2018/12/12.
 */

/**
 * 日期时间类.
 */
public class TAR_DateTool {



    /**
     * 将年月日的int转成date
     * @param year 年
     * @param month 月 1-12
     * @param day 日
     * 注：月表示Calendar的月，比实际小1
     */
    public static Date getDate(int year, int month, int day) {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.set(year, month - 1, day);
        return mCalendar.getTime();
    }
    /**
     * 求两个日期相差天数
     *
     * @param strat 起始日期，格式yyyy-MM-dd
     * @param end 终止日期，格式yyyy-MM-dd
     * @return 两个日期相差天数
     */
    public static long getIntervalDays(String strat, String end) {
        return ((java.sql.Date.valueOf(end)).getTime() - (java.sql.Date
                .valueOf(strat)).getTime()) / (3600 * 24 * 1000);
    }
    /**
     * 获得当前年份
     * @return year(int)
     */
    public static int getCurrentYear() {
        Calendar mCalendar = Calendar.getInstance();
        return mCalendar.get(Calendar.YEAR);
    }
    /**
     * 获得当前月份
     * @return month(int) 1-12
     */
    public static int getCurrentMonth() {
        Calendar mCalendar = Calendar.getInstance();
        return mCalendar.get(Calendar.MONTH) + 1;
    }
    /**
     * 获得当月几号
     * @return day(int)
     */
    public static int getDayOfMonth() {
        Calendar mCalendar = Calendar.getInstance();
        return mCalendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 取得给定日期加上一定天数后的日期对象.
     *
     * @param date 给定的日期对象
     * @param amount 需要添加的天数，如果是向前的天数，使用负数就可以.
     * @return Date 加上一定天数以后的Date对象.
     */
    public static Date getCalcDate(Date date, int amount) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, amount);
        return cal.getTime();
    }


    /**
    * 将时间转换为时间戳
     *
    */
    public String dateToStamp(String s, SimpleDateFormat simpleDF) throws ParseException {
        if (s.length()<=1){
            return "";
        }
        String res;
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat simpleDateFormat = simpleDF;
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        if (ts>1000){
            ts = ts/1000;
        }
        res = String.valueOf(ts);
        return res;
    }

    /**
     * 时间戳转换成日期格式字符串
     * @param seconds 精确到秒的字符串
     * @param
     * @return
     */
    public static String timeStampToDate(String seconds,@Nullable String format) {
        if(seconds == null || seconds.isEmpty() || seconds.equals("null")){
            return "";
        }
        if(format == null || format.isEmpty()){
            format = "yyyy-MM-dd HH:mm:ss";
        }
//        long timeStamp = Long.valueOf(seconds);
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds)));
    }

    /**
     * 获取当前时间的时间戳
     * */
    public static String getTimeStamp(String time, SimpleDateFormat simpleDF) {
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat df = simpleDF;
        Date date = null;
        try {
            date = df.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        long timestamp = cal.getTimeInMillis();
//        System.out.println(timestamp/1000);

        return String.format("%ld", timestamp / 1000);
    }

    /**
     * 获取当前时间 2019-03-21 16:45:53
     * */
    public static String getCurrentTime(String timeFormat) {
        String pattern;
        if (timeFormat==null || timeFormat.length()<1){
            pattern = "yyyy-MM-dd HH:mm:ss";
        }else {
            pattern = timeFormat;
        }
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        Date curDate = new Date(System.currentTimeMillis());
        String time = formatter.format(curDate);
        return time;
    }






}
