package tech.qiantong.qknow.common.database.constants;

import tech.qiantong.qknow.common.database.exception.DataQueryException;

/**
 * 数据库类型
 *
 * @author QianTongDC
 * @date 2022-11-14
 */
public enum DbType {

    /**
     * MYSQL
     */
    MYSQL("MySql", "MySql数据库", "jdbc:mysql://${host}:${port}/${dbName}?useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8", "LENGTH"),
    /**
     * MARIADB
     */
    MARIADB("2", "MariaDB数据库", "jdbc:mariadb://${host}:${port}/${dbName}", "CHAR_LENGTH"),
    /**
     * ORACLE
     */
    ORACLE("Oracle11", "Oracle11g及以下数据库", "jdbc:oracle:thin:@${host}:${port}:${sid}", "LENGTH"),
    /**
     * oracle12c new pagination
     */
    ORACLE_12C("Oracle", "Oracle12c+数据库", "jdbc:oracle:thin:@${host}:${port}:${sid}", "LENGTH"),
    /**
     * POSTGRESQL
     */
    POSTGRE_SQL("5", "PostgreSql数据库", "jdbc:postgresql://${host}:${port}/${dbName}", "LENGTH"),
    /**
     * SQLSERVER2005
     */
    SQL_SERVER2008("6", "SQLServer2008及以下数据库", "jdbc:sqlserver://${host}:${port};DatabaseName=${dbName}", "LEN"),
    /**
     * SQLSERVER
     */
    SQL_SERVER("7", "SQLServer2012+数据库", "jdbc:sqlserver://${host}:${port};DatabaseName=${dbName}", "LEN"),
    /**
     * UNKONWN DB
     */
    OTHER("8", "其他数据库", "", "LENGTH"),
    /**
     * 达梦8
     */
    DM8("DM8", "达梦8", "jdbc:dm://${host}:${port}?schema=${dbName}", "LENGTH"),
    /**
     * 人大金仓数据库
     */
    KINGBASE8("Kingbase8", "人大金仓数据库", "jdbc:kingbase8://${host}:${port}/${dbName}", "LENGTH");


    /**
     * 数据库名称
     */
    private final String db;

    /**
     * 描述
     */
    private final String desc;

    /**
     * url
     */
    private final String url;

    /**
     * lengthFun
     */
    private final String lengthFun;

    public String getLengthFun() {
        return lengthFun;
    }

    public String getDb() {
        return this.db;
    }

    public String getDesc() {
        return this.desc;
    }

    public String getUrl() {
        return this.url;
    }

    DbType(String db, String desc, String url, String lengthFun) {
        this.db = db;
        this.desc = desc;
        this.url = url;
        this.lengthFun = lengthFun;
    }

    /**
     * 获取数据库类型
     *
     * @param dbType 数据库类型字符串
     */
    public static DbType getDbType(String dbType) {
        for (DbType type : DbType.values()) {
            if (type.db.equals(dbType)) {
                return type;
            }
        }
        throw new DataQueryException("不支持的数据库类型");
    }
}
