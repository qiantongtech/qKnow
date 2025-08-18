package tech.qiantong.qknow.module.ext.convert.extSchemaRelation;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qknow.module.ext.controller.admin.extSchemaRelation.vo.ExtSchemaRelationPageReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extSchemaRelation.vo.ExtSchemaRelationRespVO;
import tech.qiantong.qknow.module.ext.controller.admin.extSchemaRelation.vo.ExtSchemaRelationSaveReqVO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extSchemaRelation.ExtSchemaRelationDO;

/**
 * 关系配置 Convert
 *
 * @author qknow
 * @date 2025-02-18
 */
@Mapper
public interface ExtSchemaRelationConvert {
    ExtSchemaRelationConvert INSTANCE = Mappers.getMapper(ExtSchemaRelationConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param extSchemaRelationPageReqVO 请求参数
     * @return ExtSchemaRelationDO
     */
    ExtSchemaRelationDO convertToDO(ExtSchemaRelationPageReqVO extSchemaRelationPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param extSchemaRelationSaveReqVO 保存请求参数
     * @return ExtSchemaRelationDO
     */
    ExtSchemaRelationDO convertToDO(ExtSchemaRelationSaveReqVO extSchemaRelationSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param extSchemaRelationDO 实体对象
     * @return ExtSchemaRelationRespVO
     */
    ExtSchemaRelationRespVO convertToRespVO(ExtSchemaRelationDO extSchemaRelationDO);

    /**
     * DOList 转换为 RespVOList
     * @param extSchemaRelationDOList 实体对象列表
     * @return List<ExtSchemaRelationRespVO>
     */
    List<ExtSchemaRelationRespVO> convertToRespVOList(List<ExtSchemaRelationDO> extSchemaRelationDOList);
}
