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

package tech.qiantong.qknow.module.ext.dal.dataobject.extDatasource;

import lombok.Data;

@Data
public class ExtDataSourceTable {
    private Long id;
    private Long dataId;
    private String primaryKey;
    private Object primaryKeyData;
    private String tableName;
    private String databaseName;

    @Data
    public static class GetTableData{
        private String query;
        private String url;
        private String username;
        private String password;
        private String dbType;

        private String tableA;
        private Integer afieldNum;
        private String tableB;
    }

    @Data
    public static class GetTableDataByDataId{
        private Long databaseId;
        private Long dataId;
        private String tableName;
    }
}
