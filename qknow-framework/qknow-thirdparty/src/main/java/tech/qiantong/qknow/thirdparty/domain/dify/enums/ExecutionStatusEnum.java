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

package tech.qiantong.qknow.thirdparty.domain.dify.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 工作流执行状态枚举
 * @author : wang
 */
@Getter
public enum ExecutionStatusEnum {

    /**
     * 运行中
     */
    RUNNING("running"),

    /**
     * 执行成功
     */
    SUCCEEDED("succeeded"),

    /**
     * 执行失败
     */
    FAILED("failed"),

    /**
     * 手动停止
     */
    STOPPED("stopped");

    private final String value;

    ExecutionStatusEnum(String value) {
        this.value = value;
    }

    /**
     * Jackson 反序列化：将字符串转换为枚举（支持忽略大小写）
     * 例如：JSON中的"Running"或"RUNNING"均可转为RUNNING
     */
    @JsonCreator
    public static ExecutionStatusEnum fromValue(String value) {
        if (value == null) {
            return null;
        }
        for (ExecutionStatusEnum status : ExecutionStatusEnum.values()) {
            if (status.value.equalsIgnoreCase(value.trim())) {
                return status;
            }
        }
        throw new IllegalArgumentException("无效的执行状态: " + value);
    }

    /**
     * Jackson 序列化：将枚举转换为字符串（直接使用value）
     */
    @JsonValue
    public String getValue() {
        return value;
    }

    /**
     * 判断是否为终态（执行结束，不再变更）
     */
    public boolean isTerminal() {
        return this == SUCCEEDED || this == FAILED || this == STOPPED;
    }

    /**
     * 判断是否运行中
     */
    public boolean isRunning() {
        return this == RUNNING;
    }

    /**
     * 获取中文描述（可选，用于前端展示）
     */
    public String getDesc() {
        switch (this) {
            case RUNNING:
                return "运行中";
            case SUCCEEDED:
                return "成功";
            case FAILED:
                return "失败";
            case STOPPED:
                return "已停止";
            default:
                return "未知状态";
        }
    }
}
