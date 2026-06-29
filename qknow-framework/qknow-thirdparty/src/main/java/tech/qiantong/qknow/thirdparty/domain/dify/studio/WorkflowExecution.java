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

package tech.qiantong.qknow.thirdparty.domain.dify.studio;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 工作流执行
 * @program: qknow
 * @author wang
 * @date 2025/02/19 16:11
 **/
@Data
public class WorkflowExecution {

    /** workflow 执行 ID */
    private String id;

    /** 关联的 workflow ID */
    private String workflowId;

    /** workflow 执行状态 running / succeeded / failed / stopped */
    private String status;

    /** workflow 执行输入参数 */
    private String inputs;

    /** workflow 执行输出参数 */
    private String outputs;

    /** workflow 执行错误信息 */
    private String error;

    /** workflow 执行进度 */
    private Integer totalSteps;

    /** workflow 执行 tokens */
    private Integer totalTokens;

    /** workflow 执行开始时间 */
    private LocalDateTime createdAt;

    /** workflow 执行结束时间 */
    private LocalDateTime finishedAt;

    /** workflow 执行耗时 */
    private Double elapsedTime;

}
