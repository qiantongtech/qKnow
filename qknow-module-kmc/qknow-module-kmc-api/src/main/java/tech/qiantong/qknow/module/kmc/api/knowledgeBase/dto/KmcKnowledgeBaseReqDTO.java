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

package tech.qiantong.qknow.module.kmc.api.knowledgeBase.dto;

import lombok.Data;

/**
 * 知识库 DTO 对象 kmc_knowledge_base
 *
 * @author qknow
 * @date 2025-07-22
 */
@Data
public class KmcKnowledgeBaseReqDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 工作区id */
    private Long workspaceId;

    /** 灵桐知识库id */
    private String qmDatasetId;

    /** 名称 */
    private String name;

    /** 描述 */
    private String description;

    /** 索引方式 */
    private Integer indexingTechnique;

    /** 权限 */
    private Integer permission;

    /** Embedding 模型名称 */
    private String embeddingModel;

    /** Embedding 模型供应商 */
    private String embeddingModelProvider;

    /** 检索方法 */
    private Integer searchMethod;

    /** 是否开启 rerank */
    private Integer rerankingEnable;

    /** Rerank 模型的提供商 */
    private String rerankingProviderName;

    /** Rerank 模型的名称 */
    private String rerankingModelName;

    /** 召回条数 */
    private Long topK;

    /** 是否开启召回分数限制 */
    private Integer scoreThresholdEnabled;

    /** 召回分数限制 */
    private Long scoreThreshold;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;


}
