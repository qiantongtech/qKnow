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

package tech.qiantong.qknow.ai.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 参数类型枚举
 *
 * @author wang
 */
@AllArgsConstructor
@Getter
public enum ParameterTypeEnum {

    /**
     * 字符串
     */
    STRING("String", "字符串"),
    /**
     * 数字
     */
    NUMBER("Number", "数字"),
    /**
     * 布尔
     */
    BOOLEAN("Boolean", "布尔"),
    /**
     * 数组
     */
    ARRAY("Array", "数组");



    /**
     * 类型
     */
    private final String type;

    /**
     * 名称
     */
    private final String name;

}
