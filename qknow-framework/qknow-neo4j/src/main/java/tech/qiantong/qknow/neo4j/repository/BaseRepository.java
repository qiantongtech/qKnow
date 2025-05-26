package tech.qiantong.qknow.neo4j.repository;

import org.neo4j.driver.internal.InternalNode;
import org.neo4j.driver.internal.value.NodeValue;
import org.neo4j.driver.types.Relationship;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.common.utils.spring.SpringUtils;
import tech.qiantong.qknow.neo4j.domain.DynamicEntity;
import tech.qiantong.qknow.neo4j.wrapper.Neo4jBuildWrapper;
import tech.qiantong.qknow.neo4j.wrapper.Neo4jQueryWrapper;

import java.util.*;
import java.util.stream.Collectors;

@NoRepositoryBean  // 关键注解：阻止Spring实例化此基础仓库
public interface BaseRepository<T, ID> extends Neo4jRepository<T, ID> {

    /**
     * 根据查询条件查询
     *
     * @param wrapper
     * @return
     */
    default List<T> find(Neo4jQueryWrapper<T> wrapper) {
        Neo4jTemplate neo4jTemplate = SpringUtils.getBean(Neo4jTemplate.class);
        String cypherQuery = wrapper.getCypherQuery();
        Map<String, Object> params = wrapper.getParams();
        return neo4jTemplate.findAll(cypherQuery, params, wrapper.getEntityClass());
    }

    /**
     * 分页查询
     *
     * @param wrapper
     * @return
     */
    default PageResult<T> findPage(Neo4jQueryWrapper<T> wrapper) {
        Neo4jTemplate neo4jTemplate = SpringUtils.getBean(Neo4jTemplate.class);
        String cypherQuery = wrapper.getCypherQuery();
        Map<String, Object> params = wrapper.getParams();
        List<T> row = neo4jTemplate.findAll(cypherQuery, params, wrapper.getEntityClass());
        Long count = this.count(wrapper);
        return new PageResult<>(row, count);
    }

    default List<T> relationChain(Neo4jQueryWrapper<T> wrapper) {
        Neo4jTemplate neo4jTemplate = SpringUtils.getBean(Neo4jTemplate.class);
        String cypherQuery = wrapper.getRelationChainCypherQuery();
        Map<String, Object> params = wrapper.getParams();
         return neo4jTemplate.findAll(cypherQuery, params, wrapper.getEntityClass());
    }

    /**
     * 获取节点list, 所有属性
     *
     * @param wrapper
     * @return
     */
    default List<Map<String, Object>> nodeListMap(Neo4jQueryWrapper<T> wrapper, Set<String> labels) {
        Neo4jClient neo4jClient = SpringUtils.getBean(Neo4jClient.class);
        List<Map<String, Object>> collect = neo4jClient.query(wrapper.getNodeQuery(labels))
                .bindAll(wrapper.getParams())
                .fetchAs(NodeValue.class)  // 获取 Node 对象
                .all()
                .stream()
                .map(nodeValue -> {
                    HashMap<String, Object> properties = new HashMap<>();
                    InternalNode node = (InternalNode) nodeValue.asNode();
                    properties.put("id", node.id());
                    // 获取节点的属性
                    node.asMap().forEach((key, value) -> properties.put(key, value));
                    return properties;
                })
                .collect(Collectors.toList());
        return collect;
    }

    /**
     * 根据条件查询关系
     *
     * @param wrapper
     * @return
     */
    default List<Map<String, Object>> relListMap(Neo4jQueryWrapper<T> wrapper, Set<String> labels) {
        Neo4jClient neo4jClient = SpringUtils.getBean(Neo4jClient.class);
        Collection<Map<String, Object>> result = neo4jClient.query(wrapper.getRelQuery(labels))
                .bindAll(wrapper.getParams())
                .fetch()  // 删除 fetchAs(Record.class)，直接使用默认映射
                .all();
        ArrayList<Map<String, Object>> relationships = new ArrayList<>();
        for (Map<String, Object> objectMap : result) {
            InternalNode n = (InternalNode) objectMap.get("n");
            InternalNode m = (InternalNode) objectMap.get("m");
            Relationship r = (Relationship) objectMap.get("r");

            if (m != null) {
                Map<String, Object> map = m.asMap();
                HashMap<String, Object> relationshipMap = new HashMap<>();
                relationshipMap.put("startId", n.id());
                relationshipMap.put("endId", m.id());
                relationshipMap.put("startName", n.asMap().get("name"));
                relationshipMap.put("endName", map.get("name"));
                relationshipMap.put("id", r.id());
                relationshipMap.put("relationType", r.type());
                relationships.add(relationshipMap);
            }
        }
        return relationships;
    }

