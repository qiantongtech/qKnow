package tech.qiantong.qknow.common.database.dialect;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.RowMapper;
import tech.qiantong.qknow.common.database.constants.DbQueryProperty;
import tech.qiantong.qknow.common.database.constants.fieldtypes.PhoenixDataTypeEnum;
import tech.qiantong.qknow.common.database.core.DbColumn;
import tech.qiantong.qknow.common.database.core.DbName;
import tech.qiantong.qknow.common.database.core.DbTable;
import tech.qiantong.qknow.common.database.exception.DataQueryException;
import tech.qiantong.qknow.common.database.utils.MD5Util;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Phoenix 数据库方言
 *
 * @author QianTongDC
 * @date 2022-11-14
 */
public class PhoenixDialect extends AbstractDbDialect {


    // 定义一个包含常见Phoenix保留关键字的集合（全部转换为大写便于比较）
    private static final String[] Phoenix_RESERVED_WORDS = {
            "ACCESSIBLE", "ADD", "ALL", "ALTER", "ANALYZE", "AND", "AS", "ASC", "ASENSITIVE",
            "BEFORE", "BETWEEN", "BIGINT", "BINARY", "BLOB", "BOTH", "BY", "CALL", "CASCADE",
            "CASE", "CHANGE", "CHAR", "CHARACTER", "CHECK", "COLLATE", "COLUMN", "CONDITION",
            "CONSTRAINT", "CONVERT", "CREATE", "CROSS", "CURRENT_DATE", "CURRENT_TIME",
            "CURRENT_TIMESTAMP", "CURRENT_USER", "DATABASE", "DATABASES", "DAY_HOUR",
            "DAY_MICROSECOND", "DAY_MINUTE", "DAY_SECOND", "DEC", "DECIMAL", "DEFAULT",
            "DELETE", "DESC", "DESCRIBE", "DETERMINISTIC", "DISTINCT", "DISTINCTROW",
            "DIV", "DOUBLE", "DROP", "DUAL", "ELSE", "ELSEIF", "EXISTS", "EXPLAIN", "FALSE",
            "FLOAT", "FLOAT4", "FLOAT8", "FOR", "FORCE", "FROM", "GROUP", "HAVING", "HIGH_PRIORITY",
            "IF", "IGNORE", "IN", "INDEX", "INNER", "INSERT", "INT", "INT1", "INT2", "INT3",
            "INT4", "INT8", "INTEGER", "INTERVAL", "INTO", "IS", "JOIN", "KEY", "KEYS",
            "LEADING", "LEFT", "LIKE", "LIMIT", "LINES", "LOAD", "LOCK", "LONG", "LONGBLOB",
            "LONGTEXT", "LOW_PRIORITY", "MATCH", "MAXVALUE", "MEDIUMBLOB", "MEDIUMINT",
            "MEDIUMTEXT", "MIDDLEINT", "MINUTE_MICROSECOND", "MINUTE_SECOND", "MOD", "MODIFIES",
            "NATURAL", "NOT", "NULL", "NUMERIC", "ON", "OPTIMIZE", "OPTION", "OR", "ORDER",
            "OUTER", "PARTITION", "PRECISION", "PRIMARY", "RANGE", "READ", "REGEXP",
            "RELEASE", "RENAME", "REPEAT", "REPLACE", "REQUIRE", "RESTRICT", "RETURN", "RIGHT",
            "RLIKE", "SCHEMA", "SCHEMAS", "SECOND_MICROSECOND", "SELECT", "SET", "SHOW",
            "SMALLINT", "SQL", "SQL_BIG_RESULT", "SQL_CALC_FOUND_ROWS", "SQL_SMALL_RESULT",
            "STARTING", "STORED", "STRAIGHT_JOIN", "TABLE", "TERMINATED", "THEN", "TINYBLOB",
            "TINYINT", "TINYTEXT", "TO", "TRAILING", "TRUE", "UNION", "UNIQUE", "UNLOCK",
            "UNSIGNED", "UPDATE", "USAGE", "USE", "USING", "UTC_DATE", "UTC_TIME",
            "UTC_TIMESTAMP", "VALUES", "VARBINARY", "VARCHAR", "VARCHARACTER", "VARYING",
            "VIRTUAL", "WHEN", "WHERE", "WITH", "WRITE", "XOR", "YEAR_MONTH", "ZEROFILL",
            // + Phoenix 特有
            "UPSERT", "UNSIGNED_LONG", "SALT", "SALT_BUCKETS", "ARRAY_LENGTH", "NEXT", "VALUE", "FOR"
    };

