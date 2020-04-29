package com.lqc.util.weixin;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import enums.ResultEnum;
import exception.HxException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import play.Logger;
import play.libs.Json;
import service.RedisService;
import util.*;
import vo.weixin.WXMiniPhoneVO;
import vo.weixin.WXMiniSessionVO;
import vo.weixin.WXMiniUserInfoVO;
import vo.weixin.WXSignatureVO;

import javax.inject.Inject;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.Date;


/**
 * @author : liqinchao
 * @CreateTime : 2018/7/10 15:15
 * @Description :微信工具类
 */
@Component
public class WxUtil {
    private final Logger.ALogger logger = Logger.of("utils.WxUtil");
    private RedisService cache;

    @Inject
    public WxUtil(RedisService cache) {
        this.cache = cache;
    }

    /**
     * 获取 access_token
     *
     * @param appId  微信开发者ID
     * @param secret 微信开发者密钥
     * @return
     *
     */
    public String getAccessToken(String appId, String secret) throws Exception {
        String opt = "获取微信access_token";
        String result;
        if (null != cache.get(util.ConstCacheKey.WX_ACCESS_TOKEN_KEY)) {
            result = cache.get(util.ConstCacheKey.WX_ACCESS_TOKEN_KEY).toString();
            logger.debug(opt + ",缓存get");
        } else {
            StringBuilder url_wx = new StringBuilder();
            url_wx.append("https://api.weixin.qq.com/cgi-bin/token?");
            url_wx.append("grant_type=client_credential");
            url_wx.append("&appid=" + appId);
            url_wx.append("&secret=" + secret);
            String resp = HttpUtils.get(url_wx.toString(), null, null);
            logger.debug(opt + ",响应：" + resp);
            if (StringUtils.isNotBlank(resp)) {
                JsonNode jso = Json.parse(resp);
                if (null != jso.get("errcode") && jso.get("errcode").asInt() != 0) {
                    logger.error(opt + "，失败：" + jso.get("errmsg").asText(), jso.get("errcode").asInt());
                    throw new HxException(jso.get("errmsg").asText(), jso.get("errcode").asInt());
                }
                result = jso.get("access_token").asText();
                Long expires = (null == jso.get("expires_in")) ? ConstUtil.WX_EXPIRE_SECONDS :
                        jso.get("expires_in").asLong();
                cache.set(util.ConstCacheKey.WX_ACCESS_TOKEN_KEY, result, expires);
            } else {
                logger.error(opt + ",失败：响应结果为null");
                throw new HxException(opt + ",失败：响应结果为null");
            }
        }
        return result;
    }

    /**
     * 获取jsapi_ticket
     * 采用http GET方式请求获得jsapi_ticket（有效期7200秒，开发者必须在自己的服务全局缓存jsapi_ticket）
     *
     * @param appId  微信开发者ID
     * @param secret 微信开发者密钥
     * @return
     *
     */
    public String getJSTicket(String appId, String secret) throws Exception {
        String opt = "获取微信jsapi_ticket";
        String result;
        if (null != cache.get(util.ConstCacheKey.WX_JS_TICKET_KEY)) {
            result = cache.get(util.ConstCacheKey.WX_JS_TICKET_KEY).toString();
            logger.debug(opt + ",缓存get");
        } else {
            String access_token = getAccessToken(appId, secret);
            StringBuilder url_wx = new StringBuilder();
            url_wx.append("https://api.weixin.qq.com/cgi-bin/ticket/getticket?");
            url_wx.append("access_token=" + access_token);
            url_wx.append("&type=jsapi");
            String resp = HttpUtils.get(url_wx.toString(), null, null);
            logger.debug(opt + ",响应：" + resp);
            if (StringUtils.isNotBlank(resp)) {
                JsonNode jso = Json.parse(resp);
                if (null != jso.get("errcode") && jso.get("errcode").asInt() != 0) {
                    logger.error(opt + "，失败：" + jso.get("errmsg").asText(), jso.get("errcode").asInt());
                    throw new HxException(jso.get("errmsg").asText(), jso.get("errcode").asInt());
                }
                result = jso.get("ticket").asText();
                Long expires = (null == jso.get("expires_in")) ? ConstUtil.WX_EXPIRE_SECONDS : jso.get("expires_in").asLong();
                cache.set(util.ConstCacheKey.WX_JS_TICKET_KEY, result, expires);
            } else {
                logger.error(opt + ",失败：响应结果为null");
                throw new HxException(opt + ",失败：响应结果为null");
            }
        }
        return result;
    }

