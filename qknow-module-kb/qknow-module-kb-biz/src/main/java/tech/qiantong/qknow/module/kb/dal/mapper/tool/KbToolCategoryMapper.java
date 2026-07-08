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

package tech.qiantong.qknow.module.kb.dal.mapper.tool;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.module.kb.controller.admin.tool.vo.KbToolCategoryPageReqVO;
import tech.qiantong.qknow.module.kb.dal.dataobject.tool.KbToolCategoryDO;
import tech.qiantong.qknow.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qknow.mybatis.core.query.LambdaQueryWrapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 知识分类Mapper接口
 *
 * @author qknow
 * @date 2025-02-13
 */
@Mapper
public interface KbToolCategoryMapper extends BaseMapperX<KbToolCategoryDO> {

    default PageResult<KbToolCategoryDO> selectPage(KbToolCategoryPageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // 构造动态查询条件
        return selectPage(reqVO, new LambdaQueryWrapperX<KbToolCategoryDO>()
                .eqIfPresent(KbToolCategoryDO::getWorkspaceId, reqVO.getWorkspaceId())
                .eqIfPresent(KbToolCategoryDO::getParentId, reqVO.getParentId())
                .likeIfPresent(KbToolCategoryDO::getName, reqVO.getName())
                .eqIfPresent(KbToolCategoryDO::getOrderNum, reqVO.getOrderNum())
                .eqIfPresent(KbToolCategoryDO::getAncestors, reqVO.getAncestors())
                .eqIfPresent(KbToolCategoryDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(KbToolCategoryDO::getUpdaterId, reqVO.getUpdaterId())
                // 如果 reqVO.getName() 不为空，则添加 name 的精确匹配条件（name = '<name>'）
                // .likeIfPresent(kmcCategoryDO::getName, reqVO.getName())
                // 按照 createTime 字段降序排序
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }

    List<KbToolCategoryDO> getToolCategoryAllList(KbToolCategoryDO kmcCategoryDO);

    KbToolCategoryDO selectToolCategoryById(Long id);

    /**
     * 根据父分类ID查询所有子分类
     *
     * @param parentId 父分类ID
     * @return 子分类列表
     */
    List<KbToolCategoryDO> selectChildrenCategoryById(@Param("parentId") Long parentId);

}
