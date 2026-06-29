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

package tech.qiantong.qknow.common.database.datasource;


import cn.hutool.db.ds.simple.SimpleDataSource;
import com.mongodb.client.MongoClients;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;
import tech.qiantong.qknow.common.database.DataSourceFactory;
import tech.qiantong.qknow.common.database.DbDialect;
import tech.qiantong.qknow.common.database.DbQuery;
import tech.qiantong.qknow.common.database.DialectFactory;
import tech.qiantong.qknow.common.database.constants.DbQueryProperty;
import tech.qiantong.qknow.common.database.constants.DbType;
import tech.qiantong.qknow.common.database.exception.DataQueryException;
import tech.qiantong.qknow.common.database.query.AbstractDbQueryFactory;
import tech.qiantong.qknow.common.database.query.CacheDbQueryFactoryBean;

import javax.sql.DataSource;

public abstract class AbstractDataSourceFactory implements DataSourceFactory {

    @Override
    public DbQuery createDbQuery(DbQueryProperty property) {
        property.viald();
        DbType dbType = DbType.getDbType(property.getDbType());
        DataSource dataSource = null;
        //判断不为kafka
        if (!dbType.getDb().equals(DbType.KAFKA.getDb())
                && !dbType.getDb().equals(DbType.MONGODB.getDb())
                && !dbType.getDb().equals(DbType.REDIS.getDb())
                && !dbType.getDb().equals(DbType.RABBITMQ.getDb())
                && !dbType.getDb().equals(DbType.HDFS.getDb())
                && !dbType.getDb().equals(DbType.FTP.getDb())
                && !dbType.getDb().equals(DbType.OSS_ALIYUN.getDb())) {
            dataSource = createDataSource(property);
        }
        DbQuery dbQuery = createDbQuery(property, dataSource, dbType);
        return dbQuery;
    }

    public DbQuery createDbQuery(DbQueryProperty dbQueryProperty, DataSource dataSource, DbType dbType) {
        DbDialect dbDialect = DialectFactory.getDialect(dbType);
        if (dbDialect == null) {
            throw new DataQueryException("该数据库类型正在开发中");
        }
        AbstractDbQueryFactory dbQuery = new CacheDbQueryFactoryBean();
        dbQuery.setDbQueryProperty(dbQueryProperty);
        dbQuery.setDataSource(dataSource);
        dbQuery.setDbDialect(dbDialect);
        //判断不为kafka
        if (!dbType.getDb().equals(DbType.KAFKA.getDb())
                && !dbType.getDb().equals(DbType.HDFS.getDb())
                && !dbType.getDb().equals(DbType.MONGODB.getDb())
                && !dbType.getDb().equals(DbType.REDIS.getDb())
                && !dbType.getDb().equals(DbType.RABBITMQ.getDb())
                && !dbType.getDb().equals(DbType.FTP.getDb())
                && !dbType.getDb().equals(DbType.OSS_ALIYUN.getDb())) {
            dbQuery.setJdbcTemplate(new JdbcTemplate(dataSource));
        } else if (dbType.getDb().equals(DbType.MONGODB.getDb())) {
            dbQuery.setMongoClient(MongoClients.create(dbQueryProperty.trainToJdbcUrl()));
        } else if (dbType.getDb().equals(DbType.REDIS.getDb())) {
            RedisURI uri = RedisURI.builder()
                    .withHost(dbQueryProperty.getHost())
                    .withPort(dbQueryProperty.getPort())
                    .withPassword(StringUtils.hasText(dbQueryProperty.getPassword()) ?
                            dbQueryProperty.getPassword().toCharArray() : null)
                    .withDatabase(Integer.parseInt(dbQueryProperty.getDbName()))
                    .build();
            RedisClient client = RedisClient.create(uri);
            StatefulRedisConnection<String, String> conn = client.connect();
            dbQuery.setRedisConnection(conn);
            dbQuery.setRedisClient(client);
        }
        return dbQuery;
    }

    public DataSource createDataSource(DbQueryProperty property) {
        SimpleDataSource dataSource = null;
        if (DbType.HIVE.getDb().equals(property.getDbType())) {
            dataSource = new SimpleDataSource(property.trainToJdbcUrl(), property.getUsername(), property.getPassword(), "org.apache.hive.jdbc.HiveDriver");
        } else if (DbType.DB2.getDb().equals(property.getDbType())) {
            dataSource = new SimpleDataSource(property.trainToJdbcUrl(), property.getUsername(), property.getPassword(), "com.ibm.db2.jcc.DB2Driver");
        } else {
            dataSource = new SimpleDataSource(property.trainToJdbcUrl(), property.getUsername(), property.getPassword());
        }
        return dataSource;
    }
}
