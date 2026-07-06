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

package tech.qiantong.qknow.module.ext.controller.admin.extDatasource.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qknow.common.core.page.PageParam;

/**
 * 数据源 Request VO 对象 ext_datasource
 *
 * @author qknow
 * @date 2025-02-25
 */
@Schema(description = "数据源 Request VO")
@Data
public class ExtDatasourcePageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
    @Schema(description = "ID", example = "")
    private Long id;
    @Schema(description = "数据库连接名称", example = "")
    private String name;

    @Schema(description = "数据库类型", example = "")
    private Integer type;

    @Schema(description = "数据库地址", example = "")
    private String host;

    @Schema(description = "端口号", example = "")
    private Long port;

    @Schema(description = "数据库名称", example = "")
    private String databaseName;

    @Schema(description = "用户名", example = "")
    private String username;

    @Schema(description = "密码", example = "")
    private String password;

    @Schema(description = "连接状态", example = "")
    private Integer status;




}
