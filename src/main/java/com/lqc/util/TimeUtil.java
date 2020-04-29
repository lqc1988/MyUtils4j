package com.lqc.util;

import java.util.Calendar;
import java.util.Date;

public class TimeUtil {
	
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
	cal.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH)-1,0,0,0);
	return cal.getTime();
	}

	// 获得昨天24点时间
	public static Date getYesTimessnight() {
	Calendar cal = Calendar.getInstance();
	cal.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH)-1,23,59,59);
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
	cal.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0,0);
	cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
	cal.add(Calendar.DATE,-7);
	return cal.getTime();
	}

	// 获得上周日24点时间
	public static Date getTimesLastWeeknight() {
	Calendar cal = Calendar.getInstance();
	cal.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 23, 59,59);
	cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
	cal.add(Calendar.DATE,-1);
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
	cal.set(cal.get(Calendar.YEAR),cal.get(cal.MONTH)-1,1,0,0,0);
	return cal.getTime();
	}

	// 获得上月最后一天24点时间
	public static Date getTimesLastMonthnight() {
	Calendar cal = Calendar.getInstance();
	cal.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH)-1,cal.get(Calendar.DAY_OF_MONTH),23,59,59);
	cal.set(cal.DAY_OF_MONTH,cal.getActualMaximum(Calendar.DAY_OF_MONTH));
	return cal.getTime();
	}
	/**
	 * 获取前几天
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


	}

