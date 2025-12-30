package tech.qiantong.qknow.module.ext.convert.extRelationMappingMiddle;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qknow.module.ext.controller.admin.extRelationMappingMiddle.vo.ExtRelationMappingMiddlePageReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extRelationMappingMiddle.vo.ExtRelationMappingMiddleRespVO;
import tech.qiantong.qknow.module.ext.controller.admin.extRelationMappingMiddle.vo.ExtRelationMappingMiddleSaveReqVO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extRelationMappingMiddle.ExtRelationMappingMiddleDO;

import java.util.List;

/**
 * 关系映射中间 Convert
 *
 * @author qknow
 * @date 2025-12-16
 */
@Mapper
public interface ExtRelationMappingMiddleConvert {
    ExtRelationMappingMiddleConvert INSTANCE = Mappers.getMapper(ExtRelationMappingMiddleConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param extRelationMappingMiddlePageReqVO 请求参数
     * @return ExtRelationMappingMiddleDO
     */
     ExtRelationMappingMiddleDO convertToDO(ExtRelationMappingMiddlePageReqVO extRelationMappingMiddlePageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param extRelationMappingMiddleSaveReqVO 保存请求参数
     * @return ExtRelationMappingMiddleDO
     */
     ExtRelationMappingMiddleDO convertToDO(ExtRelationMappingMiddleSaveReqVO extRelationMappingMiddleSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param extRelationMappingMiddleDO 实体对象
     * @return ExtRelationMappingMiddleRespVO
     */
     ExtRelationMappingMiddleRespVO convertToRespVO(ExtRelationMappingMiddleDO extRelationMappingMiddleDO);

    /**
     * DOList 转换为 RespVOList
     * @param extRelationMappingMiddleDOList 实体对象列表
     * @return List<ExtRelationMappingMiddleRespVO>
     */
     List<ExtRelationMappingMiddleRespVO> convertToRespVOList(List<ExtRelationMappingMiddleDO> extRelationMappingMiddleDOList);
}
