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

package tech.qiantong.qknow.thirdparty.domain.dify.enums;

import lombok.Getter;

/**
 * 文档嵌入状态
 * @author wang
 */
@Getter
public enum DocIndexStatusEnum {
    // 索引完成
    COMPLETED("completed"),
    // 索引中
    SPLITTING("splitting"),
    // 排队中
    WAITING("waiting"),
    // 索引失败
    ERROR("error"),
    ;

    private final String type;

    DocIndexStatusEnum(String type) {
        this.type = type;
    }

    public boolean equals(String type) {
       return this.type.equals(type);
    }
}
