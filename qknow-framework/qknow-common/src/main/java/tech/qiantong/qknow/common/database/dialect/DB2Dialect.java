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
 * DB2 数据库方言
 *
 * @author QianTongDC
 * @date 2022-11-14
 */
@Slf4j
public class DB2Dialect extends AbstractDbDialect {

    /**
     * db2 参数需要是sid
     *
     * @param sid
     * @return
     */
    @Override
    public String columns(String sid, String tableName) {
        return "SELECT  " +
                "    COLNAME, " +
                "    COLNO AS COLPOSITION," +
                "    DEFAULT AS DATADEFAULT, " +
                "    NULLS AS NULLABLE, " +
                "    TYPENAME AS DATATYPE, " +
                "    LENGTH AS DATALENGTH, " +
                "    SCALE AS DATASCALE, " +
                "(CASE WHEN KEYSEQ IS NOT NULL THEN 1 ELSE 0 END) AS COLKEY, " +
                "    REMARKS AS COLCOMMENT " +
                "FROM  " +
                "    SYSCAT.COLUMNS " +
                "WHERE  " +
                "    TABSCHEMA = '" + sid + "'" +
                "    AND TABNAME = '" + tableName + "'" +
                "ORDER BY COLNO";
    }

    @Override
    public String columns(DbQueryProperty dbQueryProperty, String tableName) {
        return "SELECT  " +
                "    COLNAME, " +
                "    COLNO AS COLPOSITION," +
                "    DEFAULT AS DATADEFAULT, " +
                "    NULLS AS NULLABLE, " +
                "    TYPENAME AS DATATYPE, " +
                "    LENGTH AS DATALENGTH, " +
                "    SCALE AS DATASCALE, " +
                "(CASE WHEN KEYSEQ IS NOT NULL THEN 1 ELSE 0 END) AS COLKEY, " +
                "    REMARKS AS COLCOMMENT " +
                "FROM  " +
                "    SYSCAT.COLUMNS " +
                "WHERE  " +
                "    TABSCHEMA = '" + dbQueryProperty.getSid() + "'" +
                "    AND TABNAME = '" + tableName + "'" +
                "ORDER BY COLNO";
    }


    @Override
    public String generateCheckTableExistsSQL(DbQueryProperty dbQueryProperty, String tableName) {
        return "SELECT COUNT(*) FROM SYSCAT.TABLES WHERE TABSCHEMA = '" + dbQueryProperty.getSid() + "' AND TABNAME = '" + tableName + "'";

    }

    @Override
    public List<String> someInternalSqlGenerator(DbQueryProperty dbQueryProperty, String tableName, String tableComment, List<DbColumn> dbColumnList) {
        String sid = dbQueryProperty.getSid();

        if (StringUtils.isNotEmpty(sid)) {
            tableName = dbQueryProperty.getDbName() + "." + sid + "." + tableName;
        }

        List<String> sqlList = generateCreateSql(tableName, tableComment, dbColumnList);
        return sqlList;
    }

    private List<String> generateCreateSql(String tableName, String tableComment, List<DbColumn> columns) {
        List<String> sqlList = new ArrayList<>();
        StringBuilder createSql = new StringBuilder();

        createSql.append("CREATE TABLE ").append(tableName).append(" (");
        List<String> pkList = new ArrayList<>();
        for (DbColumn col : columns) {
            createSql.append("\n  ").append(col.getColName()).append(" ");
            createSql.append(mapColumnType(col));

            // N false 不能为空  Y true 可以为空
            if (!col.getNullable()) {
                createSql.append(" NOT NULL");
            }
            if (StringUtils.isNotEmpty(col.getDataDefault())) {
                createSql.append(" DEFAULT ").append(col.getDataDefault());
            }
            if (col.getColKey()) {
                pkList.add(col.getColName());
            }
            createSql.append(",");
        }
        if (createSql.lastIndexOf(",") == createSql.length() - 1) {
            createSql.deleteCharAt(createSql.length() - 1);
        }
        if (!pkList.isEmpty()) {
            createSql.append(",\n  PRIMARY KEY(");
            for (String pk : pkList) {
                createSql.append(pk).append(",");
            }
            createSql.deleteCharAt(createSql.length() - 1);
            createSql.append(")");
        }
        createSql.append("\n)");
        sqlList.add(createSql.toString());

        if (StringUtils.isNotEmpty(tableComment)) {
            String tableCmt = "COMMENT ON TABLE " + tableName + " IS '" + MD5Util.escapeSingleQuotes(tableComment) + "'";
            sqlList.add(tableCmt);
        }
        for (DbColumn col : columns) {
            if (StringUtils.isNotEmpty(col.getColComment())) {
                String colCmt = "COMMENT ON COLUMN " + tableName + "." + col.getColName()
                        + " IS '" + MD5Util.escapeSingleQuotes(col.getColComment()) + "'";
                sqlList.add(colCmt);
            }
        }

        //处理主键
        return sqlList;
    }

    private static boolean isStringTypeSwitchDEFAULT(String columnType) {
        switch (columnType) {
            case "VARCHAR":
            case "VARCHAR2":
            case "CHAR":
            case "CLOB":
            case "TEXT":
                return true;
            default:
                return false;
        }
    }

