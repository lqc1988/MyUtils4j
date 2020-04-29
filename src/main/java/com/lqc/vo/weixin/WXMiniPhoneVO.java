package com.lqc.vo.weixin;

/**
 * author : liqinchao
 * CreateTime : 2019/3/1 18:29
 * Description :获取微信用户绑定的手机号接口返回的数据对象(解密后)
 * ，需先调用wx.login接口。
 */
public class WXMiniPhoneVO {
    /**
     * 用户绑定的手机号（国外手机号会有区号）
     */
    public String phoneNumber;
    /**
     * 没有区号的手机号
     */
    public String purePhoneNumber;
    /**
     * 区号
     */
    public String countryCode;
    /**
     * 数据水印
     */
    public WXMiniWaterMark watermark;

    @Override
    public String toString() {
        return "WXMiniPhoneVO{" +
                "phoneNumber='" + phoneNumber + '\'' +
                ", purePhoneNumber='" + purePhoneNumber + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", watermark=" + watermark +
                '}';
    }
}
