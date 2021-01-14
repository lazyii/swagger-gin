
package org.rainday.logging;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import org.rainday.logging.spi.LogDelegate;

public class JULLogDelegate implements LogDelegate {
    private final java.util.logging.Logger logger;

    JULLogDelegate(String name) {
        this.logger = java.util.logging.Logger.getLogger(name);
    }

    public boolean isWarnEnabled() {
        return this.logger.isLoggable(Level.WARNING);
    }

    public boolean isInfoEnabled() {
        return this.logger.isLoggable(Level.INFO);
    }

    public boolean isDebugEnabled() {
        return this.logger.isLoggable(Level.FINE);
    }

    public boolean isTraceEnabled() {
        return this.logger.isLoggable(Level.FINEST);
    }

    public void fatal(Object message) {
        this.log(Level.SEVERE, message);
    }

    public void fatal(Object message, Throwable t) {
        this.log(Level.SEVERE, message, t);
    }

    public void error(Object message) {
        this.log(Level.SEVERE, message);
    }

    public void error(Object message, Object... params) {
        this.log(Level.SEVERE, message, (Throwable)null, params);
    }

    public void error(Object message, Throwable t) {
        this.log(Level.SEVERE, message, t);
    }

    public void error(Object message, Throwable t, Object... params) {
        this.log(Level.SEVERE, message, t, params);
    }

    public void warn(Object message) {
        this.log(Level.WARNING, message);
    }

    public void warn(Object message, Object... params) {
        this.log(Level.WARNING, message, (Throwable)null, params);
    }

    public void warn(Object message, Throwable t) {
        this.log(Level.WARNING, message, t);
    }

    public void warn(Object message, Throwable t, Object... params) {
        this.log(Level.WARNING, message, t, params);
    }

    public void info(Object message) {
        this.log(Level.INFO, message);
    }

    public void info(Object message, Object... params) {
        this.log(Level.INFO, message, (Throwable)null, params);
    }

    public void info(Object message, Throwable t) {
        this.log(Level.INFO, message, t);
    }

    public void info(Object message, Throwable t, Object... params) {
        this.log(Level.INFO, message, t, params);
    }

    public void debug(Object message) {
        this.log(Level.FINE, message);
    }

    public void debug(Object message, Object... params) {
        this.log(Level.FINE, message, (Throwable)null, params);
    }

    public void debug(Object message, Throwable t) {
        this.log(Level.FINE, message, t);
    }

    public void debug(Object message, Throwable t, Object... params) {
        this.log(Level.FINE, message, t, params);
    }

    public void trace(Object message) {
        this.log(Level.FINEST, message);
    }

    public void trace(Object message, Object... params) {
        this.log(Level.FINEST, message, (Throwable)null, params);
    }

    public void trace(Object message, Throwable t) {
        this.log(Level.FINEST, message, t);
    }

    public void trace(Object message, Throwable t, Object... params) {
        this.log(Level.FINEST, message, t, params);
    }

    private void log(Level level, Object message) {
        this.log(level, message, (Throwable)null);
    }

    private void log(Level level, Object message, Throwable t, Object... params) {
        if (this.logger.isLoggable(level)) {
            String msg = message == null ? "NULL" : message.toString();
            LogRecord record = new LogRecord(level, msg);
            record.setLoggerName(this.logger.getName());
            if (t != null) {
                record.setThrown(t);
            } else if (params != null && params.length != 0 && params[params.length - 1] instanceof Throwable) {
                record.setThrown((Throwable)params[params.length - 1]);
            }

            record.setSourceClassName((String)null);
            record.setParameters(params);
            this.logger.log(record);
        }
    }

    private void log(Level level, Object message, Throwable t) {
        this.log(level, message, t, (Object[])null);
    }

    public Object unwrap() {
        return this.logger;
    }
}
