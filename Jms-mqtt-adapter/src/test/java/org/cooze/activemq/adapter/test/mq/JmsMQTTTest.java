package org.cooze.activemq.adapter.test.mq;

import org.cooze.activemq.adapter.JmsMqttConfigure;
import org.cooze.activemq.adapter.JmsMqttBuilder;

/**
 * @author cooze
 * @version 1.0.0
 * @desc
 * @date 2017/9/9
 */
public class JmsMQTTTest {

    public static void main(String[] args) throws Exception {

        JmsMqttConfigure configure = new JmsMqttConfigure();
        configure.setServerId("server-1111");
        configure.setHost("127.0.0.1");
        configure.setJms_port(61616);
        configure.setMqtt_port(1883);

        JmsMqttBuilder creator = new JmsMqttBuilder(configure);
        creator.start();
    }

}
