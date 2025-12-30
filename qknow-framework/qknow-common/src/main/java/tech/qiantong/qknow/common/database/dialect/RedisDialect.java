package tech.qiantong.qknow.common.database.dialect;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import tech.qiantong.qknow.common.database.constants.DbQueryProperty;
import tech.qiantong.qknow.common.database.core.DbColumn;
import tech.qiantong.qknow.common.database.core.DbName;
import tech.qiantong.qknow.common.database.core.DbTable;
import tech.qiantong.qknow.common.database.exception.DataQueryException;

import javax.sql.DataSource;
import java.time.Duration;
import java.util.List;

@Slf4j
public class RedisDialect extends AbstractDbDialect {
    @Override
    public RowMapper<DbTable> tableMapper() {
        return null;
    }

    @Override
    public RowMapper<DbColumn> columnMapper() {
        return null;
    }

    @Override
    public String columns(DbQueryProperty dbQueryProperty, String tableName) {
        return null;
    }

    @Override
    public String generateCheckTableExistsSQL(DbQueryProperty dbQueryProperty, String tableName) {
        return null;
    }

    @Override
    public List<String> someInternalSqlGenerator(DbQueryProperty dbQueryProperty, String tableName, String tableComment, List<DbColumn> dbColumnList) {
        return null;
    }

    @Override
    public List<String> validateSpecification(String tableName, String tableComment, List<DbColumn> columns) {
        return null;
    }

    @Override
    public String tables(DbQueryProperty dbQueryProperty) {
        return null;
    }

    @Override
    public String buildQuerySqlFields(List<DbColumn> columns, String tableName, DbQueryProperty dbQueryProperty) {
        return null;
    }

    @Override
    public String getDataStorageSize(String dbName) {
        return null;
    }

    @Override
    public String getDbName() {
        return null;
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
    public Boolean validConnection(DataSource dataSource, DbQueryProperty dbQueryProperty) {
        String host = dbQueryProperty.getHost();
        Integer port = dbQueryProperty.getPort();
        String password = dbQueryProperty.getPassword();
        String dbName = dbQueryProperty.getDbName();

        // 1. 构建 RedisURI（带超时）
        RedisURI uri = RedisURI.builder()
                .withHost(host)
                .withPort(port)
                .withPassword(
                        org.springframework.util.StringUtils.hasText(password)
                                ? password.toCharArray()
                                : null
                )
                .withDatabase(
                        org.springframework.util.StringUtils.hasText(dbName)
                                ? Integer.parseInt(dbName)
                                : 0
                )
                .withTimeout(Duration.ofSeconds(5)) // 超时时间
                .build();

        // 2. 创建 RedisClient（测试用，后面关闭）
        RedisClient client = null;
        StatefulRedisConnection<String, String> conn = null;

        try {
            client = RedisClient.create(uri);

            // 3. 连接 Redis
            conn = client.connect();

            // 4. 执行一个最轻量的命令：PING
            String pong = conn.sync().ping();

            return "PONG".equalsIgnoreCase(pong);

        } catch (Exception e) {
            throw new DataQueryException("Redis 连接失败，请检查参数");
        } finally {
            // 5. 关闭连接与 client
            if (conn != null) {
                try { conn.close(); } catch (Exception ignored) {}
            }
            if (client != null) {
                try { client.shutdown(); } catch (Exception ignored) {}
            }
        }
    }
}
