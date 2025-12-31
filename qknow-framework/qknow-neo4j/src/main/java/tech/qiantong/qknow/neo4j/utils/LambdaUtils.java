/*
 * Copyright © 2026 Qiantong Technology Co., Ltd.
 * qKnow Knowledge Platform
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please apply for brand customization authorization via official channels.
 *  *
 * More information: https://qknow.qiantong.tech/business.html
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2026 江苏千桐科技有限公司
 * qKnow 知识平台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qknow.qiantong.tech/business.html
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

