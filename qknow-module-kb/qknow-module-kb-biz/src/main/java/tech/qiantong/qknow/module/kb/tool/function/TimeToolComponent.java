package tech.qiantong.qknow.module.kb.tool.function;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 时间工具组件（内聚所有时间相关工具，每个方法独立注册为工具）
 */
@Component // 主类作为Spring组件，内聚公共逻辑
public class TimeToolComponent {

    private final ZoneId DEFAULT_ZONE = ZoneId.systemDefault(); // 公共时区配置
    private final DateTimeFormatter DEFAULT_FORMATTER = DatePattern.NORM_DATETIME_FORMAT.getDateTimeFormatter(); // 公共格式化器

    public static class GetCurrentTimeTool implements Supplier<String> {
        private final ZoneId zoneId; // 复用主类的公共配置

        // 构造器注入公共配置
        public GetCurrentTimeTool(ZoneId zoneId) {
            this.zoneId = zoneId;
        }

        @Override
        public String get() {
            return LocalDateTime.now(zoneId).toString();
        }
    }

    // 结构化入参DTO
    @Data
    @JsonClassDescription("时间格式化请求参数")
    public static class FormatTimeRequest {
        @JsonProperty(required = true)
        @JsonPropertyDescription("待格式化的时间字符串，格式：yyyy-MM-dd HH:mm:ss")
        private String timeStr;

        @JsonPropertyDescription("目标格式，默认：yyyy-MM-dd HH:mm:ss，可选：yyyy-MM-dd、HH:mm")
        private String formatPattern;
    }

    // 格式化工具逻辑
    public static class FormatTimeTool implements Function<FormatTimeRequest, String> {
        private final DateTimeFormatter defaultFormatter; // 复用主类的公共配置

        public FormatTimeTool(DateTimeFormatter defaultFormatter) {
            this.defaultFormatter = defaultFormatter;
        }

        @Override
        public String apply(FormatTimeRequest request) {
            // 公共逻辑复用：处理默认格式
            String pattern = request.getFormatPattern() == null ? defaultFormatter.format(LocalDateTime.now()) : request.getFormatPattern();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            // 核心逻辑
            LocalDateTime time = LocalDateTimeUtil.parse(request.getTimeStr(), defaultFormatter);
            return time.format(formatter);
        }
    }

    // 获取当前时间工具
    @Bean("get_current_time")
    public Supplier<String> getCurrentTimeTool() {
        return new GetCurrentTimeTool(DEFAULT_ZONE); // 注入公共配置
    }

    // 格式化时间工具
    @Bean("format_time")
    public Function<FormatTimeRequest, String> formatTimeTool() {
        return new FormatTimeTool(DEFAULT_FORMATTER); // 注入公共配置
    }
}
