package com.lqc.vo;

/**
 * ClassName : WeekInfo
 * Author : liqinchao
 * CreateTime : 2019/3/18 18:11
 * Description : 周信息VO
 */
public class WeekInfo {
	/**
	 * 周几：1~7
	 */
	public int dayOfWeek;
	/**
	 * 年月日
	 * yyyy-MM-dd
	 */
	public String yearMonthDay;

	@Override
	public String toString() {
		return "WeekInfo{" +
				"dayOfWeek=" + dayOfWeek +
				", yearMonthDay='" + yearMonthDay + '\'' +
				'}';
	}
}
