package tech.qiantong.qknow.common.database.dialect;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.RowMapper;
import tech.qiantong.qknow.common.database.constants.DbQueryProperty;
import tech.qiantong.qknow.common.database.constants.fieldtypes.DM8FieldType;
import tech.qiantong.qknow.common.database.core.DbColumn;
import tech.qiantong.qknow.common.database.core.DbName;
import tech.qiantong.qknow.common.database.core.DbTable;
import tech.qiantong.qknow.common.database.exception.DataQueryException;
import tech.qiantong.qknow.common.database.utils.MD5Util;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <P>
 * 用途:ClickHouse
 * </p>
 *
 * @author: FXB
 * @create: 2024-08-07 10:28
 **/
@Slf4j
public class ClickHouseDialect extends AbstractDbDialect {

    @Override
    public String columns(String dbName, String tableName) {
        return "SELECT " +
                "name AS COLNAME, " +
                "position AS COLPOSITION, " +
                "default_expression AS DATADEFAULT, " +
                "IF(type LIKE 'Nullable(%)', 'Y', 'N') AS NULLABLE, " +
                "type AS DATATYPE, " +
                "CAST(NULL AS Nullable(String)) AS DATALENGTH, " +
                "CAST(NULL AS Nullable(String)) AS DATAPRECISION, " +
                "CAST(NULL AS Nullable(String)) AS DATASCALE, " +
                "IF(is_in_primary_key, 1, 0) AS COLKEY, " +
                "comment AS COLCOMMENT " +
                "FROM system.columns " +
                "WHERE database = '" + dbName + "' " +
                "AND table = '" + tableName + "' " +
                "ORDER BY position";
    }

    @Override
    public String columns(DbQueryProperty dbQueryProperty, String tableName) {
        return "SELECT " +
                "name AS COLNAME, " +
                "position AS COLPOSITION, " +
                "default_expression AS DATADEFAULT, " +
                "IF(type LIKE 'Nullable(%)', 'Y', 'N') AS NULLABLE, " +
                "type AS DATATYPE, " +
                "CAST(NULL AS Nullable(String)) AS DATALENGTH, " +
                "CAST(NULL AS Nullable(String)) AS DATAPRECISION, " +
                "CAST(NULL AS Nullable(String)) AS DATASCALE, " +
                "IF(is_in_primary_key, 1, 0) AS COLKEY, " +
                "comment AS COLCOMMENT " +
                "FROM system.columns " +
                "WHERE database = '" + dbQueryProperty.getDbName() + "' " +
                "AND table = '" + tableName + "' " +
                "ORDER BY position";
    }

    @Override
    public String generateCheckTableExistsSQL(DbQueryProperty dbQueryProperty, String tableName) {
        return "SELECT COUNT(*) " +
                "FROM system.tables " +
                "WHERE database = '" + dbQueryProperty.getDbName() + "' " +
                "AND name = '" + tableName + "'";
    }


    @Override
    public List<String> someInternalSqlGenerator(DbQueryProperty dbQueryProperty, String tableName, String tableComment, List<DbColumn> dbColumnList) {
        String dbName = dbQueryProperty.getDbName();
        if (StringUtils.isNotEmpty(dbName)) {
            tableName = dbName + "." + tableName;
        }
        return generateClickHouseCreateSql(tableName, tableComment, dbColumnList);
    }

    private List<String> generateClickHouseCreateSql(String tableName, String tableComment, List<DbColumn> columns) {
        List<String> sqlList = new ArrayList<>();
        StringBuilder createSql = new StringBuilder();

        createSql.append("CREATE TABLE ").append(tableName).append(" (\n");

        List<String> pkList = new ArrayList<>();
        for (int i = 0; i < columns.size(); i++) {
            DbColumn col = columns.get(i);

            String chType = mapClickHouseColumnType(col);

            // ClickHouse 的可空：Nullable(T)
            if (Boolean.TRUE.equals(col.getNullable())) {
                chType = "Nullable(" + chType + ")";
            }

            createSql.append("  ").append(col.getColName()).append(" ").append(chType);

            // DEFAULT
            if (StringUtils.isNotEmpty(col.getDataDefault())) {
                if (isStringTypeForDefault(chType)) {
                    createSql.append(" DEFAULT '").append(MD5Util.escapeSingleQuotes(col.getDataDefault())).append("'");
                } else {
                    createSql.append(" DEFAULT ").append(col.getDataDefault());
                }
            }

            // 列注释
            if (StringUtils.isNotEmpty(col.getColComment())) {
                createSql.append(" COMMENT '").append(MD5Util.escapeSingleQuotes(col.getColComment())).append("'");
            }

            // 主键收集（用于 MergeTree 的 PRIMARY KEY / ORDER BY）
            if (Boolean.TRUE.equals(col.getColKey())) {
                pkList.add(col.getColName());
            }

            if (i < columns.size() - 1) {
                createSql.append(",");
            }
            createSql.append("\n");
        }
        createSql.append(")");

        // 表注释（ClickHouse 支持在 CREATE 末尾加 COMMENT）
        if (StringUtils.isNotEmpty(tableComment)) {
            createSql.append(" COMMENT '").append(MD5Util.escapeSingleQuotes(tableComment)).append("'");
        }

        // 引擎与索引：ClickHouse 必须有 ORDER BY
        if (!pkList.isEmpty()) {
            createSql.append("\nENGINE = MergeTree()\n")
                    .append("PRIMARY KEY (").append(String.join(",", pkList)).append(")\n")
                    .append("ORDER BY (").append(String.join(",", pkList)).append(")");
        } else {
            // 没有主键时给一个空索引，保持语法合法
            createSql.append("\nENGINE = MergeTree()\nORDER BY tuple()");
        }

        sqlList.add(createSql.toString());
        return sqlList;
    }

