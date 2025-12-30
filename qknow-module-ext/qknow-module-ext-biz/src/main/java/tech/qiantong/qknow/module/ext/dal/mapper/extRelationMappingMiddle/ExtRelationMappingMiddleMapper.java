package tech.qiantong.qknow.module.ext.dal.mapper.extRelationMappingMiddle;

import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.module.ext.controller.admin.extRelationMappingMiddle.vo.ExtRelationMappingMiddlePageReqVO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extRelationMappingMiddle.ExtRelationMappingMiddleDO;
import tech.qiantong.qknow.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qknow.mybatis.core.query.LambdaQueryWrapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 关系映射中间Mapper接口
 *
 * @author qknow
 * @date 2025-12-16
 */
public interface ExtRelationMappingMiddleMapper extends BaseMapperX<ExtRelationMappingMiddleDO> {

    default PageResult<ExtRelationMappingMiddleDO> selectPage(ExtRelationMappingMiddlePageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // 构造动态查询条件
        return selectPage(reqVO, new LambdaQueryWrapperX<ExtRelationMappingMiddleDO>()
                .eqIfPresent(ExtRelationMappingMiddleDO::getRelationId, reqVO.getRelationId())
                .likeIfPresent(ExtRelationMappingMiddleDO::getTableName, reqVO.getTableName())
                .eqIfPresent(ExtRelationMappingMiddleDO::getTableField, reqVO.getTableField())
                .eqIfPresent(ExtRelationMappingMiddleDO::getRelationField, reqVO.getRelationField())
                .eqIfPresent(ExtRelationMappingMiddleDO::getCreateTime, reqVO.getCreateTime())
                // 如果 reqVO.getName() 不为空，则添加 name 的精确匹配条件（name = '<name>'）
                // .likeIfPresent(ExtRelationMappingMiddleDO::getName, reqVO.getName())
                // 按照 createTime 字段降序排序
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }
}
