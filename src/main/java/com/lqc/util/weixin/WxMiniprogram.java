package com.lqc.util.weixin;

/**
 * @author: liqinchao
 * @Date: 2020/4/9 14:37
 * @Description: 跳小程序所需数据，不需跳小程序可不用传该数据
 */
public class WxMiniprogram {
    /**
     * 所需跳转到的小程序appid-[选填]
     * （该小程序appid必须与发模板消息的公众号是绑定关联关系，暂不支持小游戏）
     */
    public String appid;
    /**
     * 所需跳转到小程序的具体页面路径-[选填]
     * 支持带参数（示例index?foo=bar），暂不支持小游戏
     */
    public String pagepath;

    @Override
    public String toString() {
        return "WxMiniprogram{" +
                "appid='" + appid + '\'' +
                ", pagepath='" + pagepath + '\'' +
                '}';
    }
}
