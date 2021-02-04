package org.rainday.swagger;

import com.fasterxml.jackson.annotation.JsonIgnoreType;
import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Info;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.Test;
import org.rainday.swagger.controller.ResourceInPackageA;
import org.rainday.swagger.picker.Reader;

/**
 * Created by admin on 2021/1/8 15:53:43.
 */
@JsonIgnoreType
public class ReaderJunit4Test {
    
    @Test
    public void readerSimpleTest() {
    
    
    
        OpenAPIConfiguration config = new SwaggerConfiguration()
                .resourcePackages(Stream.of("com.my.project.resources", "org.my.project.resources").collect(Collectors.toSet()))
                .openAPI(new OpenAPI().info(new Info().description("TEST INFO DESC")));
        
        Paths p1 = new Paths();
        System.out.println(p1 == new Paths());
        Reader reader = new Reader();
        OpenAPI api = reader.read(ResourceInPackageA.class);
    
        System.out.println(Yaml.pretty(api));
    
    }
    
}
