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

package tech.qiantong.qknow.module.kg.controller.admin.knowledge.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qknow.common.core.domain.BaseEntity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 知识文件 创建/修改 Request VO kg_knowledge_document
 *
 * @author qknow
 * @date 2025-10-20
 */
@Schema(description = "知识文件 Response VO")
@Data
public class KgKnowledgeDocumentSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "工作区id", example = "")
    private Long workspaceId;

    @Schema(description = "知识分类id", example = "")
    @NotNull(message = "知识分类id不能为空")
    private Long categoryId;

    @Schema(description = "知识分类名称", example = "")
    @Size(max = 128, message = "知识分类名称长度不能超过128个字符")
    private String categoryName;

    @Schema(description = "文件名称", example = "")
    @NotBlank(message = "文件名称不能为空")
    private String name;

    @Schema(description = "文件路径", example = "")
    @NotBlank(message = "文件路径不能为空")
    private String path;

    @Schema(description = "文件描述", example = "")
    @Size(max = 1024, message = "文件描述长度不能超过1024个字符")
    private String description;

    @Schema(description = "备注", example = "")
    @Size(max = 512, message = "备注长度不能超过512个字符")
    private String remark;


}
