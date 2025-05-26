package tech.qiantong.qknow.module.ext.convert.extDatasource;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qknow.module.ext.controller.admin.extDatasource.vo.ExtDatasourcePageReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extDatasource.vo.ExtDatasourceRespVO;
import tech.qiantong.qknow.module.ext.controller.admin.extDatasource.vo.ExtDatasourceSaveReqVO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extDatasource.ExtDatasourceDO;

/**
 * 数据源 Convert
 *
 * @author qknow
 * @date 2025-02-25
 */
@Mapper
public interface ExtDatasourceConvert {
    ExtDatasourceConvert INSTANCE = Mappers.getMapper(ExtDatasourceConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param extDatasourcePageReqVO 请求参数
     * @return ExtDatasourceDO
     */
     ExtDatasourceDO convertToDO(ExtDatasourcePageReqVO extDatasourcePageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param extDatasourceSaveReqVO 保存请求参数
     * @return ExtDatasourceDO
     */
     ExtDatasourceDO convertToDO(ExtDatasourceSaveReqVO extDatasourceSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param extDatasourceDO 实体对象
     * @return ExtDatasourceRespVO
     */
     ExtDatasourceRespVO convertToRespVO(ExtDatasourceDO extDatasourceDO);

    /**
     * DOList 转换为 RespVOList
     * @param extDatasourceDOList 实体对象列表
     * @return List<ExtDatasourceRespVO>
     */
     List<ExtDatasourceRespVO> convertToRespVOList(List<ExtDatasourceDO> extDatasourceDOList);
}
