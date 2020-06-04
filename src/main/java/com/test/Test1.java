package com.test;

import com.alibaba.fastjson.JSONObject;
import com.lqc.util.CommonUtil;
import com.lqc.vo.WeekInfo;

import java.net.InetAddress;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Test1 {
    public static void main(String[] args) throws Exception {
        //String url="http://127.0.0.1:8082/q";
        //String res_str= HttpUtils.get(url,null,null);
        //System.out.println("Response String="+res_str);
        //System.out.println("Response String to byte[]="+res_str.getBytes());
        //byte[] res_byte=HttpUtils.getByte(url,null,null);
        //System.out.println("Response byte[]="+res_byte);
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(new Date());
        cal1.add(Calendar.DATE, 1);
        System.out.println("cal1=" + cal1.getTime());
        System.out.println(CommonUtil.compareDay(cal1.getTime(), new Date()));
        System.out.println(CommonUtil.compareDay(new Date(), new Date()));
        try {
            //testStr();
            //System.out.print(CommonUtil.lastMonth());
            //testIP();
            //testDivide();
            //testCal();
            //testJson();
            //testV();
            //testCompareNull();
            testSet();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void testSub() {
        Integer a = 201905;
        String b = a.toString();
        System.out.println(b);
        System.out.println(b.length());
        System.out.println(b.substring(0, 4) + "-" + b.substring(4, 6));

        String c = (--a).toString();
        System.out.println(c);
        String d = c.substring(0, 4) + "-" + c.substring(4, 6);
        System.out.println(d);
    }

    static void testSet() {
        Set<Integer> aa = new HashSet<>();
        aa.add(33);
        int cc = 0;
        for (; ; ) {
            cc++;
            Integer bb = aa.iterator().next();
            if (null == bb) {
                System.out.println(cc + "次结果为null");
                break;
            }
        }
    }

    static void testCompareNull() {
        Integer a = null;
        System.out.print((null != a && 1 == a));
    }

    static void testIP() throws Exception {
        String localIP = InetAddress.getLocalHost().getHostAddress();
        System.out.println("localIP=" + localIP);
        System.out.println("getHostName:" + InetAddress.getLocalHost().getHostName());
        System.out.println("getHostAddress:" + InetAddress.getLocalHost().getHostAddress());
    }

    static void testDivide() {
        int a = 100, b = 1000;
        System.out.println(a / b);
        System.out.println(a % b);
    }

    /**
     * 测试日期
     */
    static void testCal() {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(new Date());
        Calendar c2 = Calendar.getInstance();
        c2.setTime(new Date());
        //c2.add(Calendar.MONTH,1);
        c2.add(Calendar.MONTH, 2);
        System.out.println(sameYearMonth(c1.getTime(), c2.getTime()));
    }

    static void testJson() {
        WeekInfo weekInfo = new WeekInfo();
        weekInfo.dayOfWeek = 2;
        weekInfo.yearMonthDay = "2020-03-12";
        System.out.println(JSONObject.toJSON(weekInfo));
        System.out.println(JSONObject.toJSONString(weekInfo));
        String res = "{\"test\":233,\"dayOfWeek\":3,\"yearMonthDay\":\"2020-03-22\"}";
        weekInfo = JSONObject.parseObject(res, WeekInfo.class);
        System.out.println(weekInfo);
    }

    static void testStr() {
        for (int i = 0; i < 65534; i++) {
            System.out.print("a");
        }
    }

    /**
     * 输出一个int的二进制数
     *
     * @param num
     */
    private static void printInfo(int num) {
        System.out.println(num + "==" + Integer.toBinaryString(num));
    }


    /**
     * 测试位移
     */
    static void testV() {
        int number = 10;
        //原始数二进制
        printInfo(number);
        //左移
        number = number << 3;
        printInfo(number);
        //右移
        number = number >> 2;
        printInfo(number);
        //无符号右移
        number = -3;
        printInfo(number);
        number = number >>> 2;
        printInfo(number);
    }

    static boolean sameYearMonth(Date d1, Date d2) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(d1);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(d2);
        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH);
    }
}
