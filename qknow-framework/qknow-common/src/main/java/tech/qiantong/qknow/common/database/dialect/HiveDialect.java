package tech.qiantong.qknow.common.database.dialect;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import tech.qiantong.qknow.common.database.constants.DbQueryProperty;
import tech.qiantong.qknow.common.database.core.DbColumn;
import tech.qiantong.qknow.common.database.core.DbName;
import tech.qiantong.qknow.common.database.core.DbTable;
import tech.qiantong.qknow.common.database.exception.DataQueryException;
import tech.qiantong.qknow.common.database.utils.MD5Util;
import tech.qiantong.qknow.common.utils.StringUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <P>
 * 用途:
 * </p>
 *
 * @author: FXB
 * @create: 2025-03-20 16:44
 **/
@Slf4j
public class HiveDialect extends AbstractDbDialect {

    @Override
    public String getDbName() {
        return "SELECT current_database()";
    }

    @Override
    public String getDbName(DbName dbName) {
        return null;
    }

    @Override
    public String tables(String dbName) {
        return "SHOW TABLES IN " + dbName ;
    }

    @Override
    public String tables(DbQueryProperty dbQueryProperty) {
        return "SHOW TABLES IN " + dbQueryProperty.getDbName() ;
    }

    @Override
    public String tablesComment(DbQueryProperty dbQueryProperty, String tableName) {
        return "DESCRIBE FORMATTED `" + dbQueryProperty.getDbName() + "`.`" + tableName + "`";
    }


    @Override
    public String columns(String dbName, String tableName) {
        return "DESCRIBE `" + dbName + "`.`" + tableName + "`";
    }

    @Override
    public String columns(DbQueryProperty dbQueryProperty, String tableName) {
        return "DESCRIBE `" + dbQueryProperty.getDbName() + "`.`" + tableName + "`";
    }

    @Override
    public String buildQuerySqlFields(List<DbColumn> columns, String tableName, DbQueryProperty dbQueryProperty) {
        String fullTableName = dbQueryProperty.getDbName() + "." + tableName;

        if (columns == null || columns.isEmpty()) {
            return "SELECT * FROM " + fullTableName;
        }

        // Hive 中字段若包含保留字或特殊字符，需加反引号保护
        String fields = columns.stream()
                .map(col -> "`" + col.getColName() + "`")
                .collect(Collectors.joining(", "));

        return "SELECT " + fields + " FROM " + fullTableName;
    }

    @Override
    public String generateCheckTableExistsSQL(DbQueryProperty dbQueryProperty, String tableName) {
        return "SHOW TABLES IN `" + dbQueryProperty.getDbName() + "` LIKE `" + tableName + "`";
    }


    @Override
    public RowMapper<DbTable> tableMapper() {
        return (ResultSet rs, int rowNum) -> {
            DbTable entity = new DbTable();
            entity.setTableName(rs.getString("tab_name"));
//            entity.setTableComment(rs.getString("TABLECOMMENT"));
            return entity;
        };
    }

    @Override
    public RowMapper<DbColumn> columnMapper() {
        return (ResultSet rs, int rowNum) -> {
            String colName = rs.getString(1);
            if (colName == null || colName.trim().isEmpty() || colName.startsWith("#")) {
                return null; // 忽略分隔符行等
            }

            DbColumn entity = new DbColumn();
            entity.setColName(colName);
            entity.setDataType(rs.getString(2));
            entity.setColComment(rs.getString(3));
            entity.setColPosition(rowNum + 1); // Hive 无列ID，手动填序号

            // 以下字段 Hive 不支持，设默认
            entity.setDataLength(null);
            entity.setDataPrecision(null);
            entity.setDataScale(null);
            entity.setColKey(false);
            entity.setNullable(true);
            entity.setDataDefault(null);

            return entity;
        };
    }

    @Override
    public List<String> validateSpecification(String tableName, String tableComment, List<DbColumn> columns) {
        List<String> errors = new ArrayList<>();

        // 1. 表名校验：推荐小写字母开头，仅允许小写字母、数字、下划线
        if (tableName == null || tableName.trim().isEmpty()) {
            errors.add("表名不能为空");
        } else {
            String tn = tableName.trim();
            if (!tn.matches("^[a-z][a-z0-9_]*$")) {
                errors.add("表名格式不符合Hive规范，建议以小写字母开头，且只能包含小写字母、数字和下划线");
            }
            if (tn.length() > 128) {
                errors.add("表名长度不能超过128个字符（Hive推荐限制）");
            }
        }

        // 2. 表注释建议提供
        if (tableComment == null || tableComment.trim().isEmpty()) {
            errors.add("建议填写表注释");
        }

        // 3. 字段列表校验
        if (columns == null || columns.isEmpty()) {
            errors.add("表必须至少包含一个字段");
        } else {
            for (DbColumn column : columns) {
                List<String> colErrors = validateHiveColumn(column);
                if (!colErrors.isEmpty()) {
                    errors.addAll(colErrors);
                }
            }
        }

        return errors;
    }

