package tech.qiantong.qknow.common.database.dialect;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import tech.qiantong.qknow.common.database.constants.DbQueryProperty;
import tech.qiantong.qknow.common.database.core.DbColumn;
import tech.qiantong.qknow.common.database.core.DbTable;
import tech.qiantong.qknow.common.database.exception.DataQueryException;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Slf4j
public class RabbitMQDialect extends AbstractDbDialect {

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
    public String getInsertOrUpdateSql(String tableName, String where, String tableFieldName, String tableFieldValue, String setValue) {
        return null;
    }

    @Override
    public Boolean validConnection(DataSource dataSource, DbQueryProperty dbQueryProperty) {
        ConnectionFactory factory = new ConnectionFactory();

        // 固定字段
        factory.setHost(dbQueryProperty.getHost());
        factory.setPort(dbQueryProperty.getPort());
        factory.setUsername(dbQueryProperty.getUsername());
        factory.setPassword(dbQueryProperty.getPassword());

        Map<String, Object> config = dbQueryProperty.getConfig();
        if (config != null && !config.isEmpty()) {
            config.forEach((k, v) -> {
                switch (k) {
                    case "virtualHost":
                    case "vhost":
                        factory.setVirtualHost(String.valueOf(v));
                        break;
                    case "connectionTimeout":
                        factory.setConnectionTimeout(Integer.parseInt(String.valueOf(v)));
                        break;
                    case "handshakeTimeout":
                        factory.setHandshakeTimeout(Integer.parseInt(String.valueOf(v)));
                        break;
                    case "requestedHeartbeat":
                        factory.setRequestedHeartbeat(Integer.parseInt(String.valueOf(v)));
                        break;
                    // 其他所有参数未来扩展在这里继续加即可
                }
            });
        }
        if (factory.getVirtualHost() == null) factory.setVirtualHost("/");
        Connection conn = null;
        Channel channel = null;
        try {
            conn = factory.newConnection();
            channel = conn.createChannel();
            channel.queueDeclare("", false, true, true, null);
            return true;
        } catch (Exception e) {
            throw new DataQueryException("RabbitMQ 连接失败, 稍后重试");
        } finally {
            try {
                if (channel != null) channel.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                throw new DataQueryException("关闭 RabbitMQ 连接出错");
            }
        }
    }
}
