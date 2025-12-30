package tech.qiantong.qknow.common.database;


import org.springframework.stereotype.Component;
import tech.qiantong.qknow.common.database.constants.DbQueryProperty;

public interface DataSourceFactory {

    /**
     * 创建数据源实例
     *
     * @param property
     * @return
     */
    DbQuery createDbQuery(DbQueryProperty property);
}