    /** ClickHouse 类型映射（根据 DbColumn 的 dataType/length/scale） */
    private static String mapClickHouseColumnType(DbColumn col) {
        String type = StringUtils.upperCase(col.getDataType());
        Long length = MD5Util.getStringToLong(col.getDataLength());
        Long scale = MD5Util.getStringToLong(col.getDataScale());

        switch (type) {
            case "VARCHAR":
            case "VARCHAR2":
            case "TEXT":
            case "CLOB":
            case "STRING":
                return "String";
            case "CHAR":
                // 如需定长，可改为 FixedString(N)，这里默认 String 更通用
                return "String";
            case "INT":
            case "INTEGER":
                return "Int32";
            case "BIGINT":
                return "Int64";
            case "SMALLINT":
                return "Int16";
            case "TINYINT":
                return "Int8";
            case "BOOLEAN":
            case "BOOL":
                return "Bool"; // ClickHouse Bool 为 UInt8 的别名
            case "DECIMAL":
            case "NUMBER":
            case "NUMERIC":
                int p = length != null ? length.intValue() : 10;
                int s = scale != null ? scale.intValue() : 0;
                if (p < 1) p = 10;
                if (s < 0) s = 0;
                return "Decimal(" + p + "," + s + ")";
            case "FLOAT":
                return "Float32";
            case "DOUBLE":
                return "Float64";
            case "DATE":
                return "Date";      // 如需更大范围可用 Date32
            case "DATETIME":
            case "TIMESTAMP":
                return "DateTime";  // 如需时区可用 DateTime('UTC+8') 等
            case "UUID":
                return "UUID";
            default:
                // 未识别类型，原样返回（或根据需要抛错/降级为 String）
                return type;
        }
    }

    /** 判断是否为字符串类型（用于 DEFAULT 是否加引号） */
    private static boolean isStringTypeForDefault(String chType) {
        String t = chType;
        // 处理 Nullable(T)
        if (t.startsWith("Nullable(") && t.endsWith(")")) {
            t = t.substring("Nullable(".length(), t.length() - 1);
        }
        return "String".equalsIgnoreCase(t) || t.startsWith("FixedString");
    }

    @Override
    public String tables(String dbName) {
        return "SELECT name AS TABLENAME, comment AS TABLECOMMENT " +
                "FROM system.tables " +
                "WHERE database = '" + dbName + "'";
    }

    @Override
    public String tables(DbQueryProperty dbQueryProperty) {
        return "SELECT name AS TABLENAME, comment AS TABLECOMMENT " +
                "FROM system.tables " +
                "WHERE database = '" + dbQueryProperty.getDbName() + "'";
    }

    @Override
    public String buildQuerySqlFields(List<DbColumn> columns, String tableName, DbQueryProperty dbQueryProperty) {
        // 如果没有传入字段，则默认使用 * 查询所有字段
        if (columns == null || columns.isEmpty()) {
            return "SELECT * FROM " + tableName;
        }

        // 根据传入的 DbColumn 列表获取所有字段名，并用逗号分隔
        String fields = columns.stream()
                .map(DbColumn::getColName)
                .collect(Collectors.joining(", "));

        // 构造最终的 SQL 查询语句
        return "SELECT " + fields + " FROM " + dbQueryProperty.getDbName() + "." + tableName;
    }

