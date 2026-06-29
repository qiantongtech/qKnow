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

package tech.qiantong.qknow.module.kmc.dal.mapper.knowledgeBase;

import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.module.kmc.controller.admin.knowledgeBase.vo.KmcKnowledgeBasePageReqVO;
import tech.qiantong.qknow.module.kmc.dal.dataobject.knowledgeBase.KmcKnowledgeBaseDO;
import tech.qiantong.qknow.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qknow.mybatis.core.query.LambdaQueryWrapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 知识库Mapper接口
 *
 * @author qknow
 * @date 2025-07-22
 */
public interface KmcKnowledgeBaseMapper extends BaseMapperX<KmcKnowledgeBaseDO> {

    default PageResult<KmcKnowledgeBaseDO> selectPage(KmcKnowledgeBasePageReqVO reqVO, Set<Long> idList, Long userId, Boolean isAdmin) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));
        LambdaQueryWrapperX<KmcKnowledgeBaseDO> queryWrapper = new LambdaQueryWrapperX<KmcKnowledgeBaseDO>()
                .eqIfPresent(KmcKnowledgeBaseDO::getWorkspaceId, reqVO.getWorkspaceId())
                .eqIfPresent(KmcKnowledgeBaseDO::getQmDatasetId, reqVO.getQmDatasetId())
                .likeIfPresent(KmcKnowledgeBaseDO::getName, reqVO.getName())
                .eqIfPresent(KmcKnowledgeBaseDO::getDescription, reqVO.getDescription())
                .eqIfPresent(KmcKnowledgeBaseDO::getIndexingTechnique, reqVO.getIndexingTechnique())
                .eqIfPresent(KmcKnowledgeBaseDO::getPermission, reqVO.getPermission())
                .eqIfPresent(KmcKnowledgeBaseDO::getEmbeddingModel, reqVO.getEmbeddingModel())
                .eqIfPresent(KmcKnowledgeBaseDO::getEmbeddingModelProvider, reqVO.getEmbeddingModelProvider())
                .eqIfPresent(KmcKnowledgeBaseDO::getSearchMethod, reqVO.getSearchMethod())
                .eqIfPresent(KmcKnowledgeBaseDO::getRerankingEnable, reqVO.getRerankingEnable())
                .likeIfPresent(KmcKnowledgeBaseDO::getRerankingProviderName, reqVO.getRerankingProviderName())
                .likeIfPresent(KmcKnowledgeBaseDO::getRerankingModelName, reqVO.getRerankingModelName())
                .eqIfPresent(KmcKnowledgeBaseDO::getTopK, reqVO.getTopK())
                .eqIfPresent(KmcKnowledgeBaseDO::getScoreThresholdEnabled, reqVO.getScoreThresholdEnabled())
                .eqIfPresent(KmcKnowledgeBaseDO::getScoreThreshold, reqVO.getScoreThreshold())
                .eqIfPresent(KmcKnowledgeBaseDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(KmcKnowledgeBaseDO::getValidFlag, reqVO.getValidFlag());

        // 添加权限条件：用户有权限的知识库 或者 用户自己创建的知识库
        if (idList != null && !idList.isEmpty() && !isAdmin) {
            queryWrapper.and(wrapper ->
                    wrapper.in(KmcKnowledgeBaseDO::getId, idList)
                            .or()
                            .eq(KmcKnowledgeBaseDO::getCreatorId, userId)
            );
        } else if (!isAdmin) {
            // 如果没有角色权限，则只显示用户自己创建的知识库
            queryWrapper.eqIfPresent(KmcKnowledgeBaseDO::getCreatorId, userId);
        }
        queryWrapper.orderByDesc(KmcKnowledgeBaseDO::getValidFlag);
        queryWrapper.orderByDesc(KmcKnowledgeBaseDO::getCreateTime);
        // 构造动态查询条件
        return selectPage(reqVO, queryWrapper
                // 如果 reqVO.getName() 不为空，则添加 name 的精确匹配条件（name = '<name>'）
                // .likeIfPresent(KmcKnowledgeBaseDO::getName, reqVO.getName())
                // 按照 createTime 字段降序排序
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }

    default List<KmcKnowledgeBaseDO> getKmcKnowledgeBaseList(Set<Long> idList, Long userId, Boolean isAdmin, Boolean isValid) {
        LambdaQueryWrapperX<KmcKnowledgeBaseDO> queryWrapper = new LambdaQueryWrapperX<KmcKnowledgeBaseDO>();

        queryWrapper.eqIfPresent(KmcKnowledgeBaseDO::getValidFlag, isValid);
        queryWrapper.orderByDesc(KmcKnowledgeBaseDO::getCreateTime);

        // 添加权限条件：用户有权限的知识库 或者 用户自己创建的知识库
        if (!idList.isEmpty() && !isAdmin) {
            queryWrapper.and(wrapper ->
                    wrapper.in(KmcKnowledgeBaseDO::getId, idList)
                            .or()
                            .eq(KmcKnowledgeBaseDO::getCreatorId, userId)
            );
        } else if (!isAdmin) {
            // 如果没有角色权限，则只显示用户自己创建的知识库
            queryWrapper.eqIfPresent(KmcKnowledgeBaseDO::getCreatorId, userId);
        }
        return selectList(queryWrapper);
    }
}
