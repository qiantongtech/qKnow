package tech.qiantong.qknow.module.ext.convert.extUnstructTask;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qknow.module.ext.controller.admin.extUnstructTask.vo.ExtUnstructTaskPageReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extUnstructTask.vo.ExtUnstructTaskRespVO;
import tech.qiantong.qknow.module.ext.controller.admin.extUnstructTask.vo.ExtUnstructTaskSaveReqVO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extUnstructTask.ExtUnstructTaskDO;

/**
 * 非结构化抽取任务 Convert
 *
 * @author qknow
 * @date 2025-02-18
 */
@Mapper
public interface ExtUnstructTaskConvert {
    ExtUnstructTaskConvert INSTANCE = Mappers.getMapper(ExtUnstructTaskConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param extUnstructTaskPageReqVO 请求参数
     * @return ExtUnstructTaskDO
     */
     ExtUnstructTaskDO convertToDO(ExtUnstructTaskPageReqVO extUnstructTaskPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param extUnstructTaskSaveReqVO 保存请求参数
     * @return ExtUnstructTaskDO
     */
     ExtUnstructTaskDO convertToDO(ExtUnstructTaskSaveReqVO extUnstructTaskSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param extUnstructTaskDO 实体对象
     * @return ExtUnstructTaskRespVO
     */
     ExtUnstructTaskRespVO convertToRespVO(ExtUnstructTaskDO extUnstructTaskDO);

    /**
     * DOList 转换为 RespVOList
     * @param extUnstructTaskDOList 实体对象列表
     * @return List<ExtUnstructTaskRespVO>
     */
     List<ExtUnstructTaskRespVO> convertToRespVOList(List<ExtUnstructTaskDO> extUnstructTaskDOList);
}
