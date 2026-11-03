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

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import tech.qiantong.qknow.common.core.domain.BaseEntity;

/**
 * agent配置 创建/修改 Request VO kb_agent_config
 *
 * @author qknow
 * @date 2026-03-19
 */
@Schema(description = "agent配置 Response VO")
@Data
public class KbAgentConfigSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "工作区id", example = "")
    private Long workspaceId;

    @Schema(description = "botid", example = "")
    @NotNull(message = "bot不能为空")
    private Long botId;

    @Schema(description = "大模型配置", example = "")
    @NotBlank(message = "大模型配置不能为空")
    private String modelConfig;

    @Schema(description = "提示词", example = "")
    private String prePrompt;

    @Schema(description = "历史记录条数", example = "1")
    private Integer historyMessageCount;

    @Schema(description = "是否启用长期记忆;0否1是", example = "false")
    private Boolean longTermMemoryEnabled;

    @Schema(description = "是否展示思考过程;0否1是", example = "true")
    private Boolean showThinkingProcess;

    @Schema(description = "参数配置", example = "")
    private String parameters;

    @Schema(description = "知识库ids", example = "")
    @Size(max = 128, message = "知识库ids长度不能超过128个字符")
    private String knowledgeIds;

    @Schema(description = "知识图谱ids", example = "")
    @Size(max = 128, message = "知识图谱ids长度不能超过128个字符")
    private String graphIds;

    @Schema(description = "工具方法ids", example = "")
    @Size(max = 128, message = "工具方法ids长度不能超过128个字符")
    private String toolMethodIds;

    @Schema(description = "skills ids", example = "")
    @Size(max = 128, message = "skills ids长度不能超过128个字符")
    private String skillIds;

    @Schema(description = "mcpConfig ids", example = "")
    @Size(max = 128, message = "mcpConfig ids长度不能超过128个字符")
    private String mcpConfigIds;

    @Schema(description = "Agent 可调用的 Agent、工作流 Bot IDs，多个用逗号分隔", example = "1,3")
    @Size(max = 128, message = "可调用Bot ids长度不能超过128个字符")
    private String callableBotIds;

    @Schema(description = "备注", example = "")
    @Size(max = 512, message = "备注长度不能超过512个字符")
    private String remark;


}
