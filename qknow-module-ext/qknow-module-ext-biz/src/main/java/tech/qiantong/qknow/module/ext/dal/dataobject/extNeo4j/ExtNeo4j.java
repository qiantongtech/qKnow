package tech.qiantong.qknow.module.ext.dal.dataobject.extNeo4j;

import lombok.Data;

/**
 * 结构化抽取任务操作neo4j
 */
public class ExtNeo4j {

    /**
     * 修改关系-结构化抽取
     */
    @Data
    public static class UpdateRelationship {
        private Long relationshipId;//旧关系id
        private String relationship;//关系
        private Long startId;
        private Long endId;
        private String startTableName;
        private String endTableName;
        private Integer extractType;//1结构化抽取 2非结构化抽取
        private Long taskId;
    }

    /**
     * 删除关系
     */
    @Data
    public static class DeleteRelationship {
        private Long relationshipId;//关系id
    }
}
