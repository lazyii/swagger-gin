package org.rainday.swagger.util;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.core.util.ParameterProcessor;
import io.swagger.v3.core.util.ReflectionUtils;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.parameters.Parameter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import org.rainday.swagger.OpenAPIExtension;
import org.rainday.swagger.OpenAPIExtensions;
import org.rainday.ws.rs.annotations.Consumes;
import org.rainday.ws.rs.annotations.Context;
import org.rainday.ws.rs.annotations.Path;
import org.rainday.ws.rs.methods.HttpMethod;
import org.rainday.swagger.utils.StringUtils;

public class ReaderUtils {
    private static final String PATH_DELIMITER = "/";

    /**
     * Collects constructor-level parameters from class.
     *
     * @param cls        is a class for collecting
     * @param components
     * @return the collection of supported parameters
     */
    public static List<Parameter> collectConstructorParameters(Class<?> cls, Components components, Consumes classConsumes, JsonView jsonViewAnnotation) {
        if (cls.isLocalClass() || (cls.isMemberClass() && !Modifier.isStatic(cls.getModifiers()))) {
            return Collections.emptyList();
        }

        List<Parameter> selected = Collections.emptyList();
        int maxParamsCount = 0;

        for (Constructor<?> constructor : cls.getDeclaredConstructors()) {
            if (!ReflectionUtils.isConstructorCompatible(constructor)
                    && !ReflectionUtils.isInject(Arrays.asList(constructor.getDeclaredAnnotations()))) {
                continue;
            }

            final Type[] genericParameterTypes = constructor.getGenericParameterTypes();
            final Annotation[][] annotations = constructor.getParameterAnnotations();

            int paramsCount = 0;
            final List<Parameter> parameters = new ArrayList<>();
            for (int i = 0; i < genericParameterTypes.length; i++) {
                final List<Annotation> tmpAnnotations = Arrays.asList(annotations[i]);
                if (isContext(tmpAnnotations)) {
                    paramsCount++;
                } else {
                    final Type genericParameterType = genericParameterTypes[i];
                    final List<Parameter> tmpParameters = collectParameters(genericParameterType, tmpAnnotations, components, classConsumes, jsonViewAnnotation);
                    if (! tmpParameters.isEmpty()) {
                        for (Parameter tmpParameter : tmpParameters) {
                            Parameter processedParameter = ParameterProcessor.applyAnnotations(
                                    tmpParameter,
                                    genericParameterType,
                                    tmpAnnotations,
                                    components,
                                    classConsumes == null ? new String[0] : classConsumes.value(),
                                    null,
                                    jsonViewAnnotation);
                            if (processedParameter != null) {
                                parameters.add(processedParameter);
                            }
                        }
                        paramsCount++;
                    }
                }
            }

            if (paramsCount >= maxParamsCount) {
                maxParamsCount = paramsCount;
                selected = parameters;
            }
        }

        return selected;
    }

    /**
     * Collects field-level parameters from class.
     *
     * @param cls        is a class for collecting
     * @param components
     * @return the collection of supported parameters
     */
    public static List<Parameter> collectFieldParameters(Class<?> cls, Components components, Consumes classConsumes, JsonView jsonViewAnnotation) {
        final List<Parameter> parameters = new ArrayList<>();
        for (Field field : ReflectionUtils.getDeclaredFields(cls)) {
            final List<Annotation> annotations = Arrays.asList(field.getAnnotations());
            final Type genericType = field.getGenericType();
            parameters.addAll(collectParameters(genericType, annotations, components, classConsumes, jsonViewAnnotation));
        }
        return parameters;
    }

    private static List<Parameter> collectParameters(Type type, List<Annotation> annotations, Components components, Consumes classConsumes, JsonView jsonViewAnnotation) {
        final Iterator<OpenAPIExtension> chain = OpenAPIExtensions.chain();
        return chain.hasNext() ? chain.next().extractParameters(annotations, type, new HashSet<>(), components, classConsumes, null, false, jsonViewAnnotation, chain).parameters :
                Collections.emptyList();
    }

    private static boolean isContext(List<Annotation> annotations) {
        for (Annotation annotation : annotations) {
            if (annotation instanceof Context) {
                return true;
            }
        }
        return false;
    }

    public static Optional<List<String>> getStringListFromStringArray(String[] array) {
        if (array == null) {
            return Optional.empty();
        }
        List<String> list = new ArrayList<>();
        boolean isEmpty = true;
        for (String value : array) {
            if (StringUtils.isNotBlank(value)) {
                isEmpty = false;
            }
            list.add(value);
        }
        if (isEmpty) {
            return Optional.empty();
        }
        return Optional.of(list);
    }
   
    public static String getPath(Path classLevelPath, Path methodLevelPath, String parentPath, boolean isSubresource) {
        if (classLevelPath == null && methodLevelPath == null && StringUtils.isEmpty(parentPath)) {
            return null;
        }
        StringBuilder b = new StringBuilder();
        appendPathComponent(parentPath, b);
        if (classLevelPath != null && !isSubresource) {
            appendPathComponent(classLevelPath.value(), b);
        }
        if (methodLevelPath != null) {
            appendPathComponent(methodLevelPath.value(), b);
        }
        return b.length() == 0 ? "/" : b.toString();
    }

    /**
     * appends a path component string to a StringBuilder
     * guarantees:
     * <ul>
     *     <li>nulls, empty strings and "/" are nops</li>
     *     <li>output will always start with "/" and never end with "/"</li>
     * </ul>
     * @param component component to be added
     * @param to output
     */
    private static void appendPathComponent(String component, StringBuilder to) {
        if (component == null || component.isEmpty() || "/".equals(component)) {
            return;
        }
        if (!component.startsWith("/") && (to.length() == 0 || '/' != to.charAt(to.length() - 1))) {
            to.append("/");
        }
        if (component.endsWith("/")) {
            to.append(component, 0, component.length() - 1);
        } else {
            to.append(component);
        }
    }

    public static String extractOperationMethod(Method method, Iterator<OpenAPIExtension> chain) {
        //直接被httpMethod注解的function
        if (method.getAnnotation(HttpMethod.class) != null) {
            HttpMethod httpMethod = method.getAnnotation(HttpMethod.class);
            return httpMethod.value().toLowerCase();
        } else if (!StringUtils.isEmpty(getHttpMethodFromCustomAnnotations(method))) {//被httpmethod派生注解，注解过的类。
            return getHttpMethodFromCustomAnnotations(method);
        } else if ((ReflectionUtils.getOverriddenMethod(method)) != null) {
            return extractOperationMethod(ReflectionUtils.getOverriddenMethod(method), chain);
        } else if (chain != null && chain.hasNext()) {
            return chain.next().extractOperationMethod(method, chain);
        } else {
            return null;
        }
    }

    public static String getHttpMethodFromCustomAnnotations(Method method) {
        for (Annotation methodAnnotation : method.getAnnotations()) {
            HttpMethod httpMethod = methodAnnotation.annotationType().getAnnotation(HttpMethod.class);
            if (httpMethod != null) {
                return httpMethod.value().toLowerCase();
            }
        }
        return null;
    }
}
