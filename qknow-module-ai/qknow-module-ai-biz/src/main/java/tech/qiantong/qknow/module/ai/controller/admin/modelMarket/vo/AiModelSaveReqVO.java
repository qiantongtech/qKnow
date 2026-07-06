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

package tech.qiantong.qknow.module.ai.controller.admin.modelMarket.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import tech.qiantong.qknow.common.core.domain.BaseEntity;

/**
 * AI 模型 创建/修改 Request VO ai_model
 *
 * @author qknow
 * @date 2025-12-23
 */
@Schema(description = "AI 模型 Response VO")
@Data
public class AiModelSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "工作区id", example = "")
    private Long workspaceId;

    @Schema(description = "秘钥id", example = "")
    @NotNull(message = "keyId 不能为空")
    private Long keyId;

    @Schema(description = "模型名称", example = "")
    @NotBlank(message = "模型名称不能为空")
    @Size(max = 128, message = "模型名称长度不能超过128个字符")
    private String name;

    @Schema(description = "模型标志", example = "")
    @Size(max = 32, message = "模型标志长度不能超过32个字符")
    private String model;

    @Schema(description = "平台", example = "")
    @Size(max = 32, message = "平台长度不能超过32个字符")
    private String platform;

    @Schema(description = "类型", example = "")
    private Integer type;

    @Schema(description = "排序值", example = "")
    private Long sort;

    @Schema(description = "状态", example = "")
    private Integer status;

    @Schema(description = "备注", example = "")
    @Size(max = 512, message = "备注长度不能超过512个字符")
    private String remark;


}
