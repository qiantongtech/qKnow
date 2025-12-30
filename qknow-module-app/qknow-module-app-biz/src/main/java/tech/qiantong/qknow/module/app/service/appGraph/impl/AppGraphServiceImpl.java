package tech.qiantong.qknow.module.app.service.appGraph.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tech.qiantong.qknow.common.core.domain.AjaxResult;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.common.utils.StringUtils;
import tech.qiantong.qknow.module.app.controller.admin.appGraph.vo.AppGraphPageReqVO;
import tech.qiantong.qknow.module.app.controller.admin.appGraph.vo.AppGraphRelationshipSaveReqVO;
import tech.qiantong.qknow.module.app.controller.admin.appGraph.vo.AppGraphVO;
import tech.qiantong.qknow.module.app.controller.admin.appGraph.vo.DeleteNodeAttributeVO;
import tech.qiantong.qknow.module.app.enums.ReleaseStatus;
import tech.qiantong.qknow.module.app.service.appGraph.AppGraphService;
import tech.qiantong.qknow.neo4j.domain.DynamicEntity;
import tech.qiantong.qknow.neo4j.domain.relationship.DynamicEntityRelationship;
import tech.qiantong.qknow.neo4j.enums.GraphLabelEnum;
import tech.qiantong.qknow.neo4j.enums.Neo4jLabelEnum;
import tech.qiantong.qknow.neo4j.repository.DynamicRepository;
import tech.qiantong.qknow.neo4j.utils.Convert;
import tech.qiantong.qknow.neo4j.wrapper.Neo4jBuildWrapper;
import tech.qiantong.qknow.neo4j.wrapper.Neo4jQueryWrapper;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static tech.qiantong.qknow.neo4j.enums.Neo4jLabelEnum.get;

/**
 * 图谱
 */
@Slf4j
@Service
public class AppGraphServiceImpl implements AppGraphService {

    @Resource
    private DynamicRepository dynamicRepository;

    /**
     * 获取图谱数据
     *
     * @param appGraphVO
     * @return
     */
    @Override
    public Map<String, Object> getGraph(AppGraphVO appGraphVO) {
        Neo4jQueryWrapper<DynamicEntity> build = new Neo4jQueryWrapper<>(DynamicEntity.class);
        Neo4jLabelEnum neo4jLabelEnum = get(appGraphVO.getEntityType());
        if (Neo4jLabelEnum.DYNAMICENTITY.eq(neo4jLabelEnum.getCode())) {
            build.eq("release_status", ReleaseStatus.PUBLISHED.getValue()); //发布状态
        } else {
            build.addLabels(neo4jLabelEnum.getLabel());
            if(appGraphVO.getEntityId() != null){
                build.eq(neo4jLabelEnum.getEntityIdName(), appGraphVO.getEntityId());
            }
        }
        List<DynamicEntity> dynamicEntities = dynamicRepository.find(build);
        JSONObject dynamicEntityJSONObject = Convert.toDynamicEntityJSONObject(dynamicEntities);
        Map<String, Object> hashMap = Maps.newHashMap();
        hashMap.put("entities", dynamicEntityJSONObject.get("entities"));
        hashMap.put("relationships", dynamicEntityJSONObject.get("relationships"));
        return hashMap;
    }

    @Override
    public PageResult<JSONObject> getGraphPage(AppGraphPageReqVO appGraphVO) {
        Neo4jQueryWrapper<DynamicEntity> build = new Neo4jQueryWrapper<>(DynamicEntity.class);
        Neo4jLabelEnum neo4jLabelEnum = get(appGraphVO.getEntityType());
        if (neo4jLabelEnum == null) {
            build.eq("release_status", ReleaseStatus.PUBLISHED.getValue()); //发布状态
        } else {
            build.addLabels(neo4jLabelEnum.getLabel());
            if(appGraphVO.getEntityId() != null){
                build.eq(neo4jLabelEnum.getEntityIdName(), appGraphVO.getEntityId());
            }
        }
        if (StringUtils.isNotEmpty(appGraphVO.getName())) {
            build.like("name", appGraphVO.getName());
        }
        build.page(appGraphVO.getPageNum(), appGraphVO.getPageSize());
        PageResult<DynamicEntity> page = dynamicRepository.findPage(build);
        JSONObject dynamicEntityJSONObject = Convert.toDynamicEntityJSONObject(page.getRows());
        JSONArray jsonArray = dynamicEntityJSONObject.getJSONArray("entities");
        return new PageResult<>(jsonArray.toList(JSONObject.class), page.getTotal());
    }

