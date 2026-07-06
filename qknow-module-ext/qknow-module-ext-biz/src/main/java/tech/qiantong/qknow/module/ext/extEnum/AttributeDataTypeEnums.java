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

package tech.qiantong.qknow.module.ext.extEnum;

/**
 * 概念属性的数据类型
 */
public enum AttributeDataTypeEnums {
    // 涵洞
    TEXT("文本", 0),
    INTEGER("整数", 1),
    FLOAT("小数", 2),
    TIME("时间", 3),
    BYTE("字节类型", 4),
    BOOLEAN("布尔值", 5),
    DICT("字典类型", 6);

    public final String code;
    public final Integer info;

    AttributeDataTypeEnums(String code, Integer info) {
        this.code = code;
        this.info = info;
    }

    public static AttributeDataTypeEnums get(String code) {
        for (AttributeDataTypeEnums v : values()) {
            if (v.eq(code)) {
                return v;
            }
        }
        return null;
    }

    // 根据code返回县市名称
    public static AttributeDataTypeEnums getNme(Integer info) {
        for (AttributeDataTypeEnums v : values()) {
            if (v.like(info)) {
                return v;
            }
        }
        return null;
    }

    public boolean eq(String code) {
        return this.code.equals(code);
    }

    public boolean like(Integer info) {
        return this.info.equals(info);
    }

    public static Integer getInfo(String code) {
        return AttributeDataTypeEnums.get(code)== null ? null : AttributeDataTypeEnums.get(code).info;
    }

    public static String getCode(Integer info) {
        AttributeDataTypeEnums nameEnums = AttributeDataTypeEnums.getNme(info);
        if (nameEnums == null) {
            return null;
        }else {
            return nameEnums.getCode();
        }

    }

    public String getCode() {
        return code;
    }

    public Integer getInfo() {
        return info;
    }
}
