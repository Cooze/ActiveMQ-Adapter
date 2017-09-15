package org.cooze.activemq.adapter;

/**
 * @author cooze
 * @version 1.0.0
 * @desc 消息通道前缀常量
 * @date 2017/9/9
 */
public class JmsMqttConst {

    //ack心跳消息通道
    public static final String ACK_HEART_BEAT_TOPIC_PREFFIX = "ACK_";

    //服务端发布消息的通道前缀
    public static final String MSG_PUBLISH_TOPIC_PREFFIX = "S_PUB_";

    //安卓应用注册通道前缀
    public static final String INIT_TOPIC_PREFFIX = "RT_";

    //安卓订阅消息主题前缀
    public static final String DEST_MQTT_TOPIC_PREFFIX = "A_SUB_";

    //JMS 客户端id前缀
    public static final String JMS_CLIENT_ID_PREFFIX = "JMS_CLIENT_ID_";

    //MQTT 客户端id前缀
    public static final String MQTT_CLIENT_ID_PREFFIX = "MQTT_CLIENT_ID_";

    //ack 心跳客户端id前缀
    public static final String ACK_HEART_BEAT_CLIENT_ID_PREFFIX = "ACK_CLIENT_ID_";

}
