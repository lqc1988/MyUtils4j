package com.lqc.mq;

import org.apache.activemq.ActiveMQConnectionFactory;
import com.lqc.util.ConstUtil;

import javax.jms.*;

/**
 * @author Admin
 */
public class ActiveMqConsumer {
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
            System.out.println("MQ消费者(MsgReceive)消息处理,start");
            Connection connection = factory.createConnection();
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = session.createQueue(ConstUtil.MQ_QUEUE_NAME_1);
            MessageConsumer consumer = session.createConsumer(queue);
            while (true) {
                Message message=consumer.receive();
                if (null == message) {
                    break;
                }
                if (message instanceof TextMessage) {
                    TextMessage msg = (TextMessage)message;
                    System.out.println("MsgReceive--" + msg.getText());
                    System.out.println("MsgReceive--Property--String>>" + msg.getStringProperty("p1"));
                    System.out.println("MsgReceive--Property--Boolean>>" + msg.getBooleanProperty("vip"));
                } else {
                    System.out.println("Queue#未知消息类型--" +message.getJMSType());
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
            Queue queue = session.createQueue(ConstUtil.MQ_QUEUE_NAME_1);
            MessageConsumer consumer = session.createConsumer(queue);
            consumer.setMessageListener(msg -> {
                if (null != msg){
                    if(msg instanceof TextMessage) {
                        TextMessage message = (TextMessage) msg;
                        try {
                            System.out.println("MsgListener--TextMessage--" + message.getText());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if(msg instanceof MapMessage) {
                        MapMessage message = (MapMessage) msg;
                        try {
                            System.out.println("MsgListener--MapMessage--" + message.getObject("k1"));
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
