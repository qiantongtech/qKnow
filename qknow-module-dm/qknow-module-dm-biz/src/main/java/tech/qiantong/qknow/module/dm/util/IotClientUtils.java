package tech.qiantong.qknow.module.dm.util;

import com.alibaba.cloudapi.sdk.model.ApiResponse;
import tech.qiantong.qknow.module.dm.entity.IoTApiClientBuilderParams;
import tech.qiantong.qknow.module.dm.entity.IoTApiRequest;
import tech.qiantong.qknow.module.dm.entity.SyncApiClient;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;

public class IotClientUtils {

    private final static String AK="20366433";
    private final static String AS="cbe0f4349c794172873e6403e2bbca0e";
    private final static String APIVERSION="0.2.4";
    private final static String HOST="api-gateway.res.iot-nsbdjssy.com:32108";

    public static SyncApiClient getSyncApiClient() {
        IoTApiClientBuilderParams builderParams = new IoTApiClientBuilderParams();
        builderParams.setAppKey(AK);
        builderParams.setAppSecret(AS);
        return new SyncApiClient(builderParams);
    }

    public static IoTApiRequest getIoTApiRequest() {
        IoTApiRequest request = new IoTApiRequest();
        //设置api的版本
        request.setApiVer(APIVERSION);
        request.setId(UUID.randomUUID().toString());
        return request;
    }

    public static String getDeviceData(String deviceName, String productKey, List<String> list) throws UnsupportedEncodingException {
        // 获取SyncApiClient
        SyncApiClient syncClient = getSyncApiClient();

        IoTApiRequest request = getIoTApiRequest();

        //设置参数
        request.putParam("productKey", productKey);
        request.putParam("deviceName", deviceName);
        request.putParam("propertyNames", list);

        //请求参数域名、path、request
        ApiResponse response = syncClient.postBody(HOST, "/device/property/list", request);

        return new String(response.getBody(), "utf-8");
    }

    public static String getDeviceProperty(String productKey) throws UnsupportedEncodingException {
        // 获取SyncApiClient
        SyncApiClient syncClient = getSyncApiClient();

        IoTApiRequest request = getIoTApiRequest();

        //设置参数
        request.putParam("productKey", productKey);

        //请求参数域名、path、request
        ApiResponse response = syncClient.postBody(HOST, "/product/ability/list", request);
        return new String(response.getBody(), "utf-8");
    }
}
