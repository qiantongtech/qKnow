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
