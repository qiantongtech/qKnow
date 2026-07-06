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

package tech.qiantong.qknow.module.ai.dal.enums;

/**
 * apiKey 状态
 *
 * @author fabian
 */
public enum ApiKeyStatus {
    /**
     * 未配置
     */
    NOT_CONFIG(0),
    /**
     * 已配置
     */
    CONFIG(1),
    /**
     * 已同步模型
     */
    SYNC(2);

    private final Integer code;

    ApiKeyStatus(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
