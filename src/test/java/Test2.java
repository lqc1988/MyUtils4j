import com.lqc.utils.CommonUtil;
import com.lqc.utils.SecurityUtil;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;


/**
 * ClassName : Test2
 * Author : liqinchao
 * CreateTime : 2019/3/18 18:11
 * Description : 测试类2
 */
public class Test2 {

    public static void main(String[] args) {
        System.out.println(41%20);
//        calcSign();
//        md5();
//        delScale();
    }
    static void delScale(){
        DecimalFormat decimalFormat = new DecimalFormat("###################.###########");
        System.out.println(decimalFormat.format(12.0d));
    }
    static void md5() {
        String str = "bfa1ba4a3dac4f18adec949ac00a44d2access_token2ddbf42fc3c947fb8e8b171e60e4af46appKeyba5e4aa800904377a60c4175030fc6c2babyList[{\"id\":\"\",\"babyName\":\"可可\",\"babyBirthday\":\"2016-02-23\",\"babyGender\":\"1\"}]timestamp1550892496000bfa1ba4a3dac4f18adec949ac00a44d2";
        try {
            System.out.println(SecurityUtil.MD5(str));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void calcSign() {
        String appKey = "ba5e4aa800904377a60c4175030fc6c2";
        String appSecret = "bfa1ba4a3dac4f18adec949ac00a44d2";
        String timestamp = System.currentTimeMillis() + "";
        HashMap<String, String> paramMap = new HashMap<>();
//        paramMap.put("flag", "1");
        paramMap.put("appKey", appKey);
//        paramMap.put("deviceStatus", "1");
//        paramMap.put("timestamp", "1552038363602");
        paramMap.put("timestamp", timestamp);
        paramMap.put("serialNo", "PB20191000010059");
        paramMap.put("snPay", "395f007919524bb5bd50855864c4fd44");
        paramMap.put("access_token", "6c3bdbbc2e96402d85670273cab51feb");
//        paramMap.put("serviceId", "47064badd5a844d289c6650074644e8c");
//        paramMap.put("sn", "2019022830603");
//        paramMap.put("orderStatus", "0");
//        paramMap.put("miniprogram", "1");
//        paramMap.put("page", "0");
//        paramMap.put("limit", "10");
//        paramMap.put("groupNo", "c372f66daa414dcca095de483a42848a");
//        paramMap.put("uid", "0afc06697add46a5b61b4d7d724db67b");
//        paramMap.put("tel", "18663001240");
//        paramMap.put("nickName", "闻");
//        paramMap.put("babyList[0].babyName", "aaa");
//        paramMap.put("babyList[0].babyBirthday", "2017-01-21");
//        paramMap.put("babyList[0].babyGender", "1");
        try {
            String sign = SecurityUtil.encryptAPIParam(appSecret, paramMap);
            System.out.println("["+CommonUtil.formatDateToStr(new Date(),null)+"]sign=" + sign);
            System.out.println("["+CommonUtil.formatDateToStr(new Date(),null)+"]timestamp=" + timestamp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
