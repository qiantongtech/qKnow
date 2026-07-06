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

package tech.qiantong.qknow.module.ext.controller.admin.extRelationMappingMiddle.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qknow.common.core.domain.BaseEntity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 关系映射中间 创建/修改 Request VO ext_relation_mapping_middle
 *
 * @author qknow
 * @date 2025-12-16
 */
@Schema(description = "关系映射中间 Response VO")
@Data
public class ExtRelationMappingMiddleSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "关系表id", example = "")
    @NotNull(message = "关系表id不能为空")
    private Long relationId;

    @Schema(description = "中间表名称", example = "")
    @NotBlank(message = "中间表名称不能为空")
    @Size(max = 32, message = "中间表名称长度不能超过32个字符")
    private String tableName;

    @Schema(description = "关联源表字段", example = "")
    @NotBlank(message = "关联源表字段不能为空")
    private String tableField;

    @Schema(description = "关联目标表字段", example = "")
    @NotBlank(message = "关联目标表字段不能为空")
    private String relationField;

    @Schema(description = "备注", example = "")
    @NotBlank(message = "备注不能为空")
    @Size(max = 512, message = "备注长度不能超过512个字符")
    private String remark;


}
