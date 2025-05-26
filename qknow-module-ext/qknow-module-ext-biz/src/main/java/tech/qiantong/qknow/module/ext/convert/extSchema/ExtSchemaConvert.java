package tech.qiantong.qknow.module.ext.convert.extSchema;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qknow.module.ext.controller.admin.extSchema.vo.ExtSchemaPageReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extSchema.vo.ExtSchemaRespVO;
import tech.qiantong.qknow.module.ext.controller.admin.extSchema.vo.ExtSchemaSaveReqVO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extSchema.ExtSchemaDO;

/**
 * 概念配置 Convert
 *
 * @author qknow
 * @date 2025-02-17
 */
@Mapper
public interface ExtSchemaConvert {
    ExtSchemaConvert INSTANCE = Mappers.getMapper(ExtSchemaConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param extSchemaPageReqVO 请求参数
     * @return ExtSchemaDO
     */
    ExtSchemaDO convertToDO(ExtSchemaPageReqVO extSchemaPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param extSchemaSaveReqVO 保存请求参数
     * @return ExtSchemaDO
     */
    ExtSchemaDO convertToDO(ExtSchemaSaveReqVO extSchemaSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param extSchemaDO 实体对象
     * @return ExtFaultRespVO
     */
    ExtSchemaRespVO convertToRespVO(ExtSchemaDO extSchemaDO);

    /**
     * DOList 转换为 RespVOList
     * @param extSchemaDOList 实体对象列表
     * @return List<ExtFaultRespVO>
     */
    List<ExtSchemaRespVO> convertToRespVOList(List<ExtSchemaDO> extSchemaDOList);
}
