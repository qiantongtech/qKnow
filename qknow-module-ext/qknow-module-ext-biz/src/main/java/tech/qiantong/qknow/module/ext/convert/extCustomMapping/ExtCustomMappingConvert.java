package tech.qiantong.qknow.module.ext.convert.extCustomMapping;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qknow.module.ext.controller.admin.extCustomMapping.vo.ExtCustomMappingPageReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extCustomMapping.vo.ExtCustomMappingRespVO;
import tech.qiantong.qknow.module.ext.controller.admin.extCustomMapping.vo.ExtCustomMappingSaveReqVO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extCustomMapping.ExtCustomMappingDO;

/**
 * 自定义映射 Convert
 *
 * @author qknow
 * @date 2025-02-25
 */
@Mapper
public interface ExtCustomMappingConvert {
    ExtCustomMappingConvert INSTANCE = Mappers.getMapper(ExtCustomMappingConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param extCustomMappingPageReqVO 请求参数
     * @return ExtCustomMappingDO
     */
     ExtCustomMappingDO convertToDO(ExtCustomMappingPageReqVO extCustomMappingPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param extCustomMappingSaveReqVO 保存请求参数
     * @return ExtCustomMappingDO
     */
     ExtCustomMappingDO convertToDO(ExtCustomMappingSaveReqVO extCustomMappingSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param extCustomMappingDO 实体对象
     * @return ExtCustomMappingRespVO
     */
     ExtCustomMappingRespVO convertToRespVO(ExtCustomMappingDO extCustomMappingDO);

    /**
     * DOList 转换为 RespVOList
     * @param extCustomMappingDOList 实体对象列表
     * @return List<ExtCustomMappingRespVO>
     */
     List<ExtCustomMappingRespVO> convertToRespVOList(List<ExtCustomMappingDO> extCustomMappingDOList);
}
