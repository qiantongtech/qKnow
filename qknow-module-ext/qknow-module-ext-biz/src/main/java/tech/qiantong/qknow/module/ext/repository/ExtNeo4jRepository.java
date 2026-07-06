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
