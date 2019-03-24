package com.lqc.test;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * ClassName : Sign
 * Author : liqinchao
 * CreateTime : 2019/3/18 18:11
 * Description : 加密工具类
 */
public class Sign {

    /**
     * 接口参数加密
     *
     * @param appSecret
     * @throws Exception
     */
    public static String encryptAPIParam(String appSecret, HashMap<String, String> paramMap) throws Exception {
        if (StringUtils.isBlank(appSecret) || null == paramMap || paramMap.isEmpty()) {
            return "参数错误";
        }
        System.currentTimeMillis();
        String appKey = paramMap.get("timestamp");
        String timestamp = paramMap.get("timestamp");
        if (StringUtils.isBlank(appKey) || StringUtils.isBlank(timestamp)) {
            return "参数错误";
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
        paramMap.remove("sign");
        Map<String, String> sortedParams = new TreeMap<>(paramMap);
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
}
