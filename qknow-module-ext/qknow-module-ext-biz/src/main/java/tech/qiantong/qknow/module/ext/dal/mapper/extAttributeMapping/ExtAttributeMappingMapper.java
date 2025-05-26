package tech.qiantong.qknow.module.ext.dal.mapper.extAttributeMapping;

import tech.qiantong.qknow.module.ext.dal.dataobject.extAttributeMapping.ExtAttributeMappingDO;
import java.util.Arrays;
import com.github.yulichang.base.MPJBaseMapper;
import tech.qiantong.qknow.common.core.page.PageResult;
import java.util.HashSet;
import java.util.Set;
import tech.qiantong.qknow.module.ext.controller.admin.extAttributeMapping.vo.ExtAttributeMappingPageReqVO;
import tech.qiantong.qknow.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qknow.mybatis.core.query.LambdaQueryWrapperX;

/**
 * 属性映射Mapper接口
 *
 * @author qknow
 * @date 2025-02-25
 */
public interface ExtAttributeMappingMapper extends BaseMapperX<ExtAttributeMappingDO> {

    default PageResult<ExtAttributeMappingDO> selectPage(ExtAttributeMappingPageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // 构造动态查询条件
        return selectPage(reqVO, new LambdaQueryWrapperX<ExtAttributeMappingDO>()
                .eqIfPresent(ExtAttributeMappingDO::getWorkspaceId, reqVO.getWorkspaceId())
                .eqIfPresent(ExtAttributeMappingDO::getTaskId, reqVO.getTaskId())
                .likeIfPresent(ExtAttributeMappingDO::getTableName, reqVO.getTableName())
                .eqIfPresent(ExtAttributeMappingDO::getTableComment, reqVO.getTableComment())
                .likeIfPresent(ExtAttributeMappingDO::getFieldName, reqVO.getFieldName())
                .eqIfPresent(ExtAttributeMappingDO::getFieldComment, reqVO.getFieldComment())
                .eqIfPresent(ExtAttributeMappingDO::getAttributeId, reqVO.getAttributeId())
                .likeIfPresent(ExtAttributeMappingDO::getAttributeName, reqVO.getAttributeName())
                .eqIfPresent(ExtAttributeMappingDO::getCreateTime, reqVO.getCreateTime())
                // 如果 reqVO.getName() 不为空，则添加 name 的精确匹配条件（name = '<name>'）
                // .likeIfPresent(ExtAttributeMappingDO::getName, reqVO.getName())
                // 按照 createTime 字段降序排序
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }
}
