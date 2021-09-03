package com.lqc.demo;


import com.lqc.util.TimeUtils;

import java.util.Date;

/**
 * @ClassName Demo1
 * @Description TODO
 * @Author liqinchao
 * @Date 2021/9/1 11:02
 * @Version 1.0
 **/
public class Demo1 {
    public static void main(String[] args) {
//        String url = "http://project-homedo.oss-cn-shanghai.aliyuncs.com/c9865094-b617-4e98-8d52-f8dfd1b08a83_9d6c2674-21b6-440e-bf0a-bbecfb6f72f9_DOCX测试方案.docx";
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
        Date date = TimeUtils.parse("1900-01-01 00:00:00","yyyy-MM-dd HH:mm:ss");
        System.out.println(date);
        System.out.println(date.getTime());
    }
}
