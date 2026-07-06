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

package tech.qiantong.qknow.common.database.constants.fieldtypes;

/**
 * Oracle 数据库支持的字段类型枚举
 */
public enum OracleFieldType {
    VARCHAR("VARCHAR"),
    VARCHAR2("VARCHAR2"),
    CHAR("CHAR"),
    NUMBER("NUMBER"),
    DATE("DATE"),
    TIMESTAMP("TIMESTAMP"),
    CLOB("CLOB");

    private final String type;

    OracleFieldType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
