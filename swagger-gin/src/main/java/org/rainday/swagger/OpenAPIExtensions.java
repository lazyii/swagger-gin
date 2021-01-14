package org.rainday.swagger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;
import org.rainday.logging.Logger;
import org.rainday.logging.LoggerFactory;

public class OpenAPIExtensions {
    
    private static final Logger logger = LoggerFactory.getLogger(OpenAPIExtensions.class.getName());

    private static List<OpenAPIExtension> extensions = null;

    public static List<OpenAPIExtension> getExtensions() {
        return extensions;
    }

    public static void setExtensions(List<OpenAPIExtension> ext) {
        extensions = ext;
    }

    public static Iterator<OpenAPIExtension> chain() {
        return extensions.iterator();
    }

    static {
        extensions = new ArrayList<>();
        ServiceLoader<OpenAPIExtension> loader = ServiceLoader.load(OpenAPIExtension.class);
        for (OpenAPIExtension ext : loader) {
            logger.info("adding extension {}", ext);
            extensions.add(ext);
        }
        extensions.add(new DefaultParameterExtension());
    }
}
