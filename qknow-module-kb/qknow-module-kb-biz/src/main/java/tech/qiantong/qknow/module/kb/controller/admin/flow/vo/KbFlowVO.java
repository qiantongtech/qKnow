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

import com.alibaba.fastjson2.JSONObject;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class KbFlowVO implements Serializable {
    /**
     * botId
     */
    @NotNull(message = "botId不能为空")
    private Long botId;

    /**
     * 工作区id
     */
    private Long workspaceId;

    /**
     * 节点列表
     */
    @NotEmpty(message = "节点列表不能为空")
    private List<JSONObject> nodes;

    /**
     * 边列表
     */
    @NotEmpty(message = "边列表不能为空")
    private List<JSONObject> edges;
}
