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

package tech.qiantong.qknow.module.kmc.dal.dataobject.knowledgeSegment;

import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import tech.qiantong.qknow.common.core.domain.BaseEntity;

/**
 * 文件分段 DO 对象 kmc_document_segment
 *
 * @author qknow
 * @date 2025-08-28
 */
@Data
@TableName(value = "kmc_document_segment")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
// @KeySequence("kmc_document_segment_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class KmcDocumentSegmentDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 工作空间id */
    private Long workspaceId;

    /** 文件名称 */
    private String documentName;

    /** 文件id */
    private Long documentId;

    /** dify段落id */
    private String qmSegmentId;

    /** 位置 */
    private Long position;

    /** dify所属文档ID */
    private String qmDocumentId;

    /** 分段内容文本 */
    private String content;

    /** 签名内容文本 */
    @JSONField(name="sign_content")
    private String signContent;

    /** 答案内容(如果有) */
    private String answer;

    /** 思考内容 */
    private String thinking;

    /** 内容长度 */
    @JSONField(name="word_count")
    private Long wordCount;

    /** token数量 */
    private Long tokens;

    /** 关键词 */
    private String keywords;

    /** 索引节点ID */
    @JSONField(name="index_node_id")
    private String indexNodeId;

    /** 索引节点哈希值 */
    @JSONField(name="index_node_hash")
    private String indexNodeHash;

    /** 访问次数 */
    @JSONField(name="hit_count")
    private Long hitCount;

    /** 启用状态 */
    private Integer enabled;

    /** 状态 */
    private String status;

    /** 完成时间戳 */
    @JSONField(name="completed_at")
    private String completedAt;

    /** 错误信息 */
    private String error;

    /** 子模块 */
    @JSONField(name="child_chunks")
    private String childChunks;

    /** 分段添加dify状态 */
    private Integer syncStatus;

    /** 父级id */
    private String parentId;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    @TableLogic
    private Boolean delFlag;

    @TableField(exist = false)
    private String docForm;


}