    /**
     * 统计 (实体数量,关系类型数量,三元组数量)
     * @param appGraphVO
     * @return
     */
    public Map<String, Object> getGraphDataStatistics(AppGraphVO appGraphVO){
        List<DynamicEntity> dynamicEntities = dynamicRepository.findAll();
        JSONObject dynamicEntityJSONObject = Convert.toDynamicEntityJSONObject(dynamicEntities);
        JSONArray entities = dynamicEntityJSONObject.getJSONArray("entities");
        JSONArray relationships = dynamicEntityJSONObject.getJSONArray("relationships");
        long uniqueRelationTypeCount = relationships.stream()
                .map(relationship -> ((JSONObject) relationship).getString("relationType"))
                .distinct()
                .count();

        Map<String, Object> result = Maps.newHashMap();
        result.put("entityCount", entities.size());   // 实体数量
        result.put("relationshipCount", uniqueRelationTypeCount);  // 去重后的关系类型数量
        result.put("tripletCount", relationships.size());  // 三元组数量
        return result;
    }

    /**
     * 新增实体
     *
     * @return
     */
    @Override
    public Boolean addNode(JSONArray jsonArray) {
        Set<Long> idSet = jsonArray.stream()
                .map(obj -> (JSONObject) obj)
                .filter(jsonObject -> jsonObject.containsKey("id") && jsonObject.get("id") != null)
                .map(jsonObject -> jsonObject.getLong("id"))
                .collect(Collectors.toSet());
        List<DynamicEntity> graphEntityList = dynamicRepository.findAllById(idSet);
        Map<Long, DynamicEntity> entityMap = graphEntityList.stream()
                .collect(Collectors.toMap(DynamicEntity::getId, entity -> entity));
        List<DynamicEntity> entityList = Lists.newArrayList();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String id = jsonObject.getString("id");
            DynamicEntity graphEntity;
            if (id != null) {
                graphEntity = entityMap.get(id);
            } else {
                graphEntity = new DynamicEntity();
                graphEntity.setReleaseStatus(ReleaseStatus.UNPUBLISHED.getValue());
                graphEntity.setCreatorId(jsonObject.getLong("creatorId"));
                graphEntity.setCreateTime(jsonObject.getDate("createTime"));
                graphEntity.setCreateBy(jsonObject.getString("createBy"));
                jsonObject.remove("creatorId");
                jsonObject.remove("createTime");
                jsonObject.remove("createBy");
            }
            // 标签
            graphEntity.addLabels(GraphLabelEnum.get(jsonObject.getInteger("entityType")).getLabel());
            // 固定字段
            graphEntity.setName(jsonObject.getString("name"));
            jsonObject.remove("name");
            jsonObject.remove("graphId");

            // 动态属性
            for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
                if (!"id".equals(entry.getKey())) {
                    graphEntity.putDynamicProperties(Convert.camelToSnake(entry.getKey()), entry.getValue());
                }
            }
            entityList.add(graphEntity);
        }
        dynamicRepository.saveAll(entityList);
        return true;
    }

    /**
     * 新增三元组 TODO
     * 根据起点id和结点id创建关系
     *
     * @param graphRelationshipSaveReqVOList
     * @return
     */
    @Override
    public Boolean addTripletRel(List<AppGraphRelationshipSaveReqVO> graphRelationshipSaveReqVOList) {
        // 取出所有的实体id
        Set<Long> idSet = graphRelationshipSaveReqVOList.stream()
                .flatMap(user -> Stream.of(user.getEntityId1(), user.getEntityId2()))
                .collect(Collectors.toSet());
        // 查询出所有的实体
        List<DynamicEntity> graphEntityList = dynamicRepository.findAllById(idSet);
        Map<Long, DynamicEntity> graphEntityMap = graphEntityList.stream()
                .collect(Collectors.toMap(DynamicEntity::getId, graphEntity -> graphEntity));
        List<Long> relationshipIds = graphRelationshipSaveReqVOList.stream()
                .map(AppGraphRelationshipSaveReqVO::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        if (!relationshipIds.isEmpty()) {
            // 删除关系
            dynamicRepository.deleteNodeByIds(relationshipIds);
        }
        // 新增/更新关系
        graphRelationshipSaveReqVOList.forEach(appGraphRelationshipSaveReqVO -> {
            DynamicEntity graphEntity1 = graphEntityMap.get(appGraphRelationshipSaveReqVO.getEntityId1());
            DynamicEntity graphEntity2 = graphEntityMap.get(appGraphRelationshipSaveReqVO.getEntityId2());
            Map<String, List<DynamicEntityRelationship>> entityMap = graphEntity1.getRelationshipEntityMap();

            List<DynamicEntityRelationship> graphEntityRelationshipList;
            if(entityMap == null) {
                entityMap = Maps.newHashMap();
            }
            // 如果有关系则新增一个关系
            if (entityMap.containsKey(appGraphRelationshipSaveReqVO.getRelationshipType())) {
                graphEntityRelationshipList = entityMap.get(appGraphRelationshipSaveReqVO.getRelationshipType());
            } else {
                graphEntityRelationshipList = Lists.newArrayList();
            }
            DynamicEntityRelationship graphEntityRelationship = new DynamicEntityRelationship();
            graphEntityRelationship.setEndNode(graphEntity2);
            graphEntityRelationshipList.add(graphEntityRelationship);
            entityMap.put(appGraphRelationshipSaveReqVO.getRelationshipType (), graphEntityRelationshipList);
            graphEntity1.setRelationshipEntityMap(entityMap);
        });
        dynamicRepository.saveAll(graphEntityList);
        return true;
    }

    /**
     * 根据节点id和属性的id删除属性
     *
     * @param deleteNodeAttributeVO
     * @return
     */
    public AjaxResult deleteNodeAttributeById(DeleteNodeAttributeVO deleteNodeAttributeVO) {
        dynamicRepository.deleteNodeAttributeById(deleteNodeAttributeVO.getNodeId(), deleteNodeAttributeVO.getAttributeKey());
        return AjaxResult.success("操作成功");
    }

    /**
     * 发布 / 取消发布
     *
     * @return
     */
    public AjaxResult updateReleaseStatus(AppGraphVO appGraphVO) {
        Neo4jLabelEnum neo4jLabelEnum = get(appGraphVO.getEntityType());
        if (neo4jLabelEnum != null) {
            String label = neo4jLabelEnum.getLabel();
            //条件map
            HashMap<String, Object> paramMap = Maps.newHashMap();
            paramMap.put(neo4jLabelEnum.getEntityIdName(), appGraphVO.getEntityId());
            //修改属性内容
            HashMap<String, Object> updateMap = Maps.newHashMap();
            updateMap.put("release_status", appGraphVO.getReleaseStatus());//发布状态 0未发布 1已发布
            dynamicRepository.updateQuery(new Neo4jBuildWrapper<>(DynamicEntity.class), label, paramMap, updateMap);
        }
        return AjaxResult.success("操作成功");
    }

    /**
     * 根据节点id删除对应的节点
     *
     * @param id
     * @return
     */
    @Override
    public AjaxResult deleteNode(Long id) {
        dynamicRepository.deleteNodeById(id);
        return AjaxResult.success("删除成功");
    }

    @Override
    public Boolean deleteNodeByIds(Long[] ids) {
        List<Long> idList = Arrays.asList(ids);
        dynamicRepository.deleteNodeByIds(idList);
        return true;
    }


    /**
     * 根据关系id删除关系
     *
     * @param id
     * @return
     */
    @Override
    public AjaxResult deleteRelationshipById(Long id) {
        dynamicRepository.deleteRelationshipById(id);
        return AjaxResult.success("删除成功");
    }

    /**
     * 根据关系ids删除关系
     *
     * @param ids
     * @return
     */
    @Override
    public AjaxResult deleteRelationshipsByIds(Long[] ids) {
        List<Long> idList = Arrays.asList(ids);
        dynamicRepository.deleteRelationshipsByIds(idList);
        return AjaxResult.success("删除成功");
    }
}
