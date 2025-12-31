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

package tech.qiantong.qknow.module.ext.dal.mapper.extStructTask;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.neo4j.repository.query.Query;
import tech.qiantong.qknow.module.ext.dal.dataobject.extStructTask.ExtStructTaskDO;
import java.util.Arrays;
import com.github.yulichang.base.MPJBaseMapper;
import tech.qiantong.qknow.common.core.page.PageResult;
import java.util.HashSet;
import java.util.Set;
import tech.qiantong.qknow.module.ext.controller.admin.extStructTask.vo.ExtStructTaskPageReqVO;
import tech.qiantong.qknow.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qknow.mybatis.core.query.LambdaQueryWrapperX;

import javax.websocket.server.PathParam;

/**
 * 结构化抽取任务Mapper接口
 *
 * @author qknow
 * @date 2025-02-25
 */
public interface ExtStructTaskMapper extends BaseMapperX<ExtStructTaskDO> {

    default PageResult<ExtStructTaskDO> selectPage(ExtStructTaskPageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "publish_time","create_time", "update_time"));

        // 构造动态查询条件
        return selectPage(reqVO, new LambdaQueryWrapperX<ExtStructTaskDO>()
                .eqIfPresent(ExtStructTaskDO::getWorkspaceId, reqVO.getWorkspaceId())
                .eqIfPresent(ExtStructTaskDO::getId, reqVO.getId())
                .likeIfPresent(ExtStructTaskDO::getName, reqVO.getName())
                .eqIfPresent(ExtStructTaskDO::getStatus, reqVO.getStatus())
                .eqIfPresent(ExtStructTaskDO::getPublishStatus, reqVO.getPublishStatus())
                .eqIfPresent(ExtStructTaskDO::getPublishTime, reqVO.getPublishTime())
                .eqIfPresent(ExtStructTaskDO::getPublisherId, reqVO.getPublisherId())
                .eqIfPresent(ExtStructTaskDO::getPublishBy, reqVO.getPublishBy())
                .eqIfPresent(ExtStructTaskDO::getDatasourceId, reqVO.getDatasourceId())
                .likeIfPresent(ExtStructTaskDO::getDatasourceName, reqVO.getDatasourceName())
                .eqIfPresent(ExtStructTaskDO::getCreateTime, reqVO.getCreateTime())
                .betweenIfPresent(ExtStructTaskDO::getCreateTime, reqVO.getParamByKey("beginCreateTime"), reqVO.getParamByKey("endCreateTime"))
                // 如果 reqVO.getName() 不为空，则添加 name 的精确匹配条件（name = '<name>'）
                // .likeIfPresent(ExtStructTaskDO::getName, reqVO.getName())
                // 按照 createTime 字段降序排序
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }

}
