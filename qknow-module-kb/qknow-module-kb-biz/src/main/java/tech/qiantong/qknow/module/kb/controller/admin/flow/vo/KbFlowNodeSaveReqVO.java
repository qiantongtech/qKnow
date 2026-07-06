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

package tech.qiantong.qknow.module.kb.controller.admin.flow.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import tech.qiantong.qknow.common.core.domain.BaseEntity;

/**
 * bot流程节点 创建/修改 Request VO kb_flow_node
 *
 * @author qknow
 * @date 2026-03-18
 */
@Schema(description = "bot流程节点 Response VO")
@Data
public class KbFlowNodeSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "工作区id", example = "")
    @NotNull(message = "工作区id不能为空")
    private Long workspaceId;

    @Schema(description = "botId", example = "")
    @NotNull(message = "botId不能为空")
    private Long botId;

    @Schema(description = "节点唯一标识", example = "")
    @NotBlank(message = "节点唯一标识不能为空")
    private String uuid;

    @Schema(description = "节点名", example = "")
    @NotBlank(message = "节点名不能为空")
    @Size(max = 128, message = "节点名长度不能超过128个字符")
    private String name;

    @Schema(description = "节点类型", example = "")
    @NotNull(message = "节点类型不能为空")
    private Integer type;

    @Schema(description = "节点配置", example = "")
    @NotBlank(message = "节点配置不能为空")
    private String config;

    @Schema(description = "节点样式", example = "")
    @NotBlank(message = "节点样式不能为空")
    private String style;

    @Schema(description = "输入参数", example = "")
    @NotBlank(message = "输入参数不能为空")
    private String input;

    @Schema(description = "输出参数", example = "")
    @NotBlank(message = "输出参数不能为空")
    private String output;

    @Schema(description = "备注", example = "")
    @NotBlank(message = "备注不能为空")
    @Size(max = 512, message = "备注长度不能超过512个字符")
    private String remark;


}
