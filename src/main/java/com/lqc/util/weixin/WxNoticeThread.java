package com.lqc.util.weixin;

import exception.HxException;
import service.RedisService;

import javax.inject.Inject;

/**
 * @author : liqinchao
 * @CreateTime : 2018/7/18 16:05
 * @Description :发送微信模板消息线程
 */
public class WxNoticeThread extends Thread {
    private final static play.Logger.ALogger logger = play.Logger.of("thread.WxNoticeThread");
    public WxMessage wxMessage;
    public String appId;
    public String secret;
    private RedisService cache;
    @Inject
    public WxNoticeThread(RedisService cache) {
        this.cache=cache;
    }

    @Override
    public void run() {
        String opt = "发送微信模板消息";
        try {
            if (null == wxMessage) {
                throw new HxException(HxException.ERROR_MSG_PARAM);
            }
            logger.info(opt + "，入参：" + wxMessage.toString());
            WxUtil wxUtil=new WxUtil(cache);
            wxUtil.sendNotice(wxMessage,appId,secret);
            logger.info(opt + "，成功。");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(opt + ",异常：", e);
        }
    }
}
