package tech.qiantong.qknow.common.database.core;

import lombok.Data;

@Data
public class DbTable {

    /**
     * 表名
     */
    private String tableName;

    /**
     * 表注释
     */
    private String tableComment;
}
