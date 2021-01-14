package org.rainday.swagger;

import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.oas.models.OpenAPI;
import org.junit.Test;
import org.rainday.swagger.controller.ResourceInPackageA;

/**
 * Created by admin on 2021/1/8 15:53:43.
 */
public class ReaderJunit4Test {
    
    @Test
    public void readerSimpleTest() {
        Reader reader = new Reader();
        OpenAPI api = reader.read(ResourceInPackageA.class);
    
        System.out.println(Yaml.pretty(api));
    }
    
}
