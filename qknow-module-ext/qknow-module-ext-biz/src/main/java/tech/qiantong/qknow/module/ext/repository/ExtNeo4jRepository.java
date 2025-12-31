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

package tech.qiantong.qknow.module.ext.repository;

import org.neo4j.driver.types.Node;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import tech.qiantong.qknow.module.ext.dal.dataobject.extExtraction.ExtExtraction;
import tech.qiantong.qknow.neo4j.repository.BaseRepository;

import java.util.List;

public interface ExtNeo4jRepository extends BaseRepository<ExtExtraction, Long> {

    /**
     * 根据任务id删除节点及其关系 结构化
     *
     * @param taskId
     * @return
     */
    @Query("MATCH (a:ExtUnStruck {dynamic_properties_task_id: $taskId}) " +
            "DETACH DELETE a")
    void deleteExtUnStruck(@Param("taskId") Long taskId);


    /**
     * 根据任务id删除节点及其关系 非结构化
     *
     * @param taskId
     * @return
     */
    @Query("MATCH (a:ExtStruck {dynamic_properties_task_id: $taskId}) " +
            "DETACH DELETE a")
    void deleteExtStruck(@Param("taskId") Long taskId);
}
