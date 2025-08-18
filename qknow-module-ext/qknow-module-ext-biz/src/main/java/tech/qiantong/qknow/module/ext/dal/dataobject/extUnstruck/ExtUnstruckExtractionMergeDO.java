package tech.qiantong.qknow.module.ext.dal.dataobject.extUnstruck;

import lombok.Data;

public class ExtUnstruckExtractionMergeDO {

    @Data
    public static class Node{
        private String name;
        private Long task_id;
        private Integer doc_id;
        private Integer paragraphIndex;
        private Integer entity_type;
    }

//    @Data
//    public static class CreateRelationshipNode{
//        private String name;
//        private Long docId;
//        private Long paragraphIndex;
//        private Long task_id;
//        private Integer entity_type;
//    }
}
