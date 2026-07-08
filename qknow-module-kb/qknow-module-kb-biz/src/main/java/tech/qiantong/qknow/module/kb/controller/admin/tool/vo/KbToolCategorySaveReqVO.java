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

package tech.qiantong.qknow.module.kb.controller.admin.tool.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import tech.qiantong.qknow.common.core.domain.BaseEntity;

/**
 * 知识分类 创建/修改 Request VO kmc_category
 *
 * @author qknow
 * @date 2025-02-13
 */
@Schema(description = "知识分类 Response VO")
@Data
public class KbToolCategorySaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "工作区id", example = "")
//    @NotNull(message = "工作区id不能为空")
    private Long workspaceId;

    @Schema(description = "父级id", example = "")
//    @NotNull(message = "父级id不能为空")
    private Long parentId;

    @Schema(description = "分类名称", example = "")
    @NotBlank(message = "分类名称不能为空")
    @Size(max = 256, message = "分类名称长度不能超过256个字符")
    private String name;

    @Schema(description = "显示顺序", example = "")
    private Long orderNum;

    @Schema(description = "祖级列表", example = "")
    @Size(max = 256, message = "祖级列表长度不能超过256个字符")
    private String ancestors;


    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;


}