    @Override
    public String getDataStorageSize(String dbName) {
        return "SELECT OWNER, SUM(BYTES) / 1024 / 1024 AS \"usedSizeMb\" FROM DBA_SEGMENTS WHERE OWNER = '" + dbName + "' AND SEGMENT_TYPE = 'TABLE' GROUP BY OWNER";
    }

    @Override
    public String getDbName() {
        return "SELECT SYS_CONTEXT('USERENV', 'CURRENT_SCHEMA') AS \"databaseName\" FROM DUAL";
    }

    @Override
    public String getDbName(DbName dbName) {
        return null;
    }


    @Override
    public RowMapper<DbColumn> columnMapper() {
        return (ResultSet rs, int rowNum) -> {
            DbColumn entity = new DbColumn();
            entity.setColName(rs.getString("COLNAME"));
            entity.setDataType(rs.getString("DATATYPE"));
            entity.setDataLength(rs.getString("DATALENGTH"));
            entity.setDataPrecision(rs.getString("DATAPRECISION"));
            if (rs.getString("DATAPRECISION") != null) {
                entity.setDataLength(rs.getString("DATAPRECISION"));
            }
            entity.setDataScale(rs.getString("DATASCALE"));
            entity.setColKey("1".equals(rs.getString("COLKEY")));
            entity.setNullable("Y".equals(rs.getString("NULLABLE")));
            entity.setColPosition(rs.getInt("COLPOSITION"));
            entity.setDataDefault(rs.getString("DATADEFAULT"));
            entity.setColComment(rs.getString("COLCOMMENT"));
            return entity;
        };
    }

    @Override
    public RowMapper<DbTable> tableMapper() {
        return (ResultSet rs, int rowNum) -> {
            DbTable entity = new DbTable();
            entity.setTableName(rs.getString("TABLENAME"));
            entity.setTableComment(rs.getString("TABLECOMMENT"));
            return entity;
        };
    }

    @Override
    public List<String> validateSpecification(String tableName, String tableComment, List<DbColumn> columns) {
        return this.validateDm8Specification(tableName, tableComment, columns);
    }


    @Override
    public Boolean validConnection(DataSource dataSource, DbQueryProperty dbQueryProperty) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(trainToJdbcUrl(dbQueryProperty), dbQueryProperty.getUsername(),
                    dbQueryProperty.getPassword());
            return conn.isValid(0);
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new DataQueryException("数据库连接失败,稍后重试");
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    throw new DataQueryException("关闭数据库连接出错");
                }
            }
        }
    }

    @Override
    public String getInsertOrUpdateSql(String tableName, String where, String tableFieldName, String tableFieldValue, String setValue) {
        // 20.x 无法单语句 upsert，这里实现 "不存在则插入"
        String sql = "INSERT INTO {tableName} ({tableFieldName}) " +
                "SELECT {tableFieldValue} FROM system.one " +   // system.one 提供单行
                "WHERE NOT EXISTS (SELECT 1 FROM {tableName} WHERE {where})";
        return StringUtils.replaceEach(sql,
                new String[]{"{tableName}", "{tableFieldName}", "{tableFieldValue}", "{where}"},
                new String[]{tableName, tableFieldName, tableFieldValue, where});
    }

    /**
     * 检验表名、表注释和字段是否符合 DM8 数据库规范
     *
     * @param tableName    表名
     * @param tableComment 表注释
     * @param columns      字段列表
     * @return 如果有错误，则返回错误信息列表；若无错误则返回空列表
     */
    public static List<String> validateDm8Specification(String tableName, String tableComment, List<DbColumn> columns) {
        List<String> errors = new ArrayList<>();

        // 1. 校验表名
        if (tableName == null || tableName.trim().isEmpty()) {
            errors.add("表名不能为空");
        } else {
            String tn = tableName.trim();
            // DM8 规范：表名必须以大写字母开头，只能包含大写字母、数字和下划线
            if (!tn.matches("^[A-Z][A-Z0-9_]*$")) {
                errors.add("表名格式不符合DM8规范，必须以大写字母开头，且只能包含大写字母、数字和下划线");
            }
            if (tn.length() > 30) {
                errors.add("表名长度不能超过30个字符");
            }
        }

        // 2. 校验表注释
        if (tableComment == null || tableComment.trim().isEmpty()) {
            errors.add("表注释不能为空");
        }

        // 3. 校验字段列表
        if (columns == null || columns.isEmpty()) {
            errors.add("表必须至少包含一个字段");
        } else {
            for (DbColumn column : columns) {
                List<String> strings = DM8FieldType.validateColumn(column);
                if (CollectionUtils.isNotEmpty(strings)) {
                    errors.addAll(strings);
                }
            }
        }
        return errors;
    }

}
