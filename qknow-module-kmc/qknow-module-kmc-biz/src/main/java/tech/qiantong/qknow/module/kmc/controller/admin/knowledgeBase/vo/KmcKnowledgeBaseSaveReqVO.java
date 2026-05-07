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

package tech.qiantong.qknow.module.kmc.controller.admin.knowledgeBase.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import tech.qiantong.qknow.common.core.domain.BaseEntity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * 知识库 创建/修改 Request VO kmc_knowledge_base
 *
 * @author qknow
 * @date 2025-07-22
 */
@Schema(description = "知识库 Response VO")
@Data
public class KmcKnowledgeBaseSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "工作区id", example = "")
    private Long workspaceId;

    @Schema(description = "灵桐知识库id", example = "")
    private String qmDatasetId;

    @Schema(description = "名称", example = "")
    @NotBlank(message = "名称不能为空")
    @Size(max = 128, message = "名称不能超过128个字符")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9_]+$",message = "名称仅支持中文、英文、数字、下划线")
    private String name;

    @Schema(description = "描述", example = "")
    @Size(max = 256, message = "描述长度不能超过256个字符")
    private String description;

    @Schema(description = "封面图", example = "")
    private String coverImage;

    @Schema(description = "索引方式", example = "")
    private String indexingTechnique;

    @Schema(description = "权限", example = "")
    private String permission;

    @Schema(description = "Embedding 模型名称", example = "")
    @Size(max = 128, message = "Embedding 模型名称长度不能超过128个字符")
    private String embeddingModel;

    @Schema(description = "Embedding 模型供应商", example = "")
    @Size(max = 128, message = "Embedding 模型供应商长度不能超过128个字符")
    private String embeddingModelProvider;

    @Schema(description = "检索方法", example = "")
    private String searchMethod;

    @Schema(description = "是否开启 rerank", example = "")
    private Boolean rerankingEnable;

    @Schema(description = "Rerank 模型的提供商", example = "")
    @Size(max = 128, message = "Rerank 模型的提供商长度不能超过128个字符")
    private String rerankingProviderName;

    @Schema(description = "Rerank 模型的名称", example = "")
    @Size(max = 128, message = "Rerank 模型的名称长度不能超过128个字符")
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

    @Schema(description = "是否有效", example = "")
    private Boolean validFlag;

    @Schema(description = "备注", example = "")
    @Size(max = 512, message = "备注长度不能超过512个字符")
    private String remark;

    @Schema(description = "标签", example = "")
    private String tags;


}