    /**
     * 生成微信JS签名
     *
     * @param appId  微信开发者ID
     * @param secret 微信开发者密钥
     * @param url
     * @return
     *
     */
    public WXSignatureVO generateJSSignature(String url, String appId, String secret) throws Exception {
        WXSignatureVO result = new WXSignatureVO();
        String nonceStr = util.RandomStringUtils.randomAlphabetic(8);
        String jsapi_ticket = getJSTicket(appId, secret);
        long timestamp = new Date().getTime();
        String str1 = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + nonceStr + "&timestamp=" +
                timestamp + "&url=" + url;
        logger.debug("生成微信JS签名，原始串=" + str1);
        String signature = DigestUtils.sha1Hex(str1);
        logger.debug("生成微信JS签名，结果=" + signature);
        result.appId = appId;
        result.nonceStr = nonceStr;
        result.signature = signature;
        result.timestamp = timestamp;
        result.url = url;
        return result;
    }

    /**
     * 获取微信openID
     *
     * @param code
     * @param appId  微信开发者ID
     * @param secret 微信开发者密钥
     * @return
     *
     */
    public String getOpenID(String code, String appId, String secret) throws Exception {
        String opt = "获取微信openID，";
        if (StringUtils.isBlank(code)) {
            logger.error(opt + ",参数错误：code为null");
            throw new HxException(ResultEnum.ERROR_WEIXIN.getDisplay(), ResultEnum.ERROR_WEIXIN.getValue());
        }
        String result;
        StringBuilder url_wx = new StringBuilder();
        url_wx.append("https://api.weixin.qq.com/sns/oauth2/access_token?");
        url_wx.append("appid=" + appId);
        url_wx.append("&secret=" + secret);
        url_wx.append("&code=" + code);
        url_wx.append("&grant_type=authorization_code");
        String resp = HttpUtils.get(url_wx.toString(), null, null);
        logger.debug(opt + "请求url=" + url_wx.toString() + "，响应1：" + resp);
        if (StringUtils.isNotBlank(resp)) {
            JsonNode jso = Json.parse(resp);
            logger.debug(opt + "响应2：" + jso);
            if (null != jso.get("errcode") && jso.get("errcode").asInt() != 0) {
                logger.error(opt + "，失败：" + jso.get("errmsg").asText(), jso.get("errcode").asInt());
                throw new HxException(jso.get("errmsg").asText(), jso.get("errcode").asInt());
            }
            result = jso.get("openid").asText();
        } else {
            logger.error(opt + "，错误：响应结果为null");
            throw new HxException(opt + ",失败：响应结果为null");
        }
        return result;
    }

