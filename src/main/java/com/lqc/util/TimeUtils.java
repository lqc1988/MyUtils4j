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
}
