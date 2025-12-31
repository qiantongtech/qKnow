/*
 * Copyright © 2026 Qiantong Technology Co., Ltd.
 * qKnow Knowledge Platform
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please apply for brand customization authorization via official channels.
 *  *
 * More information: https://qknow.qiantong.tech/business.html
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2026 江苏千桐科技有限公司
 * qKnow 知识平台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qknow.qiantong.tech/business.html
 */

package tech.qiantong.qknow.module.ext.service.extDatasource;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tech.qiantong.qknow.common.core.domain.AjaxResult;
import tech.qiantong.qknow.generator.domain.GenTable;
import tech.qiantong.qknow.module.ext.dal.dataobject.extDatasource.ExtDataSourceTable;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 结构化抽取查询数据库相关
 */
@Slf4j
@Service
public class ExtDatasourceQueryService {

    /**
     * 查询一张表的全部数据 单表, sql语句自定义
     *
     * @param getTableData
     * @return
     */
    public List<ConcurrentHashMap<String, Object>> getTableData(ExtDataSourceTable.GetTableData getTableData) {
        // SQL 查询语句
        String query = getTableData.getQuery();

        // 连接对象和其他资源
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        // 存储查询结果的列表
        List<ConcurrentHashMap<String, Object>> resultList = new ArrayList<>();
        try {
            // 获取数据库连接
            connection = getConnection(getTableData.getDbType(), getTableData.getUrl(), getTableData.getUsername(), getTableData.getPassword());
            // 创建 Statement 对象
            statement = connection.createStatement();
            // 执行查询
            resultSet = statement.executeQuery(query);
            // 获取结果集的元数据（字段信息）
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            // 遍历结果集并将每一行的数据存入 resultList
            while (resultSet.next()) {
                // 创建一个 Map 用来存储当前行的数据
                ConcurrentHashMap<String, Object> rowMap = new ConcurrentHashMap<>();

                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i); // 获取列名
                    Object columnValue = resultSet.getObject(i);   // 获取列值
                    rowMap.put(columnName, columnValue == null ? "" : columnValue);           // 将列名和列值放入 Map
                }
                // 将当前行的 Map 添加到 resultList 中
                resultList.add(rowMap);
            }
            log.info("-------查询结果---: {}", resultList);
            return resultList;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("查询异常");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("查询异常");
        } finally {
            // 关闭资源
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 连表查询关系数据
     *
     * @param getTableData
     * @return
     */
    public List<ConcurrentHashMap<String, Object>> getTableData2(ExtDataSourceTable.GetTableData getTableData) {
        // SQL 查询语句
//        String query = "SELECT * FROM your_table_name";
        String query = getTableData.getQuery();

        // 存储查询结果的列表
        List<ConcurrentHashMap<String, Object>> resultList = new ArrayList<>();
        try (
                Connection connection = getConnection(getTableData.getDbType(), getTableData.getUrl(), getTableData.getUsername(), getTableData.getPassword());
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query)
        ) {
            int columnNum = 0;
            // 获取结果集的元数据（字段信息）
            ResultSetMetaData metaData = resultSet.getMetaData();

            if (getTableData.getAfieldNum() == null) {
                //先查询一下a表有多少列
                //ResultSet resultSetNum = connection.getMetaData().getColumns(null, null, getTableData.getTableA(), null);
                //// 计算列的数量
                //while (resultSetNum.next()) {
                //    columnNum++;
                //}

                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    String tableName = metaData.getTableName(i); // 获取该字段对应的表名
                    if (getTableData.getTableA().equals(tableName)) {
                        columnNum++;
                    }
                }
            } else {
                columnNum = getTableData.getAfieldNum();
            }
            int columnCount = metaData.getColumnCount();

            // 遍历结果集并将每一行的数据存入 resultList
            while (resultSet.next()) {

                // 创建一个 Map 用来存储当前行的数据
                ConcurrentHashMap<String, Object> rowMap = new ConcurrentHashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i); // 获取列名
                    if (i <= columnNum) {
                        columnName = "a_" + columnName;
                    } else {
                        columnName = "b_" + columnName;
                    }
                    Object columnValue = resultSet.getObject(i);   // 获取列值
                    rowMap.put(columnName, columnValue == null ? "" : columnValue);           // 将列名和列值放入 Map
                }
                // 将当前行的 Map 添加到 resultList 中
                resultList.add(rowMap);
            }
            log.info("-------查询结果---: {}", resultList);
            return resultList;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("查询异常");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("查询异常");
        }
    }

    /**
     * 获取数据库表列表
     *
     * @param dbType
     * @param url
     * @param username
     * @param password
     * @return
     */
    public AjaxResult getTableList(String dbType, String url, String username, String password) {
        // 获取数据库连接
        try (Connection connection = getConnection(dbType, url, username, password)) {
            // 根据数据库类型动态构建 SQL 查询
            String sql = "SELECT table_name, table_comment, create_time, update_time FROM information_schema.tables " +
                    "where table_schema = (SELECT DATABASE()) " +
                    "AND table_name NOT LIKE 'system\\\\_%' " +
                    "AND table_name NOT LIKE 'qrtz\\\\_%' " +
                    "AND table_name NOT LIKE 'gen\\\\_%'";
            // 执行查询并获取表信息
            ArrayList<GenTable> genTables = new ArrayList<>();
            try (PreparedStatement stmt = connection.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    GenTable genTable = new GenTable();
                    genTable.setTableName(rs.getString("table_name"));
                    genTable.setTableComment(rs.getString("table_comment"));
                    genTable.setCreateTime(rs.getDate("create_time"));
                    genTable.setUpdateTime(rs.getDate("update_time"));
                    genTables.add(genTable);
                }
            }
            return AjaxResult.success(genTables);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error();
        }
    }

    public AjaxResult getTableData(String dbType, String databaseName, String url, String username, String password, String tableName) {
        // 获取数据库连接
        try (Connection connection = getConnection(dbType, url, username, password);) {

            // 根据数据库类型动态构建 SQL 查询，获取列的详细信息（包括描述）
            String sql = "SELECT COLUMN_NAME, COLUMN_TYPE, IS_NULLABLE, COLUMN_KEY, COLUMN_DEFAULT, EXTRA, COLUMN_COMMENT " +
                    "FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = ? AND TABLE_SCHEMA = ?;";

            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                // 设置查询的表名和数据库名
                stmt.setString(1, tableName);
                stmt.setString(2, databaseName);  // 需要提供数据库名称

                try (ResultSet rs = stmt.executeQuery()) {
                    // 创建一个 JSONArray 存储所有列的信息
                    JSONArray columnsArray = new JSONArray();
                    // 遍历查询结果
                    while (rs.next()) {
                        // 创建一个 JSONObject 来存储每一列的信息
                        JSONObject columnObject = new JSONObject();
                        String columnName = rs.getString("COLUMN_NAME");
                        String columnType = rs.getString("COLUMN_TYPE");
                        String isNullable = rs.getString("IS_NULLABLE");
                        String key = rs.getString("COLUMN_KEY");
                        String defaultValue = rs.getString("COLUMN_DEFAULT");
                        String extra = rs.getString("EXTRA");
                        String columnComment = rs.getString("COLUMN_COMMENT");  // 获取字段描述（注释）

                        // 将每列的相关信息存储到 JSON 对象中
                        columnObject.put("columnName", columnName);
                        columnObject.put("columnType", columnType);
                        columnObject.put("isNullable", isNullable);
                        columnObject.put("key", key);
                        columnObject.put("defaultValue", defaultValue);
                        columnObject.put("extra", extra);
                        columnObject.put("description", columnComment);  // 添加字段描述

                        // 将该列的 JSON 对象添加到 JSONArray 中
                        columnsArray.add(columnObject);
                    }
                    return AjaxResult.success(columnsArray);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                // 处理异常
            }
            return AjaxResult.error("获取表详情失败");
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error();
        }
    }

    // 根据数据库类型获取连接
    private static Connection getConnection(String dbType, String url, String username, String password) throws Exception {
        if (dbType.equalsIgnoreCase("Mysql")) {
            // MySQL连接
            return DriverManager.getConnection(url, username, password);
        } else if (dbType.equalsIgnoreCase("oracle")) {
            // Oracle连接
            return DriverManager.getConnection(url, username, password);
        } else if (dbType.equalsIgnoreCase("postgresql")) {
            // PostgreSQL连接
            return DriverManager.getConnection(url, username, password);
        } else if (dbType.equalsIgnoreCase("dm8")) {
            // DM8连接
            return DriverManager.getConnection(url, username, password);
        }
        throw new Exception("Unsupported database type");
    }
}
