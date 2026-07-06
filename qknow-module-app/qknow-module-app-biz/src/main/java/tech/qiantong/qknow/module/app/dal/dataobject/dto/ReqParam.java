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

package tech.qiantong.qknow.module.app.dal.dataobject.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
public class ReqParam implements Serializable {

    private static final long serialVersionUID=1L;

    private String paramId;

    /**
     * 参数名称
     */
    @NotBlank(message = "参数名称不能为空")
    private String paramName;

    /**
     * 是否为空
     */
    @NotNull(message = "是否为空不能为空")
    private String nullable;

    /**
     * 描述
     */
    @NotBlank(message = "描述不能为空")
    private String paramComment;

    /**
     * 操作符
     */
    @NotNull(message = "操作符不能为空")
    private String whereType;

    /**
     * 参数类型
     */
    @NotBlank(message = "参数类型不能为空")
    private String paramType;

    /**
     * 示例值
     */
    private String exampleValue;

    /**
     * 默认值
     */
    private String defaultValue;


    /**
     * 默认值
     */
    private List<ReqParam> ReqParamlist;

}
