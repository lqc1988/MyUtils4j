package com.test;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Date;

/**
 * @ClassName Test2
 * @Description TODO
 * @Author liqinchao
 * @Date 2022/2/28 10:41
 * @Version 1.0
 **/
public class Test2 {
	public static void main(String[] args) {
		System.out.println(DateFormatUtils.format(new Date(),"yyyy-MM-dd"));
	}
}
