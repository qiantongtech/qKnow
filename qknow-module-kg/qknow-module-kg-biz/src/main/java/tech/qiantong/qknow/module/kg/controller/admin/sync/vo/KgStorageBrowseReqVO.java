/*
 * Copyright (c) 2026 Jiangsu Qiantong Technology Co., Ltd.
 */
package tech.qiantong.qknow.module.kg.controller.admin.sync.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "第三方存储连接及文件浏览请求")
@Data
public class KgStorageBrowseReqVO {

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
