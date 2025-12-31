/*
 * Copyright © 2026 Qiantong Technology Co., Ltd.
 * qKnow Knowledge Platform
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please apply for brand customization authorization via official channels.
 *  *
 * More information: https://qknow.qiantong.tech/business.html
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2026 江苏千桐科技有限公司
 * qKnow 知识平台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qknow.qiantong.tech/business.html
 */

package tech.qiantong.qknow.common.utils.ip;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import tech.qiantong.qknow.common.config.AniviaConfig;
import tech.qiantong.qknow.common.utils.StringUtils;
import tech.qiantong.qknow.common.utils.http.HttpUtils;
import tech.qiantong.qknow.common.constant.Constants;

/**
 * 获取地址类
 *
 * @author qknow
 */
public class AddressUtils
{
    private static final Logger log = LoggerFactory.getLogger(AddressUtils.class);

    // IP地址查询
    public static final String IP_URL = "http://whois.pconline.com.cn/ipJson.jsp";

    // 未知地址
    public static final String UNKNOWN = "XX XX";

    public static String getRealAddressByIP(String ip)
    {
        // 内网不查询
        if (IpUtils.internalIp(ip))
        {
            return "内网IP";
        }
        if (AniviaConfig.isAddressEnabled())
        {
            try
            {
                String rspStr = HttpUtils.sendGet(IP_URL, "ip=" + ip + "&json=true", Constants.GBK);
                if (StringUtils.isEmpty(rspStr))
                {
                    log.error("获取地理位置异常 {}", ip);
                    return UNKNOWN;
                }
                JSONObject obj = JSON.parseObject(rspStr);
                String region = obj.getString("pro");
                String city = obj.getString("city");
                return String.format("%s %s", region, city);
            }
            catch (Exception e)
            {
                log.error("获取地理位置异常 {}", ip);
            }
        }
        return UNKNOWN;
    }
}
