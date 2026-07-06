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

package tech.qiantong.qknow.module.kmc.controller.admin.kmcDocument.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import tech.qiantong.qknow.common.core.domain.BaseEntity;

/**
 * 知识文件 创建/修改 Request VO kmc_document
 *
 * @author qknow
 * @date 2025-02-14
 */
@Schema(description = "知识文件 Response VO")
@Data
public class KmcDocumentSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "工作区id", example = "")
    private Long workspaceId;

    @Schema(description = "知识分类id", example = "")
    @NotNull(message = "知识分类id不能为空")
    private Long categoryId;

    @Schema(description = "知识分类名称", example = "")
    @Size(max = 256, message = "知识分类名称长度不能超过256个字符")
    private String categoryName;

    @Schema(description = "文件名称", example = "")
    @NotBlank(message = "文件名称不能为空")
    @Size(max = 256, message = "文件名称长度不能超过256个字符")
    private String name;

    @Schema(description = "文件路径", example = "")
    @NotBlank(message = "文件路径不能为空")
    @Size(max = 256, message = "文件路径长度不能超过256个字符")
    private String path;

    @Schema(description = "文件描述", example = "")
    @Size(max = 256, message = "文件描述长度不能超过256个字符")
    private String description;

    @Schema(description = "预览次数", example = "")
    private Long previewCount;

    @Schema(description = "下载次数", example = "")
    private Long downloadCount;

    @Schema(description = "同步状态", example = "")
    private Integer syncStatus;

    @Schema(description = "更新人id", example = "")
    private Long updaterId;

    @Schema(description = "知识库id", example = "")
    @NotNull(message = "知识库id不能为空")
    private Long knowledgeBaseId;

    @Schema(description = "知识库名称", example = "")
    private String knowledgeBaseName;

    @Schema(description = "分段模式", example = "")
    @NotNull(message = "分段模式不能为空")
    private String mode;

    @Schema(description = "父分段的召回模式", example = "")
    private String parentMode;

    @Schema(description = "替换连续空格、换行符、制表符", example = "")
    private Boolean removeExtraSpaces;

    @Schema(description = "删除 URL、电子邮件地址", example = "")
    private Boolean removeUrlsEmails;

    @Schema(description = "分段重叠", example = "")
    @NotNull(message = "分段重叠不能为空")
    private String chunkOverlap;

    @Schema(description = "最大长度", example = "")
    @NotNull(message = "分段最大长度不能为空")
    private Integer maxTokens;

    @Schema(description = "分段标识符", example = "")
    @NotNull(message = "分段标识符不能为空")
    private String separator;

    @Schema(description = "索引内容的形式", example = "")
    private String docForm;

    @Schema(description = "在 Q&A 模式下，指定文档的语言", example = "")
    private String docLanguage;

    @Schema(description = "子分段最大长度", example = "")
    private Integer subchunkMaxTokens;

    @Schema(description = "子分段分隔符", example = "")
    private String subchunkSeparator;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;

    @Schema(description = "对话模型", example = "")
    @Size(max = 128, message = "备注长度不能超过128个字符")
    private String chatModel;

    @Schema(description = "对话模型提供商", example = "")
    @Size(max = 128, message = "备注长度不能超过128个字符")
    private String chatModelProvider;
}
