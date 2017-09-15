package org.cooze.activemq.adapter.test;

import org.cooze.activemq.android.MqBuilder;

/**
 * @author cooze
 * @version 1.0.0
 * @desc
 * @date 2017/9/9
 */
public class Terminal_A {

    public static void main(String[] args) throws Exception {
        //服务端发布消息主题名称
        String serverId = "server-1111";

        //客户端订阅(mqtt)主题名称
        String androidId = "android-1111";

        String host = "192.168.100.111";
        int port = 1883;

        MqBuilder mqBuilder = new MqBuilder(host, port, serverId, androidId);
        mqBuilder.setMonitor((message) -> System.out.println(message));
        mqBuilder.start();
    }
}
