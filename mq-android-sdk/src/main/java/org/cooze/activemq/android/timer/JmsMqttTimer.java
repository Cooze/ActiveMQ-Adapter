package org.cooze.activemq.android.timer;

import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author cooze
 * @version 1.0.0
 * @desc
 * @date 2017/9/9
 */
public class JmsMqttTimer {

    private volatile AtomicInteger counter = new AtomicInteger();

    private JmsMqttTimerTask task;
    private volatile int count;
    private volatile int period;

    public JmsMqttTimer() {
    }

    public void start(JmsMqttTimerTask task, int count, int period) {
        this.task = task;
        this.count = count;
        this.period = period;
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

        executorService.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                {
                    task.run();
                    counter.incrementAndGet();
                    if (counter.get() == count) {
                        executorService.shutdownNow();
                    }
                }
            }
        }, 2, period, TimeUnit.SECONDS);
    }
}
