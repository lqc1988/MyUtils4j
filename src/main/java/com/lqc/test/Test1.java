package com.lqc.test;

import com.lqc.utils.CommonUtil;
import com.lqc.utils.ConstUtil;

import java.net.InetAddress;

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

            System.out.print(CommonUtil.lastMonth());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    static void testStr(){
        for (int i = 0; i < 65534; i++) {
            System.out.print("a");
        }
    }
}
