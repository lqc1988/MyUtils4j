package com.lqc.utils;

import com.alibaba.fastjson.JSONObject;
import com.lqc.enums.ResultEnum;
import com.lqc.exception.MyException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.*;

/**
 * ClassName : SecurityUtil
 * Author : liqinchao
 * CreateTime : 2019/3/18 18:11
 * Description : 加密工具类
 */
public class SecurityUtil {
    private static Logger logger = LoggerFactory.getLogger(SecurityUtil.class);
    /**
     * API公共参数--appKey
     */
    public static final String API_PARAM_APP_KEY = "appKey";
    /**
     * API公共参数--timestamp
     */
    public static final String API_PARAM_TIMESTAMP = "timestamp";
    /**
     * API公共参数--sign
     */
    public static final String API_PARAM_SIGN = "sign";
    /**
     * API公共参数--access_token
     */
    public static final String API_PARAM_ACCESS_TOKEN = "access_token";

    /**
     * 签名有效期，单位：分钟
     */
    public static final int SIGN_EXPIRE = 3;

    public static ResourceBundle conf = ResourceBundle.getBundle("config");
    /**
     * 中心平台appkey
     */
    public static final String APP_KEY_CENTER = conf.getString("app.key.center");
    /**
     * 中心平台密钥
     */
    public static final String APP_SECRET_CENTER = conf.getString("app.secret.center");
    /**
     * 校验接口基础入参
     *
     * @param appSecret
     * @param paramMap
     * @throws Exception
     */
    public static void validateAPIParam(String appSecret, Map<String, String> paramMap) throws Exception {
        if (StringUtils.isBlank(appSecret) || null == paramMap || paramMap.isEmpty()) {
            throw new MyException(ResultEnum.ERR_PARAM.getDisplay());
        }
        String appKey = paramMap.get(API_PARAM_APP_KEY);
        String timestamp = paramMap.get(API_PARAM_TIMESTAMP);
        String sign = paramMap.get(API_PARAM_SIGN);
        if (StringUtils.isBlank(appKey) || StringUtils.isBlank(timestamp) || StringUtils.isBlank(sign)) {
            throw new MyException(ResultEnum.ERR_PARAM.getDisplay());
        }
        if (CommonUtil.compareMinute(new Date(), new Date(Long.parseLong(timestamp))) > SIGN_EXPIRE) {
            throw new MyException(ResultEnum.SIGN_INVALID.getDisplay());
        }
        String paraStr= JSONObject.toJSONString(paramMap);
        String signCalc = calcSignature(paramMap, appSecret);
        logger.debug("appSecret="+appSecret+"，入参："+paraStr +"，服务端计算签名：" + signCalc);
        if (!sign.equals(signCalc)) {
            throw new MyException(ResultEnum.SIGN_INVALID.getDisplay());
        }
    }

    /**
     * 接口参数加密
     *
     * @param appSecret
     * @throws Exception
     */
    public static String encryptAPIParam(String appSecret, HashMap<String, String> paramMap) throws Exception {
        if (StringUtils.isBlank(appSecret) || null == paramMap || paramMap.isEmpty()) {
            throw new MyException(ResultEnum.ERR_PARAM.getDisplay());
        }
        String appKey = paramMap.get(API_PARAM_APP_KEY);
        String timestamp = paramMap.get(API_PARAM_TIMESTAMP);
        if (StringUtils.isBlank(appKey) || StringUtils.isBlank(timestamp)) {
            throw new MyException(ResultEnum.ERR_PARAM.getDisplay());
        }
        return calcSignature(paramMap, appSecret);
    }

    /**
     * 第三方平台调用中心平台时计算接口签名
     *
     * @param paramMap
     * @return
     * @throws IOException
     */
    private final static String calcSignature(Map<String, String> paramMap, String secret) throws IOException {
        //计算时移除sign本身
        paramMap.remove(API_PARAM_SIGN);
        // 先将参数以其参数名的字典序升序进行排序
        Map<String, String> sortedParams = new TreeMap<>(paramMap);
        // 遍历排序后的字典，将所有参数按"key=value"格式拼接在一起
        StringBuilder baseString = new StringBuilder(secret);
        for (Map.Entry<String, String> param : sortedParams.entrySet()) {
            baseString.append(param.getKey());
            if (StringUtils.isBlank(param.getValue())) {
                continue;
            }
            baseString.append(URLEncoder.encode(param.getValue(), "utf8"));
//            baseString.append(param.getValue());
        }
        baseString.append(secret);
        logger.debug("加密原始串：" + baseString);
        // 使用MD5对待签名串求签
        return MD5(baseString.toString()).toUpperCase();
    }


