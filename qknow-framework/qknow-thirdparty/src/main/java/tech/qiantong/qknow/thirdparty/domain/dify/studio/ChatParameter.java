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

import com.alibaba.fastjson2.JSONArray;
import lombok.Data;

/**
 * 参数类型
 * @author wang
 * @date 2025/04/23 16:42
 **/
@Data
public class ChatParameter {

    /** 参数名称 */
    private String type;

    /** 字段名称 */
    private String fieldName;

    /** 显示名称 */
    private String label;

    /** 是否必填 */
    private Boolean required;

    /** 默认值 */
    private String defaultValue;

    /** 最大长度 */
    private Integer maxLength;

    /** 文件类型 */
    private JSONArray fileTypes;

    /** 文件格式 */
    private JSONArray fileExtensions;
}
