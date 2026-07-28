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

package tech.qiantong.qknow.module.kb.controller.admin.agent.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qknow.common.core.annotation.Excel;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * agent配置 Response VO 对象 kb_agent_config
 *
 * @author qknow
 * @date 2026-03-19
 */
@Schema(description = "agent配置 Response VO")
@Data
public class KbAgentConfigRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    @Schema(description = "ID")
    private Long id;

    @Excel(name = "工作区id")
    @Schema(description = "工作区id", example = "")
    private Long workspaceId;

    @Schema(description = "botid", example = "")
    private Long botId;

    @Excel(name = "大模型配置")
    @Schema(description = "大模型配置", example = "")
    private String modelConfig;

    @Excel(name = "提示词")
    @Schema(description = "提示词", example = "")
    private String prePrompt;

    @Excel(name = "参数配置")
    @Schema(description = "参数配置", example = "")
    private String parameters;

    @Excel(name = "知识库ids")
    @Schema(description = "知识库ids", example = "")
    private String knowledgeIds;

    @Excel(name = "知识图谱ids")
    @Schema(description = "知识图谱ids", example = "")
    private String graphIds;

    @Excel(name = "工具方法 ids")
    @Schema(description = "工具方法 ids", example = "")
    private String toolMethodIds;

    @Excel(name = "知识库名称列表")
    @Schema(description = "知识库名称列表", example = "")
    private List<String> knowledgeNames;

    @Excel(name = "工具方法名称列表")
    @Schema(description = "工具方法名称列表", example = "")
    private List<String> toolMethodNames;

    @Excel(name = "skills ids")
    @Schema(description = "skills ids", example = "")
    private String skillIds;

    @Excel(name = "skills 名称列表")
    @Schema(description = "skills 名称列表", example = "")
    private List<String> skillNames;

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
