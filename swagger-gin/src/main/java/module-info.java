import org.rainday.swagger.OpenAPIExtension;

/**
 * Created by admin on 2021/1/13 19:39:30.
 */
module swagger.gin {
    requires com.fasterxml.jackson.databind;
    
    requires swagger.gin.annotations;
    requires swagger.gin.logging;
    requires swagger.gin.utils;
    
    requires swagger.core;
    requires swagger.models;
    requires swagger.annotations;
    
    uses OpenAPIExtension;
    //provides OpenAPIExtension with DefaultParameterExtension;
}
