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

package tech.qiantong.qknow.module.ext.service.neo4j.service;

import tech.qiantong.qknow.common.core.domain.AjaxResult;
import tech.qiantong.qknow.module.ext.dal.dataobject.extNeo4j.ExtNeo4j;
import tech.qiantong.qknow.module.ext.dal.dataobject.extraction.ExtExtractionDO;

import java.util.List;

public interface ExtNeo4jService {
    //把抽取出来的数据存储到neo4j数据库
    public AjaxResult insertExtractionList(List<ExtExtractionDO> extractionList);

//    public AjaxResult getByName(String name);

//    //获取全部数据
//    public AjaxResult getExtExtractionAll();

    public AjaxResult getExtractionAllData();

    /**
     * 根据节点id删除对应的节点
     *
     * @param id
     * @return
     */
    public AjaxResult deleteNode(Long id);

    public AjaxResult selectByTaskId(Long taskId, Integer extractType);

    public AjaxResult updateRelationship(ExtNeo4j.UpdateRelationship updateRelationship);

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
    public AjaxResult deleteNodeAttribute(Long taskId, Integer extractType, String tableName, Integer dataId, String attributeKey);

    /**
     * 修改节点的某个属性
     *
     * @param taskId
     * @param
     * @param tableName
     * @param dataId
     * @param attributeKey
     * @param attributeValue
     * @return
     */
    public AjaxResult updateNodeAttribute(Long taskId, String tableName, Integer dataId, String attributeKey, String attributeValue);

    /**
     * 修改节点的某个属性
     * @param relationshipId
     * @return
     */
    public AjaxResult deleteRelationship(Long relationshipId);

    /**
     * 修改发布状态
     *
     * @param taskId
     * @param extractType
     * @param releaseStatus
     * @return
     */
    public AjaxResult updateByTaskIdAndExtractType(Long taskId, Integer extractType, Integer releaseStatus);

    /**
     * 根据任务id删除之前的抽取结果 非结构化抽取
     *
     * @param extExtractionDO
     * @return
     */
    public AjaxResult deleteExtUnStruck(ExtExtractionDO extExtractionDO);

    /**
     * 根据任务id删除之前的抽取结果 结构化抽取
     * @param extExtractionDO
     * @return
     */
    public AjaxResult deleteExtStruck(ExtExtractionDO extExtractionDO);

    //查询
    public AjaxResult getExtExtraction(ExtExtractionDO extExtractionDO);
}
