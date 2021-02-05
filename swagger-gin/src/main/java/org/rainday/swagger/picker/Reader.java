package org.rainday.swagger.picker;

import io.swagger.v3.core.util.AnnotationsUtils;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.tags.Tag;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.rainday.logging.Logger;
import org.rainday.logging.LoggerFactory;

public class Reader {
    private static final Logger logger = LoggerFactory.getLogger(Reader.class);

    public static final String DEFAULT_MEDIA_TYPE_VALUE = "*/*";
    public static final String DEFAULT_DESCRIPTION = "default response";
    
    //protected OpenAPIConfiguration config;

    public static OpenAPI openAPI;
    public static Components components;
    public static Paths paths;
    public static Set<Tag> openApiTags;

    public Reader() {
        this.openAPI = new OpenAPI();
        this.components = new Components();
        this.paths = new Paths();
        this.openApiTags = new LinkedHashSet<>();
    }

    public OpenAPI getOpenAPI() {
        return openAPI;
    }

    /**
     * Scans a single class for Swagger annotations - does not invoke ReaderListeners
     */
    public OpenAPI read(Class<?> cls) {
        return read(cls, resolveApplicationPath(), null, false, null, null, new LinkedHashSet<String>(), new ArrayList<Parameter>(), new HashSet<Class<?>>());
    }

    /**
     * Scans a set of classes for both ReaderListeners and OpenAPI annotations. All found listeners will
     * be instantiated before any of the classes are scanned for OpenAPI annotations - so they can be invoked
     * accordingly.
     *
     * @param classes a set of classes to scan
     * @return the generated OpenAPI definition
     */
    public OpenAPI read(Set<Class<?>> classes) {
        Set<Class<?>> sortedClasses = new TreeSet<>((class1, class2) -> {
            if (class1.equals(class2)) {
                return 0;
            } else if (class1.isAssignableFrom(class2)) {
                return -1;
            } else if (class2.isAssignableFrom(class1)) {
                return 1;
            }
            return class1.getName().compareTo(class2.getName());
        });
        sortedClasses.addAll(classes);
        sortedClasses.forEach(this::read);
        return openAPI;
    }

    public OpenAPI read(Set<Class<?>> classes, Map<String, Object> resources) {
        return read(classes);
    }
    
    /**
     * read project context (base context, like "spring servlet context path")
     *
     * servers:
     * - url: http://localhost{port}{basePath}
     *   variables:
     *      port:
     *          # 如果不存在可以不填
     *          default: ":80"
     *      basePath
     *          default: "/demo"
     * @return
     */
    protected String resolveApplicationPath() {
        //todo read port
        
        //todo read basePath
        
        return "";
    }

    public static OpenAPI read(Class<?> cls,
                               String parentPath,
                               String parentMethod,
                               boolean isSubresource,
                               RequestBody parentRequestBody,
                               ApiResponses parentResponses,
                               Set<String> parentTags,
                               List<Parameter> parentParameters,
                               Set<Class<?>> scannedResources) {
    
        OpenAPIDefinitionAttr openApiAttr = new OpenAPIDefinitionAttr(cls, parentPath, parentMethod, isSubresource, parentRequestBody,
                parentResponses, parentTags, parentParameters,
                scannedResources);
        
        Hidden hidden = openApiAttr.getHidden();
        io.swagger.v3.oas.annotations.tags.Tag[] apiTags = openApiAttr.getApiTags();
        
        if (hidden != null) {
            return openAPI;
        }
        
        // iterate class methods
        Method[] methods = cls.getMethods();
        for (Method method : methods) {
            MethodPicker.pick(method, openApiAttr);
        }

        // if no components object is defined in openApi instance passed by client, set openAPI.components to resolved components (if not empty)
        if (!isEmptyComponents(components) && openAPI.getComponents() == null) {
            openAPI.setComponents(components);
        }

        // add tags from class to definition tags
        AnnotationsUtils.getTags(apiTags, true).ifPresent(tags -> openApiTags.addAll(tags));

        if (!openApiTags.isEmpty()) {
            Set<Tag> tagsSet = new LinkedHashSet<>();
            if (openAPI.getTags() != null) {
                for (Tag tag : openAPI.getTags()) {
                    if (tagsSet.stream().noneMatch(t -> t.getName().equals(tag.getName()))) {
                        tagsSet.add(tag);
                    }
                }
            }
            for (Tag tag : openApiTags) {
                if (tagsSet.stream().noneMatch(t -> t.getName().equals(tag.getName()))) {
                    tagsSet.add(tag);
                }
            }
            openAPI.setTags(new ArrayList<>(tagsSet));
        }
        return openAPI;
    }

    private static boolean isEmptyComponents(Components components) {
        if (components == null) {
            return true;
        }
        if (components.getSchemas() != null && components.getSchemas().size() > 0) {
            return false;
        }
        if (components.getSecuritySchemes() != null && components.getSecuritySchemes().size() > 0) {
            return false;
        }
        if (components.getCallbacks() != null && components.getCallbacks().size() > 0) {
            return false;
        }
        if (components.getExamples() != null && components.getExamples().size() > 0) {
            return false;
        }
        if (components.getExtensions() != null && components.getExtensions().size() > 0) {
            return false;
        }
        if (components.getHeaders() != null && components.getHeaders().size() > 0) {
            return false;
        }
        if (components.getLinks() != null && components.getLinks().size() > 0) {
            return false;
        }
        if (components.getParameters() != null && components.getParameters().size() > 0) {
            return false;
        }
        if (components.getRequestBodies() != null && components.getRequestBodies().size() > 0) {
            return false;
        }
        if (components.getResponses() != null && components.getResponses().size() > 0) {
            return false;
        }

        return true;
    }

}
