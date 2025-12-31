/*
 * Copyright © 2026 Qiantong Technology Co., Ltd.
 * qKnow Knowledge Platform
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please apply for brand customization authorization via official channels.
 *  *
 * More information: https://qknow.qiantong.tech/business.html
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2026 江苏千桐科技有限公司
 * qKnow 知识平台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qknow.qiantong.tech/business.html
 */

package tech.qiantong.qknow.module.ext.controller.admin.extDatasource.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import tech.qiantong.qknow.common.core.domain.BaseEntity;

/**
 * 数据源 创建/修改 Request VO ext_datasource
 *
 * @author qknow
 * @date 2025-02-25
 */
@Schema(description = "数据源 Response VO")
@Data
public class ExtDatasourceSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "数据库连接名称", example = "")
    @NotBlank(message = "数据库连接名称不能为空")
    @Size(max = 256, message = "数据库连接名称长度不能超过256个字符")
    private String name;

    @Schema(description = "数据库类型", example = "")
    @NotNull(message = "数据库类型不能为空")
    private Integer type;

    @Schema(description = "数据库地址", example = "")
    @NotBlank(message = "数据库地址不能为空")
    @Size(max = 256, message = "数据库地址长度不能超过256个字符")
    private String host;

    @Schema(description = "端口号", example = "")
    @NotNull(message = "端口号不能为空")
    private Long port;

    @Schema(description = "用户名", example = "")
    @Size(max = 256, message = "用户名长度不能超过256个字符")
    private String username;

    @Schema(description = "数据库名称", example = "")
    @Size(max = 32, message = "数据库名称长度不能超过32个字符")
    private String databaseName;

    @Schema(description = "密码", example = "")
    @Size(max = 256, message = "密码长度不能超过256个字符")
    private String password;

    @Schema(description = "连接状态", example = "")
//    @NotNull(message = "连接状态不能为空")
    private Integer status;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;


}
