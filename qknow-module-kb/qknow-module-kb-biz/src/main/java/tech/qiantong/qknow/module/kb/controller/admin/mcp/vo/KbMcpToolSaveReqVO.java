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

package tech.qiantong.qknow.module.kb.controller.admin.mcp.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import tech.qiantong.qknow.common.core.domain.BaseEntity;

/**
 * mcp 工具 创建/修改 Request VO kb_mcp_tool
 *
 * @author qknow
 * @date 2026-06-16
 */
@Schema(description = "mcp 工具 Response VO")
@Data
public class KbMcpToolSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "mcpId", example = "")
    @NotNull(message = "mcpId不能为空")
    private Long mcpId;

    @Schema(description = "名字", example = "")
    @NotBlank(message = "名字不能为空")
    @Size(max = 256, message = "名字长度不能超过256个字符")
    private String name;

    @Schema(description = "描述", example = "")
    @NotBlank(message = "描述不能为空")
    @Size(max = 512, message = "描述长度不能超过512个字符")
    private String description;

    @Schema(description = "备注", example = "")
    @NotBlank(message = "备注不能为空")
    @Size(max = 512, message = "备注长度不能超过512个字符")
    private String remark;


}
