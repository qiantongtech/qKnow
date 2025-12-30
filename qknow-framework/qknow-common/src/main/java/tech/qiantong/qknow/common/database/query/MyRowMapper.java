package tech.qiantong.qknow.common.database.query;

import com.kingbase8.util.KBobject;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.LinkedCaseInsensitiveMap;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * <P>
 * 用途:
 * </p>
 *
 * @author: FXB
 * @create: 2025-07-16 09:56
 **/
public class MyRowMapper implements RowMapper<Map<String, Object>> {
    @Override
    public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
        ResultSetMetaData meta = rs.getMetaData();
        int columnCount = meta.getColumnCount();
        Map<String, Object> row = this.createColumnMap(columnCount);
        for (int i = 1; i <= columnCount; i++) {
            String colName = meta.getColumnLabel(i);
            Object value = rs.getObject(i);
            if (value instanceof KBobject) {
                value = ((KBobject) value).getValue();
            } else {
                value = this.getColumnValue(rs, i);
            }

            if (value instanceof Map) {
                String digits = value.toString().replaceAll("\\D", "");
                if (!digits.isEmpty()) {
                    value = Long.parseLong(digits);
                }
            }

            if (value instanceof BigInteger) {
                value = ((BigInteger) value).longValue();
            }
            if (value instanceof BigDecimal) {
                value = ((BigDecimal) value).doubleValue();
            }

            if (value instanceof Number) {
                value = ((Number) value).longValue();
            }

            row.putIfAbsent(colName, value);
        }
        return row;
    }

    protected Map<String, Object> createColumnMap(int columnCount) {
        return new LinkedCaseInsensitiveMap(columnCount);
    }

    protected String getColumnKey(String columnName) {
        return columnName;
    }

    @Nullable
    protected Object getColumnValue(ResultSet rs, int index) throws SQLException {
        return JdbcUtils.getResultSetValue(rs, index);
    }
}
