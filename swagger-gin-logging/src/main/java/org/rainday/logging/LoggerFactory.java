
package org.rainday.logging;


import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.rainday.logging.spi.LogDelegate;
import org.rainday.logging.spi.LogDelegateFactory;

public class LoggerFactory {
    
    private static volatile LogDelegateFactory delegateFactory;
    
    private static final ConcurrentMap<String, Logger> loggers = new ConcurrentHashMap<>();
    
    static {
        LoggerFactory.delegateFactory = loadDelagateFactory();
    }
    
    private static synchronized LogDelegateFactory loadDelagateFactory() {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try {
            try {
                Class<?> clz = loader.loadClass("org.apache.logging.log4j.Logger");
                return (LogDelegateFactory) clz.newInstance();
            } catch (ClassNotFoundException e) {
                return new JULLogDelegateFactory();
            }
        } catch (Exception e) {
            throw new RuntimeException("unknown error", e);
        }
    }
    
    public static Logger getLogger(final Class<?> clazz) {
        String name = clazz.isAnonymousClass() ?
                clazz.getEnclosingClass().getCanonicalName() :
                clazz.getCanonicalName();
        return getLogger(name);
    }
    
    public static Logger getLogger(final String name) {
        Logger logger = loggers.get(name);
        
        if (logger == null) {
            LogDelegate delegate = delegateFactory.createDelegate(name);
            
            logger = new Logger(delegate);
            
            Logger oldLogger = loggers.putIfAbsent(name, logger);
            
            if (oldLogger != null) {
                logger = oldLogger;
            }
        }
        
        return logger;
    }
    
    public static void removeLogger(String name) {
        loggers.remove(name);
    }
}
