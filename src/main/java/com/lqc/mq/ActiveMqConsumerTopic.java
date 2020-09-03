package com.lqc.mq;

import org.apache.activemq.ActiveMQConnectionFactory;
import com.lqc.util.ConstUtil;
import javax.jms.*;

/**
 * @author Admin
 */
public class ActiveMqConsumerTopic {
    static final ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(ConstUtil.MQ_USER_NAME, ConstUtil.MQ_PASSWORD, ConstUtil.MQ_URL);

    public static void main(String[] args) {
        consumerReceive();
//        consumerListener();
    }

    /**
     * 同步阻塞式消费
     */
    static void consumerReceive() {
        try {
            System.out.println("MQ消费者>>>>>>2<<<<<<<<(MsgReceive)消息处理,start");
            Connection connection = factory.createConnection();
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Topic topic = session.createTopic(ConstUtil.MQ_TOPIC_NAME_1);
            MessageConsumer consumer = session.createConsumer(topic);
            while (true) {
                Message message=consumer.receive();
                if (null == message) {
                    break;
                }
                if (message instanceof TextMessage) {
                    TextMessage msg = (TextMessage) message;
                    System.out.println("Topic#MsgReceive--" + msg.getText());
                } else {
                    System.out.println("Topic#未知消息类型--" +message.getJMSType());
                }
            }
            consumer.close();
            session.close();
            connection.close();
            System.out.println("MQ消费者(MsgReceive)消息处理,end");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 异步监听消费
     */
    static void consumerListener() {
        try {
            System.out.println("MQ消费者(MsgListener)消息处理,start");
            Connection connection = factory.createConnection();
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Topic topic = session.createTopic(ConstUtil.MQ_TOPIC_NAME_1);
            MessageConsumer consumer = session.createConsumer(topic);
            consumer.setMessageListener(msg -> {
                if (null != msg){
                    if(msg instanceof TextMessage) {
                        TextMessage message = (TextMessage) msg;
                        try {
                            System.out.println("Topic#MsgListener--TextMessage--" + message.getText());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if(msg instanceof MapMessage) {
                        MapMessage message = (MapMessage) msg;
                        try {
                            System.out.println("Topic#MsgListener--MapMessage--" + message.getObject("k1"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            System.in.read();
            consumer.close();
            session.close();
            connection.close();
            System.out.println("MQ消费者(MsgListener)消息处理,end");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
