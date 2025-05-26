package tech.qiantong.qknow.module.dm.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author zhongfu.xiezf on 17/10/18.
 */
public class IoTApiRequest {
    private String id;
    private String version = "1.0";
    private Map<String, Object> request = new HashMap<String, Object>();
    private Map<String, Object> params = new HashMap<String, Object>();

    public IoTApiRequest() {
        id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Map<String, Object> getRequest() {
        return request;
    }

    public void setRequest(Map<String, Object> request) {
        this.request = request;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public void setApiVer(String apiVer) {
        request.put("apiVer", apiVer);
    }

    public void setIotToken(String iotToken) {
        request.put("iotToken", iotToken);
    }

    public void setCloudToken(String cloudToken) {
        request.put("cloudToken", cloudToken);
    }

    public void putParam(String key, Object value) {
        params.put(key, value);
    }
}
