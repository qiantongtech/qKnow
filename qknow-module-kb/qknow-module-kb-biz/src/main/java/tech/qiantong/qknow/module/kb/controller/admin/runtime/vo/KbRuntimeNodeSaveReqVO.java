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

package tech.qiantong.qknow.module.kb.controller.admin.runtime.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import tech.qiantong.qknow.common.core.domain.BaseEntity;

/**
 * bot节点运行 创建/修改 Request VO kb_runtime_node
 *
 * @author qknow
 * @date 2026-03-18
 */
@Schema(description = "bot节点运行 Response VO")
@Data
public class KbRuntimeNodeSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "工作区id", example = "")
    @NotNull(message = "工作区id不能为空")
    private Long workspaceId;

    @Schema(description = "运行时id", example = "")
    @NotNull(message = "运行时id不能为空")
    private Long runtimeId;

    @Schema(description = "节点id", example = "")
    @NotNull(message = "节点id不能为空")
    private Long nodeId;

    @Schema(description = "步骤号", example = "")
    @NotNull(message = "步骤号不能为空")
    private Long step;

    @Schema(description = "输入数据", example = "")
    @NotBlank(message = "输入数据不能为空")
    private String input;

    @Schema(description = "输出数据", example = "")
    @NotBlank(message = "输出数据不能为空")
    private String output;

    @Schema(description = "备注", example = "")
    @NotBlank(message = "备注不能为空")
    @Size(max = 512, message = "备注长度不能超过512个字符")
    private String remark;


}
