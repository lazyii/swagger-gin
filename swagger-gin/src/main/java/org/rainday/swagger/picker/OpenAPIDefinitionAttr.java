package org.rainday.swagger.picker;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import io.swagger.v3.core.util.AnnotationsUtils;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.ReflectionUtils;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponses;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.rainday.swagger.util.ReaderUtils;
import org.rainday.ws.rs.annotations.Consumes;
import org.rainday.ws.rs.annotations.Path;
import org.rainday.ws.rs.annotations.Produces;

/**
 * Created by admin on 2021/2/2 13:20:56.
 */
public class OpenAPIDefinitionAttr {
    Class<?> cls;
    String parentPath;
    String parentMethod;
    boolean isSubresource;
    RequestBody parentRequestBody;
    ApiResponses parentResponses;
    Set<String> parentTags;
    List<Parameter> parentParameters;
    Set<Class<?>> scannedResources;
    
    Set<String> classTags = new LinkedHashSet<>();
    List<io.swagger.v3.oas.models.servers.Server> classServers = new ArrayList<>();
    List<Parameter> globalParameters = new ArrayList<>();
    Optional<io.swagger.v3.oas.models.ExternalDocumentation> classExternalDocumentation;
    JavaType classType;
    BeanDescription bd;

    Hidden hidden;
    Path apiPath;
    ApiResponse[] classResponses;
    ExternalDocumentation apiExternalDocs;
    Tag[] apiTags;
    Server[] apiServers;
    Consumes classConsumes;
    Produces classProduces;
    boolean classDeprecated;
    
    public OpenAPIDefinitionAttr() {}
    
    public OpenAPIDefinitionAttr(Class<?> cls) {
        this.cls = cls;
    
        this.hidden = cls.getAnnotation(Hidden.class);
        // class path
        this.apiPath = ReflectionUtils.getAnnotation(cls, Path.class);
    
        this.classResponses = ReflectionUtils.getRepeatableAnnotationsArray(cls, io.swagger.v3.oas.annotations.responses.ApiResponse.class);
    
        //List<io.swagger.v3.oas.annotations.security.SecurityScheme> apiSecurityScheme = ReflectionUtils.getRepeatableAnnotations(cls, io.swagger.v3.oas.annotations.security.SecurityScheme.class);
        //List<io.swagger.v3.oas.annotations.security.SecurityRequirement> apiSecurityRequirements = ReflectionUtils.getRepeatableAnnotations(cls, io.swagger.v3.oas.annotations.security.SecurityRequirement.class);
    
        this.apiExternalDocs = ReflectionUtils.getAnnotation(cls, ExternalDocumentation.class);
        this.apiTags = ReflectionUtils.getRepeatableAnnotationsArray(cls, io.swagger.v3.oas.annotations.tags.Tag.class);
        this.apiServers = ReflectionUtils.getRepeatableAnnotationsArray(cls, Server.class);
    
        this.classConsumes = ReflectionUtils.getAnnotation(cls, Consumes.class);
        this.classProduces = ReflectionUtils.getAnnotation(cls, Produces.class);
        this.classDeprecated = ReflectionUtils.getAnnotation(cls, Deprecated.class) != null;
        
        //class tags, consider only name to add to class operations
        if (apiTags != null) {
            AnnotationsUtils.getTags(apiTags, false)
                    .ifPresent(tags -> tags.stream().map(io.swagger.v3.oas.models.tags.Tag::getName).forEach(classTags::add));
        }
        // parent tags
        if (isSubresource) {
            if (parentTags != null) {
                classTags.addAll(parentTags);
            }
        }
    
        // servers
        final List<io.swagger.v3.oas.models.servers.Server> classServers = new ArrayList<>();
        if (apiServers != null) {
            AnnotationsUtils.getServers(apiServers).ifPresent(classServers::addAll);
        }
    
        // class external docs
        classExternalDocumentation = AnnotationsUtils.getExternalDocumentation(apiExternalDocs);
    
        // look for constructor-level annotated properties
        globalParameters.addAll(ReaderUtils.collectConstructorParameters(cls, Reader.components, classConsumes, null));
    
        // look for field-level annotated properties
        globalParameters.addAll(ReaderUtils.collectFieldParameters(cls, Reader.components, classConsumes, null));
    
        classType = TypeFactory.defaultInstance().constructType(cls);
        bd = Json.mapper().getSerializationConfig().introspect(classType);
    
    }
    
