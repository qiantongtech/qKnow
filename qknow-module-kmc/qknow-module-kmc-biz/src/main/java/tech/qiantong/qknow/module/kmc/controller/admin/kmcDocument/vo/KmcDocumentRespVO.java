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
