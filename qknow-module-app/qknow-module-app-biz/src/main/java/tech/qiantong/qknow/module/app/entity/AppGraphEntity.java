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
