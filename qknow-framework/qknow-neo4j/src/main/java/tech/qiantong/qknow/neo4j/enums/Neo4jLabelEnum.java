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

package tech.qiantong.qknow.neo4j.enums;

import lombok.Getter;

@Getter
public enum Neo4jLabelEnum {
    DYNAMICENTITY( "DynamicEntity", 0, ""), //公共标签
    STRUCTURED( "ExtStruck", 1, "task_id"), // 结构化
    UNSTRUCTURED("ExtUnStruck", 2, "task_id"), // 非结构化
    GRAPHENTITY("GraphEntity", 3, "graph_id"); // 故障

    @Getter
    private final String label;
    private final Integer code;
    private final String entityIdName;

    // 构造方法
    private Neo4jLabelEnum(String label, Integer code, String entityIdName) {
        this.label = label;
        this.code = code;
        this.entityIdName = entityIdName;
    }

    public static Neo4jLabelEnum get(Integer code) {
        for (Neo4jLabelEnum v : values()) {
            if (v.eq(code)) {
                return v;
            }
        }
        return null;
    }

    public boolean eq(Integer code) {
        return this.code.equals(code);
    }

}
