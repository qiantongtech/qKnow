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

package tech.qiantong.qknow.module.app.entity;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.*;
import tech.qiantong.qknow.module.app.relationship.AppGraphEntityRelationship;
import tech.qiantong.qknow.neo4j.domain.BaseNeo4jEntity;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 动态实体
 */
@Data
@Node("AppGraphEntity")
public class AppGraphEntity extends BaseNeo4jEntity {
    @Id
    @GeneratedValue
    Long id;

    @Property(name = "name")
    private String name;

    // 动态节点
    @DynamicLabels
    private Set<String> labels = Sets.newHashSet();

    // 动态属性
    @CompositeProperty(prefix = "", delimiter = "")
    private Map<String, Object> dynamicProperties = Maps.newHashMap();

    // 建立关系
    @Relationship(direction = Relationship.Direction.OUTGOING)
    private Map<String, List<AppGraphEntityRelationship>> relationshipEntityMap;

    public void addLabels(String label) {
        this.labels.add(label);
    }

    public void putDynamicProperties(String key, Object value) {
        this.dynamicProperties.put(key, value);
    }

    public void addRelationship(String relationshipName, AppGraphEntity endNode) {
        AppGraphEntityRelationship relationship = new AppGraphEntityRelationship();
        relationship.setEndNode(endNode);
        if (this.relationshipEntityMap == null) {
            this.relationshipEntityMap = Maps.newHashMap();
        }
        List<AppGraphEntityRelationship> relationshipList = this.relationshipEntityMap.get(relationshipName);
        if (relationshipList == null) {
            relationshipList = Lists.newArrayList();
        }
        relationshipList.add(relationship);
        this.relationshipEntityMap.put(relationshipName, relationshipList);
    }


}
