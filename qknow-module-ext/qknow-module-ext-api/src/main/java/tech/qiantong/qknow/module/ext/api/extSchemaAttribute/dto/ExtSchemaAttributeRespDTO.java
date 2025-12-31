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

package tech.qiantong.qknow.module.ext.api.extSchemaAttribute.dto;

import lombok.*;

/**
 * 概念属性 DTO 对象 ext_schema_attribute
 *
 * @author qknow
 * @date 2025-02-17
 */
@Data
public class ExtSchemaAttributeRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 工作区id */
    private Long workspaceId;

    /** 概念id */
    private Long schemaId;

    /** 概念名称 */
    private String schemaName;

    /** 属性名称 */
    private String name;

    /** 属性名称代码 */
    private String nameCode;

    /** 是否必填 */
    private Integer requireFlag;

    /** 数据类型 */
    private Integer dataType;

    /** 单/多值 */
    private Integer multipleFlag;

    /** 校验方式 */
    private Integer validateType;

    /** 最小值（用于区间校验） */
    private Long minValue;

    /** 最大值（用于区间校验） */
    private Long maxValue;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;


}
