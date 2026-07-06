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

package tech.qiantong.qknow.module.ai.dal.mapper.modelMarket;

import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.module.ai.controller.admin.modelMarket.vo.AiModelPageReqVO;
import tech.qiantong.qknow.module.ai.dal.dataobject.modelMarket.AiModelDO;
import tech.qiantong.qknow.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qknow.mybatis.core.query.LambdaQueryWrapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * AI 模型Mapper接口
 *
 * @author qknow
 * @date 2025-12-23
 */
public interface AiModelMapper extends BaseMapperX<AiModelDO> {

    default PageResult<AiModelDO> selectPage(AiModelPageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id","name", "create_time", "update_time"));

        // 构造动态查询条件
        return selectPage(reqVO, new LambdaQueryWrapperX<AiModelDO>()
                .eqIfPresent(AiModelDO::getWorkspaceId, reqVO.getWorkspaceId())
                .eqIfPresent(AiModelDO::getKeyId, reqVO.getKeyId())
                .likeIfPresent(AiModelDO::getName, reqVO.getName())
                .eqIfPresent(AiModelDO::getModel, reqVO.getModel())
                .eqIfPresent(AiModelDO::getPlatform, reqVO.getPlatform())
                .eqIfPresent(AiModelDO::getType, reqVO.getType())
                .eqIfPresent(AiModelDO::getSort, reqVO.getSort())
                .eqIfPresent(AiModelDO::getStatus, reqVO.getStatus())
                .eqIfPresent(AiModelDO::getCreateTime, reqVO.getCreateTime())
                // 如果 reqVO.getName() 不为空，则添加 name 的精确匹配条件（name = '<name>'）
                // .likeIfPresent(AiModelDO::getName, reqVO.getName())
                // 按照 createTime 字段降序排序
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }
}
