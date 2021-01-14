package org.rainday.swagger.picker;

import io.swagger.v3.core.util.AnnotationsUtils;
import io.swagger.v3.core.util.ReflectionUtils;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.tags.Tag;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.rainday.swagger.exception.DuplicatedException;

/**
 * Created by admin on 2021/1/13 15:56:24.
 */
public class OpenAPIDefinitionPicker implements Picker{
    
    public static Map<String, Class> classMap = new HashMap<>();
    
    @Override
    public void pick(OpenAPI openAPI, Set<Class<?>> classes) {
        for (Class<?> cls : classes) {
            if (classMap.containsKey(cls.getSimpleName())) {
                Class existCls = classMap.remove(cls.getSimpleName());
                if (existCls.getCanonicalName().equals(cls.getCanonicalName())) {
                    throw new DuplicatedException(cls.getCanonicalName() + " duplicated ");
                } else {
                    classMap.put(existCls.getCanonicalName(), existCls);
                    classMap.put(cls.getCanonicalName(), cls);
                }
            }
        }
    }
    
    void pickTags(OpenAPI openAPI, Map<String, Class> classMap) {
        classMap.forEach((k, v) -> {
            Tag tag = new Tag();
            tag.setName(k);
            //todo 实现tag-description的提取
            tag.setDescription(k);
            //ReflectionUtils.getRepeatableAnnotations()
            openAPI.addTagsItem(tag);
        });
    }
    
    void pickServers(OpenAPI openAPI, Map<String, Class> classMap) {
        //todo 待实现
    }
    
    void pickSingle(OpenAPI openAPI, Class<?> cls) {
        // OpenApiDefinition
        OpenAPIDefinition openAPIDefinition = ReflectionUtils.getAnnotation(cls, OpenAPIDefinition.class);
    
        if (openAPIDefinition != null) {
        
            // info
            AnnotationsUtils.getInfo(openAPIDefinition.info()).ifPresent(info -> openAPI.setInfo(info));

//            todo  // OpenApiDefinition security requirements
//            SecurityParser
//                    .getSecurityRequirements(openAPIDefinition.security())
//                    .ifPresent(s -> openAPI.setSecurity(s));
            //
            // OpenApiDefinition external docs
            AnnotationsUtils
                    .getExternalDocumentation(openAPIDefinition.externalDocs())
                    .ifPresent(docs -> openAPI.setExternalDocs(docs));
        
            // OpenApiDefinition tags
        
            // OpenApiDefinition servers
            AnnotationsUtils.getServers(openAPIDefinition.servers()).ifPresent(servers -> openAPI.setServers(servers));
        
            // OpenApiDefinition extensions
            if (openAPIDefinition.extensions().length > 0) {
                openAPI.setExtensions(AnnotationsUtils
                        .getExtensions(openAPIDefinition.extensions()));
            }
        
        }
    }
}
