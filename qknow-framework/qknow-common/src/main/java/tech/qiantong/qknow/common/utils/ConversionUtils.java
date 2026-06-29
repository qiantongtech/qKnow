/*
 * Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
 *
 * This file is part of qKnow Intelligent Agent Building Platform (Open Source Edition).
 *
 * qKnow is licensed under Apache License 2.0 with additional qKnow terms.
 * You may use qKnow for commercial purposes, but you may not remove, hide,
 * modify, or replace the qKnow logo, copyright notices, license notices,
 * or attribution information without a separate commercial license.
 *
 * White-label use, OEM distribution, rebranding, or presenting qKnow as
 * another product requires separate commercial authorization from
 * Jiangsu Qiantong Technology Co., Ltd.
 *
 * Business License: https://community.qknow.ai/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

package tech.qiantong.qknow.common.utils;

public class ConversionUtils {

    /**
     * 将字符串转换为 Long 类型。如果字符串为空或无法转换，则返回 0L。
     *
     * @param dataLength 要转换的字符串
     * @return 转换后的 Long 类型值
     */
    public static Long getStringToLong(String dataLength) {
        if (StringUtils.isEmpty(dataLength)) {
            return 0L;
        }
        try {
            return Long.parseLong(dataLength);
        } catch (NumberFormatException e) {
            // 如果转换失败，则返回 0L
            return 0L;
        }
    }

    /**
     * 将字符串转换为 Integer 类型。如果字符串为空或无法转换，则返回 0。
     *
     * @param dataLength 要转换的字符串
     * @return 转换后的 Integer 类型值
     */
    public static Integer getStringToInt(String dataLength) {
        if (StringUtils.isEmpty(dataLength)) {
            return 0;
        }
        try {
            return Integer.parseInt(dataLength);
        } catch (NumberFormatException e) {
            // 如果转换失败，则返回 0
            return 0;
        }
    }

    /**
     * 将字符串转换为 Double 类型。如果字符串为空或无法转换，则返回 0.0。
     *
     * @param dataLength 要转换的字符串
     * @return 转换后的 Double 类型值
     */
    public static Double getStringToDouble(String dataLength) {
        if (StringUtils.isEmpty(dataLength)) {
            return 0.0;
        }
        try {
            return Double.parseDouble(dataLength);
        } catch (NumberFormatException e) {
            // 如果转换失败，则返回 0.0
            return 0.0;
        }
    }

    /**
     * 将字符串转换为 Boolean 类型。如果字符串为空或无法转换，则返回 false。
     *
     * @param value 要转换的字符串
     * @return 转换后的 Boolean 类型值
     */
    public static Boolean getStringToBoolean(String value) {
        if (StringUtils.isEmpty(value)) {
            return false;
        }
        return Boolean.parseBoolean(value);
    }


}
