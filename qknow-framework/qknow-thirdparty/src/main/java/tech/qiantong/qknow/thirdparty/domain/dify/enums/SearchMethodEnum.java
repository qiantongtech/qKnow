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
 *  检索方法
 */
@Getter
public enum SearchMethodEnum {
    KEYWORD_SEARCH("keyword_search"), // 关键字检索
    SEMANTIC_SEARCH("semantic_search"), // 语义检索
    FULL_TEXT_SEARCH("full_text_search"), // 全文检索
    HYBRID_SEARCH("hybrid_search"); // 混合检索

    private final String type;

    SearchMethodEnum(String type) {
        this.type = type;
    }
}
