package tech.qiantong.qknow.module.kb.utils;

import com.alibaba.fastjson2.JSONObject;

/**
 * 常规处理
 * @author wang
 * @date 2026/03/19 18:00
 **/
public class NodeUtils {

    /**
     * 替换字符串中的占位符（{{key}} 格式）
     *
     * @param value 包含占位符的原始字符串，例如："姓名：{{name}}，年龄：{{age}}"
     * @param param JSON格式的参数字符串，例如："{\"name\":\"张三\",\"age\":20}"
     * @return 替换后的字符串，若入参非法则返回原始value
     * @throws IllegalArgumentException 当param不是合法JSON字符串时抛出（非运行时可根据场景调整）
     */
    public static String replacePlaceholder(String value, String param) {
        // 空值校验
        if (value == null || value.isEmpty()) {
            return value;
        }
        if (param == null || param.isEmpty()) {
            return value;
        }

        // 解析JSON参数
        JSONObject paramJson;
        try {
            paramJson = JSONObject.parseObject(param);
        } catch (Exception e) {
            String errorMsg = String.format("解析参数字符串失败，param=%s，异常信息：%s", param, e.getMessage());
            throw new IllegalArgumentException(errorMsg, e);
        }

        // 替换占位符
        StringBuilder valueBuilder = new StringBuilder(value);
        paramJson.forEach((key, val) -> {
            if (key != null) {
                String placeholder = "{{" + key + "}}";
                String replacement = val.toString();
                // 替换所有匹配的占位符
                replaceAll(valueBuilder, placeholder, replacement);
            }
        });

        return valueBuilder.toString();
    }

    /**
     * 辅助方法：替换StringBuilder中的所有指定字符串
     *
     * @param builder 待替换的StringBuilder
     * @param target  要替换的目标字符串
     * @param replacement 替换后的字符串
     */
    private static void replaceAll(StringBuilder builder, String target, String replacement) {
        int index = builder.indexOf(target);
        while (index != -1) {
            builder.replace(index, index + target.length(), replacement);
            index = builder.indexOf(target, index + replacement.length());
        }
    }
}
