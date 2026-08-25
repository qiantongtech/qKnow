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

package tech.qiantong.qknow.module.kmc.controller.admin.sync.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "第三方存储连接及文件浏览请求")
@Data
public class KmcStorageBrowseReqVO {

    @Valid
    @NotNull(message = "连接信息不能为空")
    private Connection connection;

    @Schema(description = "当前目录", example = "/")
    private String path = "/";

    @Schema(description = "文件名称")
    private String fileName;

    @Schema(description = "页码")
    private Integer pageNum = 1;

    @Schema(description = "每页数量")
    private Integer pageSize = 10;

    @Data
    public static class Connection {

        @NotBlank(message = "数据连接类型不能为空")
        private String datasourceType;

        private String ip;
        private String port;
        private String username;
        private String password;
        private String config;
        private String keyId;
        private String keySecret;
        private String bucket;
        private String endpoint;
        private String domain;
    }
}

