package tech.qiantong.qknow.module.kb.api.flow.util;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import tech.qiantong.qknow.common.core.domain.CommonResult;

import java.util.Objects;

public class BotUtil {

    /**
     * 获取 CommonResult 内容，默认key为 text
     *
     * @param commonResult 统一返回值
     * @return 内容
     */
    public static String getChatResponseContent(CommonResult<String> commonResult) {
        return getChatResponseContent(commonResult, "text");
    }

    /**
     * 获取 CommonResult 内容
     *
     * @param commonResult 统一返回值
     * @param key          数据所属的 key
     * @return 内容
     */
    public static String getChatResponseContent(CommonResult<String> commonResult, String key) {
        String data = commonResult.getData();
        if (StrUtil.isBlank(data)) {
            return null;
        }
        if (!data.startsWith("{")){
            return "";
        }
        JSONObject jsonObject = JSONObject.parseObject(data);
        String string = jsonObject.getString(key);
        return Objects.isNull(string) ? "" : string;
    }
}
