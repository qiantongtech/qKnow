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

package tech.qiantong.qknow.module.kmc.controller.admin.knowledgeBase.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qknow.common.core.annotation.Excel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 知识库 Response VO 对象 kmc_knowledge_base
 *
 * @author qknow
 * @date 2025-07-22
 */
@Schema(description = "知识库 Response VO")
@Data
public class KmcKnowledgeBaseRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    @Schema(description = "ID")
    private Long id;

    @Excel(name = "工作区id")
    @Schema(description = "工作区id", example = "")
    private Long workspaceId;

    @Excel(name = "灵桐知识库id")
    @Schema(description = "灵桐知识库id", example = "")
    private String qmDatasetId;

    @Excel(name = "名称")
    @Schema(description = "名称", example = "")
    private String name;

    @Excel(name = "描述")
    @Schema(description = "描述", example = "")
    private String description;

    @Schema(description = "封面图", example = "")
    private String coverImage;

    @Excel(name = "索引方式")
    @Schema(description = "索引方式", example = "")
    private String indexingTechnique;

    @Excel(name = "权限")
    @Schema(description = "权限", example = "")
    private String permission;

    @Excel(name = "Embedding 模型名称")
    @Schema(description = "Embedding 模型名称", example = "")
    private String embeddingModel;

    @Excel(name = "Embedding 模型供应商")
    @Schema(description = "Embedding 模型供应商", example = "")
    private String embeddingModelProvider;

    @Excel(name = "检索方法")
    @Schema(description = "检索方法", example = "")
    private String searchMethod;

    @Excel(name = "是否开启 rerank")
    @Schema(description = "是否开启 rerank", example = "")
    private Boolean rerankingEnable;

    @Excel(name = "Rerank 模型的提供商")
    @Schema(description = "Rerank 模型的提供商", example = "")
    private String rerankingProviderName;

    @Excel(name = "Rerank 模型的名称")
    @Schema(description = "Rerank 模型的名称", example = "")
    private String rerankingModelName;

    @Excel(name = "召回条数")
    @Schema(description = "召回条数", example = "")
    private Long topK;

    @Excel(name = "是否开启召回分数限制")
    @Schema(description = "是否开启召回分数限制", example = "")
    private Boolean scoreThresholdEnabled;

    @Excel(name = "召回分数限制")
    @Schema(description = "召回分数限制", example = "")
    private BigDecimal scoreThreshold;

    @Schema(description = "语义", example = "")
    private BigDecimal keywordWeight;

    @Schema(description = "关键字", example = "")
    private BigDecimal vectorWeight;

    private String rerankingMode;

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间", example = "")
    private Date createTime;

    @Excel(name = "更新人")
    @Schema(description = "更新人", example = "")
    private String updateBy;

    @Excel(name = "更新人id")
    @Schema(description = "更新人id", example = "")
    private Long updaterId;

    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间", example = "")
    private Date updateTime;

    @Excel(name = "备注")
    @Schema(description = "备注", example = "")
    private String remark;

    @Excel(name = "文件数量")
    private Integer fileCount;

    @Excel(name = "标签")
    private String tags;
}
