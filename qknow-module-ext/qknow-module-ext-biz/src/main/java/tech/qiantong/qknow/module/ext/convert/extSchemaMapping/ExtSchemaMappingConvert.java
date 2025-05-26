package tech.qiantong.qknow.module.ext.convert.extSchemaMapping;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qknow.module.ext.controller.admin.extSchemaMapping.vo.ExtSchemaMappingPageReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extSchemaMapping.vo.ExtSchemaMappingRespVO;
import tech.qiantong.qknow.module.ext.controller.admin.extSchemaMapping.vo.ExtSchemaMappingSaveReqVO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extSchemaMapping.ExtSchemaMappingDO;

/**
 * 概念映射 Convert
 *
 * @author qknow
 * @date 2025-02-25
 */
@Mapper
public interface ExtSchemaMappingConvert {
    ExtSchemaMappingConvert INSTANCE = Mappers.getMapper(ExtSchemaMappingConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param extSchemaMappingPageReqVO 请求参数
     * @return ExtSchemaMappingDO
     */
     ExtSchemaMappingDO convertToDO(ExtSchemaMappingPageReqVO extSchemaMappingPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param extSchemaMappingSaveReqVO 保存请求参数
     * @return ExtSchemaMappingDO
     */
     ExtSchemaMappingDO convertToDO(ExtSchemaMappingSaveReqVO extSchemaMappingSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param extSchemaMappingDO 实体对象
     * @return ExtSchemaMappingRespVO
     */
     ExtSchemaMappingRespVO convertToRespVO(ExtSchemaMappingDO extSchemaMappingDO);

    /**
     * DOList 转换为 RespVOList
     * @param extSchemaMappingDOList 实体对象列表
     * @return List<ExtSchemaMappingRespVO>
     */
     List<ExtSchemaMappingRespVO> convertToRespVOList(List<ExtSchemaMappingDO> extSchemaMappingDOList);
}
