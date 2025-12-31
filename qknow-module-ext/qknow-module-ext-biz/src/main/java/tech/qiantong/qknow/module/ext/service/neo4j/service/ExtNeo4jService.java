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
