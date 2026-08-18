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

package tech.qiantong.qknow.module.kmc.dal.dataobject.document;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.qiantong.qknow.common.core.domain.BaseEntity;

import java.util.List;

/**
 * 知识文件 DO 对象 kmc_document
 *
 * @author qknow
 * @date 2025-02-14
 */
@Data
@TableName(value = "kmc_document")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
// @KeySequence("kmc_document_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KmcDocumentDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField(exist = false)
    private List<Long> ids;

    /** 工作区id */
    private Long workspaceId;

    /** 知识分类id */
    private Long categoryId;

    /** 知识分类名称 */
    private String categoryName;

    /** 文件名称 */
    private String name;

    /** 文件路径 */
    private String path;

    /** 文件描述 */
    private String description;

    /** 预览次数 */
    private Long previewCount;

    /** 下载次数 */
    private Long downloadCount;

    /** 同步状态 */
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
    @TableField(value = "`separator`")
    private String separator;

    /** 索引内容的形式 */
    private String docForm;

    /** 在 Q&A 模式下，指定文档的语言 */
    private String docLanguage;

    /**
     * 对话模型
     */
    private String chatModel;

    /**
     * 对话模型提供商
     */
    private String chatModelProvider;

    /** 子分段最大长度 */
    private Integer subchunkMaxTokens;

    /** 子分段分隔符 */
    private String subchunkSeparator;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    @TableLogic
    private Boolean delFlag;

    /**  灵桐文档ID */
    @TableField(exist = false)
    private String qmDocumentId;

    @TableField(exist = false)
    /** 灵桐知识库id */
    private String qmDatasetId;

    /** 分段数量 */
    private Integer segmentNum;

    /** 文件类型 */
    private String fileType;

    /** JSON风格 */
    private String jsonStyle;

}
