package org.cooze.activemq.adapter.test;

import org.cooze.activemq.adapter.JmsMqttConfigure;
import org.cooze.activemq.adapter.JmsMqttBuilder;

/**
 * @author cooze
 * @version 1.0.0
 * @desc
 * @date 2017/9/9
 */
public class JmsMqttBuilderTest {

    public static void main(String[] args) throws Exception {

        String host = "192.168.100.111";
        int mqtt_port = 1883;
        int jms_port = 61616;

        JmsMqttConfigure configure = new JmsMqttConfigure();


        configure.setServerId("server-1111");
        configure.setHost(host);
        configure.setMqtt_port(mqtt_port);
        configure.setJms_port(jms_port);

        JmsMqttBuilder jmsMqttCreator = new JmsMqttBuilder(configure);
        jmsMqttCreator.start();

    }

}
