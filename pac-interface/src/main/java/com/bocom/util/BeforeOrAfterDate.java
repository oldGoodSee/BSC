package com.bocom.util;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by MyPC on 2016/12/12.
 */
public class BeforeOrAfterDate {

    /**
     * 查询当前日期前(后)x天的日期
     *
     * @param date 当前日期
     * @param day  天数（如果day数为负数,说明是此日期前的天数）
     * @return yyyy-MM-dd
     */
    public static String beforNumDay(Date date, int day) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_YEAR, day);
        return new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
    }

    /**
     * 查询当前日期前(后)x天的日期
     *
     * @param date 当前日期
     * @param day  天数（如果day数为负数,说明是此日期前的天数）
     * @return yyyyMMdd
     */
    public static String beforNumberDay(Date date, int day) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_YEAR, day);
        return new SimpleDateFormat("yyyyMMdd").format(c.getTime());
    }

    /**
     * 查询当前日期前(后)x天的日期
     *
     * @param millis 当前日期毫秒数
     * @param day    天数（如果day数为负数,说明是此日期前的天数）
     * @return long 毫秒数只显示到天，时间全为0
     * @throws ParseException
     */
    public static long beforDateNum(long millis, int day) throws ParseException {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(millis);
        c.add(Calendar.DAY_OF_YEAR, day);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(c.getTimeInMillis());
        Date newDate = sdf.parse(sdf.format(date));
        return newDate.getTime();
    }

    /**
     * 查询当前日期前(后)x天的日期
     *
     * @param millis 当前日期毫秒数
     * @param day    天数（如果day数为负数,说明是此日期前的天数）
     * @return yyyy-MM-dd
     */
    public static String beforLongDate(long millis, int day) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(millis);
        c.add(Calendar.DAY_OF_YEAR, day);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(c.getTimeInMillis());
        return sdf.format(date);
    }

    public static void main(String[] args) {
        try {
            BeforeOrAfterDate bd = new BeforeOrAfterDate();
            long nowDate = System.currentTimeMillis();
            System.out.println("nowDate = " + nowDate);
            long beforDate = bd.beforDateNum(nowDate, 3);
            System.out.println("beforDate = " + beforDate);
            Date date = new Date(beforDate);
            System.out.println("毫秒值结果日期 = " + date.toLocaleString());
            System.out.println("yyyyMMdd结果日期  = " + bd.beforNumberDay(new Date(nowDate), 3));
            System.out.println("yyyy-MM-dd结果日期  = " + bd.beforNumDay(new Date(nowDate), 3));
            System.out.println("毫秒值获取日期结果 = " + bd.beforLongDate(nowDate, 3));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}