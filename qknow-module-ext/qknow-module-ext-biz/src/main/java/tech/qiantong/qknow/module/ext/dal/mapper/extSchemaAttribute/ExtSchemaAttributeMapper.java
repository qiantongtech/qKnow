package tech.qiantong.qknow.module.ext.dal.mapper.extSchemaAttribute;

import tech.qiantong.qknow.module.ext.dal.dataobject.extSchemaAttribute.ExtSchemaAttributeDO;
import java.util.Arrays;
import com.github.yulichang.base.MPJBaseMapper;
import tech.qiantong.qknow.common.core.page.PageResult;
import java.util.HashSet;
import java.util.Set;
import tech.qiantong.qknow.module.ext.controller.admin.extSchemaAttribute.vo.ExtSchemaAttributePageReqVO;
import tech.qiantong.qknow.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qknow.mybatis.core.query.LambdaQueryWrapperX;

/**
 * 概念属性Mapper接口
 *
 * @author qknow
 * @date 2025-02-17
 */
public interface ExtSchemaAttributeMapper extends BaseMapperX<ExtSchemaAttributeDO> {

    default PageResult<ExtSchemaAttributeDO> selectPage(ExtSchemaAttributePageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // 构造动态查询条件
        return selectPage(reqVO, new LambdaQueryWrapperX<ExtSchemaAttributeDO>()
                .eqIfPresent(ExtSchemaAttributeDO::getSchemaId, reqVO.getId())
                .eqIfPresent(ExtSchemaAttributeDO::getWorkspaceId, reqVO.getWorkspaceId())
                .eqIfPresent(ExtSchemaAttributeDO::getSchemaId, reqVO.getSchemaId())
                .likeIfPresent(ExtSchemaAttributeDO::getSchemaName, reqVO.getSchemaName())
                .eqIfPresent(ExtSchemaAttributeDO::getName, reqVO.getName())
                .eqIfPresent(ExtSchemaAttributeDO::getNameCode, reqVO.getNameCode())
                .eqIfPresent(ExtSchemaAttributeDO::getRequireFlag, reqVO.getRequireFlag())
                .eqIfPresent(ExtSchemaAttributeDO::getDataType, reqVO.getDataType())
                .eqIfPresent(ExtSchemaAttributeDO::getMultipleFlag, reqVO.getMultipleFlag())
                .eqIfPresent(ExtSchemaAttributeDO::getValidateType, reqVO.getValidateType())
                .eqIfPresent(ExtSchemaAttributeDO::getMinValue, reqVO.getMinValue())
                .eqIfPresent(ExtSchemaAttributeDO::getMaxValue, reqVO.getMaxValue())
                .eqIfPresent(ExtSchemaAttributeDO::getCreateTime, reqVO.getCreateTime())
                // 如果 reqVO.getName() 不为空，则添加 name 的精确匹配条件（name = '<name>'）
                // .likeIfPresent(ExtSchemaAttributeDO::getName, reqVO.getName())
                // 按照 createTime 字段降序排序
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }
}
