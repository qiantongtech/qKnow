/*
 * Copyright © 2026 Qiantong Technology Co., Ltd.
 * qKnow Knowledge Platform
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please apply for brand customization authorization via official channels.
 *  *
 * More information: https://qknow.qiantong.tech/business.html
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2026 江苏千桐科技有限公司
 * qKnow 知识平台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qknow.qiantong.tech/business.html
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
