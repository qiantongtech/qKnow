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

package tech.qiantong.qknow.module.ext.service.neo4j.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.driver.Driver;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.stereotype.Service;
import tech.qiantong.qknow.common.core.domain.AjaxResult;
import tech.qiantong.qknow.module.app.enums.ReleaseStatus;
import tech.qiantong.qknow.module.ext.dal.dataobject.extExtraction.ExtExtraction;
import tech.qiantong.qknow.module.ext.dal.dataobject.extNeo4j.ExtNeo4j;
import tech.qiantong.qknow.module.ext.dal.dataobject.extUnstruck.ExtUnstruckExtraction;
import tech.qiantong.qknow.module.ext.dal.dataobject.extUnstruck.ExtUnstruckExtractionMergeDO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extraction.ExtExtractionDO;
import tech.qiantong.qknow.module.ext.extEnum.ExtReleaseStatus;
import tech.qiantong.qknow.module.ext.extEnum.ExtractType;
import tech.qiantong.qknow.module.ext.repository.ExtNeo4jRepository;
import tech.qiantong.qknow.module.ext.repository.ExtUnstruckNeo4jRepository;
import tech.qiantong.qknow.module.ext.service.neo4j.service.ExtNeo4jService;
import tech.qiantong.qknow.neo4j.domain.DynamicEntity;
import tech.qiantong.qknow.neo4j.enums.Neo4jLabelEnum;
import tech.qiantong.qknow.neo4j.repository.DynamicRepository;
import tech.qiantong.qknow.neo4j.wrapper.Neo4jBuildWrapper;
import tech.qiantong.qknow.neo4j.wrapper.Neo4jQueryWrapper;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class ExtNeo4jServiceImpl implements ExtNeo4jService {

    @Resource
    private Driver driver;
    @Resource
    private ExtNeo4jRepository repository;
    @Resource
    private Neo4jTemplate neo4jTemplate;
    @Resource
    private Neo4jClient neo4jClient;
    @Resource
    private DynamicRepository dynamicRepository;
    @Resource
    private ExtNeo4jRepository faultRepository;
    @Resource
    private ExtUnstruckNeo4jRepository unstruckNeo4jRepository;

    /**
     * 非结构化抽取任务
     * 把抽取出来的数据存储到neo4j数据库 一个段落执行一次
     *
     * @param extractionList
     * @return
     */
    @Override
    public AjaxResult insertExtractionList(List<ExtExtractionDO> extractionList) {
        for (ExtExtractionDO extractionDO : extractionList) {
            try {
                Neo4jBuildWrapper<DynamicEntity> startBuild = new Neo4jBuildWrapper<>(DynamicEntity.class);
                ExtUnstruckExtractionMergeDO.Node startMerge = new ExtUnstruckExtractionMergeDO.Node();
                startMerge.setName(extractionDO.getHead());
                startMerge.setTask_id(extractionDO.getTaskId());
                startMerge.setDoc_id(extractionDO.getDocId());
                startMerge.setParagraphIndex(extractionDO.getParagraphIndex());
                startMerge.setEntity_type(ExtractType.UNSTRUCTURED.getValue());
                ConcurrentHashMap<String, Object> startMergeMap = new ObjectMapper().readValue(JSONObject.toJSONString(startMerge), ConcurrentHashMap.class);

                ExtUnstruckExtractionMergeDO.Node endMerge = new ExtUnstruckExtractionMergeDO.Node();
                endMerge.setName(extractionDO.getTail());
                endMerge.setTask_id(extractionDO.getTaskId());
                endMerge.setDoc_id(extractionDO.getDocId());
                endMerge.setParagraphIndex(extractionDO.getParagraphIndex());
                endMerge.setEntity_type(ExtractType.UNSTRUCTURED.getValue());
                ConcurrentHashMap<String, Object> endMergeMap = new ObjectMapper().readValue(JSONObject.toJSONString(endMerge), ConcurrentHashMap.class);

                ConcurrentHashMap<String, Object> propertiesMap = new ConcurrentHashMap<>();
                propertiesMap.put("task_id", extractionDO.getTaskId());
                propertiesMap.put("entity_type", ExtractType.UNSTRUCTURED.getValue());
                propertiesMap.put("release_status", ExtReleaseStatus.UNPUBLISHED.getStatus());
                propertiesMap.put("doc_id", extractionDO.getDocId());
                propertiesMap.put("paragraph_index", extractionDO.getParagraphIndex());
                propertiesMap.put("confidence", extractionDO.getConfidence());
                propertiesMap.put("workspace_id", extractionDO.getWorkspaceId() == null ? "" : extractionDO.getWorkspaceId());

                ConcurrentHashMap<String, Object> startMap = new ConcurrentHashMap<>();
                startMap.put("name", extractionDO.getHead());
                startMap.putAll(propertiesMap);
                ConcurrentHashMap<String, Object> endMap = new ConcurrentHashMap<>();
                endMap.put("name", extractionDO.getTail());
                endMap.putAll(propertiesMap);
                dynamicRepository.mergeCreateNode(Neo4jLabelEnum.DYNAMICENTITY.getLabel() + ":" + Neo4jLabelEnum.UNSTRUCTURED.getLabel(), startBuild, startMergeMap, startMap);
                dynamicRepository.mergeCreateNode(Neo4jLabelEnum.DYNAMICENTITY.getLabel() + ":" + Neo4jLabelEnum.UNSTRUCTURED.getLabel(), startBuild, endMergeMap, endMap);

                //关系
                ConcurrentHashMap<String, Object> relMa = new ConcurrentHashMap<>();
                relMa.put("task_id", extractionDO.getTaskId());
                relMa.put("entity_type", ExtractType.UNSTRUCTURED.getValue());
                dynamicRepository.mergeRelationship(Neo4jLabelEnum.UNSTRUCTURED.getLabel(), startBuild, startMergeMap, endMergeMap, extractionDO.getRelation(), relMa);
            } catch (Exception e) {
                log.info("数据导入到Neo4j异常: {}", e);
            }
        }
        return AjaxResult.success();
    }

//    /**
//     * 修改属性
//     *
//     * @param taskId
//     * @param extractType
//     * @param releaseStatus
//     * @return
//     */
//    public AjaxResult updateNodesAttribute(Long taskId, Integer extractType, Integer releaseStatus) {
//        String nodeQuery = "MATCH (n:ExtExtraction) WHERE n.task_id = $task_id AND n.entity_type = $entity_type " +
//                "SET n.release_status = $release_status " +
//                "RETURN n";
//        ConcurrentHashMap<String, Object> startParams = new ConcurrentHashMap<>();
//        startParams.put("task_id", taskId);
//        startParams.put("entity_type", extractType);
//        startParams.put("release_status", releaseStatus);
//        neo4jClient.query(nodeQuery)
//                .bindAll(startParams)
//                .run();
//        return AjaxResult.success("操作成功");
//    }

    /**
     * 删除节点的某个属性
     *
     * @param taskId
     * @param extractType
     * @param tableName
     * @param dataId
     * @param attributeKey
     * @return
     */
    @Override
    public AjaxResult deleteNodeAttribute(Long taskId, Integer extractType, String tableName, Integer dataId, String attributeKey) {
        String nodeQuery = "MATCH (n:ExtExtraction) " +
                "WHERE n.task_id = $task_id AND n.entity_type = $extract_type AND n.table_name = $table_name AND n.data_id = $data_id " +
                "REMOVE n." + attributeKey + " " +
                "RETURN n";
        ConcurrentHashMap<String, Object> startParams = new ConcurrentHashMap<>();
        startParams.put("task_id", taskId);
        startParams.put("entity_type", extractType);
        startParams.put("table_name", tableName);
        startParams.put("data_id", dataId);
        neo4jClient.query(nodeQuery)
                .bindAll(startParams)
                .run();
        return AjaxResult.success("操作成功");
    }

    /**
     * 删除关系
     *
     * @param relationshipId
     * @return
     */
    @Override
    public AjaxResult deleteRelationship(Long relationshipId) {
        faultRepository.deleteRelationshipById(relationshipId);
        return AjaxResult.success("操作成功");
    }

    /**
     * 修改关系
     *
     * @param
     * @param
     * @return
     */
    @Override
    public AjaxResult updateRelationship(ExtNeo4j.UpdateRelationship updateRelationship) {
        String deleteQuery = "MATCH ()-[r]->() " +
                "WHERE ID(r) = " + updateRelationship.getRelationshipId() + " " +
                "DELETE r";
        neo4jClient.query(deleteQuery).run();

        if (Integer.valueOf(1).equals(updateRelationship.getExtractType())) {
            //结构化抽取三元组关系添加
            String createRelationshipQuery = "MATCH (n:ExtExtraction {data_id: $startId, task_id: $task_id, table_name: $startTableName}), " +
                    "(m:ExtExtraction {data_id: $endId, task_id: $task_id, table_name: $endTableName}) " +
                    "MERGE (n)-[r:" + updateRelationship.getRelationship() + "]->(m) " +
                    "ON CREATE SET r.task_id = $task_id " +
                    "RETURN r, ID(r) AS relationshipId;";
            ConcurrentHashMap<String, Object> relationshipParams = new ConcurrentHashMap<>();
            relationshipParams.put("startId", updateRelationship.getStartId());
            relationshipParams.put("endId", updateRelationship.getEndId());
            relationshipParams.put("startTableName", updateRelationship.getStartTableName());
            relationshipParams.put("endTableName", updateRelationship.getEndTableName());
            relationshipParams.put("task_id", updateRelationship.getTaskId());
            // 执行创建关系查询
            neo4jClient.query(createRelationshipQuery)
                    .bindAll(relationshipParams)
                    .run();
        } else if (Integer.valueOf(2).equals(updateRelationship.getExtractType())) {
            //非结构化抽取三元组关系添加
            String query = "MATCH (a:ExtExtraction), (b:ExtExtraction) " +
                    "WHERE ID(a) = $startId AND ID(b) = $endId " +
                    "MERGE (a)-[r:" + updateRelationship.getRelationship() + "]->(b) " +
                    "ON CREATE SET r.task_id = $task_id " +
                    "RETURN r, ID(r) AS relationshipId;";
            ConcurrentHashMap<String, Object> relationshipParams = new ConcurrentHashMap<>();
            relationshipParams.put("startId", updateRelationship.getStartId());
            relationshipParams.put("endId", updateRelationship.getEndId());
            relationshipParams.put("task_id", updateRelationship.getTaskId());
            // 执行创建关系查询
            neo4jClient.query(query)
                    .bindAll(relationshipParams)
                    .run();
        }
        return AjaxResult.success("操作成功");
    }

    /**
     * 修改节点的某个属性
     *
     * @param taskId
     * @param tableName
     * @param dataId
     * @param attributeKey
     * @param attributeValue
     * @return
     */
    @Override
    public AjaxResult updateNodeAttribute(Long taskId, String tableName, Integer dataId, String attributeKey, String attributeValue) {
        ConcurrentHashMap<String, Object> paramMap = new ConcurrentHashMap<>();
        paramMap.put("task_id", taskId);
        paramMap.put("table_name", tableName);
        paramMap.put("data_id", dataId);
        ConcurrentHashMap<String, Object> updateMap = new ConcurrentHashMap<>();
        updateMap.put(attributeKey, attributeValue);
        Neo4jBuildWrapper<DynamicEntity> wrapper = new Neo4jBuildWrapper<>(DynamicEntity.class);
        dynamicRepository.updateQuery(wrapper, Neo4jLabelEnum.STRUCTURED.getLabel(), paramMap, updateMap);
        return AjaxResult.success("操作成功");
    }

    /**
     * 修改发布状态
     *
     * @param taskId
     * @param extractType
     * @param releaseStatus
     * @return
     */
    @Override
    public AjaxResult updateByTaskIdAndExtractType(Long taskId, Integer extractType, Integer releaseStatus) {
        ConcurrentHashMap<String, Object> paramMap = new ConcurrentHashMap<>();
        paramMap.put("task_id", taskId.toString());
        paramMap.put("entity_type", extractType.toString());
        ConcurrentHashMap<String, Object> updateMap = new ConcurrentHashMap<>();
        updateMap.put("release_status", releaseStatus.toString());

        switch (extractType) {
            case 1:
                dynamicRepository.updateQuery(new Neo4jBuildWrapper<>(DynamicEntity.class), Neo4jLabelEnum.STRUCTURED.getLabel(), paramMap, updateMap);
                break;
            case 2:
                dynamicRepository.updateQuery(new Neo4jBuildWrapper<>(DynamicEntity.class), Neo4jLabelEnum.UNSTRUCTURED.getLabel(), paramMap, updateMap);
                break;
        }
        return AjaxResult.success("操作成功");
    }

    /**
     * 获取结构化任务抽取结果
     *
     * @param taskId
     * @return
     */
    @Override
    public AjaxResult selectByTaskId(Long taskId, Integer extractType) {
        Neo4jQueryWrapper<DynamicEntity> build = new Neo4jQueryWrapper<>(DynamicEntity.class);
        build.eq("task_id", taskId.toString());
        Set<String> labelSet = new HashSet<>();
        labelSet.add(Neo4jLabelEnum.STRUCTURED.getLabel());
        //获取实体
        List<Map<String, Object>> entities = dynamicRepository.nodeListMap(build, labelSet);
        //获取关系

        List<Map<String, Object>> relationships = dynamicRepository.relListMap(build, labelSet);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("entities", entities);
        hashMap.put("relationships", relationships);
        return AjaxResult.success("获取成功", hashMap);
    }

    /**
     * 根据任务Id查询neo4j三元组数据 非结构化
     *
     * @param extExtractionDO
     * @return
     */
    @Override
    public AjaxResult getExtExtraction(ExtExtractionDO extExtractionDO) {
        Long taskId = Long.valueOf(extExtractionDO.getTaskId());
        //获取实体
        Neo4jQueryWrapper<DynamicEntity> build = new Neo4jQueryWrapper<>(DynamicEntity.class);
        build.eq("task_id", taskId.toString());
        Set<String> labelSet = new HashSet<>();
        labelSet.add(Neo4jLabelEnum.UNSTRUCTURED.getLabel());
        List<Map<String, Object>> entities = dynamicRepository.nodeListMap(build, labelSet);
        //获取关系
        List<Map<String, Object>> relationships = dynamicRepository.relListMap(build, labelSet);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("entities", entities);
        hashMap.put("relationships", relationships);
        return AjaxResult.success("获取成功", hashMap);
    }

    /**
     * 根据节点id删除对应的节点
     *
     * @return
     */
    @Override
    public AjaxResult deleteNode(Long id) {
        faultRepository.deleteNodeById(id);
        return AjaxResult.success();
    }

    /**
     * 根据任务id删除之前的抽取结果 非结构化抽取
     *
     * @param extExtractionDO
     * @return
     */
    @Override
    public AjaxResult deleteExtUnStruck(ExtExtractionDO extExtractionDO) {
        repository.deleteExtUnStruck(extExtractionDO.getTaskId());
        return AjaxResult.success("删除成功");
    }

    /**
     * 根据任务id删除之前的抽取结果
     * @param extExtractionDO
     * @return
     */
    @Override
    public AjaxResult deleteExtStruck(ExtExtractionDO extExtractionDO) {
        repository.deleteExtStruck(extExtractionDO.getTaskId());
        return AjaxResult.success("删除成功");
    }

    /**
     * 获取全部数据
     *
     * @return
     */
    @Override
    public AjaxResult getExtractionAllData() {
        Neo4jQueryWrapper<DynamicEntity> build = new Neo4jQueryWrapper<>(DynamicEntity.class);
        build.eq("release_status", ReleaseStatus.PUBLISHED.getValue().toString()); //发布状态
        //获取实体
        Set<String> labelSet = new HashSet<>();
        labelSet.add(Neo4jLabelEnum.UNSTRUCTURED.getLabel());
        labelSet.add(Neo4jLabelEnum.STRUCTURED.getLabel());
        labelSet.add(Neo4jLabelEnum.GRAPHENTITY.getLabel());
        List<Map<String, Object>> entities = dynamicRepository.nodeListMap(build, labelSet);
        //获取关系
        List<Map<String, Object>> relationships = dynamicRepository.relListMap(build, labelSet);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("entities", entities);
        hashMap.put("relationships", relationships);
        return AjaxResult.success("获取成功", hashMap);
    }
}
