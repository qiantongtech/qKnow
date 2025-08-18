package tech.qiantong.qknow.module.ext.convert.extUnstructTaskDocRel;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qknow.module.ext.controller.admin.extUnstructTaskDocRel.vo.ExtUnstructTaskDocRelPageReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extUnstructTaskDocRel.vo.ExtUnstructTaskDocRelRespVO;
import tech.qiantong.qknow.module.ext.controller.admin.extUnstructTaskDocRel.vo.ExtUnstructTaskDocRelSaveReqVO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extUnstructTaskDocRel.ExtUnstructTaskDocRelDO;

/**
 * 任务文件关联 Convert
 *
 * @author qknow
 * @date 2025-02-19
 */
@Mapper
public interface ExtUnstructTaskDocRelConvert {
    ExtUnstructTaskDocRelConvert INSTANCE = Mappers.getMapper(ExtUnstructTaskDocRelConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param extUnstructTaskDocRelPageReqVO 请求参数
     * @return ExtUnstructTaskDocRelDO
     */
     ExtUnstructTaskDocRelDO convertToDO(ExtUnstructTaskDocRelPageReqVO extUnstructTaskDocRelPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param extUnstructTaskDocRelSaveReqVO 保存请求参数
     * @return ExtUnstructTaskDocRelDO
     */
     ExtUnstructTaskDocRelDO convertToDO(ExtUnstructTaskDocRelSaveReqVO extUnstructTaskDocRelSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param extUnstructTaskDocRelDO 实体对象
     * @return ExtUnstructTaskDocRelRespVO
     */
     ExtUnstructTaskDocRelRespVO convertToRespVO(ExtUnstructTaskDocRelDO extUnstructTaskDocRelDO);

    /**
     * DOList 转换为 RespVOList
     * @param extUnstructTaskDocRelDOList 实体对象列表
     * @return List<ExtUnstructTaskDocRelRespVO>
     */
     List<ExtUnstructTaskDocRelRespVO> convertToRespVOList(List<ExtUnstructTaskDocRelDO> extUnstructTaskDocRelDOList);
}
