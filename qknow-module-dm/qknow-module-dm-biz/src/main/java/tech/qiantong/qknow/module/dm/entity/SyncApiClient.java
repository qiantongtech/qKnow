package tech.qiantong.qknow.module.dm.entity;
/*
 * Copyright 2017 Alibaba Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.alibaba.cloudapi.sdk.annotation.ThreadSafe;
import com.alibaba.cloudapi.sdk.client.ApacheHttpClient;
import com.alibaba.cloudapi.sdk.enums.HttpMethod;
import com.alibaba.cloudapi.sdk.enums.Scheme;
import com.alibaba.cloudapi.sdk.model.ApiRequest;
import com.alibaba.cloudapi.sdk.model.ApiResponse;
import com.alibaba.fastjson.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Map;

import static com.alibaba.cloudapi.sdk.enums.HttpConnectionModel.MULTIPLE_CONNECTION;

/**
 * @author zhongfu.xiezf
 */
@ThreadSafe
public final class SyncApiClient extends ApacheHttpClient {

    public SyncApiClient(IoTApiClientBuilderParams builderParams) {
        super.init(builderParams);
    }

    public ApiResponse postBody(String host, String path, IoTApiRequest request, boolean isHttps,
                                Map<String, String> headers)
        throws UnsupportedEncodingException {
        byte[] body = JSONObject.toJSONString(request).getBytes("UTF-8");
        ApiRequest apiRequest = new ApiRequest(HttpMethod.POST_BODY, path,
            body);
        apiRequest.setHttpConnectionMode(MULTIPLE_CONNECTION);
        apiRequest.setScheme(isHttps ? Scheme.HTTPS : Scheme.HTTP);
        apiRequest.setHost(host);
        if (null != headers && headers.size() > 0) {
            for (Map.Entry<String, String> header : headers.entrySet()) {
                apiRequest.getHeaders().put(header.getKey(), Arrays.asList(header.getValue()));
            }
        }
        return sendSyncRequest(apiRequest);
    }

    public ApiResponse postBody(String host, String path, IoTApiRequest request, boolean isHttps)
        throws UnsupportedEncodingException {

        return postBody(host, path, request, isHttps, null);
    }

    public ApiResponse postBody(String host, String path, IoTApiRequest request)
        throws UnsupportedEncodingException {
        return postBody(host, path, request, false, null);
    }

}

