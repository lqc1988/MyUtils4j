package com.lqc.vo.weixin;

/**
 * author : liqinchao
 * CreateTime : 2019/3/1 18:35
 * Description :微信小程序敏感数据数据水印( watermark )
 */
public class WXMiniWaterMark {
    /**
     * 敏感数据归属 appId，开发者可校验此参数与自身 appId 是否一致
     */
    public String appid;
    /**
     * 敏感数据获取的时间戳, 开发者可以用于数据时效性校验
     */
    public long timestamp;

    @Override
    public String toString() {
        return "WXWaterMark{" +
                "appid='" + appid + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
