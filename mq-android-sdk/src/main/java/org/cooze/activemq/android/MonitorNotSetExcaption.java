package org.cooze.activemq.android;

/**
 * @author cooze
 * @version 1.0.0
 * @desc
 * @date 2017/9/9
 */
public class MonitorNotSetExcaption extends Exception {
    public MonitorNotSetExcaption() {
        super();
    }

    public MonitorNotSetExcaption(String message) {
        super(message);
    }

    public MonitorNotSetExcaption(String message, Throwable cause) {
        super(message, cause);
    }

    public MonitorNotSetExcaption(Throwable cause) {
        super(cause);
    }

    protected MonitorNotSetExcaption(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
