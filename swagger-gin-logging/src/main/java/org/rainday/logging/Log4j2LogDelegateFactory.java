package org.rainday.logging;

import org.rainday.logging.spi.LogDelegate;
import org.rainday.logging.spi.LogDelegateFactory;


public class Log4j2LogDelegateFactory implements LogDelegateFactory {
    
    public LogDelegate createDelegate(final String name) {
        return new Log4j2LogDelegate(name);
    }
    
}
