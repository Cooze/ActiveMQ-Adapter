package org.cooze.activemq.adapter;

/**
 * @author cooze
 * @version 1.0.0
 * @desc
 * @date 2017/9/9
 */
public class JmsMqttConfigure {


    private String host = "127.0.0.1";
    private int mqtt_port = 1883;
    private int jms_port = 61616;

    //客户端id
    private String androidId;
    private String receiveJmsTopicName;
    private String ackTopicName;
    private String destMqttTopicName;

    //服务端id
    private String serverId;


    public JmsMqttConfigure() {
    }

    public JmsMqttConfigure(String host, int mqtt_port, int jms_port, String androidId, String receiveJmsTopicName, String ackTopicName, String destMqttTopicName, String serverId) {
        this.host = host;
        this.androidId = androidId;
        this.mqtt_port = mqtt_port;
        this.jms_port = jms_port;
        this.receiveJmsTopicName = receiveJmsTopicName;
        this.ackTopicName = ackTopicName;
        this.destMqttTopicName = destMqttTopicName;
        this.serverId = serverId;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getAndroidId() {
        return androidId;
    }

    public void setAndroidId(String androidId) {
        this.androidId = androidId;
    }

    public int getMqtt_port() {
        return mqtt_port;
    }

    public void setMqtt_port(int mqtt_port) {
        this.mqtt_port = mqtt_port;
    }

    public int getJms_port() {
        return jms_port;
    }

    public void setJms_port(int jms_port) {
        this.jms_port = jms_port;
    }

    public String getReceiveJmsTopicName() {
        return receiveJmsTopicName;
    }

    public void setReceiveJmsTopicName(String receiveJmsTopicName) {
        this.receiveJmsTopicName = receiveJmsTopicName;
    }

    public String getAckTopicName() {
        return ackTopicName;
    }

    public void setAckTopicName(String ackTopicName) {
        this.ackTopicName = ackTopicName;
    }

    public String getDestMqttTopicName() {
        return destMqttTopicName;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public void setDestMqttTopicName(String destMqttTopicName) {
        this.destMqttTopicName = destMqttTopicName;
    }

    public JmsMqttConfigure rebuild(String androidId, String receiveJmsTopicName, String ackTopicName, String destMqttTopicName) {
        JmsMqttConfigure rebuildObj = this.copy();
        rebuildObj.setAndroidId(androidId);
        rebuildObj.setReceiveJmsTopicName(receiveJmsTopicName);
        rebuildObj.setAckTopicName(ackTopicName);
        rebuildObj.setDestMqttTopicName(destMqttTopicName);
        return rebuildObj;
    }

    public JmsMqttConfigure copy() {
        return new JmsMqttConfigure(this.host, this.mqtt_port, this.jms_port, this.androidId, this.receiveJmsTopicName, this.ackTopicName, this.destMqttTopicName, this.serverId);
    }

}