    /**
     * 根据条件修改节点属性
     *
     * @param label
     * @param paramMap
     * @param updateMap
     * @return
     */
    default List<T> updateQuery(Neo4jBuildWrapper<T> wrapper, String label, Map<String, Object> paramMap, Map<String, Object> updateMap) {
        Neo4jTemplate neo4jTemplate = SpringUtils.getBean(Neo4jTemplate.class);
        String query = wrapper.updateQuery(label, paramMap, updateMap);
        return neo4jTemplate.findAll(query, wrapper.getEntityClass());
    }

    /**
     * 如果节点存在, 更新此节点的属性, 如果节点不存在, 创建节点和属性
     *
     * @param wrapper
     * @param mergeMap
     * @param attributeMap
     * @return
     */
    default List<T> mergeCreateNode(String label, Neo4jBuildWrapper<T> wrapper, Map<String, Object> mergeMap, Map<String, Object> attributeMap) {
        Neo4jTemplate neo4jTemplate = SpringUtils.getBean(Neo4jTemplate.class);
        String cypherQuery = wrapper.mergeCreateNode(label, mergeMap, attributeMap);
        return neo4jTemplate.findAll(cypherQuery, wrapper.getEntityClass());
    }

    /**
     * 根据起点和结点创建关系 如果起点或者终点不存在, 会创建节点
     *
     * @param wrapper
     * @param startNodeMap
     * @param endNodeMap
     * @param rel
     * @return
     */
    default List<T> mergeRelationship(String label, Neo4jBuildWrapper<T> wrapper, Map<String, Object> startNodeMap, Map<String, Object> endNodeMap, String rel, Map<String, Object> relMap) {
        Neo4jTemplate neo4jTemplate = SpringUtils.getBean(Neo4jTemplate.class);
        String cypherQuery = wrapper.createRelationship(label, startNodeMap, endNodeMap, rel, relMap);
        return neo4jTemplate.findAll(cypherQuery, wrapper.getEntityClass());
    }

    /**
     * 根据起点id和结点id创建关系
     * @param label
     * @param wrapper
     * @param startNodeId
     * @param endNodeId
     * @param rel
     * @param relMap
     * @return
     */
    default List<T> mergeRelationship(String label, Neo4jBuildWrapper<T> wrapper, Long startNodeId, Long endNodeId, String rel, Map<String, Object> relMap) {
        Neo4jTemplate neo4jTemplate = SpringUtils.getBean(Neo4jTemplate.class);
        String cypherQuery = wrapper.createRelationship(label, startNodeId, endNodeId, rel, relMap);
        return neo4jTemplate.findAll(cypherQuery, wrapper.getEntityClass());
    }

    /**
     * 统计查询
     *
     * @param wrapper
     * @return
     */
    default Long count(Neo4jQueryWrapper<T> wrapper) {
        Neo4jTemplate neo4jTemplate = SpringUtils.getBean(Neo4jTemplate.class);
        return neo4jTemplate.count(wrapper.getCypherCountQuery(), wrapper.getParams());
    }

    /**
     * 根据节点id删除节点和相关关系
     *
     * @param nodeId
     */
    @Query("MATCH (n) WHERE id(n) = $nodeId DETACH DELETE n")
    void deleteNodeById(@Param("nodeId") Long nodeId);

    /**
     * 根据节点id删除节点和相关关系
     *
     * @param nodeIds
     */
    @Query("MATCH (n) WHERE id(n) IN $nodeIds DETACH DELETE n")
    void deleteNodeByIds(@Param("nodeIds") List<Long> nodeIds);

    /**
     * 根据节点id和属性的id删除属性
     *
     */
//    @Query("MATCH (n) WHERE id(n) = $nodeId REMOVE n.$attributeKey RETURN n")
//    void deleteNodeAttributeById(@Param("nodeId") Long nodeId, @Param("attributeKey") String attributeKey);
    default void deleteNodeAttributeById(Long nodeId, String attributeKey){
        Neo4jTemplate neo4jTemplate = SpringUtils.getBean(Neo4jTemplate.class);
        Neo4jBuildWrapper<DynamicEntity> wrapper = new Neo4jBuildWrapper<>(DynamicEntity.class);
        neo4jTemplate.findAll(wrapper.deleteNodeAttribute(nodeId,attributeKey), wrapper.getEntityClass());
    }

    /**
     * 根据关系id删除关系
     *
     * @param relationshipId
     */
    @Query("MATCH ()-[r]->() WHERE id(r) = $relationshipId DELETE r")
    void deleteRelationshipById(@Param("relationshipId") Long relationshipId);

    /**
     * 根据关系id集合删除关系
     *
     * @param relationshipIds
     */
    @Query("MATCH ()-[r]->() WHERE id(r) IN $relationshipIds DELETE r")
    void deleteRelationshipsByIds(@Param("relationshipIds") List<Long> relationshipIds);
}
