package org.cooze.activemq.android;

import org.cooze.activemq.android.timer.JmsMqttTimer;
import org.cooze.activemq.android.timer.JmsMqttTimerTask;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.mqtt.client.*;

import java.util.Timer;
import java.util.TimerTask;


/**
 * @author cooze
 * @version 1.0.0
 * @desc
 * @date 2017/9/7
 */
public class MqBuilder {

    //ack心跳通道前缀
    public static final String ACK_HEART_BEAT_TOPIC_PREFFIX = "ACK_";

    //安卓应用注册通道前缀
    public static final String INIT_TOPIC_PREFFIX = "RT_";

    //安卓订阅消息主题前缀
    public static final String DEST_MQTT_TOPIC_PREFFIX = "A_SUB_";

    private String host;
    private int port;
    private String serverId;
    private String androidId;


    public MqBuilder() {
    }

    public MqBuilder(String host, int port, String serverId, String androidId) {
        this.host = host;
        this.port = port;
        this.serverId = serverId;
        this.androidId = androidId;
    }

    private void ackHeartBeat(String androidId, String host, int port) throws Exception {
        String topic = ACK_HEART_BEAT_TOPIC_PREFFIX + androidId;
        mqtt(host, port, topic, androidId, 1);
    }

    private void mqtt(String host, int port, String topicName, String msg, int type) throws Exception {
        BlockingConnection connection;
        MQTT mqtt = new MQTT();
        mqtt.setHost(host, port);
        connection = mqtt.blockingConnection();
        connection.connect();

        switch (type) {
            case 1:
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        publish(connection, topicName, msg);
                    }
                }, 0, 100);
                break;
            case 2:
                new JmsMqttTimer().start(new JmsMqttTimerTask() {
                    @Override
                    public void run() {
                        publish(connection, topicName, msg);
                    }
                }, 5, 2);
                break;
        }
    }


    private void publish(BlockingConnection connection, String topicName, String msg) {
        try {
            connection.publish(topicName, msg.getBytes(), QoS.EXACTLY_ONCE, false);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                connection.disconnect();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }


    private void initRegister(String serverId, String androidId, String host, int port) throws Exception {
        String topic = INIT_TOPIC_PREFFIX + serverId;
        mqtt(host, port, topic, androidId, 2);
    }

    private void subcribe(String host, int port, String androidId) throws Exception {
        MQTT mqtt = new MQTT();
        mqtt.setHost(host, port);

        String client_id = "terminal_" + androidId;
        mqtt.setClientId(UTF8Buffer.utf8(client_id));
        BlockingConnection connection = mqtt.blockingConnection();

        String topicName = DEST_MQTT_TOPIC_PREFFIX + androidId;

        Topic[] topics = {new Topic(topicName, QoS.EXACTLY_ONCE)};
        connection.connect();
        connection.subscribe(topics);
        try {
            while (true) {
                Message message = connection.receive();
                if (message != null && message.getPayload() != null) {
                    this.monitor.monitor(new String(message.getPayload()));
                    message.ack();
                }
            }
        } catch (Exception e) {
        } finally {
            if (connection != null) {
                try {
                    connection.disconnect();
                } catch (Exception e) {

                }
            }
        }
    }

    private MessageMonitor monitor;

    public MqBuilder setMonitor(MessageMonitor monitor) {
        this.monitor = monitor;
        return this;
    }

    public void start() throws Exception {
        if (this.monitor == null) {
            throw new MonitorNotSetExcaption("monitor is null,please set your message monitor before invoke this method.");
        }
        initRegister(this.serverId, this.androidId, this.host, this.port);
        ackHeartBeat(this.androidId, host, port);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    subcribe(host, port, androidId);
                } catch (Exception e) {
                }
            }
        }).start();
    }


}
