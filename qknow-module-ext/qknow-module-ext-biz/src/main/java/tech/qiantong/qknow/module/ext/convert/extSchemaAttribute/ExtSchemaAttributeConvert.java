package tech.qiantong.qknow.module.ext.convert.extSchemaAttribute;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qknow.module.ext.controller.admin.extSchemaAttribute.vo.ExtSchemaAttributePageReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extSchemaAttribute.vo.ExtSchemaAttributeRespVO;
import tech.qiantong.qknow.module.ext.controller.admin.extSchemaAttribute.vo.ExtSchemaAttributeSaveReqVO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extSchemaAttribute.ExtSchemaAttributeDO;

/**
 * 概念属性 Convert
 *
 * @author qknow
 * @date 2025-02-17
 */
@Mapper
public interface ExtSchemaAttributeConvert {
    ExtSchemaAttributeConvert INSTANCE = Mappers.getMapper(ExtSchemaAttributeConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param extSchemaAttributePageReqVO 请求参数
     * @return ExtSchemaAttributeDO
     */
    ExtSchemaAttributeDO convertToDO(ExtSchemaAttributePageReqVO extSchemaAttributePageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param extSchemaAttributeSaveReqVO 保存请求参数
     * @return ExtSchemaAttributeDO
     */
    ExtSchemaAttributeDO convertToDO(ExtSchemaAttributeSaveReqVO extSchemaAttributeSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param extSchemaAttributeDO 实体对象
     * @return ExtSchemaAttributeRespVO
     */
    ExtSchemaAttributeRespVO convertToRespVO(ExtSchemaAttributeDO extSchemaAttributeDO);

    /**
     * DOList 转换为 RespVOList
     * @param extSchemaAttributeDOList 实体对象列表
     * @return List<ExtSchemaAttributeRespVO>
     */
    List<ExtSchemaAttributeRespVO> convertToRespVOList(List<ExtSchemaAttributeDO> extSchemaAttributeDOList);
}