    @Override
    public String buildQuerySqlFields(List<DbColumn> columns, String tableName, DbQueryProperty dbQueryProperty) {
        // 如果没有传入字段，则默认使用 * 查询所有字段
        if (columns == null || columns.isEmpty()) {
            return "SELECT * FROM \"" + dbQueryProperty.getDbName() + "\".\"" + tableName + "\"";
        }

        // 对字段名进行保留字转义处理
        String fields = columns.stream()
                .map(column -> "\"" + escapeReservedKeyword(column.getColName()) + "\"")
                .collect(Collectors.joining(", "));

        // 构造最终查询语句（加上 schema + 表名的转义）
        return "SELECT " + fields + " FROM \"" + dbQueryProperty.getDbName() + "\".\"" + tableName + "\"";
    }

    @Override
    public String columns(DbQueryProperty dbQueryProperty, String tableName) {
        return "SELECT COLUMN_NAME AS COLNAME, " +
                "ORDINAL_POSITION AS COLPOSITION, " +
                "DATA_TYPE AS DATATYPE, " +
                "COLUMN_FAMILY, " +
                "NULLABLE " +
                "FROM SYSTEM.CATALOG " +
                "WHERE TABLE_SCHEM = '" + dbQueryProperty.getDbName() + "' " +
                "AND TABLE_NAME = '" + tableName + "' " +
                "AND COLUMN_NAME IS NOT NULL " +
                "ORDER BY ORDINAL_POSITION";
    }

    @Override
    public RowMapper<DbColumn> columnMapper() {
        return (ResultSet rs, int rowNum) -> {
            DbColumn entity = new DbColumn();
            entity.setColName(rs.getString("COLNAME"));
            entity.setDataType(PhoenixDataTypeEnum.fromCode(rs.getInt("DATATYPE")));
            entity.setDataLength(null); // Phoenix 无该字段
            entity.setDataPrecision(null);
            entity.setDataScale(null);
            entity.setColKey(false); // 无法获取主键信息
            entity.setNullable(rs.getInt("NULLABLE") == 1);
            entity.setColPosition(rs.getInt("COLPOSITION"));
            entity.setDataDefault(null); // 无默认值支持
            entity.setColComment(null);  // 无注释支持
            return entity;
        };
    }

    @Override
    public String generateCheckTableExistsSQL(DbQueryProperty dbQueryProperty, String tableName) {
        return "SELECT COUNT(*) " +
                "FROM SYSTEM.CATALOG " +
                "WHERE TABLE_SCHEM = '" + dbQueryProperty.getDbName() + "' " +
                "AND TABLE_NAME = '" + tableName + "' " +
                "AND TABLE_TYPE = 'u'";
    }

    /**
     * 没有表备注
     * @param dbQueryProperty
     * @return
     */
    @Override
    public String tables(DbQueryProperty dbQueryProperty) {
        return "SELECT TABLE_NAME AS TABLENAME FROM SYSTEM.CATALOG " +
                "WHERE TABLE_SCHEM = '" + dbQueryProperty.getDbName() + "' " +
                "AND TABLE_TYPE = 'u'";
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
    public Boolean validConnection(DataSource dataSource, DbQueryProperty dbQueryProperty) {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("SELECT 1");
            return true;
        } catch (SQLException e) {
            throw new DataQueryException("数据库连接失败，稍后重试");
        }
    }

    @Override
    public String getDbName() {
        throw new UnsupportedOperationException("Phoenix 不支持通过 SQL 获取当前 schema，请从配置中获取");
    }

    @Override
    public String getDbName(DbName dbName) {
        return null;
    }

    @Override
    public String getInsertOrUpdateSql(String tableName, String where, String tableFieldName, String tableFieldValue, String setValue) {
        return null;
    }

    @Override
    public List<String> validateSpecification(String tableName, String tableComment, List<DbColumn> columns) {
        return null;
    }

    @Override
    public String getDataStorageSize(String dbName) {
        return null;
    }


