package com.may.utils;

import com.may.enums.ResultEnum;
import com.may.exception.MyException;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * ClassName : SecurityUtil
 * Author : liqinchao
 * CreateTime : 2019/3/18 18:11
 * Description : 加密工具类
 */
public class SecurityUtil {
    private static Logger logger = LogManager.getLogger("utils.SecurityUtil");
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
        String appKey = paramMap.get(ConstUtil.API_PARAM_APP_KEY);
        String timestamp = paramMap.get(ConstUtil.API_PARAM_TIMESTAMP);
        String sign = paramMap.get(ConstUtil.API_PARAM_SIGN);
        if (StringUtils.isBlank(appKey) || StringUtils.isBlank(timestamp) || StringUtils.isBlank(sign)) {
            throw new MyException(ResultEnum.ERR_PARAM.getDisplay());
        }
        if (CommonUtil.compareMinute(new Date(), new Date(Long.parseLong(timestamp))) > ConstUtil.SIGN_EXPIRE) {
            throw new MyException(ResultEnum.SIGN_INVALIDATE.getDisplay());
        }
        String paraStr= JSONObject.fromObject(paramMap).toString();
        String signCalc = calcSignature(paramMap, appSecret);
        logger.debug("appSecret="+appSecret+"，入参："+paraStr +"，服务端计算签名：" + signCalc);
        if (!sign.equals(signCalc)) {
            throw new MyException(ResultEnum.SIGN_INVALIDATE.getDisplay());
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
        String appKey = paramMap.get(ConstUtil.API_PARAM_APP_KEY);
        String timestamp = paramMap.get(ConstUtil.API_PARAM_TIMESTAMP);
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
        paramMap.remove(ConstUtil.API_PARAM_SIGN);
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
        String appSecret = "bfa1ba4a3dac4f18adec949ac00a44d2";
        HashMap<String, String> paramMap = new HashMap<>();
        //接口公共参数
        paramMap.put("appKey", "ba5e4aa800904377a60c4175030fc6c2");
        String timestamp = System.currentTimeMillis()+"";
        System.out.println("timestamp:"+timestamp);
        paramMap.put("timestamp", timestamp);
        paramMap.put("serialNo", "123");
        paramMap.put("clientVersion", "1.0.0");
        paramMap.put("userName", "test");
        String sign = encryptAPIParam(appSecret, paramMap);
        System.out.println("sign:" + sign);
        paramMap.put("sign", sign);
        validateAPIParam(appSecret, paramMap);
    }
}
