package org.rainday.swagger.picker;

import io.swagger.v3.oas.models.OpenAPI;
import java.util.Set;

/**
 * Created by admin on 2021/1/13 15:51:52.
 */
public interface Picker {
    
    void pick(OpenAPI openAPI, Set<Class<?>> classes);
}