    @Override
    public List<String> someInternalSqlGenerator(DbQueryProperty dbQueryProperty, String tableName, String tableComment, List<DbColumn> dbColumnList) {
        List<String> sqlList = new ArrayList<>();
        List<String> primaryKeys = new ArrayList<>();
        StringBuilder sql = new StringBuilder();

        sql.append("CREATE TABLE \"").append(dbQueryProperty.getDbName()).append("\".\"").append(tableName).append("\" (\n");

        for (DbColumn column : dbColumnList) {
            String columnType = column.getDataType();
            String colName = column.getColName();

            sql.append("  ").append(escapeReservedKeyword(colName)).append(" ");

            switch (columnType.toUpperCase()) {
                case "VARCHAR":
                case "VARCHAR2":
                case "TEXT":
                    sql.append("VARCHAR");
                    break;
                case "CHAR":
                    sql.append("CHAR");
                    break;
                case "INT":
                case "INTEGER":
                    sql.append("INTEGER");
                    break;
                case "BIGINT":
                    sql.append("BIGINT");
                    break;
                case "TINYINT":
                    sql.append("TINYINT");
                    break;
                case "DECIMAL":
                    sql.append(generateColumnSQLPhoenix("DECIMAL", column.getDataLength(), column.getDataScale(), 65, 30));
                    break;
                case "FLOAT":
                    sql.append("FLOAT");
                    break;
                case "DOUBLE":
                    sql.append("DOUBLE");
                    break;
                case "DATE":
                    sql.append("DATE");
                    break;
                case "DATETIME":
                case "TIMESTAMP":
                    sql.append("TIMESTAMP");
                    break;
                default:
                    sql.append("VARCHAR");
                    break;
            }

            if (!column.getNullable()) {
                sql.append(" NOT NULL");
            }

            if (Boolean.TRUE.equals(column.getColKey())) {
                primaryKeys.add(escapeReservedKeyword(colName));
            }

            sql.append(",\n");
        }

        // 去掉最后一行的逗号
        sql.setLength(sql.length() - 2);

        // 添加主键约束（Phoenix 建表必须有主键）
        if (!primaryKeys.isEmpty()) {
            sql.append(",\n  CONSTRAINT pk PRIMARY KEY (")
                    .append(String.join(", ", primaryKeys))
                    .append(")");
        } else {
            sql.append(",\n  CONSTRAINT pk PRIMARY KEY (")
                    .append(escapeReservedKeyword(dbColumnList.get(0).getColName()))
                    .append(")");
        }

        sql.append("\n)");

        sqlList.add(sql.toString());
        return sqlList;
    }


    public static String escapeReservedKeyword(String colName) {
        if (colName == null || colName.isEmpty()) return colName;
        for (String reserved : Phoenix_RESERVED_WORDS) {
            if (reserved.equalsIgnoreCase(colName)) {
                return "\"" + colName + "\"";
            }
        }
        return "\"" + colName + "\""; // Phoenix 推荐全字段加引号
    }
    public static String generateColumnSQLPhoenix(String columnType, String columnLength, String columnScale, int maxLength, int maxScale) {
        StringBuilder sql = new StringBuilder(columnType);

        // 仅当是需要长度和小数位数的类型时，才处理长度
        if (columnType.equalsIgnoreCase("DECIMAL") || columnType.equalsIgnoreCase("FLOAT")) {
            if (StringUtils.isNotEmpty(columnLength)) {
                int length = NumberUtils.toInt(columnLength, 18); // 防止 NumberFormatException
                // 限制长度不超过最大长度
                if (length > maxLength) {
                    length = maxLength;
                }

                sql.append("(").append(length);

                // 如果列类型是 DECIMAL 并且提供了小数位数，则附加小数位
                if (columnType.equalsIgnoreCase("DECIMAL") && StringUtils.isNotEmpty(columnScale)) {
                    int scale = Integer.parseInt(columnScale);
                    // 限制小数位数不超过最大值
                    if (scale > maxScale) {
                        scale = maxScale;
                    }
                    sql.append(", ").append(scale);
                }

                sql.append(")");
            }
        }

        return sql.toString();
    }


//---------------------------------------------------------------------------------------------------------------------------------------------------


}
