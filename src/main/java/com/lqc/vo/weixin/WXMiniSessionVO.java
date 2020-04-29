package com.lqc.vo.weixin;

/**
 * author : liqinchao
 * CreateTime : 2019/3/1 18:10
 * Description :微信小程序code2Session接口返回的数据对象
 */
public class WXMiniSessionVO {
    /**
     * 用户唯一标识
     */
    public String openid;
    /**
     * 会话密钥
     */
    public String session_key;
    /**
     * 用户在开放平台的唯一标识符，在满足 UnionID 下发条件的情况下会返回，详见 UnionID 机制说明。
     */
    public String unionid;
    /**
     * 错误码
     */
    public Integer errcode;
    /**
     * 错误信息
     */
    public String errmsg;

    @Override
    public String toString() {
        return "WXMiniSessionVO{" +
                "openid='" + openid + '\'' +
                ", session_key='" + session_key + '\'' +
                ", unionid='" + unionid + '\'' +
                ", errcode='" + errcode + '\'' +
                ", errmsg='" + errmsg + '\'' +
                '}';
    }
}
