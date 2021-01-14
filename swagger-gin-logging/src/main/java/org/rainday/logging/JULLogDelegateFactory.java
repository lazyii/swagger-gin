package org.rainday.logging;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;
import org.rainday.logging.spi.LogDelegate;
import org.rainday.logging.spi.LogDelegateFactory;

public class JULLogDelegateFactory implements LogDelegateFactory {
    public JULLogDelegateFactory() {
    }

    public static void loadConfig() {
        try {
            InputStream is = JULLogDelegateFactory.class.getClassLoader().getResourceAsStream("swagger-default-jul-logging.properties");
            Throwable var1 = null;

            try {
                if (is != null) {
                    LogManager.getLogManager().readConfiguration(is);
                }
            } catch (Throwable var11) {
                var1 = var11;
                throw var11;
            } finally {
                if (is != null) {
                    if (var1 != null) {
                        try {
                            is.close();
                        } catch (Throwable var10) {
                            var1.addSuppressed(var10);
                        }
                    } else {
                        is.close();
                    }
                }

            }
        } catch (IOException var13) {
        }

    }

    public LogDelegate createDelegate(String name) {
        return new JULLogDelegate(name);
    }

    static {
        if (System.getProperty("java.util.logging.config.file") == null) {
            loadConfig();
        }

    }
}
