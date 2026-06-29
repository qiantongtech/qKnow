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

package tech.qiantong.qknow.module.kb.dal.enums;

import java.util.Objects;

/**
 * bot 类型枚举
 */
public enum BotTypeEnums {
    WORK_FLOW(0),
    CHAT_FLOW(1),
    AGENT(2);

    private final Integer code;

    BotTypeEnums(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    /**
     * 根据code返回枚举
     *
     * @param code code
     * @return 枚举类型
     */
    public static BotTypeEnums get(Integer code) {
        for (BotTypeEnums v : values()) {
            if (Objects.equals(v.getCode(), code)) {
                return v;
            }
        }
        return null;
    }
}
