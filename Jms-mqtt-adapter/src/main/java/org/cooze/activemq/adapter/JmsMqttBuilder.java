package org.cooze.activemq.adapter;

import org.cooze.activemq.adapter.core.AckCache;
import org.cooze.activemq.adapter.core.JmsToMQTT;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.mqtt.client.*;

/**
 * @author cooze
 * @version 1.0.0
 * @desc JMS
 * @date 2017/9/9
 */
public class JmsMqttBuilder {


    private BlockingConnection connection;

    private JmsMqttConfigure configure;

    public JmsMqttBuilder(JmsMqttConfigure configure) {
        this.configure = configure;
    }

    //只初始化一次
    public void start() throws Exception {
        MQTT mqtt = new MQTT();
        mqtt.setHost(configure.getHost(), configure.getMqtt_port());


        mqtt.setClientId(UTF8Buffer.utf8(JmsMqttConst.MQTT_CLIENT_ID_PREFFIX + configure.getServerId()));
        this.connection = mqtt.blockingConnection();


        Topic[] topics = {new Topic(JmsMqttConst.INIT_TOPIC_PREFFIX + configure.getServerId(), QoS.EXACTLY_ONCE)};
        this.connection.connect();
        this.connection.subscribe(topics);
        Thread thread = new Thread(() -> run());
        thread.start();
    }

    private void run() {
        try {
            while (true) {
                Message message = this.connection.receive();
                if (message != null && message.getPayload() != null) {
                    String yp_sn = new String(message.getPayload(), "utf-8");
                    checkAndCreateTopic(yp_sn);
                    message.ack();
                }
            }
        } catch (Exception e) {
        } finally {
            if (this.connection != null) {
                try {
                    this.connection.disconnect();
                } catch (Exception e) {

                }
            }
        }
    }

    private void checkAndCreateTopic(String androidId) {
        if (!AckCache.getInstance().topicIsStart(androidId)) {
            //re-define configure
            String receiveTopicName = JmsMqttConst.MSG_PUBLISH_TOPIC_PREFFIX + configure.getServerId();
            String ackTopicName = JmsMqttConst.ACK_HEART_BEAT_TOPIC_PREFFIX + androidId;
            String destMqttTopicName = JmsMqttConst.DEST_MQTT_TOPIC_PREFFIX + androidId;
            JmsMqttConfigure conf = this.configure.rebuild(androidId, receiveTopicName, ackTopicName, destMqttTopicName);
            newJmsMqtt(conf);
        }
    }

    private void newJmsMqtt(JmsMqttConfigure conf) {
        String clientId = conf.getAndroidId();

        JmsToMQTT subcribeFactory = new JmsToMQTT(clientId, conf.getHost(), conf.getMqtt_port(), conf.getJms_port());
        Thread t = new Thread(() -> {
            try {
                subcribeFactory.start(conf.getReceiveJmsTopicName(), conf.getDestMqttTopicName(), conf.getAckTopicName());
            } catch (Exception e) {
            }
        });

        t.start();
        subcribeFactory.setThread(t);
        AckCache.getInstance().setMqttTopic(conf.getAndroidId());
    }

}
