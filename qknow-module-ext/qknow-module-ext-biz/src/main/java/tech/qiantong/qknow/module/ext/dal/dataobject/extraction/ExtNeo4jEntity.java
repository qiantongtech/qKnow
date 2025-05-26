package tech.qiantong.qknow.module.ext.dal.dataobject.extraction;

import lombok.Data;


public class ExtNeo4jEntity {

    @Data
    public static class Entity {
        private String name;
        private Long id;
        private String taskId;
        private String docId;
        private String paragraphIndex;

        public Entity(String name, Long id, String taskId, String docId, String paragraphIndex) {
            this.name = name;
            this.id = id;
            this.taskId = taskId;
            this.docId = docId;
            this.paragraphIndex = paragraphIndex;
        }
    }

    // 定义关系类
    @Data
    public static class Relationship {
        private Long id;
        private Long startId;
        private String startName;
        private Long endId;
        private String endName;
        private String relationType;

        public Relationship(Long id, Long startId, String startName, Long endId, String endName, String relationType) {
            this.id = id;
            this.startId = startId;
            this.startName = startName;
            this.endId = endId;
            this.endName = endName;
            this.relationType = relationType;
        }
    }

}