    /**
     * 微信小程序登录凭证校验。
     * 通过 wx.login() 接口获得临时登录凭证code后
     * 传到开发者服务器调用此接口完成登录流程。
     * 更多使用方法详见 小程序登录。
     *
     * @param code   登录时获取的 code
     * @param appId  微信开发者ID
     * @param secret 微信开发者密钥
     * @return
     *
     */
    public WXMiniSessionVO getSession_wx_mini(String code, String appId, String secret) throws Exception {
        String opt = "微信小程序登录凭证校验，";
        if (StringUtils.isBlank(code)) {
            logger.error(opt + ",参数错误：code为null");
            throw new HxException(ResultEnum.ERROR_WEIXIN.getDisplay(), ResultEnum.ERROR_WEIXIN.getValue());
        }
        WXMiniSessionVO respData;
        StringBuilder url_wx = new StringBuilder();
        url_wx.append("https://api.weixin.qq.com/sns/jscode2session?");
        url_wx.append("appid=" + appId);
        url_wx.append("&secret=" + secret);
        url_wx.append("&js_code=" + code);
        url_wx.append("&grant_type=authorization_code");
        String resp = HttpUtils.get(url_wx.toString(), null, null);
        logger.debug(opt + "请求url=" + url_wx.toString() + "，响应：" + resp);
        if (StringUtils.isNotBlank(resp)) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            respData = mapper.readValue(resp, WXMiniSessionVO.class);
            if (null != respData.errcode && 0 != respData.errcode) {
                logger.error(opt + "失败：" + respData);
                throw new HxException(respData.errmsg, respData.errcode);
            }
            cache.set(util.ConstCacheKey.WX_OPENID_MINI_KEY + respData.openid, respData.session_key);
        } else {
            logger.error(opt + "，错误：响应结果为null");
            throw new HxException(opt + ",失败：响应结果为null");
        }
        return respData;
    }

    /**
     * 组装微信获取用户openID的URL
     *
     * @param appId 微信开发者ID
     * @param url
     * @return
     *
     */
    public static String assembleAuthURL(String url, String appId) throws Exception {
        if (StringUtils.isBlank(url)) {
            throw new HxException("URL不能为空");
        }
        url = URLEncoder.encode(url, "utf-8");
        StringBuilder url_wx = new StringBuilder();
        url_wx.append("https://open.weixin.qq.com/connect/oauth2/authorize?");
        url_wx.append("appid=" + appId);
        url_wx.append("&redirect_uri=" + url);
        url_wx.append("&response_type=code");
        url_wx.append("&scope=snsapi_base");
        url_wx.append("&state=123#wechat_redirect");
        return url_wx.toString();
    }

    /**
     * 发送微信模板消息
     *
     * @param wxMessage
     * @param appId     微信开发者ID
     * @param secret    微信开发者密钥
     * @return
     *
     */
    public void sendNotice(WxMessage wxMessage, String appId, String secret) throws Exception {
        String opt = "发送微信模板消息";
        if (null == wxMessage) {
            logger.error(opt + "，失败，wxMessage为null");
            throw new HxException(HxException.ERROR_MSG_PARAM);
        }
        String access_token = getAccessToken(appId, secret);
        StringBuilder url_wx = new StringBuilder();
        url_wx.append("https://api.weixin.qq.com/cgi-bin/message/template/send?");
        url_wx.append("access_token=" + access_token);
        String body = Json.stringify(Json.toJson(wxMessage));
        logger.debug(opt + "，请求body：" + body);
        String resp = HttpUtils.post(url_wx.toString(), null, null, body);
        logger.debug(opt + "，响应：" + resp);
        if (StringUtils.isNotBlank(resp)) {
            JsonNode jso = Json.parse(resp);
            if (null != jso.get("errcode") && jso.get("errcode").asInt() != 0) {
                logger.error(opt + "，失败：" + jso.get("errmsg").asText(), jso.get("errcode").asInt());
                throw new HxException(jso.get("errmsg").asText(), jso.get("errcode").asInt());
            }
        } else {
            logger.error(opt + "，失败：响应结果为null");
            throw new HxException(opt + ",失败：响应结果为null");
        }
    }

    /**
     * 微信小程序授权获取手机号数据解密
     *
     * @param encryptedData 包括敏感数据在内的完整用户信息的加密数据
     * @param iv            加密算法的初始向量
     * @param appid         敏感数据归属 appId，开发者可校验此参数与自身 appId 是否一致
     * @param sessionKey    会话密钥
     * @return
     *
     */
    public WXMiniPhoneVO decrypt_phone_wx_mini(String encryptedData, String iv,
                                               String appid, String sessionKey) throws Exception {
        String opt = "解密微信小程序手机号数据，";
        logger.debug(opt + "开始。");
        String plainData = decrypt_wx_mini(opt, encryptedData, iv, appid, sessionKey);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        WXMiniPhoneVO vo = mapper.readValue(plainData, WXMiniPhoneVO.class);
        logger.debug(opt + "成功，结果：" + vo.toString());
        return vo;
    }

    /**
     * 微信小程序授权获取用户信息数据解密
     *
     * @param encryptedData 包括敏感数据在内的完整用户信息的加密数据
     * @param iv            加密算法的初始向量
     * @param appid         敏感数据归属 appId，开发者可校验此参数与自身 appId 是否一致
     * @param sessionKey    会话密钥
     * @return
     *
     */
    public WXMiniUserInfoVO decrypt_UserInfo_wx_mini(String encryptedData, String iv,
                                                     String appid, String sessionKey) throws Exception {
        String opt = "解密微信小程序用户信息数据，";
        logger.debug(opt + "开始。");
        String plainData = decrypt_wx_mini(opt, encryptedData, iv, appid, sessionKey);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        WXMiniUserInfoVO vo = mapper.readValue(plainData, WXMiniUserInfoVO.class);
        logger.debug(opt + "成功，结果：" + vo.toString());
        return vo;
    }

    /**
     * 继续微信小程序加密数据
     *
     * @param opt
     * @param encryptedData
     * @param iv
     * @param appid
     * @param sessionKey
     * @return
     *
     */
    private String decrypt_wx_mini(String opt, String encryptedData, String iv,
                                   String appid, String sessionKey) throws Exception {
        if (StringUtils.isBlank(encryptedData) || StringUtils.isBlank(iv)
                || StringUtils.isBlank(appid) || StringUtils.isBlank(sessionKey)) {
            throw new HxException(ResultEnum.ERR_PARAM.getDisplay());
        }
        opt += "\n入参：encryptedData=" + encryptedData +
                "\n，iv=" + iv +
                "\n，appid=" + appid +
                "\n，sessionKey=" + sessionKey + "，";
        byte[] aesKey = Base64.getDecoder().decode(sessionKey);
        byte[] aesIV = Base64.getDecoder().decode(iv);
        byte[] aesCipher = Base64.getDecoder().decode(encryptedData);
        byte[] plainText = AESUtil.decrypt(aesCipher, aesKey, aesIV);
        if (null == plainText) {
            logger.error(opt + "失败：encryptedData为null");
            throw new HxException(opt + "失败");
        }
        String plainData = new String(plainText, "utf-8");
        logger.debug(opt + "解密后的数据：{}", plainData);
//        System.out.println(opt + "解密后的数据：" + plainData);
        return plainData;
    }
}
