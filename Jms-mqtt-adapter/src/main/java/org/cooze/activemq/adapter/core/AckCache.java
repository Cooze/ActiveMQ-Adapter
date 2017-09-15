package org.cooze.activemq.adapter.core;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author cooze
 * @version 1.0.0
 * @desc 心跳信息缓存
 * @date 2017/9/8
 */
public class AckCache {

    private static final ReentrantLock lock = new ReentrantLock();

    //心跳标记缓存，一个客户端对应一个缓存标记
    private volatile ConcurrentHashMap<String, Boolean> alive = new ConcurrentHashMap<>();

    //客户端上下线标识,一个客户端对应一个上下线标记
    private volatile ConcurrentSkipListSet<String> mqttTopicSet = new ConcurrentSkipListSet<>();

    private AckCache() {
    }

    private volatile static AckCache instance;

    public static synchronized AckCache getInstance() {
        lock.lock();
        try {
            if (instance == null) {
                instance = new AckCache();
                return instance;
            }
        } finally {
            lock.unlock();
        }
        return instance;
    }

    public void setAlive(String key, Boolean value) {

        lock.lock();
        try {
            alive.put(key, value);
        } finally {
            lock.unlock();
        }

    }

    public Boolean getAlive(String key) {
        return alive.get(key);
    }

    public void remove(String key) {
        lock.lock();
        try {
            alive.remove(key);
        } finally {
            lock.unlock();
        }
    }

    public void setMqttTopic(String yp_sn) {

        lock.lock();
        try {
            mqttTopicSet.add(yp_sn);
        } finally {
            lock.unlock();
        }

    }

    public void removeMqttTopic(String clientId) {

        lock.lock();
        try {
            mqttTopicSet.remove(clientId);
        } finally {
            lock.unlock();
        }

    }

    public Boolean topicIsStart(String yp_sn) {
        return mqttTopicSet.contains(yp_sn);
    }


}
