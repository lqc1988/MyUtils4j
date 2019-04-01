package com.test;

import com.lqc.utils.HttpUtils;

public class Test1 {
    public static void main(String[] args) throws Exception{
        String url="http://127.0.0.1:8082/q";
        String res_str= HttpUtils.get(url,null,null);
        System.out.println("Response String="+res_str);
        System.out.println("Response String to byte[]="+res_str.getBytes());
        byte[] res_byte=HttpUtils.getByte(url,null,null);
        System.out.println("Response byte[]="+res_byte);
    }
}
