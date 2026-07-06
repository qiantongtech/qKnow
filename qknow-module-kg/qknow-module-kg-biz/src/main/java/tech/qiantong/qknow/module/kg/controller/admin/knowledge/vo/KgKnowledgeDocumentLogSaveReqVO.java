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
 * 文件操作日志 创建/修改 Request VO kg_knowledge_document_log
 *
 * @author qknow
 * @date 2025-10-22
 */
@Schema(description = "文件操作日志 Response VO")
@Data
public class KgKnowledgeDocumentLogSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "工作区id", example = "")
    @NotNull(message = "工作区id不能为空")
    private Long workspaceId;

    @Schema(description = "用户id", example = "")
    private Long userId;

    @Schema(description = "用户名", example = "")
    @NotBlank(message = "用户名不能为空")
    @Size(max = 256, message = "用户名长度不能超过256个字符")
    private String userName;

    @Schema(description = "文件id", example = "")
    private Long documentId;

    @Schema(description = "文件名", example = "")
    @NotBlank(message = "文件名不能为空")
    @Size(max = 256, message = "文件名长度不能超过256个字符")
    private String documentName;

    @Schema(description = "操作类型", example = "")
    @NotBlank(message = "操作类型不能为空")
    private Integer type;

    @Schema(description = "备注", example = "")
    @NotBlank(message = "备注不能为空")
    @Size(max = 512, message = "备注长度不能超过512个字符")
    private String remark;


}
