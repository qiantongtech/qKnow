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
