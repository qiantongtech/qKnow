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

package tech.qiantong.qknow.module.ext.dal.mapper.extUnstructTaskText;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import tech.qiantong.qknow.module.ext.dal.dataobject.extUnstructTaskText.ExtUnstructTaskTextDO;

import java.util.Arrays;

import com.github.yulichang.base.MPJBaseMapper;
import tech.qiantong.qknow.common.core.page.PageResult;

import java.util.HashSet;
import java.util.Set;

import tech.qiantong.qknow.module.ext.controller.admin.extUnstructTaskText.vo.ExtUnstructTaskTextPageReqVO;
import tech.qiantong.qknow.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qknow.mybatis.core.query.LambdaQueryWrapperX;

/**
 * 任务文件段落关联Mapper接口
 *
 * @author qknow
 * @date 2025-02-21
 */
public interface ExtUnstructTaskTextMapper extends BaseMapperX<ExtUnstructTaskTextDO> {

    default PageResult<ExtUnstructTaskTextDO> selectPage(ExtUnstructTaskTextPageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // 构造动态查询条件
        return selectPage(reqVO, new LambdaQueryWrapperX<ExtUnstructTaskTextDO>()
                .eqIfPresent(ExtUnstructTaskTextDO::getWorkspaceId, reqVO.getWorkspaceId())
                .eqIfPresent(ExtUnstructTaskTextDO::getTaskId, reqVO.getTaskId())
                .eqIfPresent(ExtUnstructTaskTextDO::getDocId, reqVO.getDocId())
                .eqIfPresent(ExtUnstructTaskTextDO::getParagraphIndex, reqVO.getParagraphIndex())
                .eqIfPresent(ExtUnstructTaskTextDO::getText, reqVO.getText())
                .eqIfPresent(ExtUnstructTaskTextDO::getCreateTime, reqVO.getCreateTime())
                // 如果 reqVO.getName() 不为空，则添加 name 的精确匹配条件（name = '<name>'）
                // .likeIfPresent(ExtUnstructTaskTextDO::getName, reqVO.getName())
                // 按照 createTime 字段降序排序
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }

    @Delete("DELETE FROM ext_unstruct_task_text WHERE task_id = #{taskId}")
    int deleteByTaskId(@Param("taskId") Long taskId);
}
