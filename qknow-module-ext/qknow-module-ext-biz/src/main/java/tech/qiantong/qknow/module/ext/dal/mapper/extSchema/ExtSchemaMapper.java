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

package tech.qiantong.qknow.module.ext.dal.mapper.extSchema;

import tech.qiantong.qknow.module.ext.dal.dataobject.extSchema.ExtSchemaDO;
import java.util.Arrays;
import com.github.yulichang.base.MPJBaseMapper;
import tech.qiantong.qknow.common.core.page.PageResult;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import tech.qiantong.qknow.module.ext.controller.admin.extSchema.vo.ExtSchemaPageReqVO;
import tech.qiantong.qknow.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qknow.mybatis.core.query.LambdaQueryWrapperX;

/**
 * 概念配置Mapper接口
 *
 * @author qknow
 * @date 2025-02-17
 */
public interface ExtSchemaMapper extends BaseMapperX<ExtSchemaDO> {

    default PageResult<ExtSchemaDO> selectPage(ExtSchemaPageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // 构造动态查询条件
        return selectPage(reqVO, new LambdaQueryWrapperX<ExtSchemaDO>()
                .eqIfPresent(ExtSchemaDO::getWorkspaceId, reqVO.getWorkspaceId())
                .likeIfPresent(ExtSchemaDO::getName, reqVO.getName())
                .likeIfPresent(ExtSchemaDO::getCreateBy, reqVO.getCreateBy())
                .eqIfPresent(ExtSchemaDO::getDescription, reqVO.getDescription())
                .eqIfPresent(ExtSchemaDO::getCreateTime, reqVO.getCreateTime())
                //添加时，用于精确查询，以便判断有没有重复数据
                .eqIfPresent(ExtSchemaDO::getName, reqVO.getExactName())
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }

    List<ExtSchemaDO> getExtSchemaAllList(ExtSchemaDO extSchemaDO);
}
