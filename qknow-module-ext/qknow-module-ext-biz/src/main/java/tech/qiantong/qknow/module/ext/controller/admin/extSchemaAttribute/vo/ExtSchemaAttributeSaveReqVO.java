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

package tech.qiantong.qknow.module.ext.controller.admin.extSchemaAttribute.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Date;
import tech.qiantong.qknow.common.core.domain.BaseEntity;

/**
 * 概念属性 创建/修改 Request VO ext_schema_attribute
 *
 * @author qknow
 * @date 2025-02-17
 */
@Schema(description = "概念属性 Response VO")
@Data
public class ExtSchemaAttributeSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "工作区id", example = "")
//    @NotNull(message = "工作区id不能为空")
    private Long workspaceId;

    @Schema(description = "概念id", example = "")
    @NotNull(message = "概念id不能为空")
    private Long schemaId;

    @Schema(description = "概念名称", example = "")
    @NotBlank(message = "概念名称不能为空")
    @Size(max = 256, message = "概念名称长度不能超过256个字符")
    private String schemaName;

    @Schema(description = "属性名称", example = "")
    @NotBlank(message = "属性名称不能为空")
    @Size(max = 256, message = "属性名称长度不能超过256个字符")
    private String name;

    @Schema(description = "属性名称代码", example = "")
    @NotBlank(message = "属性名称代码不能为空")
    @Size(max = 256, message = "属性名称代码长度不能超过256个字符")
    private String nameCode;

    @Schema(description = "是否必填", example = "")
    @NotNull(message = "是否必填不能为空")
    private Integer requireFlag;

    @Schema(description = "数据类型", example = "")
    @NotNull(message = "数据类型不能为空")
    private Integer dataType;

    @Schema(description = "单/多值", example = "")
    @NotNull(message = "单/多值不能为空")
    private Integer multipleFlag;

    @Schema(description = "校验方式", example = "")
    private Integer validateType;

    @Schema(description = "最小值", example = "")
    private Long minValue;

    @Schema(description = "最大值", example = "")
    private Long maxValue;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;

    @Schema(description = "描述", example = "")
    @Size(max = 1000, message = "描述长度不能超过1000个字符")
    private String description;


}
