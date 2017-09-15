package org.cooze.activemq.adapter.test.mq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.cooze.activemq.adapter.JmsMqttConst;

import javax.jms.*;

/**
 * @author cooze
 * @version 1.0.0
 * @desc
 * @date 2017/9/7
 */
public class Publish {

    public static void main(String[] args) throws Exception {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://127.0.0.1:61616");
        Connection connection = connectionFactory.createConnection();
        connection.start();

        String iserver_sn = "server-1111";
        String topicName = JmsMqttConst.MSG_PUBLISH_TOPIC_PREFFIX + iserver_sn;
        Session session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createTopic(topicName);
        MessageProducer producer = session.createProducer(destination);
        producer.setDeliveryMode(DeliveryMode.PERSISTENT);

        for (int i = 0; i < 10; i++) {
            TextMessage message = session.createTextMessage("--->测试消息000" + i);
            producer.send(message);
        }
        session.commit();
        session.close();
        connection.close();

    }
}