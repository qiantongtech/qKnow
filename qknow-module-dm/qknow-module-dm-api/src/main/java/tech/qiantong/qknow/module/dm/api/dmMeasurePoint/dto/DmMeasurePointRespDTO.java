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

package tech.qiantong.qknow.module.dm.api.dmMeasurePoint.dto;

import lombok.Data;

/**
 * 物联网测点 DTO 对象 dm_measure_point
 *
 * @author qknow
 * @date 2025-02-20
 */
@Data
public class DmMeasurePointRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 工作区id */
    private Long workspaceId;

    /** 测点名称 */
    private String name;

    /** 测点号 */
    private String code;

    /** 设备名称 */
    private String deviceName;

    /** 测点类型 */
    private Integer type;

    /** 设备key */
    private String deviceKey;

    /** 前缀 */
    private String prefix;

    /** 是否实时获取 */
    private Integer realtimeFlag;

    /** 同步频率（分钟） */
    private Long frequency;

    /** 单位 */
    private String unit;

    /** 是否为故障诊断 */
    private Integer failureFlag;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;


}
