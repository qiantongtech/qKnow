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

package tech.qiantong.qknow.module.app.controller.admin.appGraph.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;

/**
 * 三元组 Request VO ext_schema
 *
 * @author qknow
 * @date 2025-02-17
 */
@Schema(description = "三元组 Response VO")
@Data
public class AppGraphRelationshipSaveReqVO {

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "实体id1")
    @NotBlank(message = "实体不能为空")
    private Long entityId1;

    @Schema(description = "关系名称")
    @NotBlank(message = "关系不能为空")
    private String relationshipType;

    @Schema(description = "实体id2")
    @NotBlank(message = "实体不能为空")
    private Long entityId2;

}
