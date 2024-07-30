package com.lqc.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2017/4/25.
 */
public class TimeUtils {

    //Asia/Kuala_Lumpur +8
    private static final ZoneId defaultZoneId = ZoneId.systemDefault();

    public static LocalDate toLocalDate(Date date) {
        //1. Convert Date -> Instant
        Instant instant = date.toInstant();
        //2. Instant + system default time zone + toLocalDate() = LocalDate
        LocalDate localDate = instant.atZone(defaultZoneId).toLocalDate();
        return localDate;
    }

    public static LocalDateTime toLocalDateTime(Date date) {
        //1. Convert Date -> Instant
        Instant instant = date.toInstant();
        //3. Instant + system default time zone + toLocalDateTime() = LocalDateTime
        LocalDateTime localDateTime = instant.atZone(defaultZoneId).toLocalDateTime();
        return localDateTime;
    }

    public static ZonedDateTime toZonedDateTime(Date date) {
        //1. Convert Date -> Instant
        Instant instant = date.toInstant();
        //4. Instant + system default time zone = ZonedDateTime
        ZonedDateTime zonedDateTime = instant.atZone(defaultZoneId);
        return zonedDateTime;
    }

    public static Date toDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
    }

    public static Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(defaultZoneId).toInstant());
    }

    public static Date toDate(ZonedDateTime zonedDateTime) {
        return Date.from(zonedDateTime.toInstant());
    }

    public static String format(Date date) {
        return toLocalDateTime(date).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
    }

    public static String format(Date date, String pattern) {
        return toLocalDateTime(date).format(DateTimeFormatter.ofPattern(pattern));
    }

    public static Date parse(String date, String pattern) {
        try {
            return toDate(LocalDateTime.parse(date, DateTimeFormatter.ofPattern(pattern)));
        } catch (Exception e) {
            return toDate(LocalDate.parse(date, DateTimeFormatter.ofPattern(pattern)));
        }
    }

    /**
     * 日期加减天数计算
     * @param date  原日期
     * @param amount 加减的天数（减传负数）
     * @return
     */
    public static Date addDays(Date date, Integer amount) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, amount);
        return  cal.getTime();
    }

    /**
     * 日期加减小时数计算
     * @param date  原日期
     * @param ammount 加减的小时数（减传负数）
     * @return
     */
    public static Date addHours(Date date, Integer ammount) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR_OF_DAY, ammount);
        return  cal.getTime();
    }

    public static void main(String[] args) {
    }


    // 获得当天0点时间
    public static Date getTimesmorning() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    // 获得当天24点时间
    public static Date getTimesnight() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 24);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    // 获得昨天0点时间
    public static Date getYesTimesmorning() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) - 1, 0, 0, 0);
        return cal.getTime();
    }

    // 获得昨天24点时间
    public static Date getYesTimessnight() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) - 1, 23, 59, 59);
        return cal.getTime();
    }

    // 获得本周一0点时间
    public static Date getTimesWeekmorning() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return cal.getTime();
    }

    // 获得本周日24点时间
    public static Date getTimesWeeknight() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getTimesWeekmorning());
        cal.add(Calendar.DAY_OF_WEEK, 7);
        return cal.getTime();
    }

    // 获得上周一0点时间
    public static Date getTimesLastWeekmorning() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.add(Calendar.DATE, -7);
        return cal.getTime();
    }

    // 获得上周日24点时间
    public static Date getTimesLastWeeknight() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

    // 获得本月第一天0点时间
    public static Date getTimesMonthmorning() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }

    // 获得本月最后一天24点时间
    public static Date getTimesMonthnight() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 24);
        return cal.getTime();
    }

    // 获得上月第一天0点时间
    public static Date getTimesLastMonthmorning() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(cal.MONTH) - 1, 1, 0, 0, 0);
        return cal.getTime();
    }

    // 获得上月最后一天24点时间
    public static Date getTimesLastMonthnight() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) - 1, cal.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        cal.set(cal.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }

    /**
     * 获取前几天
     *
     * @param d
     * @param day
     * @return
     */
    public static Date getDateBefore(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
        return now.getTime();
    }

    /**
     * 获取几天后
     *
     * @param d
     * @param day
     * @return
     */
    public static Date getDateAfter(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
        return now.getTime();
    }

    /**
     * 日期相隔天数
     *
     * @param startDateInclusive
     * @param endDateExclusive
     * @return
     */
    public static int periodDays(LocalDate startDateInclusive, LocalDate endDateExclusive) {
        return Period.between(startDateInclusive, endDateExclusive).getDays();
    }


    /**
     * 日期相隔天数
     * @param endTime
     * @param startTime
     * @return
     */
    public static int periodDays(Date endTime, Date startTime) {
        LocalDateTime endTimer = LocalDateTime.ofInstant(endTime.toInstant(), ZoneId.systemDefault());
        LocalDateTime startTimer = LocalDateTime.ofInstant(startTime.toInstant(), ZoneId.systemDefault());
        Long days = Duration.between(startTimer, endTimer).toDays();
        return days.intValue();
    }


}
