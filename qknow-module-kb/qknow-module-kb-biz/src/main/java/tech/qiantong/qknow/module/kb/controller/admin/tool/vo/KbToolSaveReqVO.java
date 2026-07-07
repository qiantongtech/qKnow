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
 * 工具管理 创建/修改 Request VO kb_tool
 *
 * @author qknow
 * @date 2026-03-19
 */
@Schema(description = "工具管理 Response VO")
@Data
public class KbToolSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "工作区id", example = "")
    private Long workspaceId;

    @Schema(description = "名称", example = "")
    @NotBlank(message = "名称不能为空")
    @Size(max = 32, message = "名称长度不能超过32个字符")
    private String name;

    @Schema(description = "描述", example = "")
    //@NotBlank(message = "描述不能为空")
    @Size(max = 128, message = "描述长度不能超过128个字符")
    private String description;

    @Schema(description = "标签", example = "")
    @Size(max = 256, message = "标签长度不能超过256个字符")
    private String tags;

    @Schema(description = "类型", example = "")
    private Integer type;

    @Schema(description = "来源", example = "")
    @Size(max = 128, message = "来源长度不能超过128个字符")
    private String source;

    @Schema(description = "备注", example = "")
    @Size(max = 512, message = "备注长度不能超过512个字符")
    private String remark;

    @Schema(description = "分类id", example = "")
    private Long categoryId;

    @Schema(description = "工具内容", example = "")
    private String content;

    @Schema(description = "参数定义", example = "")
    private String paramSchema;

    @Schema(description = "状态", example = "")
    private Integer status;
}
