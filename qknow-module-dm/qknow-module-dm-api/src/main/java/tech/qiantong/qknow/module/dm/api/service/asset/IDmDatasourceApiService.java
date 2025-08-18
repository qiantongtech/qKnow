package tech.qiantong.qknow.module.dm.api.service.asset;


import tech.qiantong.qknow.module.dm.api.datasource.dto.DmDatasourceRespDTO;

/**
 * 数据源Service接口
 *
 * @author lhs
 * @date 2025-01-21
 */
public interface IDmDatasourceApiService {
    public DmDatasourceRespDTO getDatasourceById(Long id);

}
