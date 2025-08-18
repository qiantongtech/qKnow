package tech.qiantong.qknow.neo4j.utils;

import org.springframework.data.neo4j.core.schema.CompositeProperty;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 解析器
 * @author wang
 * @date 2025/02/28 09:10
 **/
public class LambdaUtils {
    @FunctionalInterface
    public interface PropertyFunction<T, R> extends Serializable {
        R apply(T t);
    }

    public static <T> String getPropertyName(PropertyFunction<T, ?> func) {
        try {
            Method method = func.getClass().getDeclaredMethod("writeReplace");
            method.setAccessible(true);
            SerializedLambda serializedLambda = (SerializedLambda) method.invoke(func);

            String methodName = serializedLambda.getImplMethodName();
            if (methodName.startsWith("get")) {
                return resolveFieldName(methodName.substring(3));
            } else if (methodName.startsWith("is")) {
                return resolveFieldName(methodName.substring(2));
            }
            throw new IllegalArgumentException("Invalid getter method: " + methodName);
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract property name from lambda", e);
        }
    }

    private static String resolveFieldName(String methodSuffix) {
        return Character.toLowerCase(methodSuffix.charAt(0)) + methodSuffix.substring(1);
    }

    public static String processCompositeProperties(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        String prefixName = "";
        for (Field field : fields) {
            if (field.isAnnotationPresent(CompositeProperty.class)) {
                CompositeProperty annotation = field.getAnnotation(CompositeProperty.class);
                String prefix = annotation.prefix();
                String delimiter = annotation.delimiter();

                if (prefix.isEmpty()) {
                    prefix = field.getName();
                }
                prefixName = prefix + delimiter;
                break;
            }
        }
        return prefixName;
    }

}

