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

package tech.qiantong.qknow.module.kmc.controller.admin.knowledgeSegment.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qknow.common.core.annotation.Excel;

import java.io.Serializable;
import java.util.Date;

/**
 * 文件分段 Response VO 对象 kmc_document_segment
 *
 * @author qknow
 * @date 2025-08-28
 */
@Schema(description = "文件分段 Response VO")
@Data
public class KmcDocumentSegmentRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    @Schema(description = "ID")
    private Long id;

    @Excel(name = "工作空间id")
    @Schema(description = "工作空间id", example = "")
    private Long workspaceId;

    @Excel(name = "文件名称")
    @Schema(description = "文件名称", example = "")
    private String documentName;

    @Excel(name = "文件id")
    @Schema(description = "文件id", example = "")
    private Long documentId;

    @Excel(name = "dify段落id")
    @Schema(description = "dify段落id", example = "")
    private String qmSegmentId;

    @Excel(name = "位置")
    @Schema(description = "位置", example = "")
    private Long position;

    @Excel(name = "dify所属文档ID")
    @Schema(description = "dify所属文档ID", example = "")
    private String qmDocumentId;

    @Excel(name = "分段内容文本")
    @Schema(description = "分段内容文本", example = "")
    private String content;

    @Excel(name = "签名内容文本")
    @Schema(description = "签名内容文本", example = "")
    private String signContent;

    @Excel(name = "答案内容(如果有)")
    @Schema(description = "答案内容(如果有)", example = "")
    private String answer;

    @Excel(name = "内容长度")
    @Schema(description = "内容长度", example = "")
    private Long wordCount;

    @Excel(name = "token数量")
    @Schema(description = "token数量", example = "")
    private Long tokens;

    @Excel(name = "关键词")
    @Schema(description = "关键词", example = "")
    private String keywords;

    @Excel(name = "索引节点ID")
    @Schema(description = "索引节点ID", example = "")
    private String indexNodeId;

    @Excel(name = "索引节点哈希值")
    @Schema(description = "索引节点哈希值", example = "")
    private String indexNodeHash;

    @Excel(name = "访问次数")
    @Schema(description = "访问次数", example = "")
    private Long hitCount;

    @Excel(name = "启用状态")
    @Schema(description = "启用状态", example = "")
    private Integer enabled;

    @Excel(name = "状态")
    @Schema(description = "状态", example = "")
    private String status;

    @Excel(name = "完成时间戳")
    @Schema(description = "完成时间戳", example = "")
    private String completedAt;

    @Excel(name = "错误信息")
    @Schema(description = "错误信息", example = "")
    private String error;

    @Excel(name = "子模块")
    @Schema(description = "子模块", example = "")
    private String childChunks;

    @Excel(name = "分段添加dify状态")
    @Schema(description = "分段添加dify状态", example = "")
    private Integer syncStatus;


    @Excel(name = "父级id")
    @Schema(description = "父级id", example = "")
    private String parentId;

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

}
