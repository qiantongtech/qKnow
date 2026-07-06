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
