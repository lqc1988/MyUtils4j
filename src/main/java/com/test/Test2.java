package com.test;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.util.*;

/**
 * @ClassName Test2
 * @Description TODO
 * @Author liqinchao
 * @Date 2022/2/28 10:41
 * @Version 1.0
 **/
public class Test2 {
    public static void main(String[] args) {
//		System.out.println(DateFormatUtils.format(new Date(),"yyyy-MM-dd"));
//		System.out.println(  (300/13));
//		System.out.println(  (float) 3/13);
//		Calendar calendar = Calendar.getInstance();
//		calendar.setTime(new Date());
//		System.out.println(calendar.get(Calendar.DAY_OF_MONTH))
        String fileUrl = "http://api.kuaidi100.com/label/getImage/20220711/904984CB74DC40A4B83759C4563DACEA";
        String ext = fileUrl.substring(fileUrl.lastIndexOf("."));
        System.out.println(ext);
    }
}
