package com.test;

import com.lqc.util.CommonUtil;

import java.util.Calendar;
import java.util.Date;

public class Test1 {
    public static void main(String[] args) throws Exception{
        //String url="http://127.0.0.1:8082/q";
        //String res_str= HttpUtils.get(url,null,null);
        //System.out.println("Response String="+res_str);
        //System.out.println("Response String to byte[]="+res_str.getBytes());
        //byte[] res_byte=HttpUtils.getByte(url,null,null);
        //System.out.println("Response byte[]="+res_byte);
        Calendar cal1=Calendar.getInstance();
        cal1.setTime(new Date());
        cal1.add(Calendar.DATE,1);
        System.out.println("cal1="+cal1.getTime());
        System.out.println(CommonUtil.compareDay(cal1.getTime(),new Date()));
        System.out.println(CommonUtil.compareDay(new Date(),new Date()));
    }
}
