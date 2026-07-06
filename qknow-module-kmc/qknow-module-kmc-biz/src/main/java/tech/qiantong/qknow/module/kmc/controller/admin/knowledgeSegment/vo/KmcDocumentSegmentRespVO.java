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
