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
