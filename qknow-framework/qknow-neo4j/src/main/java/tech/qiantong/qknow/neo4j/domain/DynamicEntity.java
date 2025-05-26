package tech.qiantong.qknow.neo4j.domain;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.neo4j.core.schema.*;
import tech.qiantong.qknow.neo4j.domain.relationship.DynamicEntityRelationship;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 动态实体
 */
@Data
@EqualsAndHashCode(exclude = {"relationshipEntityMap"}) // 排除 relationshipEntityMap 字段
@Node
public class DynamicEntity {
    @Id
    @GeneratedValue
    Long id;

    @Property(name = "name")
    private String name;

    // 动态节点
    @DynamicLabels
    private Set<String> labels = Sets.newHashSet();

    // 动态属性
    @CompositeProperty(prefix = "", delimiter = "_")
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

    // 获取实体的属性
    public Object getProperty(String key) {
        return dynamicProperties.get(key);
    }

    // 合并 relationshipEntityMap
    public void mergeRelationshipEntityMap(Map<String, List<DynamicEntityRelationship>> otherMap) {
        for (Map.Entry<String, List<DynamicEntityRelationship>> entry : otherMap.entrySet()) {
            String key = entry.getKey();
            List<DynamicEntityRelationship> value = entry.getValue();
            relationshipEntityMap.computeIfAbsent(key, k -> Lists.newArrayList()).addAll(value);
        }
    }
}
