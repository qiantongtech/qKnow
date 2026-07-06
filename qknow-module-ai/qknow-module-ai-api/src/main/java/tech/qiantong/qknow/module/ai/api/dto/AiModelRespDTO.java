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

package tech.qiantong.qknow.module.ai.api.dto;

import lombok.*;

/**
 * AI 模型 DTO 对象 ai_model
 *
 * @author qknow
 * @date 2025-12-23
 */
@Data
public class AiModelRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 工作区id */
    private Long workspaceId;

    /** 秘钥id */
    private Long keyId;

    /** 模型名称 */
    private String name;

    /** 模型标志 */
    private String model;

    /** 平台 */
    private String platform;

    /** 类型 */
    private Integer type;

    /** 排序值 */
    private Long sort;

    /** 状态 */
    private Integer status;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;


}
