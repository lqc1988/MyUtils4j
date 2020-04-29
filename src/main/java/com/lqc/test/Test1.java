package com.lqc.test;

import java.util.Calendar;
import java.util.Date;

/**
 * ClassName : Test1
 * Author : liqinchao
 * CreateTime : 2019/3/27 16:37
 * Description : 测试类
 */
public class Test1 {
    public static void main(String[] args){
//        System.out.println(ConstUtil.SMS_SIGN);
//        System.out.println(ConstUtil.APP_MOD);
        try {
//            String localIP= InetAddress.getLocalHost().getHostAddress();
//            System.out.println("localIP="+localIP);
//            int a=100,b=1000;
//            System.out.println(a/b);
//            System.out.println(a%b);
//            System.out.println("getHostName:"+InetAddress.getLocalHost().getHostName());
//            System.out.println("getHostAddress:"+InetAddress.getLocalHost().getHostAddress());
//            testStr();

            //System.out.print(CommonUtil.lastMonth());
            Calendar c1=Calendar.getInstance();
            c1.setTime(new Date());
            Calendar c2=Calendar.getInstance();
            c2.setTime(new Date());
            //c2.add(Calendar.MONTH,1);
            c2.add(Calendar.MONTH,2);
            System.out.print(sameYearMonth(c1.getTime(),c2.getTime()));

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    static void testStr(){
        for (int i = 0; i < 65534; i++) {
            System.out.print("a");
        }
    }
    static boolean sameYearMonth(Date d1, Date d2) {
        Calendar c1=Calendar.getInstance();
        c1.setTime(d1);
        Calendar c2=Calendar.getInstance();
        c2.setTime(d2);
        return c1.get(Calendar.YEAR)==c2.get(Calendar.YEAR)&&c1.get(Calendar.MONTH)==c2.get(Calendar.MONTH);
    }
}
