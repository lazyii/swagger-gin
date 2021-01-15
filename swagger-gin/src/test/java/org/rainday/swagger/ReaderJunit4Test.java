package org.rainday.swagger;

import com.fasterxml.jackson.annotation.JsonIgnoreType;
import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Paths;
import org.junit.Test;
import org.rainday.swagger.controller.ResourceInPackageA;

/**
 * Created by admin on 2021/1/8 15:53:43.
 */
@JsonIgnoreType
public class ReaderJunit4Test {
    
    @Test
    public void readerSimpleTest() {
    
        Paths p1 = new Paths();
        System.out.println(p1 == new Paths());
        Reader reader = new Reader();
        OpenAPI api = reader.read(ResourceInPackageA.class);
    
        System.out.println(Yaml.pretty(api));
    
    }
    
}
