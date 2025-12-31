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

package tech.qiantong.qknow.module.ext.dal.dataobject.extExtraction;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.*;
import tech.qiantong.qknow.neo4j.domain.BaseNeo4jEntity;
import tech.qiantong.qknow.neo4j.domain.DynamicEntity;
import tech.qiantong.qknow.neo4j.domain.relationship.DynamicEntityRelationship;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@Node("ExtExtraction")
public class ExtExtraction extends BaseNeo4jEntity {
    @Id
    @GeneratedValue
    Long id;

    @Property(name = "name")
    private String name;

    @Property(name = "task_id")
    private String task_id;

    @Property(name = "extract_type")
    private String extract_type;

    @Property(name = "release_status")
    private String release_status;

    // 动态节点
    @DynamicLabels
    private Set<String> labels = Sets.newHashSet();

    // 动态属性
    @CompositeProperty(prefix = "", delimiter = "")
    private Map<String, Object> dynamicProperties = Maps.newHashMap();

    // 建立关系
    @Relationship(direction = Relationship.Direction.OUTGOING)
    private Map<String, List<DynamicEntityRelationship>> relationshipEntityMap;

    public void addLabels(String label) {
        this.labels.add(label);
    }

    public void putDynamicProperties(String key, Object value) {
        this.dynamicProperties.put(key, value);
    }

    public void addRelationship(String relationshipName, DynamicEntity endNode) {
        DynamicEntityRelationship relationship = new DynamicEntityRelationship();
        relationship.setEndNode(endNode);
        if (this.relationshipEntityMap == null) {
            this.relationshipEntityMap = Maps.newHashMap();
        }
        List<DynamicEntityRelationship> relationshipList = this.relationshipEntityMap.get(relationshipName);
        if (relationshipList == null) {
            relationshipList = Lists.newArrayList();
        }
        relationshipList.add(relationship);
        this.relationshipEntityMap.put(relationshipName, relationshipList);
    }
}
