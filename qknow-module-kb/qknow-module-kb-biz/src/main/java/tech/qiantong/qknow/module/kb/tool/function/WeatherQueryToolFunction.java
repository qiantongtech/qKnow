package tech.qiantong.qknow.module.kb.tool.function;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.function.Function;

import static cn.hutool.core.date.DatePattern.NORM_DATETIME_PATTERN;

/**
 * 工具：查询指定城市的天气信息
 *
 * @author 芋道源码
 */
@Slf4j
@Component("weather_query")
public class WeatherQueryToolFunction
        implements Function<WeatherQueryToolFunction.Request, WeatherQueryToolFunction.Response> {

    private static final String WEATHER_API_URL = "http://wttr.in/{}?format=j1";

    @Data
    @JsonClassDescription("查询指定城市的天气信息")
    public static class Request {

        /**
         * 城市名称
         */
        @JsonProperty(required = true, value = "city")
        @JsonPropertyDescription("城市名称，例如：北京、上海、广州")
        private String city;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {

        /**
         * 城市名称
         */
        private String city;

        /**
         * 天气信息
         */
        private WeatherInfo weatherInfo;

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class WeatherInfo {

            /**
             * 温度（摄氏度）
             */
            private Integer temperature;

            /**
             * 天气状况
             */
            private String condition;

            /**
             * 湿度百分比
             */
            private Integer humidity;

            /**
             * 风速（km/h）
             */
            private Integer windSpeed;

            /**
             * 查询时间
             */
            private String queryTime;

        }

    }

    @Override
    public Response apply(Request request) {
        // 检查城市名称是否为空
        if (StrUtil.isBlank(request.getCity())) {
            return new Response("未知城市", null);
        }

        // 获取天气数据
        String city = request.getCity();
        Response.WeatherInfo weatherInfo = fetchWeatherFromAPI(city);
        
        // 如果 API 调用失败，返回空值
        if (weatherInfo == null) {
            log.error("天气 API 调用失败，无法获取城市：{} 的天气信息", city);
            return new Response(city, null);
        }
        
        return new Response(city, weatherInfo);
    }

    /**
     * 从 wttr.in API 获取真实天气数据
     * API 文档：https://wttr.in/:help
     */
    private Response.WeatherInfo fetchWeatherFromAPI(String city) {
        try {
            // 构建 API URL
            String url = StrUtil.format(WEATHER_API_URL, city);

            // 发送 HTTP GET 请求
            String response = HttpUtil.get(url);

            // 解析 JSON 响应
            JSONObject json = JSONUtil.parseObj(response);

            // 获取当前天气数据（nearest_area 数组的第一个元素）
            JSONArray nearestArea = json.getJSONArray("nearest_area");
            if (nearestArea == null || nearestArea.isEmpty()) {
                return null;
            }

            JSONObject area = nearestArea.getJSONObject(0);

            // 获取天气信息（current_condition 数组的第一个元素）
            JSONArray currentCondition = json.getJSONArray("current_condition");
            if (currentCondition == null || currentCondition.isEmpty()) {
                return null;
            }

            JSONObject condition = currentCondition.getJSONObject(0);

            // 提取温度（摄氏度）
            Integer temperature = condition.getInt("temp_C");

            // 提取天气状况描述（中文）
            String conditionDesc = condition.getJSONArray("weatherDesc")
                    .getJSONObject(0)
                    .getStr("value", "未知");

            // 提取湿度百分比
            Integer humidity = condition.getInt("humidity");

            // 提取风速（km/h）
            Integer windSpeed = condition.getInt("windspeedKmph");

            // 返回天气信息
            return new Response.WeatherInfo(
                    temperature != null ? temperature : 0,
                    conditionDesc,
                    humidity != null ? humidity : 0,
                    windSpeed != null ? windSpeed : 0,
                    LocalDateTimeUtil.format(LocalDateTime.now(), NORM_DATETIME_PATTERN)
            );

        } catch (Exception e) {
            log.error("调用天气 API 失败，城市：{}", city, e);
            return null;
        }
    }

    /**
     * 生成模拟的天气数据（备用）
     */
    private Response.WeatherInfo generateMockWeatherInfo() {
        // 随机生成温度（10-35 度）
        int temperature = (int) (Math.random() * 26) + 10;

        // 随机生成湿度（30-90%）
        int humidity = (int) (Math.random() * 61) + 30;

        // 随机生成风速（0-30 km/h）
        int windSpeed = (int) (Math.random() * 31);

        // 随机天气状况
        String[] conditions = {"晴", "多云", "阴", "小雨", "中雨", "大雨"};
        String condition = conditions[(int) (Math.random() * conditions.length)];

        return new Response.WeatherInfo(
                temperature,
                condition,
                humidity,
                windSpeed,
                LocalDateTimeUtil.format(LocalDateTime.now(), NORM_DATETIME_PATTERN)
        );
    }

}
