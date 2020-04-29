package com.lqc.util;

import com.lqc.exception.MyException;
import org.apache.commons.lang3.StringUtils;

import javax.mail.Address;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * @author: liqinchao
 * @Date: 2020/4/17 14:36
 * @Description: Email工具类
 */
public class EmailUtil {
    /**
     * 收件人数据传输对象
     */
    static class DestDTO {
        public String destAddr;
        public String destName;
    }
    /**
     * 配置文件
     */
    public static final String FROM_NAME = ConstUtil.conf.getString("email.server.fromName");
    // 发件人的邮箱
    public static final String FROM_ADDR = ConstUtil.conf.getString("email.server.fromAddr");
    // 发件人的密码/授权码
    // PS: 某些邮箱服务器为了增加邮箱本身密码的安全性，给 SMTP 客户端设置了独立密码（有的邮箱称为“授权码”）,
    //     对于开启了独立密码的邮箱, 这里的邮箱密码必需使用这个独立密码（授权码）。
    public static final String FROM_PWD = ConstUtil.conf.getString("email.server.fromPwd");
    //private static String fromName = "恒信东方";
    //private static String fromAddr = "actionabc@163.com";
    //private static String fromPwd = "XXNZXBXUERNJSBAG";

    // 发件人邮箱的 SMTP 服务器地址, 必须准确, 不同邮件服务器地址不同, 一般(只是一般, 绝非绝对)格式为: smtp.xxx.com
    // 网易126邮箱的 SMTP 服务器地址为: smtp.126.com
    private static final String SMTP_HOST = ConstUtil.conf.getString("email.server.smtpHost");
    //端口号
    private static final String SMTP_PORT = ConstUtil.conf.getString("email.server.smtpPort");
    //当前模式：prod-生产，test-测试，dev-研发
    private static final String MOD = ConstUtil.conf.getString("email.server.mod");


    /**
     * 初始化配置
     * @return
     */
    public static Properties initProperties() {
        // 1. 创建参数配置, 用于连接邮件服务器的参数配置
        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.smtp.host", SMTP_HOST);
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.smtp.port", SMTP_PORT);

        // PS: 某些邮箱服务器要求 SMTP 连接需要使用 SSL 安全认证 (为了提高安全性, 邮箱支持SSL连接, 也可以自己开启),
        //     如果无法连接邮件服务器, 仔细查看控制台打印的 log, 如果有有类似 “连接失败, 要求 SSL 安全连接” 等错误,
        //     取消下面 /* ... */ 之间的注释代码, 开启 SSL 安全连接。
        /*
        // SMTP 服务器的端口 (非 SSL 连接的端口一般默认为 25, 可以不添加, 如果开启了 SSL 连接,
        //                  需要改为对应邮箱的 SMTP 服务器的端口, 具体可查看对应邮箱服务的帮助,
        //                  QQ邮箱的SMTP(SLL)端口为465或587, 其他邮箱自行去查看)
        final String smtpPort = "465";
        props.setProperty("mail.smtp.port", smtpPort);
        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.socketFactory.port", smtpPort);
        */
        return props;
    }

    /**
     * 发送邮件
     *
     * @param destAddr 接收人Email
     * @param destName 接收人姓名
     * @param subject  主题
     * @param content  内容
     * @param props     邮件服务配置，可使用 EmailUtil.initProperties()方法获取
     * @throws Exception
     */
    public static void sendEmail(String destAddr, String destName, String subject, String content,Properties props) throws Exception {
        sendEmail(destAddr, destName, subject, content, null,props);
    }

    /**
     * 发送邮件
     *
     * @param destAddr 接收人Email
     * @param destName 接收人姓名
     * @param subject  主题
     * @param content  内容
     * @param type     内容类型，默认：text/html;charset=UTF-8，支持HTML标签
     * @param props     邮件服务配置，可使用 EmailUtil.initProperties()方法获取
     * @throws Exception
     */
    public static void sendEmail(String destAddr, String destName, String subject, String content, String type,Properties props) throws Exception {
        if (StringUtils.isBlank(destAddr) || StringUtils.isBlank(destName) || StringUtils.isBlank(subject) || StringUtils.isBlank(content)) {
            throw new MyException("参数错误");
        }
        List<DestDTO> destList = new ArrayList<>();
        DestDTO dest = new DestDTO();
        dest.destAddr = destAddr;
        dest.destName = destName;
        destList.add(dest);
        sendEmail(destList, subject, content, type,props);
    }

