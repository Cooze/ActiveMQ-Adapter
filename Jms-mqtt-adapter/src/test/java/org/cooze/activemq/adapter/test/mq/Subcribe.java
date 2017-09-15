package org.cooze.activemq.adapter.test.mq;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @author cooze
 * @version 1.0.0
 * @desc
 * @date 2017/9/7
 */
public class Subcribe {

    public static void main(String[] args) throws Exception {


        String clientId = "xxxxxxxxxx";
        ConnectionFactory cf = new ActiveMQConnectionFactory("tcp://loalhost:61616");
        Connection connection = cf.createConnection();

        connection.setClientID(clientId);
        connection.start();


        Session session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
        Topic topic = session.createTopic("TestTopic");
        TopicSubscriber consumer = session.createDurableSubscriber(topic, clientId);


        while (true) {
            TextMessage message = (TextMessage) consumer.receive();
            session.commit();
            System.out.println("222接收到的消息是:" + message.getText());
        }


//        session.close();
//        connection.close();


    }
}
