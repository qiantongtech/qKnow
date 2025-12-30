package tech.qiantong.qknow.common.database.dialect;


import lombok.extern.slf4j.Slf4j;
import tech.qiantong.qknow.common.database.DbDialect;
import tech.qiantong.qknow.common.database.constants.DbType;

import java.util.EnumMap;
import java.util.Map;

@Slf4j
public class DialectRegistry {

    private final Map<DbType, DbDialect> dialect_enum_map = new EnumMap<>(DbType.class);

    public DialectRegistry() {
        dialect_enum_map.put(DbType.MARIADB, new MariaDBDialect());
        dialect_enum_map.put(DbType.MYSQL, new MySqlDialect());
        dialect_enum_map.put(DbType.ORACLE_12C, new Oracle12cDialect());
        dialect_enum_map.put(DbType.ORACLE, new OracleDialect());
        dialect_enum_map.put(DbType.POSTGRE_SQL, new PostgreDialect());
        dialect_enum_map.put(DbType.SQL_SERVER2008, new SQLServer2008Dialect());
        dialect_enum_map.put(DbType.SQL_SERVER, new SQLServerDialect());
        dialect_enum_map.put(DbType.DM8, new DM8Dialect());
        dialect_enum_map.put(DbType.OTHER, new UnknownDialect());
        dialect_enum_map.put(DbType.KINGBASE8, new Kingbase8Dialect());
        dialect_enum_map.put(DbType.KAFKA, new KafkaDialect());
        dialect_enum_map.put(DbType.HIVE, new HiveDialect());
        dialect_enum_map.put(DbType.HDFS, new HdfsDialect());
        dialect_enum_map.put(DbType.DORIS, new DorisDialect());
        dialect_enum_map.put(DbType.PHOENIX, new PhoenixDialect());
        dialect_enum_map.put(DbType.FTP, new FtpDialect());
        dialect_enum_map.put(DbType.OSS_ALIYUN, new OSSAliyunDialect());
        dialect_enum_map.put(DbType.CLICK_HOUSE, new ClickHouseDialect());
        if (isMongoDBAvailable()) {
            dialect_enum_map.put(DbType.MONGODB, new MongoDBDialect());
        }
        dialect_enum_map.put(DbType.REDIS, new RedisDialect());
        dialect_enum_map.put(DbType.RABBITMQ, new RabbitMQDialect());
        dialect_enum_map.put(DbType.DB2, new DB2Dialect());
        dialect_enum_map.put(DbType.OSCAR, new OSCARDialect());
    }

    public DbDialect getDialect(DbType dbType) {
        return dialect_enum_map.get(dbType);
    }

    public static boolean isMongoDBAvailable() {
        try {
            Class.forName("org.bson.conversions.Bson");
            Class.forName("com.mongodb.client.MongoClient");
            return true;
        } catch (ClassNotFoundException e) {
            log.info("无Mongo依赖");
            return false;
        }
    }
}
