package tech.qiantong.qknow.module.ext.convert.unstructTaskRelation;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qknow.module.ext.controller.admin.unstructTaskRelation.vo.ExtUnstructTaskRelationPageReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.unstructTaskRelation.vo.ExtUnstructTaskRelationRespVO;
import tech.qiantong.qknow.module.ext.controller.admin.unstructTaskRelation.vo.ExtUnstructTaskRelationSaveReqVO;
import tech.qiantong.qknow.module.ext.dal.dataobject.unstructTaskRelation.ExtUnstructTaskRelationDO;

import java.util.List;

/**
 * 任务文件关联 Convert
 *
 * @author qknow
 * @date 2025-04-03
 */
@Mapper
public interface ExtUnstructTaskRelationConvert {
    ExtUnstructTaskRelationConvert INSTANCE = Mappers.getMapper(ExtUnstructTaskRelationConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param extUnstructTaskRelationPageReqVO 请求参数
     * @return ExtUnstructTaskRelationDO
     */
     ExtUnstructTaskRelationDO convertToDO(ExtUnstructTaskRelationPageReqVO extUnstructTaskRelationPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param extUnstructTaskRelationSaveReqVO 保存请求参数
     * @return ExtUnstructTaskRelationDO
     */
     ExtUnstructTaskRelationDO convertToDO(ExtUnstructTaskRelationSaveReqVO extUnstructTaskRelationSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param extUnstructTaskRelationDO 实体对象
     * @return ExtUnstructTaskRelationRespVO
     */
     ExtUnstructTaskRelationRespVO convertToRespVO(ExtUnstructTaskRelationDO extUnstructTaskRelationDO);

    /**
     * DOList 转换为 RespVOList
     * @param extUnstructTaskRelationDOList 实体对象列表
     * @return List<ExtUnstructTaskRelationRespVO>
     */
     List<ExtUnstructTaskRelationRespVO> convertToRespVOList(List<ExtUnstructTaskRelationDO> extUnstructTaskRelationDOList);
}
