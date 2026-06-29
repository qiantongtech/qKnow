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
 * 索引类型枚举
 */
@Getter
public enum IndexingTechniqueEnum {
    HIGH_QUALITY("high_quality"), // 高质量：使用 embedding 模型进行嵌入，构建为向量数据库索引
    ECONOMY("economy"); //  经济：使用 keyword table index 的倒排索引进行构建

    private final String type;

    IndexingTechniqueEnum(String type) {
        this.type = type;
    }
}