    /**
     * MD5
     *
     * @param baseString
     * @return
     * @throws IOException
     */
    public final static String MD5(String baseString) throws IOException {
        StringBuilder sign = new StringBuilder();
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(baseString.getBytes("UTF-8"));
            for (int i = 0; i < bytes.length; i++) {
                String hex = Integer.toHexString(bytes[i] & 0xFF);
                if (hex.length() == 1) {
                    sign.append("0");
                }
                sign.append(hex);
            }
        } catch (GeneralSecurityException ex) {
            throw new IOException(ex);
        }
        return sign.toString();
    }

    public static void main(String[] args) throws Exception {
        testSet();
    }
    static void testSet(){
        Set<Integer> aa=new HashSet<>();
        aa.add(33);
        int cc=0;
        for (;;) {
            cc++;
            Integer bb=aa.iterator().next();
            if (null==bb){
                System.out.println(cc+"次结果为null");
                break;
            }
        }
    }
    static void testSub(){
        Integer a=201905;
        String b=a.toString();
        System.out.println(b);
        System.out.println(b.length());
        System.out.println(b.substring(0,4)+"-"+b.substring(4,6));

        String c=(--a).toString();
        System.out.println(c);
        String d=c.substring(0,4)+"-"+c.substring(4,6);
        System.out.println(d);
    }
    static void testSign() throws Exception{
        String appSecret = "bfa1ba4a3dac4f18adec949ac00a44d2";
        HashMap<String, String> paramMap = new HashMap<>();
        //接口公共参数
        paramMap.put("appKey", "ba5e4aa800904377a60c4175030fc6c2");
        String timestamp = System.currentTimeMillis()+"";
        System.out.println("timestamp:"+timestamp);
        paramMap.put("timestamp", timestamp);
//        paramMap.put("tel", "13146653605");
//        paramMap.put("name", "管理后台webAdmin");
//        paramMap.put("type", "3");
//        paramMap.put("price", new BigDecimal("0.00").toString());
//        paramMap.put("categoryId", "351");
//        paramMap.put("grantAppKey", "c1dfc45857954622865d6a5d112fc65f");
        paramMap.put("access_token", "82109d8dca6b4922885ed507e36d11ab");
//        paramMap.put("serviceId", "fa8c269a8cab434dbca51b97c0bc19e1");
//        paramMap.put("serialNo", "PB00001000130699");
//        paramMap.put("serviceType", "0");
//        paramMap.put("eventType", "D");
//        paramMap.put("serialNo", "VT000010002201C8");
//        paramMap.put("url", "pages/service-detail/service-detail");
        String sign = encryptAPIParam(appSecret, paramMap);
//        encrypt4ShopXX(paramMap);
        System.out.println("sign:" + sign);
//        paramMap.put("sign", sign);
//        validateAPIParam(appSecret, paramMap);
    }

    /**
     * 运营中心调用其他应用接口请求参数加密
     *
     *
     */
    public static void encrypt4ShopXX(final HashMap<String, String> paramMap) throws Exception {
        if (null == paramMap) {
            throw new MyException(ResultEnum.ERR_PARAM.getDisplay());
        }
        String appKey = APP_KEY_CENTER;
        String appSecret = APP_SECRET_CENTER;
        encrypt4ShopXX(paramMap, appKey, appSecret);
    }

    /**
     * 运营中心调用其他应用接口请求参数加密
     *
     *
     */
    public static void encrypt4ShopXX(final HashMap<String, String> paramMap,
                                      final String appKey, final String appSecret) throws Exception {
        if (null == paramMap) {
            throw new MyException(ResultEnum.ERR_PARAM.getDisplay());
        }
//        String timestamp = System.currentTimeMillis() + "";
        paramMap.put(API_PARAM_APP_KEY, appKey);
        paramMap.put(API_PARAM_TIMESTAMP, paramMap.get("timestamp"));
        //获取签名
        String sign = calcSignature4ShopXX(paramMap, appSecret);
        System.out.println("sign:" + sign);
        paramMap.put(API_PARAM_SIGN, sign);
    }
    /**
     * 计算接口签名用来调用shop++
     *
     * @param paramMap
     * @return
     * @throws IOException
     */
    private final static String calcSignature4ShopXX(final Map<String, String> paramMap, final String secret) throws IOException {
        //计算时移除sign本身
        paramMap.remove(API_PARAM_SIGN);
        // 先将参数以其参数名的字典序升序进行排序
        Map<String, String> sortedParams = new TreeMap<>(paramMap);
        // 遍历排序后的字典，将所有参数按"key=value"格式拼接在一起
        StringBuilder baseString = new StringBuilder(secret);
        for (Map.Entry<String, String> param : sortedParams.entrySet()) {
            baseString.append(param.getKey());
            if (StringUtils.isBlank(param.getValue())) {
                continue;
            }
            //加密时对value值进行urlEncode处理
            baseString.append(URLEncoder.encode(param.getValue(), "utf8"));
        }
        baseString.append(secret);
        logger.debug("加密原始串(calcSignature4ShopXX)：" + baseString);
        // 使用MD5对待签名串求签
        return MD5(baseString.toString()).toUpperCase();
    }

}
