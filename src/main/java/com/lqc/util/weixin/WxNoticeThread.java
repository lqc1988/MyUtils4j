package com.lqc.util.weixin;

import com.lqc.enums.ResultEnum;
import com.lqc.exception.MyException;
import com.lqc.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author : liqinchao
 * @CreateTime : 2018/7/18 16:05
 * @Description :发送微信模板消息线程
 */
public class WxNoticeThread extends Thread {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    public WxMessage wxMessage;
    public String appId;
    public String secret;
    //private RedisService cache;

    @Override
    public void run() {
        String opt = "发送微信模板消息";
        try {
            if (null == wxMessage) {
                throw new MyException(ResultEnum.ERR_PARAM.getDisplay());
            }
            logger.info(opt + "，入参：" + wxMessage.toString());
            //WxUtil wxUtil=new WxUtil(cache);
            WxUtil wxUtil = new WxUtil();
            wxUtil.sendNotice(wxMessage, appId, secret);
            logger.info(opt + "，成功。");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(opt + ",异常：", e);
        }
    }
}
