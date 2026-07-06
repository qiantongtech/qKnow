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

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qknow.common.core.page.PageParam;

import java.math.BigDecimal;

/**
 * 知识库 Request VO 对象 kmc_knowledge_base
 *
 * @author qknow
 * @date 2025-07-22
 */
@Schema(description = "知识库 Request VO")
@Data
public class KmcKnowledgeBasePageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
    @Schema(description = "ID", example = "")
    private Long id;
    @Schema(description = "工作区id", example = "")
    private Long workspaceId;

    @Schema(description = "灵桐知识库id", example = "")
    private String qmDatasetId;

    @Schema(description = "名称", example = "")
    private String name;

    @Schema(description = "描述", example = "")
    private String description;

    @Schema(description = "封面图", example = "")
    private String coverImage;

    @Schema(description = "索引方式", example = "")
    private String indexingTechnique;

    @Schema(description = "权限", example = "")
    private String permission;

    @Schema(description = "Embedding 模型名称", example = "")
    private String embeddingModel;

    @Schema(description = "Embedding 模型供应商", example = "")
    private String embeddingModelProvider;

    @Schema(description = "检索方法", example = "")
    private String searchMethod;

    @Schema(description = "是否开启 rerank", example = "")
    private Boolean rerankingEnable;

    @Schema(description = "Rerank 模型的提供商", example = "")
    private String rerankingProviderName;

    @Schema(description = "Rerank 模型的名称", example = "")
    private String rerankingModelName;

    @Schema(description = "召回条数", example = "")
    private Long topK;

    @Schema(description = "是否开启召回分数限制", example = "")
    private Boolean scoreThresholdEnabled;

    @Schema(description = "召回分数限制", example = "")
    private BigDecimal scoreThreshold;

    @Schema(description = "语义", example = "")
    private BigDecimal keywordWeight;

    @Schema(description = "关键字", example = "")
    private BigDecimal vectorWeight;

    private String rerankingMode;

    private Boolean validFlag;


}
