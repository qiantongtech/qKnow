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

package tech.qiantong.qknow.module.ext.controller.admin.extTaskLog.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qknow.common.core.page.PageParam;

import java.util.Date;

/**
 * 抽取任务执行日志 Request VO 对象 ext_task_log
 *
 * @author qknow
 * @date 2025-12-03
 */
@Schema(description = "抽取任务执行日志 Request VO")
@Data
public class ExtTaskLogPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
    @Schema(description = "ID", example = "")
    private Long id;
    @Schema(description = "工作区id", example = "")
    private Long workspaceId;

    @Schema(description = "任务id", example = "")
    private Long taskId;

    @Schema(description = "任务类型", example = "")
    private String taskType;

    @Schema(description = "任务名称", example = "")
    private String taskName;

    @Schema(description = "状态", example = "")
    private String status;

    @Schema(description = "错误消息", example = "")
    private String errorMsg;

    @Schema(description = "执行开始时间", example = "")
    private Date startTime;

    @Schema(description = "执行结束时间", example = "")
    private Date endTime;




}
