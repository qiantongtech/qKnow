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

package tech.qiantong.qknow.module.ext.dal.mapper.extUnstructTask;

import org.apache.ibatis.annotations.Update;
import tech.qiantong.qknow.module.ext.dal.dataobject.extStructTask.ExtStructTaskDO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extUnstructTask.ExtUnstructTaskDO;
import java.util.Arrays;
import com.github.yulichang.base.MPJBaseMapper;
import tech.qiantong.qknow.common.core.page.PageResult;
import java.util.HashSet;
import java.util.Set;
import tech.qiantong.qknow.module.ext.controller.admin.extUnstructTask.vo.ExtUnstructTaskPageReqVO;
import tech.qiantong.qknow.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qknow.mybatis.core.query.LambdaQueryWrapperX;

/**
 * 非结构化抽取任务Mapper接口
 *
 * @author qknow
 * @date 2025-02-18
 */
public interface ExtUnstructTaskMapper extends BaseMapperX<ExtUnstructTaskDO> {

    default PageResult<ExtUnstructTaskDO> selectPage(ExtUnstructTaskPageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id","publish_time", "create_time", "update_time"));

        // 构造动态查询条件
        return selectPage(reqVO, new LambdaQueryWrapperX<ExtUnstructTaskDO>()
                .eqIfPresent(ExtUnstructTaskDO::getId, reqVO.getId())
                .eqIfPresent(ExtUnstructTaskDO::getWorkspaceId, reqVO.getWorkspaceId())
                .likeIfPresent(ExtUnstructTaskDO::getName, reqVO.getName())
                .eqIfPresent(ExtUnstructTaskDO::getStatus, reqVO.getStatus())
                .eqIfPresent(ExtUnstructTaskDO::getPublishStatus, reqVO.getPublishStatus())
                .eqIfPresent(ExtUnstructTaskDO::getPublishTime, reqVO.getPublishTime())
                .eqIfPresent(ExtUnstructTaskDO::getPublisherId, reqVO.getPublisherId())
                .eqIfPresent(ExtUnstructTaskDO::getPublishBy, reqVO.getPublishBy())
                .eqIfPresent(ExtUnstructTaskDO::getCreateTime, reqVO.getCreateTime())
                .betweenIfPresent(ExtUnstructTaskDO::getCreateTime, reqVO.getParamByKey("beginCreateTime"), reqVO.getParamByKey("endCreateTime"))
                // 如果 reqVO.getName() 不为空，则添加 name 的精确匹配条件（name = '<name>'）
                // .likeIfPresent(ExtUnstructTaskDO::getName, reqVO.getName())
                // 按照 createTime 字段降序排序
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }

    // 选择性更新：只更新非空字段
    @Update("<script>" +
            "UPDATE ext_unstruct_task " +
            "<set>" +
            "<if test='status != null'>status = #{status},</if>" +
            "</set>" +
            "WHERE id = #{id}" +
            "</script>")
    int updateExtUnstructTask(ExtUnstructTaskDO extUnstructTaskDO);
}