    public OpenAPIDefinitionAttr(Class<?> cls,
                                 String parentPath,
                                 String parentMethod,
                                 boolean isSubresource,
                                 RequestBody parentRequestBody,
                                 ApiResponses parentResponses,
                                 Set<String> parentTags,
                                 List<Parameter> parentParameters,
                                 Set<Class<?>> scannedResources) {
        this(cls);
        this.parentPath = parentPath;
        this.parentMethod = parentMethod;
        this.isSubresource = isSubresource;
        this.parentRequestBody = parentRequestBody;
        this.parentResponses = parentResponses;
        this.parentTags = parentTags;
        this.parentParameters = parentParameters;
        this.scannedResources = scannedResources;
    }
  
    
    public Hidden getHidden() {
        return hidden;
    }
    
    public void setHidden(Hidden hidden) {
        this.hidden = hidden;
    }
    
    public Path getApiPath() {
        return apiPath;
    }
    
    public void setApiPath(Path apiPath) {
        this.apiPath = apiPath;
    }
    
    public ApiResponse[] getClassResponses() {
        return classResponses;
    }
    
    public void setClassResponses(ApiResponse[] classResponses) {
        this.classResponses = classResponses;
    }
    
    public ExternalDocumentation getApiExternalDocs() {
        return apiExternalDocs;
    }
    
    public void setApiExternalDocs(ExternalDocumentation apiExternalDocs) {
        this.apiExternalDocs = apiExternalDocs;
    }
    
    public Tag[] getApiTags() {
        return apiTags;
    }
    
    public void setApiTags(Tag[] apiTags) {
        this.apiTags = apiTags;
    }
    
    public Server[] getApiServers() {
        return apiServers;
    }
    
    public void setApiServers(Server[] apiServers) {
        this.apiServers = apiServers;
    }
    
    public Consumes getClassConsumes() {
        return classConsumes;
    }
    
    public void setClassConsumes(Consumes classConsumes) {
        this.classConsumes = classConsumes;
    }
    
    public Produces getClassProduces() {
        return classProduces;
    }
    
    public void setClassProduces(Produces classProduces) {
        this.classProduces = classProduces;
    }
    
    public boolean isClassDeprecated() {
        return classDeprecated;
    }
    
    public void setClassDeprecated(boolean classDeprecated) {
        this.classDeprecated = classDeprecated;
    }
    
    public Class<?> getCls() {
        return cls;
    }
    
    public void setCls(Class<?> cls) {
        this.cls = cls;
    }
    
    public String getParentPath() {
        return parentPath;
    }
    
    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }
    
    public String getParentMethod() {
        return parentMethod;
    }
    
    public void setParentMethod(String parentMethod) {
        this.parentMethod = parentMethod;
    }
    
    public boolean isSubresource() {
        return isSubresource;
    }
    
    public void setSubresource(boolean subresource) {
        isSubresource = subresource;
    }
    
    public RequestBody getParentRequestBody() {
        return parentRequestBody;
    }
    
    public void setParentRequestBody(RequestBody parentRequestBody) {
        this.parentRequestBody = parentRequestBody;
    }
    
    public ApiResponses getParentResponses() {
        return parentResponses;
    }
    
    public void setParentResponses(ApiResponses parentResponses) {
        this.parentResponses = parentResponses;
    }
    
    public Set<String> getParentTags() {
        return parentTags;
    }
    
    public void setParentTags(Set<String> parentTags) {
        this.parentTags = parentTags;
    }
    
    public List<Parameter> getParentParameters() {
        return parentParameters;
    }
    
    public void setParentParameters(List<Parameter> parentParameters) {
        this.parentParameters = parentParameters;
    }
    
    public Set<Class<?>> getScannedResources() {
        return scannedResources;
    }
    
    public void setScannedResources(Set<Class<?>> scannedResources) {
        this.scannedResources = scannedResources;
    }
    
    public Set<String> getClassTags() {
        return classTags;
    }
    
    public void setClassTags(Set<String> classTags) {
        this.classTags = classTags;
    }
    
    public List<io.swagger.v3.oas.models.servers.Server> getClassServers() {
        return classServers;
    }
    
    public void setClassServers(List<io.swagger.v3.oas.models.servers.Server> classServers) {
        this.classServers = classServers;
    }
    
    public List<Parameter> getGlobalParameters() {
        return globalParameters;
    }
    
    public void setGlobalParameters(List<Parameter> globalParameters) {
        this.globalParameters = globalParameters;
    }
    
    public Optional<io.swagger.v3.oas.models.ExternalDocumentation> getClassExternalDocumentation() {
        return classExternalDocumentation;
    }
    
    public void setClassExternalDocumentation(Optional<io.swagger.v3.oas.models.ExternalDocumentation> classExternalDocumentation) {
        this.classExternalDocumentation = classExternalDocumentation;
    }
    
    public JavaType getClassType() {
        return classType;
    }
    
    public void setClassType(JavaType classType) {
        this.classType = classType;
    }
    
    public BeanDescription getBd() {
        return bd;
    }
    
    public void setBd(BeanDescription bd) {
        this.bd = bd;
    }
    
}
