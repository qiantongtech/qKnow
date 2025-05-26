package tech.qiantong.qknow.module.dm.dal.mapper.datasource;

import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.module.dm.controller.admin.datasource.vo.DmDatasourcePageReqVO;
import tech.qiantong.qknow.module.dm.dal.dataobject.datasource.DmDatasourceDO;
import tech.qiantong.qknow.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qknow.mybatis.core.query.LambdaQueryWrapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 数据源Mapper接口
 *
 * @author lhs
 * @date 2025-01-21
 */
public interface DmDatasourceMapper extends BaseMapperX<DmDatasourceDO> {

    default PageResult<DmDatasourceDO> selectPage(DmDatasourcePageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // 构造动态查询条件
        return selectPage(reqVO, new LambdaQueryWrapperX<DmDatasourceDO>()
                .likeIfPresent(DmDatasourceDO::getDatasourceName, reqVO.getDatasourceName())
                .eqIfPresent(DmDatasourceDO::getDatasourceType, reqVO.getDatasourceType())
                .eqIfPresent(DmDatasourceDO::getDatasourceConfig, reqVO.getDatasourceConfig())
                .eqIfPresent(DmDatasourceDO::getIp, reqVO.getIp())
                .eqIfPresent(DmDatasourceDO::getPort, reqVO.getPort())
                .eqIfPresent(DmDatasourceDO::getListCount, reqVO.getListCount())
                .eqIfPresent(DmDatasourceDO::getSyncCount, reqVO.getSyncCount())
                .eqIfPresent(DmDatasourceDO::getDataSize, reqVO.getDataSize())
                .eqIfPresent(DmDatasourceDO::getDescription, reqVO.getDescription())
                .eqIfPresent(DmDatasourceDO::getCreateTime, reqVO.getCreateTime())
                // 如果 reqVO.getName() 不为空，则添加 name 的精确匹配条件（name = '<name>'）
                // .likeIfPresent(DaDatasourceDO::getName, reqVO.getName())
                // 按照 createTime 字段降序排序
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }

    public List<DmDatasourceDO> getDataSourceByAsset();
}
