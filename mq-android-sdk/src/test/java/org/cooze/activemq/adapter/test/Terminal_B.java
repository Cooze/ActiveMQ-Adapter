package org.cooze.activemq.adapter.test;

import org.cooze.activemq.android.MqBuilder;

/**
 * @author cooze
 * @version 1.0.0
 * @desc
 * @date 2017/9/9
 */
public class Terminal_B {
    public static void main(String[] args) throws Exception {
        String host = "192.168.100.111";
        String serverId = "server-1111";
        String androidId = "android-1112";
        int port = 1883;

        MqBuilder mqBuilder = new MqBuilder(host, port, serverId, androidId);
        mqBuilder.setMonitor((message) -> System.out.println(message));
        mqBuilder.start();

    }
}
