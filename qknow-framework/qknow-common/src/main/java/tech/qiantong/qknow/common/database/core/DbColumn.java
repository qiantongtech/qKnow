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

package tech.qiantong.qknow.common.database.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DbColumn {

    /**
     * 列名
     */
    private String colName;

    /**
     * 数据类型
     */
    private String dataType;

    /**
     * 数据长度
     */
    private String dataLength;

    /**
     * 数据精度
     */
    private String dataPrecision;

    /**
     * 数据小数位
     */
    private String dataScale;

    /**
     * 是否主键 true是主键 false不是主键
     */
    private Boolean colKey;

    /**
     * 是否允许为空 true 允许为空 false 不允许为空
     */
    private Boolean nullable;

    /**
     * 列的序号
     */
    private Integer colPosition;

    /**
     * 列默认值
     */
    private String dataDefault;

    /**
     * 列注释
     */
    private String colComment;
}
