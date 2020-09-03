package com.lqc.mq;

import com.lqc.util.ConstUtil;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @author Admin
 */
public class ActiveMqProducer {
    static final ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(ConstUtil.MQ_USER_NAME, ConstUtil.MQ_PASSWORD, ConstUtil.MQ_URL);

    public static void main(String[] args) {
        producer1();
        producer2();
    }

    /**
     * 生产者
     */
    static void producer1() {
        try {
            System.out.println("MQ生产消息(TextMessage)发送,start");
            Connection connection = factory.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = session.createQueue(ConstUtil.MQ_QUEUE_NAME_1);
            MessageProducer producer = session.createProducer(queue);
            for (int i = 0; i < 3; i++) {
                TextMessage msg = session.createTextMessage("BB测试msg---" + i);
                //对消息体设置特殊属性
                msg.setStringProperty("p1","pv1");
                msg.setBooleanProperty("vip",true);
                //配置是否持久化：此方式无效
//                msg.setJMSDeliveryMode(DeliveryMode.NON_PERSISTENT);
                //配置是否持久化：此方式有效
                producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
                producer.send(msg);
                //配置是否持久化：此方式有效
//                producer.send(msg,DeliveryMode.NON_PERSISTENT,0,1000L);
            }
            producer.close();
            session.close();
            connection.close();
            System.out.println("MQ生产消息(TextMessage)发送,end");
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生产者
     */
    static void producer2() {
        try {
            System.out.println("MQ生产消息(MapMessage)发送,start");
            Connection connection = factory.createConnection();
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = session.createQueue(ConstUtil.MQ_QUEUE_NAME_1);
            MessageProducer producer = session.createProducer(queue);
            for (int i = 0; i < 3; i++) {
                MapMessage msg = session.createMapMessage();
                msg.setJMSDeliveryMode(DeliveryMode.PERSISTENT);
                msg.setObject("k1", "v>>" + i);
                producer.send(msg);
            }
            producer.close();
            session.close();
            connection.close();
            System.out.println("MQ生产消息(MapMessage)发送,end");
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
