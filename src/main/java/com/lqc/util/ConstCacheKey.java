package com.lqc.util;

/**
 * @author : liqinchao
 * @CreateTime : 2018/5/8 15:32
 * @Description :cache key统一声明
 */
public class ConstCacheKey {
    /**
     * 店长角色默认权限缓存key
     */
    public static final String AUTH_DEFAULT_MANAGER_ENABLE="auth.default.manager.enable";
    /**
     * 收银员角色默认权限缓存key
     */
    public static final String AUTH_DEFAULT_CASHIER_ENABLE="auth.default.cashier.enable";
    /**
     *  管理员所有权限key{+用户ID}
     */
    public static final String AUTH_ALL_KEY_ADMIN="auth.all.key.admin.";
    /**
     *  商家管理员所有权限key{+用户ID}
     */
    public static final String AUTH_ALL_KEY_ADMIN_BUSINESS="auth.all.key.admin.business.";
    /**
     *  短信发送间隔缓存key{+用户手机号}
     */
    public static final String SMS_SEND_KEY="sms.send.";
    /**
     *  验证码发送间隔缓存key{+用户手机号}
     */
    public static final String SMS_CODE_KEY="sms.code.";
    /**
     *  验证码IP发送条数缓存key
     */
    public static final String SMS_COUNT_IP_KEY="sms.limit.ip.";
    /**
     *  验证码手机号发送条数缓存key
     */
    public static final String SMS_COUNT_TEL_KEY="sms.limit.tel.";
    /**
     *  微信access_token缓存key，有效期7200秒
     */
    public static final String WX_ACCESS_TOKEN_KEY="wx.access.token.";
    /**
     *  微信jsapi_ticket缓存key，有效期7200秒
     */
    public static final String WX_JS_TICKET_KEY="wx.js.ticket.";
    /**
     *  微信小程序，用户openid-session_key缓存key，有效期7200秒
     */
    public static final String WX_OPENID_MINI_KEY="wx.openid.mini.";
    /**
     * 手机号段缓存key
     */
    public static final String PHONE_PROVINCE_KEY = "phone_province_";

    /**
     * 交易密码缓存key
     */
    public static final String TRADE_PWD_TOKEN_KEY = "trade_pwd_token_";

    /**
     * 区域缓存key
     */
    public static final String COMPLETE_AREA_KEY = "complete_area_";
    /**
     * 省份缓存key
     */
    public static final String AREA_KEY_PROVINCE= "area_province_";
    /**
     * 城市缓存key
     */
    public static final String AREA_KEY_CITY= "area_city_";
    /**
     * 区县缓存key
     */
    public static final String AREA_KEY_DISTRICT= "area_district_";
    /**
     * socket连接缓存key+{设备序列号}
     */
    public static final String SOCKET_NODE_KEY_DEVICE = "socket.node.device.";
    /**
     * socket连接缓存key+{设备组编号}
     */
    public static final String SOCKET_NODE_KEY_DEVICE_GROUP = "socket.node.device.group.";
    /**
     * 设备组建立socket连接的服务器IP缓存key+{设备组编号}
     */
    public static final String SOCKET_NODE_KEY_DEVICE_GROUP_IP = "socket.node.device.group.ip.";
    /**
     * oauth授权用户订购业务对象缓存key+{授权码}
     */
    public static final String OAUTH_CODE_KEY = "oauth.code.";
    /**
     * 订单不存在的缓存key+{支付号/订单号}
     */
    public static final String ORDER_EXIST_NOT = "order.exist.not.";
    /**
     * 设备锁缓存key+{设备序列号}
     */
    public static final String DEVICE_LOCK = "device.lock.";
    /**
     * 商品信息key
     */
    public static final String SHOPXX_GOODS_CODE = "shopxx.goods.code.";
    /**
     * 店铺业务分成key
     */
    public static final String STORE_APP_DIVIDE = "store.app.divide.";
    /**
     *  税率列表key{+商家类型}
     */
    public static final String TAX_LIST_KEY="tax.list.key.";

}
