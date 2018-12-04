package com.may.utils;


import java.math.BigDecimal;
import java.util.ResourceBundle;

/**
 * ClassName: ConstUtil
 * CreateTime 2017年12月11日 下午3:46:39
 * author : liqinchao
 * Description: 系统常量工具类
 */
public class ConstUtil {
    public static ResourceBundle conf = ResourceBundle.getBundle("config");

    /**
     * 当前应用运行模式
     */
    public static final String APP_MOD = conf.getString("app.mod");
    /**
     * 网站标题
     */
    public static final String WEB_TITLE_CENTER = conf.getString("web.title");
    /**
     * 网站域名
     */
    public static final String DOMAIN_CENTER = conf.getString("web.domain");
    /**
     * 文件服务器地址
     */
    public static final String DOMAIN_FILE = conf.getString("file.domain");
    /**
     * 短信签名
     */
    public static final String SMS_SIGN = conf.getString("sms.sign");
    /**
     * 文件存储根目录
     */
    public static final String FILE_PATH_ROOT = conf.getString("file.path.root");
    /**
     * 上传文件路径
     */
    public static final String FILE_PATH_UPLOAD = conf.getString("file.path.upload");
    /**
     * 错误文件路径
     */
    public static final String FILE_PATH_ERROR = conf.getString("file.path.error");
    /**
     * 图片存储路径
     */
    public final static String FILE_PATH_IMG = conf.getString("file.path.img");
    /**
     * 图片验证码key，其对应的session中的value是验证码在缓存中的key。
     */
    public static final String IMG_VALIDATE_CODE = "img_" + RandomUtils.randomAlphabetic(16);
    /**
     * 复杂图片验证码key，其对应的session中的value是验证码在缓存中的key。
     */
    public static final String IMG_VALIDATE_CODE_FZ = "imgFZ_" + RandomUtils.randomAlphabetic(16);
    /**
     * 当前管理员用户名，保存在session中
     */
    public static final String SESSION_CURRENT_USER_NAME = "un_" + RandomUtils.randomAlphabetic(16);

    /**
     * 当前用户ID，保存在session中
     */
    public static final String SESSION_CURRENT_USER_ID = "session.admin.id";
    /**
     * 由于play是stateless的，session实际上是保存在Cookie中，所以session超时时间要根据最后请求时间来决定
     */
    public static final String LAST_REQUEST_TIME = "lrt_" + RandomUtils.randomAlphabetic(16);
    /**
     * 当前会员用户名，保存在session中
     */
    public static final String SESSION_CURRENT_MEMBER_NAME = "un_" + RandomUtils.randomAlphabetic(16);

    /**
     * 当前会员ID，保存在session中
     */
    public static final String SESSION_CURRENT_MEMBER_ID = "session.member.id";
    /**
     * 会员最后请求时间
     */
    public static final String LAST_REQUEST_TIME_MEMBER = "lrt_" + RandomUtils.randomAlphabetic(16);

    /**
     * 用户被禁用权限的session key （+userId）
     */
    public static final String USER_AUTH_BAN_KEY = "user_auth_ban_";

    //----- stream 文件上传相关参数 start -----//
    public static final String FILE_NAME_FIELD = "name";
    public static final String FILE_SIZE_FIELD = "size";
    public static final String TOKEN_FIELD = "token";
    public static final String SUCCESS_FIELD = "success";
    public static final String SUCC_NUM_FIELD = "succ";
    public static final String FAIL_NUM_FIELD = "fail";
    public static final String MESSAGE_FIELD = "message";
    public static final String START_FIELD = "start";
    public static final String FILE_SIZE_SHOW_FIELD = "sizeShow";
    public static final String ERROR_FILE_NAME_FIELD = "errorFileName";
    public static final String ERROR_FILE_PATH_FIELD = "errorFilePath";
    public static final String CONTENT_RANGE_HEADER = "content-range";
    //----- stream 文件上传相关参数 end -----//

    public static final BigDecimal zero = new BigDecimal("0");
    /**
     * 当前用户的微信ID，保存在session中
     */
    public static final String SESSION_WEIXIN_ID = "session.weixin.id";
    /**
     * 短信发送间隔，单位：秒
     */
    public static final int SMS_EXPIRE_SECONDS = 60;
    /**
     * 微信token、ticket等有效期：7200秒
     */
    public static final int WX_EXPIRE_SECONDS = 7200;
    /**
     * 请求来源path的session key
     */
    public static final String SOURCE_URI_KEY = "source_uri";
    /**
     * oauth access_token有效期：单位秒，默认30天
     */
    public static final int ACCESS_TOKEN_EXPIRE = 30*24*60*60;
    /**
     * oauth接口会员session ID
     */
    public static final String SESSION_OAUTH_MEMBER_ID = "session.oauth.member.id";
    /**
     * oauth接口会员业务关系ID的session ID
     */
    public static final String SESSION_OAUTH_MEMBER_APP_ID = "session.oauth.member.app.id";

    /**
     * 数据导出每次上线数(1百万)
     */
    public static final int SHEET_ROW_MAX = 1000000;

}
