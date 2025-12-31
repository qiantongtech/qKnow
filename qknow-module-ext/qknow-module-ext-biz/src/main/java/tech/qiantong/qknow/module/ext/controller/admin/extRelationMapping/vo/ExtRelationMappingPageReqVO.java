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

package tech.qiantong.qknow.module.ext.controller.admin.extRelationMapping.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qknow.common.core.page.PageParam;

/**
 * 关系映射 Request VO 对象 ext_relation_mapping
 *
 * @author qknow
 * @date 2025-02-25
 */
@Schema(description = "关系映射 Request VO")
@Data
public class ExtRelationMappingPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
    @Schema(description = "ID", example = "")
    private Long id;
    @Schema(description = "工作区id", example = "")
    private Long workspaceId;

    @Schema(description = "任务id", example = "")
    private Long taskId;

    @Schema(description = "表名", example = "")
    private String tableName;

    @Schema(description = "表显示名称", example = "")
    private String tableComment;

    @Schema(description = "字段名", example = "")
    private String fieldName;

    @Schema(description = "字段显示名称", example = "")
    private String fieldComment;

    @Schema(description = "关系", example = "")
    private String relation;

    @Schema(description = "关联表", example = "")
    private String relationTable;

    @Schema(description = "关联表名称", example = "")
    private String relationTableName;

    @Schema(description = "关联表字段", example = "")
    private String relationField;

    @Schema(description = "关联表实体字段", example = "")
    private String relationNameField;

}
