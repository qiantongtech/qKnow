package tech.qiantong.qknow.common.httpClient;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class HttpUtils {
    private static final CloseableHttpClient httpClient;

    // HTTP 方法常量
    public static final String POST = "POST";
    public static final String GET = "GET";
    public static final String PUT = "PUT";
    public static final String DELETE = "DELETE";  // 新增 DELETE 常量

    static {
        // 设置连接池
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
        connManager.setMaxTotal(200); // 最大连接数
        connManager.setDefaultMaxPerRoute(20); // 每个路由的最大连接数

        // 设置请求配置
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(60000)  // 连接超时
                .setSocketTimeout(60000)   // 数据传输超时
                .setConnectionRequestTimeout(10000)  // 请求超时
                .build();

        // 创建HttpClient实例
        httpClient = HttpClients.custom()
                .setConnectionManager(connManager)
                .setDefaultRequestConfig(requestConfig)
                .build();
    }

    /**
     * 执行请求并返回响应对象
     *
     * @param method 请求方法
     * @param url URL
     * @param params 请求参数
     * @param headers 请求头
     * @return 响应对象
     * @throws IOException IO异常
     */
    private static ResponseObject executeRequest(String method, String url, Map<String, Object> params, List<HeaderEntity> headers) throws IOException {
        // 创建请求
        HttpUriRequest request;
        if (POST.equals(method)) {
            request = new HttpPost(url);
            StringEntity entity = new StringEntity(JSONObject.toJSONString(params), StandardCharsets.UTF_8);
            ((HttpPost) request).setEntity(entity);
        } else if (GET.equals(method)) {
            request = new HttpGet(url);
        } else if (PUT.equals(method)) {
            request = new HttpPut(url);
            StringEntity entity = new StringEntity(JSONObject.toJSONString(params), StandardCharsets.UTF_8);
            ((HttpPut) request).setEntity(entity);
        } else if (DELETE.equals(method)) {
            request = new HttpDelete(url);
        } else {
            throw new IllegalArgumentException("Unsupported HTTP method: " + method);
        }

        // 添加请求头
        if (headers != null && !headers.isEmpty()) {
            for (HeaderEntity header : headers) {
                request.addHeader(header.getKey(), header.getValue());
            }
        }

        // 执行请求
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            // 构造响应对象
            ResponseObject responseObject = new ResponseObject();
            responseObject.setStatus(response.getStatusLine().getStatusCode());
            responseObject.setHeaders(response.getAllHeaders());

            // 获取响应体
            String contentType = response.getFirstHeader("Content-Type").getValue();
            String body = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);

            // 根据Content-Type判断响应内容类型并返回
            if (contentType.contains("application/json")) {
                responseObject.setBody(JSONObject.parseObject(body));
            } else {
                responseObject.setBody(body);
            }

            return responseObject;
        }
    }

    /**
     * 发送GET请求并返回响应对象
     *
     * @param url URL地址
     * @param headers 请求头
     * @return 响应对象
     * @throws IOException IO异常
     */
    public static ResponseObject sendGet(String url, List<HeaderEntity> headers) throws IOException {
        return executeRequest(GET, url, null, headers);
    }

    /**
     * 发送POST请求并返回响应对象
     *
     * @param url URL地址
     * @param params 请求参数
     * @param headers 请求头
     * @return 响应对象
     * @throws IOException IO异常
     */
    public static ResponseObject sendPost(String url, Map<String, Object> params, List<HeaderEntity> headers) throws IOException {
        return executeRequest(POST, url, params, headers);
    }

    /**
     * 发送PUT请求并返回响应对象
     *
     * @param url URL地址
     * @param params 请求参数
     * @param headers 请求头
     * @return 响应对象
     * @throws IOException IO异常
     */
    public static ResponseObject sendPut(String url, Map<String, Object> params, List<HeaderEntity> headers) throws IOException {
        return executeRequest(PUT, url, params, headers);
    }

    /**
     * 发送DELETE请求并返回响应对象
     *
     * @param url URL地址
     * @param headers 请求头
     * @return 响应对象
     * @throws IOException IO异常
     */
    public static ResponseObject sendDelete(String url, List<HeaderEntity> headers) throws IOException {
        return executeRequest(DELETE, url, null, headers);
    }

    /**
     * 响应对象，封装HTTP请求的响应信息
     */
    public static class ResponseObject {
        private int status;  // HTTP 状态码
        private Header[] headers;  // 响应头
        private Object body;  // 响应体（可能是 JSON 对象或字符串）

        // Getters and setters
        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public Header[] getHeaders() {
            return headers;
        }

        public void setHeaders(Header[] headers) {
            this.headers = headers;
        }

        public Object getBody() {
            return body;
        }

        public void setBody(Object body) {
            this.body = body;
        }
    }
}
