package org.cooze.activemq.adapter.core;

import org.cooze.activemq.adapter.JmsMqttConst;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.mqtt.client.*;

import java.util.concurrent.*;
import java.util.concurrent.Future;

/**
 * @author cooze
 * @version 1.0.0
 * @desc 该类为ACK心跳的实现类
 * @date 2017/9/8
 */
public class Ack {


    private boolean ack = true;

    private String clientId;
    private String ackHeartBeatTopicName;

    private long timeout = 3;

    private BlockingConnection connection;

    /**
     * @param host                  ip
     * @param port                  mqtt端口，默认使用1883
     * @param timeout               设置ack的超时时间
     * @param clientId              clientId，要办证唯一，并且在一次创建之后不再变更。
     * @param ackHeartBeatTopicName ack心跳主题名称
     * @throws Exception
     */
    public Ack(String host, int port, long timeout, String clientId, String ackHeartBeatTopicName) throws Exception {
        MQTT mqtt = new MQTT();
        mqtt.setHost(host, port);
        mqtt.setClientId(UTF8Buffer.utf8(clientId));
        this.clientId = clientId;
        this.ackHeartBeatTopicName = ackHeartBeatTopicName;
        this.connection = mqtt.blockingConnection();
        this.timeout = timeout;
        Topic[] topics = {new Topic(ackHeartBeatTopicName, QoS.EXACTLY_ONCE)};
        this.connection.connect();
        this.connection.subscribe(topics);
    }

    /**
     * 接收ack心跳信息
     *
     * @throws Exception
     */
    private void recieve() throws Exception {

        Message message = connection.receive();
        if (message != null && message.getPayload() != null) {
            message.ack();
            AckCache.getInstance().setAlive(this.clientId, true);
        }
    }

    /**
     * 停止ack心跳的订阅连接
     *
     * @throws Exception
     */
    public void stop() throws Exception {
        if (this.connection != null) {
            this.connection.disconnect();
        }
    }

    /**
     * 心跳超时检测
     */
    public void ticktock() {

        ExecutorService executor = Executors.newSingleThreadExecutor();

        Future<Boolean> future = executor.submit(() -> {
            recieve();
            return true;
        });

        try {
            future.get(timeout, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            ack = false;
        } catch (ExecutionException e) {
            ack = false;
        } catch (TimeoutException e) {
            ack = false;
        } finally {
            executor.shutdownNow();

            try {
                executor.awaitTermination(Integer.MAX_VALUE, TimeUnit.SECONDS);
            } catch (InterruptedException e) {

            }
            //心跳超时则移除心跳标志以及移除android在线标志
            if (!ack) {
                AckCache.getInstance().remove(clientId);
                AckCache.getInstance().removeMqttTopic(this.ackHeartBeatTopicName.replaceAll(JmsMqttConst.ACK_HEART_BEAT_TOPIC_PREFFIX, ""));
            }

        }
    }

}
