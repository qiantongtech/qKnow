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

import java.util.HashMap;
import java.util.Map;

public enum PhoenixDataTypeEnum {

    TINYINT(1, "TINYINT"),
    SMALLINT(2, "SMALLINT"),
    INTEGER(3, "INTEGER"),
    BIGINT(4, "BIGINT"),
    FLOAT(5, "FLOAT"),
    DOUBLE(6, "DOUBLE"),
    DECIMAL(7, "DECIMAL"),
    BOOLEAN(8, "BOOLEAN"),
    VARCHAR(12, "VARCHAR"),
    CHAR(-1, "CHAR"),
    BINARY(-2, "BINARY"),
    VARBINARY(-3, "VARBINARY"),
    DATE(91, "DATE"),
    TIME(92, "TIME"),
    TIMESTAMP(93, "TIMESTAMP"),
    ARRAY(1111, "ARRAY"),
    UNKNOWN(Integer.MIN_VALUE, "UNKNOWN");

    private final int code;
    private final String typeName;

    PhoenixDataTypeEnum(int code, String typeName) {
        this.code = code;
        this.typeName = typeName;
    }

    public int getCode() {
        return code;
    }

    public String getTypeName() {
        return typeName;
    }

    private static final Map<Integer, PhoenixDataTypeEnum> CACHE = new HashMap<>();

    static {
        for (PhoenixDataTypeEnum value : values()) {
            CACHE.put(value.code, value);
        }
    }

    public static String fromCode(int code) {
        return CACHE.getOrDefault(code, UNKNOWN).getTypeName();
    }
}
