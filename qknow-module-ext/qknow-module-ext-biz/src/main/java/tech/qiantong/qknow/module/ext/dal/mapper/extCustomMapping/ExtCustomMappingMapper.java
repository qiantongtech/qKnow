package tech.qiantong.qknow.module.ext.dal.mapper.extCustomMapping;

import tech.qiantong.qknow.module.ext.dal.dataobject.extCustomMapping.ExtCustomMappingDO;
import java.util.Arrays;
import com.github.yulichang.base.MPJBaseMapper;
import tech.qiantong.qknow.common.core.page.PageResult;
import java.util.HashSet;
import java.util.Set;
import tech.qiantong.qknow.module.ext.controller.admin.extCustomMapping.vo.ExtCustomMappingPageReqVO;
import tech.qiantong.qknow.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qknow.mybatis.core.query.LambdaQueryWrapperX;

/**
 * 自定义映射Mapper接口
 *
 * @author qknow
 * @date 2025-02-25
 */
public interface ExtCustomMappingMapper extends BaseMapperX<ExtCustomMappingDO> {

    default PageResult<ExtCustomMappingDO> selectPage(ExtCustomMappingPageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // 构造动态查询条件
        return selectPage(reqVO, new LambdaQueryWrapperX<ExtCustomMappingDO>()
                .eqIfPresent(ExtCustomMappingDO::getWorkspaceId, reqVO.getWorkspaceId())
                .eqIfPresent(ExtCustomMappingDO::getTaskId, reqVO.getTaskId())
                .likeIfPresent(ExtCustomMappingDO::getTableName, reqVO.getTableName())
                .eqIfPresent(ExtCustomMappingDO::getTableComment, reqVO.getTableComment())
                .likeIfPresent(ExtCustomMappingDO::getFieldName, reqVO.getFieldName())
                .eqIfPresent(ExtCustomMappingDO::getFieldComment, reqVO.getFieldComment())
                .eqIfPresent(ExtCustomMappingDO::getSqlStatement, reqVO.getSqlStatement())
                .eqIfPresent(ExtCustomMappingDO::getCreateTime, reqVO.getCreateTime())
                // 如果 reqVO.getName() 不为空，则添加 name 的精确匹配条件（name = '<name>'）
                // .likeIfPresent(ExtCustomMappingDO::getName, reqVO.getName())
                // 按照 createTime 字段降序排序
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }
}
