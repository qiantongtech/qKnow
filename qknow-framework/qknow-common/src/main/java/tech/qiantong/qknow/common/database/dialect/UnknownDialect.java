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

package tech.qiantong.qknow.common.database.dialect;

import org.springframework.jdbc.core.RowMapper;
import tech.qiantong.qknow.common.database.constants.DbQueryProperty;
import tech.qiantong.qknow.common.database.core.DbColumn;
import tech.qiantong.qknow.common.database.core.DbName;
import tech.qiantong.qknow.common.database.core.DbTable;
import tech.qiantong.qknow.common.database.exception.DataQueryException;

import java.util.List;
import java.util.Map;

/**
 * 未知 数据库方言
 *
 * @author QianTongDC
 * @date 2022-11-14
 */
public class UnknownDialect extends AbstractDbDialect {

    @Override
    public String columns(String dbName, String tableName) {
        throw new DataQueryException("不支持的数据库类型");
    }

    @Override
    public String columns(DbQueryProperty dbQueryProperty, String tableName) {
        throw new DataQueryException("不支持的数据库类型");
    }
    @Override
    public String getPkColumnNames(DbQueryProperty dbQueryProperty, String tableName) {
        throw new DataQueryException("不支持的数据库类型");
    }

    @Override
    public String generateCheckTableExistsSQL(DbQueryProperty dbQueryProperty, String tableName) {
        throw new DataQueryException("不支持的数据库类型");
    }


    @Override
    public String buildTableNameByDbType(DbQueryProperty dbQueryProperty, String tableName) {
        throw new DataQueryException("不支持的数据库类型");
    }

    @Override
    public List<String> someInternalSqlGenerator(DbQueryProperty dbQueryProperty, String tableName, String tableComment, List<DbColumn> dbColumnList) {
        throw new DataQueryException("不支持的数据库类型");
    }

    @Override
    public List<String> validateSpecification(String tableName, String tableComment, List<DbColumn> columns) {
        throw new DataQueryException("不支持的数据库类型");
    }

    @Override
    public String tables(String dbName) {
        throw new DataQueryException("不支持的数据库类型");
    }
    @Override
    public String tables(DbQueryProperty dbQueryProperty) {
        throw new DataQueryException("不支持的数据库类型");
    }

    @Override
    public String buildQuerySqlFields(List<DbColumn> columns, String tableName, DbQueryProperty dbQueryProperty) {
        throw new DataQueryException("不支持的数据库类型");
    }

    @Override
    public String buildPaginationSql(String sql, long offset, long count) {
        throw new DataQueryException("不支持的数据库类型");
    }

    @Override
    public String count(String sql) {
        throw new DataQueryException("不支持的数据库类型");
    }

    @Override
    public String countNew(String sql, Map<String, Object> params) {
        throw new DataQueryException("不支持的数据库类型");
    }

    @Override
    public String getDataStorageSize(String dbName) {
        throw new DataQueryException("不支持的数据库类型");
    }

    @Override
    public String getDbName() {
        throw new DataQueryException("不支持的数据库类型");
    }

    @Override
    public String getDbName(DbName dbName) {
        throw new DataQueryException("不支持的数据库类型");
    }

    @Override
    public String getInsertOrUpdateSql(String tableName, String where, String tableFieldName, String tableFieldValue, String setValue) {
        throw new DataQueryException("不支持的数据库类型");
    }

    @Override
    public RowMapper<DbColumn> columnMapper() {
        throw new DataQueryException("不支持的数据库类型");
    }

    @Override
    public RowMapper<DbTable> tableMapper() {
        throw new DataQueryException("不支持的数据库类型");
    }
}
