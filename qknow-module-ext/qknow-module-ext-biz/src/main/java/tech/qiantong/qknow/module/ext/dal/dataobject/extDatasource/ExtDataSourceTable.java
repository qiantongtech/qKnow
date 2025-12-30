package tech.qiantong.qknow.module.ext.dal.dataobject.extDatasource;

import lombok.Data;

@Data
public class ExtDataSourceTable {
    private Long id;
    private Long dataId;
    private String primaryKey;
    private Object primaryKeyData;
    private String tableName;
    private String databaseName;

    @Data
    public static class GetTableData{
        private String query;
        private String url;
        private String username;
        private String password;
        private String dbType;

        private String tableA;
        private Integer afieldNum;
        private String tableB;
    }

    @Data
    public static class GetTableDataByDataId{
        private Long databaseId;
        private Long dataId;
        private String tableName;
    }
}