    private static List<String> validateHiveColumn(DbColumn column) {
        List<String> errors = new ArrayList<>();

        String colName = column.getColName();
        if (colName == null || colName.trim().isEmpty()) {
            errors.add("字段名不能为空");
        } else if (!colName.matches("^[a-z][a-z0-9_]*$")) {
            errors.add("字段名必须以小写字母开头，且只能包含小写字母、数字和下划线");
        }

        if (column.getDataType() == null || column.getDataType().trim().isEmpty()) {
            errors.add("字段 " + colName + " 的数据类型不能为空");
        }

        if (column.getColComment() == null || column.getColComment().trim().isEmpty()) {
            errors.add("字段 " + colName + " 的注释不能为空");
        }

        return errors;
    }

    @Override
    public List<String> someInternalSqlGenerator(DbQueryProperty dbQueryProperty, String tableName, String tableComment, List<DbColumn> dbColumnList) {
        String dbName = dbQueryProperty.getDbName();

        if (StringUtils.isNotEmpty(dbName)) {
            tableName = dbName + "." + tableName;
        }

        return generateHiveCreateSql(tableName, tableComment, dbColumnList);
    }

    private List<String> generateHiveCreateSql(String tableName, String tableComment, List<DbColumn> columns) {
        List<String> sqlList = new ArrayList<>();
        StringBuilder createSql = new StringBuilder();

        createSql.append("CREATE TABLE ").append(tableName).append(" (\n");

        for (int i = 0; i < columns.size(); i++) {
            DbColumn col = columns.get(i);
            createSql.append("  `").append(col.getColName()).append("` ");
            createSql.append(mapHiveColumnType(col));

            // Hive 不支持 NOT NULL 和 DEFAULT
            if (StringUtils.isNotEmpty(col.getColComment())) {
                createSql.append(" COMMENT '").append(MD5Util.escapeSingleQuotes(col.getColComment())).append("'");
            }

            if (i < columns.size() - 1) {
                createSql.append(",");
            }
            createSql.append("\n");
        }

        createSql.append(")");

        if (StringUtils.isNotEmpty(tableComment)) {
            createSql.append(" COMMENT '").append(MD5Util.escapeSingleQuotes(tableComment)).append("'");
        }

        createSql.append(" STORED AS ORC"); // 默认使用 ORC，可根据需求调整

        sqlList.add(createSql.toString());

        return sqlList;
    }

    private static String mapHiveColumnType(DbColumn col) {
        String type = col.getDataType().toUpperCase();

        Long length = MD5Util.getStringToLong(col.getDataLength());
        Long scale = MD5Util.getStringToLong(col.getDataScale());

        switch (type) {
            case "VARCHAR":
            case "VARCHAR2":
            case "CHAR":
            case "STRING":
            case "TEXT":
                return "STRING"; // Hive 无长度限制
            case "INT":
            case "INTEGER":
                return "INT";
            case "BIGINT":
                return "BIGINT";
            case "DECIMAL":
                return "DECIMAL(" + (length != null ? length : 10) + "," + (scale != null ? scale : 0) + ")";
            case "DOUBLE":
            case "FLOAT":
                return "DOUBLE";
            case "DATE":
            case "DATETIME":
            case "TIMESTAMP":
                return "TIMESTAMP";
            default:
                return "STRING"; // 默认用 STRING 容错
        }
    }

    @Override
    public String getDataStorageSize(String dbName) {
        return null;
    }

    @Override
    public String getInsertOrUpdateSql(String tableName, String where, String tableFieldName, String tableFieldValue, String setValue) {
        return null;
    }

    @Override
    public Boolean validConnection(DataSource dataSource, DbQueryProperty dbQueryProperty) {
        try (Connection conn = DriverManager.getConnection(
                trainToJdbcUrl(dbQueryProperty),
                dbQueryProperty.getUsername(),
                dbQueryProperty.getPassword())
        ) {
            // Hive JDBC 某些版本不实现 isValid，手动执行简单查询更保险
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("SELECT 1");
            }
            return true;
        } catch (SQLException e) {
            log.error("数据库连接验证失败: {}", e.getMessage(), e);
            throw new DataQueryException("数据库连接失败，请稍后重试");
        }
    }
}