    private static String mapColumnType(DbColumn col) {
        // 类似 Oracle
        String type = col.getDataType();
        Long length = MD5Util.getStringToLong(col.getDataLength());
        Long scale = MD5Util.getStringToLong(col.getDataScale());

        switch (type) {
            case "varchar":
            case "varchar2":
            case "VARCHAR":
            case "VARCHAR2":
                return "VARCHAR(" + (length != null ? length : 255) + ")";
            case "CHAR":
                return "CHAR(" + (length != null ? length : 1) + ")";
            case "SMALLINT":
                return "SMALLINT";
            case "INT":
            case "INTEGER":
                return "INTEGER";
            case "BIGINT":
                return "BIGINT";
            case "REAL":
                return "REAL";
            case "DOUBLE":
                return "DOUBLE";
            case "FLOAT":
                return "FLOAT";
            case "DECIMAL":
                return "DECIMAL(" + (length != null ? length : 10) + "," + (scale != null ? scale : 0) + ")";
            case "NUMBER":
            case "NUMERIC":
                return "NUMERIC(" + (length != null ? length : 10) + "," + (scale != null ? scale : 0) + ")";
            case "DATE":
                return "DATE";
            case "DATETIME":
                return "TIMESTAMP";
            case "TIMESTAMP":
                return "TIMESTAMP";
            case "TEXT":
            case "BLOB":
                return "BLOB(2GB)";
            case "CLOB":
                return "CLOB(2GB)";
            default:
                return type;
        }
    }

    /**
     * db2 参数需要是sid
     *
     * @param sid
     * @return
     */
    @Override
    public String tables(String sid) {
        return "SELECT TABNAME AS TABLENAME,REMARKS AS TABLECOMMENT " +
                "FROM SYSCAT.TABLES " +
                "WHERE TABSCHEMA = '" + sid + "' " +
                "ORDER BY TABNAME";
    }

    @Override
    public String buildPaginationSql(String originalSql, long offset, long pageSize) {
        long pageNo = offset / pageSize + 1;
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT tmp.* FROM (SELECT temp_table.*, ROW_NUMBER() OVER (ORDER BY 1) AS rownum FROM ( ");
        sqlBuilder.append(originalSql).append(" ) AS temp_table) tmp ");
        sqlBuilder.append("WHERE tmp.rownum BETWEEN  ").append((pageNo - 1) * pageSize + 1).append(" AND ").append((pageNo - 1) * pageSize + pageSize);
        return sqlBuilder.toString();
    }

    @Override
    public String tables(DbQueryProperty dbQueryProperty) {
        return "SELECT TABNAME AS TABLENAME,REMARKS AS TABLECOMMENT " +
                "FROM SYSCAT.TABLES " +
                "WHERE TABSCHEMA = '" + dbQueryProperty.getSid() + "' " +
                "ORDER BY TABNAME";
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
        return "SELECT " + fields + " FROM " + dbQueryProperty.getSid() + "." + tableName;
    }

    /**
     * db2 参数需要是sid
     *
     * @param sid
     * @return
     */
    @Override
    public String getDataStorageSize(String sid) {
        return "SELECT " +
                "    TABSCHEMA AS OWNER," +
                "    SUM(DATA_OBJECT_P_SIZE + INDEX_OBJECT_P_SIZE + LOB_OBJECT_P_SIZE + " +
                "     LONG_OBJECT_P_SIZE + XML_OBJECT_P_SIZE) AS usedSizeMb" +
                "FROM " +
                "    SYSIBMADM.ADMINTABINFO " +
                "WHERE " +
                "    TABSCHEMA = '" + sid + "' " +
                "GROUP BY TABSCHEMA";
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
        String jdbcUrl = trainToJdbcUrl(dbQueryProperty);
        try (Connection conn = DriverManager.getConnection(jdbcUrl, dbQueryProperty.getUsername(),
                dbQueryProperty.getPassword())) {
            return conn.isValid(0);
        } catch (SQLException e) {
            log.error("数据库连接失败", e);
            throw new DataQueryException("数据库连接失败,稍后重试");
        }
    }

    @Override
    public String getInsertOrUpdateSql(String tableName, String where, String tableFieldName, String tableFieldValue, String setValue) {
        String sql = "MERGE INTO {tableName} USING (SELECT COUNT(1) count FROM {tableName}  WHERE {where}) c ON (c.count > 0) WHEN MATCHED THEN UPDATE SET {setValue} WHERE {where} WHEN NOT MATCHED THEN INSERT ({tableFieldName}) VALUES ({tableFieldValue})";
        sql = StringUtils
                .replace(sql, "{tableName}", tableName)
                .replace("{where}", where)
                .replace("{tableFieldName}", tableFieldName)
                .replace("{tableFieldValue}", tableFieldValue)
                .replace("{setValue}", setValue);
        return sql;
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

    @Override
    public String getTableName(DbQueryProperty property, String tableName) {
        return property.getSid() + "." + tableName;
    }
}
