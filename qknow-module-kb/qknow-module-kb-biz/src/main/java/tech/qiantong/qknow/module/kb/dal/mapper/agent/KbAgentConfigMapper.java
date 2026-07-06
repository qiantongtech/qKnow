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

package tech.qiantong.qknow.module.kb.dal.mapper.agent;

import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.module.kb.controller.admin.agent.vo.KbAgentConfigPageReqVO;
import tech.qiantong.qknow.module.kb.dal.dataobject.agent.KbAgentConfigDO;
import tech.qiantong.qknow.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qknow.mybatis.core.query.LambdaQueryWrapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * agent配置Mapper接口
 *
 * @author qknow
 * @date 2026-03-19
 */
public interface KbAgentConfigMapper extends BaseMapperX<KbAgentConfigDO> {

    default PageResult<KbAgentConfigDO> selectPage(KbAgentConfigPageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // 构造动态查询条件
        return selectPage(reqVO, new LambdaQueryWrapperX<KbAgentConfigDO>()
                .eqIfPresent(KbAgentConfigDO::getWorkspaceId, reqVO.getWorkspaceId())
                .eqIfPresent(KbAgentConfigDO::getBotId, reqVO.getBotId())
                .eqIfPresent(KbAgentConfigDO::getModelConfig, reqVO.getModelConfig())
                .eqIfPresent(KbAgentConfigDO::getPrePrompt, reqVO.getPrePrompt())
                .eqIfPresent(KbAgentConfigDO::getParameters, reqVO.getParameters())
                .eqIfPresent(KbAgentConfigDO::getKnowledgeIds, reqVO.getKnowledgeIds())
                .eqIfPresent(KbAgentConfigDO::getGraphIds, reqVO.getGraphIds())
                .eqIfPresent(KbAgentConfigDO::getToolMethodIds, reqVO.getToolMethodIds())
                .eqIfPresent(KbAgentConfigDO::getCreateTime, reqVO.getCreateTime())
                // 如果 reqVO.getName() 不为空，则添加 name 的精确匹配条件（name = '<name>'）
                // .likeIfPresent(KbAgentConfigDO::getName, reqVO.getName())
                // 按照 createTime 字段降序排序
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }
}
