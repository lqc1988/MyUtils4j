package com.lqc.util;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import play.libs.Json;
import service.RedisService;
import vo.WXVo;

import javax.inject.Inject;


/**
 * @Description :微信工具类
 */
@Component
public class WxHelper {
    private final static Logger logger= LoggerFactory.getLogger(util.WxHelper.class);

	@Inject
    RedisService cache;
    /**
     * 获取 access_token
     *
     * @return
     *
     */
    public String getAccessToken(String wx_appid,String wx_secret) throws Exception {
        String opt = "获取微信access_token";
        String result;
        Object obj=cache.get(ConstUtil.WX_ACCESS_TOKEN+wx_appid);
        if (null != obj) {
            result = obj.toString();
            logger.debug(opt + ",缓存get");
            logger.info(opt + ",缓存get"+result);
        } else {
            StringBuilder url_sb = new StringBuilder();
            url_sb.append("https://api.weixin.qq.com/cgi-bin/token?");
            url_sb.append("grant_type=client_credential");
            url_sb.append("&appid=" + wx_appid);
            url_sb.append("&secret=" + wx_secret);
//            logger.info(opt + ",请求：" + url_sb);
            String resp = HttpUtils.get(url_sb.toString(), null, null);
            logger.debug(opt + ",响应：" + resp);
            if (StringUtils.isNotBlank(resp)) {
                JsonNode jso = Json.parse(resp);
                if (null != jso.get("errcode") && jso.get("errcode").asInt() != 0) {
                    throw new Exception(opt + "，失败：" + jso.get("errmsg"));
                }
                result = jso.get("access_token").asText();
                logger.info(opt + ",新建"+result);
                Long expires= (null==jso.get("expires_in"))?ConstUtil.WX_EXPIRE_SECONDS:jso.get("expires_in").asLong();
                cache.set(ConstUtil.WX_ACCESS_TOKEN+wx_appid, result, expires);
            } else {
                logger.error(opt + ",失败：响应结果为null");
                throw new Exception(opt + ",失败：响应结果为null");
            }
        }
        return result;
    }

    /**
     * 获取jsapi_ticket
     * 采用http GET方式请求获得jsapi_ticket（有效期7200秒，开发者必须在自己的服务全局缓存jsapi_ticket）
     *
     * @return
     *
     */
    public String getJsTicket(String wx_appid,String wx_secret) throws Exception {
        String opt = "获取jsapi_ticket";
        String result;
        if (null != cache.get(ConstUtil.WX_JS_API_TICKET+wx_appid)) {
            result = cache.get(ConstUtil.WX_JS_API_TICKET+wx_appid).toString();
            logger.debug(opt + ",缓存get");
        } else {
            String access_token = getAccessToken(wx_appid,wx_secret);
            StringBuilder url_sb = new StringBuilder();
            url_sb.append("https://api.weixin.qq.com/cgi-bin/ticket/getticket?");
            url_sb.append("access_token=" + access_token);
            url_sb.append("&type=jsapi");
            String resp = HttpUtils.get(url_sb.toString(), null, null);
            logger.debug(opt + ",响应：" + resp);
            if (StringUtils.isNotBlank(resp)) {
                JsonNode jso = Json.parse(resp);
                if (null != jso.get("errcode") && jso.get("errcode").asInt() != 0) {
                    throw new Exception(opt + "，失败：" + jso.get("errmsg"));
                }
                result = jso.get("ticket").asText();
                Long expires= (null==jso.get("expires_in"))?ConstUtil.WX_EXPIRE_SECONDS:jso.get("expires_in").asLong();
                cache.set(ConstUtil.WX_JS_API_TICKET+wx_appid, result, expires);
            } else {
                logger.error(opt + ",失败：响应结果为null");
                throw new Exception(opt + ",失败：响应结果为null");
            }
        }
        return result;
    }

    /**
     * 生成微信JS签名
     * @param url
     * @return
     *
     */
    public WXVo generateJSSignature(String url,String wx_appid,String wx_secret)  {
    	WXVo result = null;
    	try {
    		result = new WXVo();
    		String nonceStr= RandomStringUtils.randomAlphabetic(8);
            String jsapi_ticket = getJsTicket(wx_appid,wx_secret);
            String timestamp = System.currentTimeMillis()+"";
            String str1="jsapi_ticket="+jsapi_ticket+"&noncestr="+nonceStr+"&timestamp="+timestamp+"&url="+url;
            logger.debug("生成微信JS签名，原始串="+str1);
            String signature= DigestUtils.sha1Hex(str1);
            logger.debug("生成微信JS签名，结果="+signature);
            result.appId=wx_appid;
            result.nonceStr=nonceStr;
            result.signature=signature;
            result.timestamp=timestamp;
            result.url=url;
//            System.out.println(result.appId+"-----"+url);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
        
        return result;
    }

}
