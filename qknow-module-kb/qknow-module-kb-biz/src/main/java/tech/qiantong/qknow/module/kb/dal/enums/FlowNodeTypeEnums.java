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
 * 流程节点类型枚举
 */
public enum FlowNodeTypeEnums {
    START(1, "start"),
    LLM(2, "llm"),
    REPLY(3, "reply"),
    CONDITION(4, "condition"),
    ;

    private final Integer code;
    private final String name;

    FlowNodeTypeEnums(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 根据name获取枚举
     *
     * @param name 节点名字
     * @return 节点类型
     */
    public static FlowNodeTypeEnums getByName(String name) {
        for (FlowNodeTypeEnums value : FlowNodeTypeEnums.values()) {
            if (Objects.equals(value.name, name)) {
                return value;
            }
        }
        return null;
    }

    /**
     * 根据 code 获取枚举
     *
     * @param code 节点code
     * @return 节点类型
     */
    public static FlowNodeTypeEnums getByCode(Integer code) {
        for (FlowNodeTypeEnums value : FlowNodeTypeEnums.values()) {
            if (Objects.equals(value.code, code)) {
                return value;
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
