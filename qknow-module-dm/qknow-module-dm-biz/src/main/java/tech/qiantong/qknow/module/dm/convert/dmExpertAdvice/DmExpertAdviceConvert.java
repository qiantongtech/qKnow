package tech.qiantong.qknow.module.dm.convert.dmExpertAdvice;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qknow.module.dm.controller.admin.dmExpertAdvice.vo.DmExpertAdvicePageReqVO;
import tech.qiantong.qknow.module.dm.controller.admin.dmExpertAdvice.vo.DmExpertAdviceRespVO;
import tech.qiantong.qknow.module.dm.controller.admin.dmExpertAdvice.vo.DmExpertAdviceSaveReqVO;
import tech.qiantong.qknow.module.dm.dal.dataobject.dmExpertAdvice.DmExpertAdviceDO;

/**
 * 专家经验 Convert
 *
 * @author qknow
 * @date 2025-02-20
 */
@Mapper
public interface DmExpertAdviceConvert {
    DmExpertAdviceConvert INSTANCE = Mappers.getMapper(DmExpertAdviceConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param dmExpertAdvicePageReqVO 请求参数
     * @return DmExpertAdviceDO
     */
    DmExpertAdviceDO convertToDO(DmExpertAdvicePageReqVO dmExpertAdvicePageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param dmExpertAdviceSaveReqVO 保存请求参数
     * @return DmExpertAdviceDO
     */
    DmExpertAdviceDO convertToDO(DmExpertAdviceSaveReqVO dmExpertAdviceSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param dmExpertAdviceDO 实体对象
     * @return DmExpertAdviceRespVO
     */
    DmExpertAdviceRespVO convertToRespVO(DmExpertAdviceDO dmExpertAdviceDO);

    /**
     * DOList 转换为 RespVOList
     * @param dmExpertAdviceDOList 实体对象列表
     * @return List<DmExpertAdviceRespVO>
     */
    List<DmExpertAdviceRespVO> convertToRespVOList(List<DmExpertAdviceDO> dmExpertAdviceDOList);
}
