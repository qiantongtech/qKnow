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

package tech.qiantong.qknow.module.ext.controller.admin.extRelationMappingMiddle.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qknow.common.core.domain.BaseEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 关系映射中间 创建/修改 Request VO ext_relation_mapping_middle
 *
 * @author qknow
 * @date 2025-12-16
 */
@Schema(description = "关系映射中间 Response VO")
@Data
public class ExtRelationMappingMiddleSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "关系表id", example = "")
    @NotNull(message = "关系表id不能为空")
    private Long relationId;

    @Schema(description = "中间表名称", example = "")
    @NotBlank(message = "中间表名称不能为空")
    @Size(max = 32, message = "中间表名称长度不能超过32个字符")
    private String tableName;

    @Schema(description = "关联源表字段", example = "")
    @NotBlank(message = "关联源表字段不能为空")
    private String tableField;

    @Schema(description = "关联目标表字段", example = "")
    @NotBlank(message = "关联目标表字段不能为空")
    private String relationField;

    @Schema(description = "备注", example = "")
    @NotBlank(message = "备注不能为空")
    @Size(max = 512, message = "备注长度不能超过512个字符")
    private String remark;


}
