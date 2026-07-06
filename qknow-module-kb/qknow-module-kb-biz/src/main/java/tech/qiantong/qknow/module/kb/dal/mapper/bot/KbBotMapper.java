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

package tech.qiantong.qknow.module.kb.dal.mapper.bot;


import java.util.Arrays;

import tech.qiantong.qknow.common.core.page.PageResult;

import java.util.HashSet;
import java.util.Set;

import tech.qiantong.qknow.module.kb.controller.admin.bot.vo.KbBotPageReqVO;
import tech.qiantong.qknow.module.kb.dal.dataobject.bot.KbBotDO;
import tech.qiantong.qknow.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qknow.mybatis.core.query.LambdaQueryWrapperX;

/**
 * bot 管理Mapper接口
 *
 * @author qknow
 * @date 2026-03-18
 */
public interface KbBotMapper extends BaseMapperX<KbBotDO> {

    default PageResult<KbBotDO> selectPage(KbBotPageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // 构造动态查询条件
        return selectPage(reqVO, new LambdaQueryWrapperX<KbBotDO>()
                .eqIfPresent(KbBotDO::getWorkspaceId, reqVO.getWorkspaceId())
                .likeIfPresent(KbBotDO::getName, reqVO.getName())
                .eqIfPresent(KbBotDO::getType, reqVO.getType())
                .eqIfPresent(KbBotDO::getDescription, reqVO.getDescription())
                .eqIfPresent(KbBotDO::getBuiltinFlag, reqVO.getBuiltinFlag())
                .eqIfPresent(KbBotDO::getCreateTime, reqVO.getCreateTime())
                // 如果 reqVO.getName() 不为空，则添加 name 的精确匹配条件（name = '<name>'）
                // .likeIfPresent(KbBotDO::getName, reqVO.getName())
                // 按照 createTime 字段降序排序
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }
}
