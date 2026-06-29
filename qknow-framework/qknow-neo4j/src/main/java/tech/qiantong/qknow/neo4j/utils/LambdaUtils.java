/*
 * Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
 *
 * This file is part of qKnow Intelligent Agent Building Platform (Open Source Edition).
 *
 * qKnow is licensed under Apache License 2.0 with additional qKnow terms.
 * You may use qKnow for commercial purposes, but you may not remove, hide,
 * modify, or replace the qKnow logo, copyright notices, license notices,
 * or attribution information without a separate commercial license.
 *
 * White-label use, OEM distribution, rebranding, or presenting qKnow as
 * another product requires separate commercial authorization from
 * Jiangsu Qiantong Technology Co., Ltd.
 *
 * Business License: https://community.qknow.ai/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

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

