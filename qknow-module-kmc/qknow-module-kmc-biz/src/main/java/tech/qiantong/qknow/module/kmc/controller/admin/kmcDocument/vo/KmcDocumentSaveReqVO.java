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

package tech.qiantong.qknow.module.kmc.controller.admin.kmcDocument.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qknow.common.core.domain.BaseEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 知识文件 创建/修改 Request VO kmc_document
 *
 * @author qknow
 * @date 2025-02-14
 */
@Schema(description = "知识文件 Response VO")
@Data
public class KmcDocumentSaveReqVO extends BaseEntity{

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "工作区id", example = "")
    private Long workspaceId;

    @Schema(description = "知识分类id", example = "")
    @NotNull(message = "知识分类id不能为空")
    private Long categoryId;

    @Schema(description = "知识分类名称", example = "")
    @Size(max = 256, message = "知识分类名称长度不能超过256个字符")
    private String categoryName;

    @Schema(description = "文件名称", example = "")
    @NotBlank(message = "文件名称不能为空")
    @Size(max = 256, message = "文件名称长度不能超过256个字符")
    private String name;

    @Schema(description = "文件路径", example = "")
    @NotBlank(message = "文件路径不能为空")
    @Size(max = 256, message = "文件路径长度不能超过256个字符")
    private String path;

    @Schema(description = "文件描述", example = "")
    @Size(max = 256, message = "文件描述长度不能超过256个字符")
    private String description;

    @Schema(description = "更新人id", example = "")
    private Long updaterId;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;
}
