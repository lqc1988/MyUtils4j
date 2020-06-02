package com.lqc.test;

import com.alibaba.fastjson.JSONObject;
import com.lqc.util.qrcode.QRCodeUtils;
import com.lqc.vo.WeekInfo;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Calendar;
import java.util.Date;

/**
 * ClassName : Test1
 * Author : liqinchao
 * CreateTime : 2019/3/27 16:37
 * Description : 测试类
 */
public class Test1 {
    public static void main(String[] args) {
        try {
            //testStr();
            //System.out.print(CommonUtil.lastMonth());
            //testIP();
            //testDivide();
            //testCal();
            //testJson();
            //testV();
            //testCompareNull();
            qrCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void qrCode() {
        //String content = "https://downloadpkg.app3c.cn/app/download?path=http://A6982009354795.qiniucdn.apicloud-system.com/6a1b49678efc7ce7f800e79dd74b1aa0_d&ver=0.1.12&size=4.78M";
        String content = "http://suo.im/6nBrmg";
        File qrFile = new File("/data/genFile/" + System.currentTimeMillis() + ".jpg");
        //File logoFile = new File(ClassLoader.getSystemResource("images/logo_hxdf.jpg").getPath());
        File codeFile = QRCodeUtils.createQRCode(content, 80,null);
        try {
            FileUtils.copyFile(codeFile, qrFile);
        } catch (Exception e) {
            e.printStackTrace();
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
