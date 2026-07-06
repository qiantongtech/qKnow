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

package tech.qiantong.qknow.module.system.controller.admin.system.message.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 消息 创建/修改 Request VO message
 *
 * @author qknow
 * @date 2024-10-31
 */
@Schema(description = "消息 Response VO")
@Data
public class MessageSaveReqVO {

    private static final long serialVersionUID = 1L;

    @Schema(description = "发送人", example = "")
    private Long senderId;


    @Schema(description = "接收人", example = "")
    private Long receiverId;

    @Schema(description = "消息模块", example = "")
    @NotNull(message = "消息模块不能为空")
    private Integer module;


    @Schema(description = "实体类型", example = "")
    private Integer entityType;


    @Schema(description = "实体id", example = "")
    private Long entityId;

    @Schema(description = "消息链接", example = "")
    @NotBlank(message = "消息链接不能为空")
    @Size(max = 256, message = "消息链接长度不能超过256个字符")
    private String entityUrl;

    private Integer delFlag;
    private String id;
    private Integer hasRead;

}
