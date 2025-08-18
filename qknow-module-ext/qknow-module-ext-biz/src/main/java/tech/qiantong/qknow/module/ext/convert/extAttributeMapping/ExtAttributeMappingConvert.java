package tech.qiantong.qknow.module.ext.convert.extAttributeMapping;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qknow.module.ext.controller.admin.extAttributeMapping.vo.ExtAttributeMappingPageReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extAttributeMapping.vo.ExtAttributeMappingRespVO;
import tech.qiantong.qknow.module.ext.controller.admin.extAttributeMapping.vo.ExtAttributeMappingSaveReqVO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extAttributeMapping.ExtAttributeMappingDO;

/**
 * 属性映射 Convert
 *
 * @author qknow
 * @date 2025-02-25
 */
@Mapper
public interface ExtAttributeMappingConvert {
    ExtAttributeMappingConvert INSTANCE = Mappers.getMapper(ExtAttributeMappingConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param extAttributeMappingPageReqVO 请求参数
     * @return ExtAttributeMappingDO
     */
     ExtAttributeMappingDO convertToDO(ExtAttributeMappingPageReqVO extAttributeMappingPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param extAttributeMappingSaveReqVO 保存请求参数
     * @return ExtAttributeMappingDO
     */
     ExtAttributeMappingDO convertToDO(ExtAttributeMappingSaveReqVO extAttributeMappingSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param extAttributeMappingDO 实体对象
     * @return ExtAttributeMappingRespVO
     */
     ExtAttributeMappingRespVO convertToRespVO(ExtAttributeMappingDO extAttributeMappingDO);

    /**
     * DOList 转换为 RespVOList
     * @param extAttributeMappingDOList 实体对象列表
     * @return List<ExtAttributeMappingRespVO>
     */
     List<ExtAttributeMappingRespVO> convertToRespVOList(List<ExtAttributeMappingDO> extAttributeMappingDOList);
}
