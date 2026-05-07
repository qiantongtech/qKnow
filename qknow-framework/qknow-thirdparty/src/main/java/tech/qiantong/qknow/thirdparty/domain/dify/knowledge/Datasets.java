/*
 * Copyright (c) 2026 Jiangsu Qiantong Technology Co., Ltd.
 *  *
 * Software Name: qKnow Knowledge Platform (Business Edition)
 * Software Copyright Registration No. 15980140
 *  *
 * [RIGHTS AND LICENSE STATEMENT]
 * This file contains non-public commercial source code of which Jiangsu Qiantong
 * Technology Co., Ltd. lawfully possesses complete intellectual property rights.
 *  *
 * Access and use are limited to entities or individuals who have signed a valid
 * commercial license agreement, within the scope stipulated in the agreement.
 * The "accessibility" of this source code is premised on lawful authorization
 * and does not constitute any form of transfer of intellectual property rights
 * or implied licensing.
 *  *
 * [PROHIBITIONS]
 * Unless explicitly agreed in the license agreement, the following acts in any
 * form are strictly prohibited:
 * 1. Copying, disseminating, disclosing, selling, renting, or redistributing
 * this source code;
 * 2. Providing the software's functionality to third parties via SaaS, PaaS,
 * cloud hosting, or other means;
 * 3. Using this software or its derivative versions to develop products that
 * compete with the Right Holder;
 * 4. Providing or displaying this source code or related technical information
 * to unauthorized third parties;
 * 5. Tampering with, circumventing, or destroying copyright notices, license
 * verifications, or other technical protection measures.
 *  *
 * [LEGAL LIABILITY]
 * Any unauthorized use constitutes an infringement of trade secrets and
 * intellectual property rights.
 *  *
 * The Right Holder will strictly pursue liability for breach of contract and
 * infringement in accordance with the commercial agreement and laws such as
 * the "Copyright Law of the People's Republic of China" and the "Anti-Unfair
 * Competition Law".
 *  *
 * ============================================================================
 *  *
 * Copyright (c) 2026 江苏千桐科技有限公司
 *  *
 * 软件名称：qKnow 知识平台（商业版） | 软著登字第15980140号
 *  *
 * 【权利与授权声明】
 * 本文件属于江苏千桐科技有限公司依法享有完全知识产权的非公开商业源代码。
 * 仅限已签署有效商业授权合同的单位或个人在约定范围内查阅和使用。
 * 源代码的“可访问性”均以合法授权为前提，不构成任何形式的知识产权转让或默示授权。
 *  *
 * 【禁止事项】
 * 除授权合同明确约定外，严禁任何形式的：
 * 1. 复制、传播、披露、出售、出租或再分发本源代码；
 * 2. 通过 SaaS、PaaS、云托管等方式向第三方提供本软件功能；
 * 3. 将本软件或其衍生版本用于开发与权利人构成竞争的产品；
 * 4. 向未授权第三方提供或展示本源代码或相关技术信息；
 * 5. 篡改、规避或破坏版权标识、授权校验及其他技术保护措施。
 *  *
 * 【法律责任】
 * 任何未经授权的利用行为，均构成对商业秘密及知识产权的侵害。
 * 权利人将依据商业合同及《中华人民共和国著作权法》《反不正当竞争法》
 * 等法律法规，严厉追究违约与侵权责任。
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
