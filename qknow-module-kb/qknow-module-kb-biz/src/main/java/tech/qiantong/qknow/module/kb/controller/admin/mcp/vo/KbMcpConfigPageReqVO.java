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
import lombok.Data;
import tech.qiantong.qknow.common.core.page.PageParam;

/**
 * mcp 配置 Request VO 对象 kb_mcp_config
 *
 * @author qknow
 * @date 2026-06-16
 */
@Schema(description = "mcp 配置 Request VO")
@Data
public class KbMcpConfigPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
    @Schema(description = "ID", example = "")
    private Long id;
    @Schema(description = "名称", example = "")
    private String name;

    @Schema(description = "描述", example = "")
    private String description;

    @Schema(description = "类型", example = "")
    private Integer type;

    @Schema(description = "工具数量", example = "")
    private Long toolNum;

    @Schema(description = "状态", example = "")
    private Integer status;

    @Schema(description = "url", example = "")
    private String url;

    @Schema(description = "url 请求头", example = "")
    private String urlHeader;

    @Schema(description = "命令", example = "")
    private String command;




}
