package com.lqc.vo.weixin;

/**
 * @author : liqinchao
 * @CreateTime : 2018/7/25 17:15
 * @Description :微信签名对象VO
 */
public class WXSignatureVO {
    /**
     * 随机字符串
     */
    public String appId;
    /**
     * 随机字符串
     */
    public String nonceStr;
    /**
     * 时间戳
     */
    public long timestamp;
    /**
     * 签名
     */
    public String signature;
    /**
     * 当前网页的URL，不包含#及其后面部分
     */
    public String url;

    @Override
    public String toString() {
        return "WxSignatureVO{" +
                "appId='" + appId + '\'' +
                ", nonceStr='" + nonceStr + '\'' +
                ", timestamp=" + timestamp +
                ", signature='" + signature + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
