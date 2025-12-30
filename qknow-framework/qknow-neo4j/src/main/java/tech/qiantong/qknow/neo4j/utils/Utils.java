package tech.qiantong.qknow.neo4j.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 * 工具
 * @author wang
 * @date 2025/11/18 16:02
 **/
public class Utils {

    /**
     * 判断值的类型并格式化
     * @param value
     * @return
     **/
    public static String getValueString(Object value) {

        String valueString;
        if (value instanceof String) {
            valueString = "'" + value + "'";
        } else if (value instanceof Map) {
            // 将Map转换为JSON字符串
            try {
                Map<?, ?> map = (Map<?, ?>) value;
                if (map.isEmpty() || map.size() == 0) {
                    valueString = "''";  // 空Map返回空字符串
                } else {
                    ObjectMapper mapper = new ObjectMapper();
                    String json = mapper.writeValueAsString(value);
                    valueString = "'" + json.replace("'", "\\'") + "'";
                }
            } catch (Exception e) {
                valueString = "''";
            }
        } else if (value instanceof LocalDateTime) {
            // 处理 LocalDateTime 类型
           LocalDateTime dateTime = (LocalDateTime) value;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            valueString = "'" + dateTime.format(formatter) + "'";
        } else if (value instanceof Date) {
            // 将 Date 转换为 LocalDateTime
            Date date = (Date) value;
            Instant instant = date.toInstant();
            ZoneId zoneId = ZoneId.systemDefault(); // 使用系统默认时区
            LocalDateTime dateTime = instant.atZone(zoneId).toLocalDateTime();

            // 格式化 LocalDateTime
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            valueString = "'" + dateTime.format(formatter) + "'";
        } else if (value instanceof Collection) {
            Collection<?> coll = (Collection<?>) value;
            if (coll.isEmpty()) {
                return null;
            }
            StringBuilder sb = new StringBuilder("[");
            boolean first = true;
            for (Object item : coll) {
                if (!first) sb.append(", ");
                String itemStr = getValueString(item);
                sb.append(itemStr);
                first = false;
            }
            sb.append("]");
            return sb.toString();
        }  else if(value == null) {
            valueString = "null";
        } else {
            valueString = value.toString();
        }
        return valueString;
    }

    // 判断是否是字符串上下文的操作符
    private static boolean isStringLikeOperator(String op) {
        return "CONTAINS".equalsIgnoreCase(op)
                || "STARTS WITH".equalsIgnoreCase(op)
                || "ENDS WITH".equalsIgnoreCase(op);
    }
}
