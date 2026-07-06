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
import tech.qiantong.qknow.common.core.annotation.Excel;

import java.io.Serializable;
import java.util.Date;

/**
 * 知识文件 Response VO 对象 kmc_document
 *
 * @author qknow
 * @date 2025-02-14
 */
@Schema(description = "知识文件 Response VO")
@Data
public class KmcDocumentRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    @Schema(description = "ID")
    private Long id;

    @Excel(name = "工作区id")
    @Schema(description = "工作区id", example = "")
    private Long workspaceId;

    @Excel(name = "知识分类id")
    @Schema(description = "知识分类id", example = "")
    private Long categoryId;

    @Excel(name = "知识分类名称")
    @Schema(description = "知识分类名称", example = "")
    private String categoryName;

    @Excel(name = "文件名称")
    @Schema(description = "文件名称", example = "")
    private String name;

    @Excel(name = "文件路径")
    @Schema(description = "文件路径", example = "")
    private String path;

    @Excel(name = "文件描述")
    @Schema(description = "文件描述", example = "")
    private String description;

    @Excel(name = "预览次数")
    @Schema(description = "预览次数", example = "")
    private Long previewCount;

    @Excel(name = "下载次数")
    @Schema(description = "下载次数", example = "")
    private Long downloadCount;

    @Excel(name = "同步状态")
    @Schema(description = "同步状态", example = "")
    private Integer syncStatus;

    /** 知识库id */
    private Long knowledgeBaseId;

    /** 知识库名称 */
    private String knowledgeBaseName;

    /** 分段模式 */
    private String mode;

    /** 父分段的召回模式 */
    private String parentMode;

    /** 替换连续空格、换行符、制表符 */
    private Boolean removeExtraSpaces;

    /** 删除 URL、电子邮件地址 */
    private Boolean removeUrlsEmails;

    /** 分段重叠 */
    private String chunkOverlap;

    /** 最大长度 */
    private Integer maxTokens;

    /** 自定义分段标识符 */
    private String separator;

    /** 索引内容的形式 */
    private String docForm;

    /** 在 Q&A 模式下，指定文档的语言 */
    private String docLanguage;

    /** 子分段最大长度 */
    private Integer subchunkMaxTokens;

    /** 子分段分隔符 */
    private String subchunkSeparator;

    @Excel(name = "是否有效")
    @Schema(description = "是否有效", example = "")
    private Boolean validFlag;

    @Excel(name = "删除标志")
    @Schema(description = "删除标志", example = "")
    private Boolean delFlag;

    @Excel(name = "创建人")
    @Schema(description = "创建人", example = "")
    private String createBy;

    @Excel(name = "创建人id")
    @Schema(description = "创建人id", example = "")
    private Long creatorId;

    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间", example = "")
    private Date createTime;

    @Excel(name = "更新人")
    @Schema(description = "更新人", example = "")
    private String updateBy;

    @Excel(name = "更新人id")
    @Schema(description = "更新人id", example = "")
    private Long updaterId;

    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间", example = "")
    private Date updateTime;

    @Excel(name = "备注")
    @Schema(description = "备注", example = "")
    private String remark;

    @Excel(name = "对话模型")
    @Schema(description = "对话模型", example = "")
    private String chatModel;

    @Excel(name = "对话模型提供商")
    @Schema(description = "对话模型提供商", example = "")
    private String chatModelProvider;
}