    /**
     * 发送邮件
     *
     * @param destList 接收人数据
     * @param subject  主题
     * @param content  内容
     * @param type     内容类型，默认：text/html;charset=UTF-8，支持HTML标签
     * @param props     邮件服务配置，可使用 EmailUtil.initProperties()方法获取
     * @throws Exception
     */
    public static void sendEmail(List<DestDTO> destList, String subject, String content, String type,Properties props) throws Exception {
        if (null == destList || destList.isEmpty() || StringUtils.isBlank(subject) || StringUtils.isBlank(content)) {
            throw new MyException("参数错误");
        }
        type = StringUtils.isBlank(type) ? "text/html;charset=UTF-8" : type;

        // 2. 根据配置创建会话对象, 用于和邮件服务器交互
        Session session = Session.getInstance(props);
        // 设置为debug模式, 可以查看详细的发送 log
        session.setDebug(StringUtils.isBlank(MOD) || !"prod".equals(MOD));

        // 3. 创建一封邮件
        MimeMessage message = new MimeMessage(session);
        // From: 发件人
        message.setFrom(new InternetAddress(FROM_ADDR, FROM_NAME, "UTF-8"));
        //  To: 收件人（可以增加多个收件人、抄送、密送）

        Address[] addresses = new Address[destList.size()];
        for (int i = 0; i < destList.size(); i++) {
            addresses[i] = new InternetAddress(destList.get(i).destAddr, destList.get(i).destName, "UTF-8");
        }
        message.setRecipients(MimeMessage.RecipientType.TO, addresses);
        // Subject: 邮件主题
        message.setSubject(subject, "UTF-8");
        message.setContent(content, type);
        // 设置发件时间
        message.setSentDate(new Date());
        // 保存设置
        message.saveChanges();

        // 4. 根据 Session 获取邮件传输对象
        Transport transport = session.getTransport();

        // 5. 使用 邮箱账号 和 密码 连接邮件服务器, 这里认证的邮箱必须与 message 中的发件人邮箱一致, 否则报错
        //
        //    PS_01: 如果连接服务器失败, 都会在控制台输出相应失败原因的log。
        //    仔细查看失败原因, 有些邮箱服务器会返回错误码或查看错误类型的链接,
        //    根据给出的错误类型到对应邮件服务器的帮助网站上查看具体失败原因。
        //
        //    PS_02: 连接失败的原因通常为以下几点, 仔细检查代码:
        //           (1) 邮箱没有开启 SMTP 服务;
        //           (2) 邮箱密码错误, 例如某些邮箱开启了独立密码;
        //           (3) 邮箱服务器要求必须要使用 SSL 安全连接;
        //           (4) 请求过于频繁或其他原因, 被邮件服务器拒绝服务;
        //           (5) 如果以上几点都确定无误, 到邮件服务器网站查找帮助。
        //
        transport.connect(FROM_ADDR, FROM_PWD);

        // 6. 发送邮件, 发到所有的收件地址, message.getAllRecipients() 获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
        transport.sendMessage(message, message.getAllRecipients());

        // 7. 关闭连接
        transport.close();


    }

    public static void main(String[] args) throws Exception {
        List<DestDTO> destList = new ArrayList<>();
        DestDTO dest = new DestDTO();
        dest.destAddr = "285462571@qq.com";
        dest.destName = "测试收件人1";
        destList.add(dest);

        DestDTO dest2 = new DestDTO();
        dest2.destAddr = "lqclh502@163.com";
        dest2.destName = "测试收件人2";
        destList.add(dest2);
        String subject = "测试邮件333";
        String content = "【恒信东方】店铺到期提醒：<br>【联营店铺】【店铺qq】即将于60天后的2020-06-15到期。<br>【买断店铺】【店铺11】即将于50天后的2020-06-05到期。<br>请登录中心平台处理。";
        String type = "text/html;charset=UTF-8";
        sendEmail(destList, subject, content, type,initProperties());
    }

    /**
     * 创建一封只包含文本的简单邮件
     *
     * @param session  和服务器交互的会话
     * @param fromAddr 发件人邮箱
     * @param destAddr 收件人邮箱
     * @return
     * @throws Exception
     */
    public static MimeMessage createMimeMessage(Session session, String fromAddr, String destAddr) throws Exception {
        // 1. 创建一封邮件
        MimeMessage message = new MimeMessage(session);

        // 2. From: 发件人
        message.setFrom(new InternetAddress(fromAddr, "恒信东方", "UTF-8"));

        // 3. To: 收件人（可以增加多个收件人、抄送、密送）
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(destAddr, "测试收件人1", "UTF-8"));

        // 4. Subject: 邮件主题
        message.setSubject("测试Java发邮件", "UTF-8");

        // 5. Content: 邮件正文（可以使用html标签）
        String content = "【恒信东方】店铺到期提醒：<br>【联营店铺】【店铺1】即将于60天后的2020-06-15到期。<br>【买断店铺】【店铺2】即将于50天后的2020-06-05到期。<br>请登录中心平台处理。";
        message.setContent(content, "text/html;charset=UTF-8");
        // 6. 设置发件时间
        message.setSentDate(new Date());

        // 7. 保存设置
        message.saveChanges();

        return message;
    }
}
