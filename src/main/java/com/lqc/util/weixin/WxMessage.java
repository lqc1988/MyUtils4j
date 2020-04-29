package com.lqc.util.weixin;

import java.util.Map;

/**
 * @author : liqinchao
 * @CreateTime : 2018/7/18 15:28
 * @Description :微信消息对象
 */
public class WxMessage {
    /**
     * 接收者openid-[必填]
     */
    public String touser;
    /**
     * 模板ID-[必填]
     */
    public String template_id;
    /**
     * 模板跳转链接-[选填]
     */
    public String url;
    /**
     * 跳小程序所需数据-[选填]
     * 不需跳小程序可不用传该数据
     */
    public WxMiniprogram miniprogram;

    public String topcolor;
    /**
     * 模板内容字体颜色-[选填]
     * 默认为黑色
     */
    public String color;
    /**
     * 	模板数据-[必填]
     */
    public Map<String,WxMetaData> data;

//    /**
//     * 微信模板数据
//     */
//    class WxData {
//        public WxMetaData first;
//        public WxMetaData keyword1;
//        public WxMetaData keyword2;
//        public WxMetaData keyword3;
//        public WxMetaData keyword4;
//        public WxMetaData remark;
//    }


    @Override
    public String toString() {
        return "WxMessage{" +
                "touser='" + touser + '\'' +
                ", template_id='" + template_id + '\'' +
                ", url='" + url + '\'' +
                ", miniprogram=" + miniprogram +
                ", topcolor='" + topcolor + '\'' +
                ", color='" + color + '\'' +
                ", data=" + data +
                '}';
    }
}
