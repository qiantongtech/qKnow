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

package tech.qiantong.qknow.thirdparty.domain.dify.knowledge;

import com.alibaba.fastjson2.JSONObject;
import lombok.Data;
import tech.qiantong.qknow.common.utils.StringUtils;

import java.math.BigDecimal;

/**
 * 创建知识库
 * @author wang
 * @date 2025/07/22 16:11
 **/
@Data
public class Datasets {

    private String id;

    /** 名称 */
    private String name;

    /** 描述 */
    private String description;

    /** 索引方式 */
    private String indexingTechnique;

    /** 权限 */
    private String permission;

    /** Embedding 模型名称 */
    private String embeddingModel;

    /** Embedding 模型供应商 */
    private String embeddingModelProvider;

    /** 检索方法 */
    private String searchMethod;

    /** 是否开启 rerank */
    private Boolean rerankingEnable;

    /** Rerank 模型的提供商 */
    private String rerankingProviderName;

    /** Rerank 模型的名称 */
    private String rerankingModelName;

    /** 召回条数 */
    private Long topK;

    /** 是否开启召回分数限制 */
    private Boolean scoreThresholdEnabled;

    /** 召回分数限制 */
    private BigDecimal scoreThreshold;

    /** 语义 */
    private BigDecimal keywordWeight;

    /** 关键字 */
    private BigDecimal vectorWeight;

    private String rerankingMode;

    public String toJSONString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", this.name);
        jsonObject.put("description", this.description);
        jsonObject.put("indexing_technique", this.indexingTechnique);
        jsonObject.put("permission", this.permission);
        jsonObject.put("embedding_model", this.embeddingModel);
        jsonObject.put("embedding_model_provider", this.embeddingModelProvider);
        // 添加检索模型参数
        JSONObject retrievalModel = new JSONObject();
        retrievalModel.put("search_method", this.searchMethod);
        retrievalModel.put("reranking_enable", this.rerankingEnable);
        // Rerank 模型配置
        JSONObject rerankingModel = new JSONObject();
        rerankingModel.put("reranking_provider_name", this.rerankingProviderName);
        rerankingModel.put("reranking_model_name", this.rerankingModelName);

        retrievalModel.put("reranking_model", rerankingModel);
        retrievalModel.put("top_k", this.topK);
        retrievalModel.put("score_threshold_enabled", this.scoreThresholdEnabled);
        retrievalModel.put("score_threshold", this.scoreThreshold);

        if (StringUtils.isNotNull(this.rerankingMode)) {
            JSONObject weights = new JSONObject();
            weights.put("weight_type", "customized");
            weights.put("keyword_setting", JSONObject.of("keyword_weight", this.keywordWeight));
            JSONObject vectorSetting = JSONObject.of("vector_weight", this.vectorWeight);
            vectorSetting.put("embedding_model_name", this.embeddingModel);
            vectorSetting.put("embedding_provider_name", this.embeddingModelProvider);
            weights.put("vector_setting", vectorSetting);
            retrievalModel.put("weights", weights);
            retrievalModel.put("reranking_mode", this.rerankingMode);
        }

        jsonObject.put("retrieval_model", retrievalModel);

        return jsonObject.toJSONString();
    }
}
