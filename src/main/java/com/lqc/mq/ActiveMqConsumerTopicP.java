package com.lqc.mq;

import com.lqc.util.ConstUtil;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @author Admin
 * 持久化订阅
 */
public class ActiveMqConsumerTopicP {
    static final ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(ConstUtil.MQ_USER_NAME, ConstUtil.MQ_PASSWORD, ConstUtil.MQ_URL);

    public static void main(String[] args) {
        consumerSubscriber();
    }

    /**
     * 同步阻塞式消费
     */
    static void consumerSubscriber() {
        try {
            System.out.println("MQ消费者>>>>>>2<<<<<<<<(TopicSubscriber)消息处理,start");
            Connection connection = factory.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Topic topic = session.createTopic(ConstUtil.MQ_TOPIC_NAME_1);
            TopicSubscriber topicSubscriber = session.createDurableSubscriber(topic, "remark...");
            connection.start();

            Message message = topicSubscriber.receive();
            while (null != message) {
                if (message instanceof TextMessage) {
                    TextMessage msg = (TextMessage) message;
                    System.out.println("Topic#TopicSubscriber--" + msg.getText());
                } else {
                    System.out.println("Topic#未知消息类型--" + message.getJMSType());
                }
                message = topicSubscriber.receive();
            }
            session.close();
            connection.close();
            System.out.println("MQ消费者(MsgReceive)消息处理,end");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
