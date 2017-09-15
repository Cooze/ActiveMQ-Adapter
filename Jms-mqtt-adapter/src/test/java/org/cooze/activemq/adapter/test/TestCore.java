package org.cooze.activemq.adapter.test;

import org.cooze.activemq.adapter.core.JmsToMQTT;

/**
 * @author cooze
 * @version 1.0.0
 * @desc
 * @date 2017/9/8
 */
public class TestCore {

    public static void main(String[] args) throws Exception {

        String clientId = "11111";
        JmsToMQTT subcribeFactory = new JmsToMQTT(clientId, "192.168.100.111", 1883, 61616);

        Thread t = new Thread(() -> {
            try {
                subcribeFactory.start("TestTopic", "cache", "ACK");
            } catch (Exception e) {
            }
        });
        t.setDaemon(true);
        t.start();
        subcribeFactory.setThread(t);

    }
}
