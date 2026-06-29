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
