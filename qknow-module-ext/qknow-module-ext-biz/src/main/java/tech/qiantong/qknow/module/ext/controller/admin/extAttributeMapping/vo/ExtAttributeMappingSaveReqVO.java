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

package tech.qiantong.qknow.module.ext.controller.admin.extAttributeMapping.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Date;
import tech.qiantong.qknow.common.core.domain.BaseEntity;

/**
 * 属性映射 创建/修改 Request VO ext_attribute_mapping
 *
 * @author qknow
 * @date 2025-02-25
 */
@Schema(description = "属性映射 Response VO")
@Data
public class ExtAttributeMappingSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "工作区id", example = "")
    @NotNull(message = "工作区id不能为空")
    private Long workspaceId;

    @Schema(description = "任务id", example = "")
    @NotNull(message = "任务id不能为空")
    private Long taskId;

    @Schema(description = "表名", example = "")
    @NotBlank(message = "表名不能为空")
    @Size(max = 256, message = "表名长度不能超过256个字符")
    private String tableName;

    @Schema(description = "表显示名称", example = "")
    @Size(max = 256, message = "表显示名称长度不能超过256个字符")
    private String tableComment;

    @Schema(description = "字段名", example = "")
    @NotBlank(message = "字段名不能为空")
    @Size(max = 256, message = "字段名长度不能超过256个字符")
    private String fieldName;

    @Schema(description = "字段显示名称", example = "")
    @Size(max = 256, message = "字段显示名称长度不能超过256个字符")
    private String fieldComment;

    @Schema(description = "属性id", example = "")
    private Long attributeId;

    @Schema(description = "属性名称", example = "")
    @Size(max = 256, message = "属性名称长度不能超过256个字符")
    private String attributeName;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;


}
