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

package tech.qiantong.qknow.module.dm.api.model.dto;

import lombok.Data;
import tech.qiantong.qknow.common.database.core.DbColumn;

/**
 * 逻辑模型属性信息 DTO 对象 DP_MODEL_COLUMN
 *
 * @author qdata
 * @date 2025-01-21
 */
@Data
public class DmModelColumnReqDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 逻辑模型表ID */
    private Long modelId;

    /** 英文名称 */
    private String engName;

    /** 中文名称 */
    private String cnName;

    /** 数据类型 */
    private String columnType;

    /** 属性长度 */
    private Long columnLength;

    /** 小数长度 */
    private Long columnScale;

    /** 默认值 */
    private String defaultValue;

    /** 是否主键 */
    private String pkFlag;

    /** 是否必填 */
    private String nullableFlag;

    /** 排序 */
    private Long sortOrder;

    /** 权威部门 */
    private String authorityDept;

    /** 数据元id */
    private Long dataElemId;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;

    // 构造方法
    public DmModelColumnReqDTO(DbColumn column) {
        if (column != null) {
            this.columnLength = (column.getDataLength() != null) ? Long.valueOf(column.getDataLength()) : null;
            this.engName = column.getColName();
            this.cnName = column.getColComment();
            this.columnType = column.getDataType();
            this.columnScale = (column.getDataScale() != null) ? Long.valueOf(column.getDataScale()) : null;
            this.defaultValue = column.getDataDefault();
            this.pkFlag = column.getColKey() ? "1" : "0";
            this.nullableFlag = column.getNullable() ? "1" : "0";
            this.sortOrder = (column.getColPosition() != null) ? Long.valueOf(column.getColPosition()) : null;
        }
    }
}
