package tech.qiantong.qknow.module.dm.entity;

import com.alibaba.cloudapi.sdk.client.ApacheHttpClient;
import com.alibaba.cloudapi.sdk.enums.HttpMethod;
import com.alibaba.cloudapi.sdk.enums.Scheme;
import com.alibaba.cloudapi.sdk.model.ApiRequest;
import com.alibaba.cloudapi.sdk.model.ApiResponse;

import java.util.Arrays;
import java.util.Map;

import static com.alibaba.cloudapi.sdk.enums.HttpConnectionModel.MULTIPLE_CONNECTION;

/**
 * @author zhenzhao.czz
 * @date 2018/2/2
 */
public class SyncApiGetClient extends ApacheHttpClient {

    public SyncApiGetClient(IoTApiClientBuilderParams builderParams) {
        super.init(builderParams);
    }

    public ApiResponse doGet(String host, String path, boolean isHttps, Map<String, String> headers,
                             Map<String, String> querys) {

        ApiRequest apiRequest = new ApiRequest(HttpMethod.GET, path);
        apiRequest.setHttpConnectionMode(MULTIPLE_CONNECTION);
        apiRequest.setScheme(isHttps ? Scheme.HTTPS : Scheme.HTTP);
        apiRequest.setHost(host);
        if (null != headers && headers.size() > 0) {
            for (Map.Entry<String, String> header : headers.entrySet()) {
                apiRequest.getHeaders().put(header.getKey(), Arrays.asList(header.getValue()));
            }
        }
        if (null != querys && querys.size() > 0) {
            for (Map.Entry<String, String> query : querys.entrySet()) {
                apiRequest.getQuerys().put(query.getKey(), query.getValue());
            }
        }
        return sendSyncRequest(apiRequest);
    }
}
