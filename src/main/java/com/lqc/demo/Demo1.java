package com.lqc.demo;


import com.alibaba.fastjson.JSON;
import com.lqc.util.TimeUtils;
import com.lqc.vo.WeekInfo;

import java.math.BigDecimal;
import java.util.*;

/**
 * @ClassName Demo1
 * @Description TODO
 * @Author liqinchao
 * @Date 2021/9/1 11:02
 * @Version 1.0
 **/
public class Demo1 {
	public static void main(String[] args) {
//		System.out.println(BigDecimal.valueOf(0.00D));
//		System.out.println(BigDecimal.valueOf(0.00D).intValue()==0);
//		compareStr("20", "20");
//		compareStr("20", "21");
//		compareStr("20", "30");
//		compareStr("30", "20");
		System.out.println("SELECT * FROM order_shipment WHERE  order_shipment.carrier_code = 'debangwuliu'".replaceAll("\\*","order_no"));
	}

	static void compareStr(String a, String b) {
		System.out.println("a:"+a+"，b:"+b+"，compare: a>b="+a.compareTo(b));
	}
	static int compareVersion(String version1, String version2) {
		String arr1[] = version1.split(".");
		String arr2[] = version2.split(".");
		return 0;
	}

	static void test1() {
//        String url = "http://project-homedo.oss-cn-shanghai.aliyuncs.com/
//        c9865094-b617-4e98-8d52-f8dfd1b08a83_9d6c2674-21b6-440e-bf0a-bbecfb6f72f9_DOCX测试方案.docx";
//        try {
//            HttpUtils.get(url, null, null);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        try {
//            MyFileUtils.downRemoteFile(url,"E:/3/1.docx");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//        System.out.println(5000%10000);
//        Date date = TimeUtils.parse("1900-01-01 00:00:00","yyyy-MM-dd HH:mm:ss");
//        System.out.println(date);
//        System.out.println(date.getTime());
//        WeekInfo weekInfo1 = new WeekInfo();
//        weekInfo1.dayOfWeek = 1;
//        WeekInfo weekInfo2 = new WeekInfo();
//        weekInfo2.dayOfWeek = 2;
//        WeekInfo weekInfo3 = new WeekInfo();
//        weekInfo3.dayOfWeek = 3;
//
//        List<WeekInfo> list1 = new ArrayList();
//        list1.add(weekInfo1);
//        list1.add(weekInfo2);
//        list1.add(weekInfo3);
//        System.out.println(list1.size());
//        System.out.println(JSON.toJSON(list1));
//
//        List<WeekInfo> list2 = Arrays.asList(weekInfo1,weekInfo2,weekInfo3);
//        System.out.println(list2.size());
//        System.out.println(JSON.toJSON(list2));

		int[] array = {1, 2, 3};
		List myList = Arrays.asList(array);
		System.out.println(myList.size());
		System.out.println(myList.get(0));

		String[] myArray = {"Apple", "Banana", "Orange"};
		List<String> myList2 = Arrays.asList(myArray);
		System.out.println(myList2.size());
		System.out.println(myList2.get(0));
	}
}
