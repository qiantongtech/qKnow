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
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import tech.qiantong.qknow.common.core.domain.BaseEntity;

import java.util.List;

/**
 * agent配置 Request VO 对象 kb_agent_config
 *
 * @author qknow
 * @date 2026-03-19
 */
@Schema(description = "agent配置 Request VO")
@Data
public class KbAgentConfigReqVO extends BaseEntity {

    @Schema(description = "ID", example = "")
    private Long id;

    @Schema(description = "工作区id", example = "")
    private Long workspaceId;

    @Schema(description = "botid", example = "")
    private Long botId;

    @Schema(description = "问题", example = "")
    private String question;

    @Schema(description = "数据源ID", example = "1")
    private Long datasourceId;

    @Schema(description = "入参", example = "")
    private String input;

    @Schema(description = "大模型配置", example = "")
    private String modelConfig;

    @Schema(description = "提示词", example = "")
    private String prePrompt;

    @Schema(description = "参数配置", example = "")
    private String parameters;

    @Schema(description = "知识库ids", example = "")
    private String knowledgeIds;

    @Schema(description = "知识图谱ids", example = "")
    private String graphIds;

    @Schema(description = "工具ids", example = "")
    private String toolMethodIds;

    @Schema(description = "skills ids", example = "")
    private String skillIds;

    @Schema(description = "mcpConfigIds", example = "")
    private String mcpConfigIds;

    @Schema(description = "Agent 可调用的 Agent、工作流 Bot IDs，多个用逗号分隔", example = "1,3")
    @Size(max = 128, message = "可调用Bot ids长度不能超过128个字符")
    private String callableBotIds;

    @Valid
    @Size(max = 40, message = "历史消息不能超过40条")
    @Schema(description = "历史聊天消息（不包含本轮问题）")
    private List<HistoryMessage> historyMessages;

    @Data
    public static class HistoryMessage {

        @Pattern(regexp = "user|assistant", message = "历史消息角色只能是user或assistant")
        @Schema(description = "消息角色", allowableValues = {"user", "assistant"})
        private String role;

        @Size(max = 16000, message = "单条历史消息不能超过16000个字符")
        @Schema(description = "消息内容")
        private String content;
    }

}
