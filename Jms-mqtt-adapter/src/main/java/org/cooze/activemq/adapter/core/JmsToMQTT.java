package org.cooze.activemq.adapter.core;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.cooze.activemq.adapter.JmsMqttConst;
import org.fusesource.mqtt.client.BlockingConnection;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.QoS;

import javax.jms.*;

/**
 * @author cooze
 * @version 1.0.0
 * @desc ActiveMQ jms消息处理转成MQTT协议消息，的转换类
 * @date 2017/9/8
 */
public class JmsToMQTT {
    //主机名
    private String host;
    //mqtt端口，默认1883
    private int mqtt_port;
    //客户端id，这个客户端id，是android上线的时候上报的.mq中用于标记一个唯一的订阅客户端
    private String clientId;

    public JmsToMQTT(String clientId, String host, int mqtt_port, int jms_port) {
        this.host = host;
        this.mqtt_port = mqtt_port;
        this.clientId = clientId;

        try {
            this.connection = new ActiveMQConnectionFactory("tcp://" + host + ":" + jms_port).createConnection();
            this.connection.setClientID(JmsMqttConst.JMS_CLIENT_ID_PREFFIX + clientId);
            this.connection.start();
            this.session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);

            MQTT mqtt = new MQTT();
            mqtt.setHost(this.host, this.mqtt_port);
            this.blockingConnection = mqtt.blockingConnection();
            this.blockingConnection.connect();
        } catch (Exception e) {
        }


    }

    private boolean isAlive = true;

    private Connection connection;

    private BlockingConnection blockingConnection;

    private Session session;

    private Thread t;

    public void setThread(Thread t) {
        this.t = t;
    }


    /**
     * 该方法的作用是将jms 主题中的消息转发到mqtt主题中，供android端使用
     *
     * @param recieveTopicName 订阅activeMQ消息主题名称
     * @param destTopicName    消息发布主题名称
     * @param ackTopicName     与客户端保持ack的主题名称
     * @throws Exception
     */
    public void start(String recieveTopicName, String destTopicName, String ackTopicName) throws Exception {

        Topic topic = session.createTopic(recieveTopicName);
        TopicSubscriber consumer = session.createDurableSubscriber(topic, JmsMqttConst.JMS_CLIENT_ID_PREFFIX + clientId);

        consumer.setMessageListener((message) -> {
            TextMessage textMessage = (TextMessage) message;
            try {
                String text = textMessage.getText();
                //接收jms消息，发送到对应客户端的mqtt主题中
                if (isAlive) {
                    blockingConnection.publish(destTopicName, text.getBytes(), QoS.EXACTLY_ONCE, false);
                    session.commit();
                }
            } catch (Exception e) {
            }
        });


        String ackClientId = JmsMqttConst.ACK_HEART_BEAT_CLIENT_ID_PREFFIX + clientId;
        AckCache.getInstance().setAlive(ackClientId, true);
        //心跳超时间为3秒
        Ack ack = new Ack(host, mqtt_port, 3, ackClientId, ackTopicName);
        //心跳检测
        do {
            ack.ticktock();
            Boolean b = AckCache.getInstance().getAlive(ackClientId);
            isAlive = b == null ? false : b;

        } while (isAlive);

        stop(t, ack);
    }

    //当客户端下线，不在发送心跳信息，则中断客户端与服务端的连接，不再消费服务端中MQ的消息
    private void stop(Thread t, Ack ack) throws Exception {

        if (this.session != null) {
            this.session.close();
        }

        if (this.connection != null) {
            this.connection.close();
        }

        if (this.blockingConnection != null) {
            this.blockingConnection.disconnect();
        }

        if (ack != null) {
            ack.stop();
        }

        if (AckCache.getInstance().topicIsStart(clientId)) {
            AckCache.getInstance().removeMqttTopic(clientId);
        }

        if (t != null) {
            t.stop();
        }

    }
}
