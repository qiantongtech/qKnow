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

import java.util.Objects;

public enum UnstructTypeEnums {
    DEEPKE("deepke", "deepke"),
    MODEL("model", "模型");
    public final String code;
    public final String info;

    UnstructTypeEnums(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public static UnstructTypeEnums get(String code) {
        for (UnstructTypeEnums v : values()) {
            if (v.eq(code)) {
                return v;
            }
        }
        return null;
    }

    // 根据code返回县市名称
    public static UnstructTypeEnums getName(String info) {
        for (UnstructTypeEnums v : values()) {
            if (v.like(info)) {
                return v;
            }
        }
        return null;
    }

    public boolean eq(String code) {
        return this.code.equals(code);
    }

    public boolean like(String info) {
        return this.info.equals(info);
    }

    public static String getInfo(String code) {
        return Objects.requireNonNull(UnstructTypeEnums.get(code)).getInfo();
    }

    public static String getCode(String info) {
        return Objects.requireNonNull(UnstructTypeEnums.getName(info)).getCode();
    }

    public String getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }
}
