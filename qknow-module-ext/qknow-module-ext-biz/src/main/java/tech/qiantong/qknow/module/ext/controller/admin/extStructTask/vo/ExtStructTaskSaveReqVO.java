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

package tech.qiantong.qknow.module.ext.controller.admin.extStructTask.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qknow.common.annotation.Excel;
import tech.qiantong.qknow.common.core.domain.BaseEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * 结构化抽取任务 创建/修改 Request VO ext_struct_task
 *
 * @author qknow
 * @date 2025-02-25
 */
@Schema(description = "结构化抽取任务 Response VO")
@Data
public class ExtStructTaskSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "工作区id", example = "")
    @NotNull(message = "工作区id不能为空")
    private Long workspaceId;

    @Schema(description = "所属图谱", example = "")
    private Long graphId;

    @Schema(description = "任务名称", example = "")
    @NotBlank(message = "任务名称不能为空")
    @Size(max = 256, message = "任务名称长度不能超过256个字符")
    private String name;

    @Schema(description = "任务状态", example = "")
    @NotNull(message = "任务状态不能为空")
    private Integer status;

    @Schema(description = "发布状态", example = "")
    @NotNull(message = "发布状态不能为空")
    private Integer publishStatus;

    @Schema(description = "发布时间", example = "")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date publishTime;

    @Schema(description = "发布人id", example = "")
//    @NotNull(message = "发布人id不能为空")
    private Long publisherId;

    @Schema(description = "发布人", example = "")
    @Size(max = 256, message = "发布人长度不能超过256个字符")
    private String publishBy;

    @Schema(description = "更新类型", example = "")
    @NotNull(message = "更新类型不能为空")
    private Integer updateType;

    @Schema(description = "更新频率", example = "")
    @NotNull(message = "更新频率不能为空")
    private String updateFrequency;

    @Schema(description = "定时更新状态", example = "")
    @NotNull(message = "定时更新状态不能为空")
    private Integer updateStatus;

    @Schema(description = "数据源id", example = "")
    @NotNull(message = "数据源id不能为空")
    private Long datasourceId;

    @Schema(description = "数据源名称", example = "")
    @NotBlank(message = "数据源名称不能为空")
    @Size(max = 256, message = "数据源名称长度不能超过256个字符")
    private String datasourceName;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;


}
