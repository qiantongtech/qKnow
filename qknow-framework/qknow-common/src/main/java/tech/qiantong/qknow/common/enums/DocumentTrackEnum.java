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

package tech.qiantong.qknow.common.enums;

/**
 * 知识文件跟踪
 * @author jw
 * @date 2025/06/26 16:54
 * @return
 */
public enum DocumentTrackEnum {
    PREVIEW(0, "preview"),
    DOWNLOAD(1, "download"),
    ;

    public final Integer code;
    public final String info;

    DocumentTrackEnum(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

    public static DocumentTrackEnum get(Integer code) {
        for (DocumentTrackEnum v : values()) {
            if (v.eq(code)) {
                return v;
            }
        }
        return null;
    }

    public boolean eq(Integer code) {
        return this.code.equals(code);
    }

    public static String getInfo(Integer code) {
        return DocumentTrackEnum.get(code).getInfo();
    }

    public static Integer getCode(String info) {
        for (DocumentTrackEnum v : values()) {
            if (v.getInfo().equals(info)) {
                return v.getCode();
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }

}
