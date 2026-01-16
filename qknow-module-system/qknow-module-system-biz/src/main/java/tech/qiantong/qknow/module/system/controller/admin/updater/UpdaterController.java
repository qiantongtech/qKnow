package tech.qiantong.qknow.module.system.controller.admin.updater;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.qiantong.qknow.common.config.AniviaConfig;
import tech.qiantong.qknow.common.core.controller.BaseController;
import tech.qiantong.qknow.common.core.domain.CommonResult;
import tech.qiantong.qknow.common.httpClient.HttpUtils;
import tech.qiantong.qknow.module.system.controller.admin.updater.vo.VersionInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * 版本更新
 *
 * @author qknow
 */
@RestController
@RequestMapping("/updater")
public class UpdaterController extends BaseController {

    /** 系统基础配置 */
    @Autowired
    private AniviaConfig qknowConfig;

    /**
     * 获取当前部署实例的版本
     */
    @GetMapping("/getLocalVersion")
    public CommonResult<Map<String, Object>> getLocalVersion() {
        String currentVersion = qknowConfig.getVersion();
        Map<String, Object> result = new HashMap<>();
        result.put("latestVersion", currentVersion);
        return CommonResult.success(result);
    }

    /**
     * 检查当前实例是否为最新版本
     */
    @GetMapping("/getCurrentAppVersion")
    public CommonResult<VersionInfo> getCurrentAppVersion() {
        // 获取本地版本信息
        String currentVersion = qknowConfig.getVersion();
        // 初始最新版本信息
        String latestVersion = currentVersion;
        // 是否需要更新
        boolean needUpdate = false;
        try {
            String remoteUrl = "https://qknow-demo.qiantong.tech/demo-api/updater/getLocalVersion";
            HttpUtils.ResponseObject response = HttpUtils.sendGet(remoteUrl, null);
            if (response.getStatus() == 200) {
                // 直接将body转换为Map处理
                Map<?, ?> responseMap = parseResponseBody(response.getBody());
                if (responseMap != null) {
                    // 提取版本信息
                    Object versionData = responseMap.get("data");
                    Object version;
                    if (versionData instanceof Map) {
                        version = ((Map<?, ?>) versionData).get("latestVersion");
                    } else {
                        version = responseMap.get("latestVersion");
                    }
                    if (version != null) {
                        latestVersion = version.toString();
                    }
                    needUpdate = !currentVersion.equals(latestVersion);
                }
            }
        } catch (Exception e) {
            logger.error("检查版本失败", e);
        }
        VersionInfo versionInfo = new VersionInfo();
        versionInfo.setCurrentVersion(currentVersion);
        versionInfo.setLatestVersion(latestVersion);
        versionInfo.setNeedUpdate(needUpdate);
        return CommonResult.success(versionInfo);
    }

    /**
     * 解析响应体为Map
     */
    private Map<?, ?> parseResponseBody(Object body) {
        if (body instanceof Map) {
            return (Map<?, ?>) body;
        } else if (body instanceof String) {
            try {
                return JSONObject.parseObject((String) body, Map.class);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }
}
