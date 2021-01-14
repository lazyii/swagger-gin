import io.swagger.v3.core.converter.ModelConverter;

/**
 * Created by admin on 2021/1/13 19:39:07.
 */
module swagger.core {
    exports io.swagger.v3.core.converter;
    exports io.swagger.v3.core.filter;
    exports io.swagger.v3.core.jackson;
    exports io.swagger.v3.core.model;
    exports io.swagger.v3.core.util;
    
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.dataformat.yaml;
    requires swagger.models;
    requires swagger.gin.logging;
    requires swagger.gin.utils;
    requires swagger.annotations;
    requires jakarta.xml.bind;
    
    uses ModelConverter;
    
}
