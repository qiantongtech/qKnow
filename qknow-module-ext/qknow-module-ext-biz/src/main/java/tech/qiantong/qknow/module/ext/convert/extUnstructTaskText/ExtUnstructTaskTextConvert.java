package tech.qiantong.qknow.module.ext.convert.extUnstructTaskText;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qknow.module.ext.controller.admin.extUnstructTaskText.vo.ExtUnstructTaskTextPageReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extUnstructTaskText.vo.ExtUnstructTaskTextRespVO;
import tech.qiantong.qknow.module.ext.controller.admin.extUnstructTaskText.vo.ExtUnstructTaskTextSaveReqVO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extUnstructTaskText.ExtUnstructTaskTextDO;

/**
 * 任务文件段落关联 Convert
 *
 * @author qknow
 * @date 2025-02-21
 */
@Mapper
public interface ExtUnstructTaskTextConvert {
    ExtUnstructTaskTextConvert INSTANCE = Mappers.getMapper(ExtUnstructTaskTextConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param extUnstructTaskTextPageReqVO 请求参数
     * @return ExtUnstructTaskTextDO
     */
     ExtUnstructTaskTextDO convertToDO(ExtUnstructTaskTextPageReqVO extUnstructTaskTextPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param extUnstructTaskTextSaveReqVO 保存请求参数
     * @return ExtUnstructTaskTextDO
     */
     ExtUnstructTaskTextDO convertToDO(ExtUnstructTaskTextSaveReqVO extUnstructTaskTextSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param extUnstructTaskTextDO 实体对象
     * @return ExtUnstructTaskTextRespVO
     */
     ExtUnstructTaskTextRespVO convertToRespVO(ExtUnstructTaskTextDO extUnstructTaskTextDO);

    /**
     * DOList 转换为 RespVOList
     * @param extUnstructTaskTextDOList 实体对象列表
     * @return List<ExtUnstructTaskTextRespVO>
     */
     List<ExtUnstructTaskTextRespVO> convertToRespVOList(List<ExtUnstructTaskTextDO> extUnstructTaskTextDOList);
}
