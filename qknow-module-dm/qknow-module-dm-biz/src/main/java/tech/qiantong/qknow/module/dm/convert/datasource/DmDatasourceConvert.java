package tech.qiantong.qknow.module.dm.convert.datasource;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qknow.module.dm.controller.admin.datasource.vo.DmDatasourcePageReqVO;
import tech.qiantong.qknow.module.dm.controller.admin.datasource.vo.DmDatasourceRespVO;
import tech.qiantong.qknow.module.dm.controller.admin.datasource.vo.DmDatasourceSaveReqVO;
import tech.qiantong.qknow.module.dm.dal.dataobject.datasource.DmDatasourceDO;


import java.util.List;

/**
 * 数据源 Convert
 *
 * @author lhs
 * @date 2025-01-21
 */
@Mapper
public interface DmDatasourceConvert {
    DmDatasourceConvert INSTANCE = Mappers.getMapper(DmDatasourceConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param daDatasourcePageReqVO 请求参数
     * @return DaDatasourceDO
     */
     DmDatasourceDO convertToDO(DmDatasourcePageReqVO daDatasourcePageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param daDatasourceSaveReqVO 保存请求参数
     * @return DaDatasourceDO
     */
     DmDatasourceDO convertToDO(DmDatasourceSaveReqVO daDatasourceSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param daDatasourceDO 实体对象
     * @return DaDatasourceRespVO
     */
     DmDatasourceRespVO convertToRespVO(DmDatasourceDO daDatasourceDO);

    /**
     * DOList 转换为 RespVOList
     * @param daDatasourceDOList 实体对象列表
     * @return List<DaDatasourceRespVO>
     */
     List<DmDatasourceRespVO> convertToRespVOList(List<DmDatasourceDO> daDatasourceDOList);
}
