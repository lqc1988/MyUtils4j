package com.lqc.vo.weixin;

/**
 * author : liqinchao
 * CreateTime : 2019/3/1 18:29
 * Description :微信小程序获取用户信息解析的对象
 * 调用wx.getUserInfo()返回的数据对象(解密后)
 * ，需先调用wx.login接口。
 */
public class WXMiniUserInfoVO {
    /**
     * openID
     */
    public String openId;
    /**
     * 昵称
     */
    public String nickName;
    /**
     * 性别
     */
    public Integer gender;
    /**
     * 语言
     */
    public String language;
    /**
     * 市
     */
    public String city;
    /**
     * 省
     */
    public String province;
    /**
     * 国家
     */
    public String country;
    /**
     * 头像url
     */
    public String avatarUrl;
    /**
     * UnionID
     */
    public String unionId;
    /**
     * 数据水印
     */
    public WXMiniWaterMark watermark;

    @Override
    public String toString() {
        return "WXMiniUserInfoVO{" +
                "openId='" + openId + '\'' +
                ", nickName='" + nickName + '\'' +
                ", gender=" + gender +
                ", language='" + language + '\'' +
                ", city='" + city + '\'' +
                ", province='" + province + '\'' +
                ", country='" + country + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", unionId='" + unionId + '\'' +
                ", watermark=" + watermark +
                '}';
    }
}
