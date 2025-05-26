package tech.qiantong.qknow.module.dm.convert.dmMeasurePoint;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qknow.module.dm.controller.admin.dmMeasurePoint.vo.DmMeasurePointPageReqVO;
import tech.qiantong.qknow.module.dm.controller.admin.dmMeasurePoint.vo.DmMeasurePointRespVO;
import tech.qiantong.qknow.module.dm.controller.admin.dmMeasurePoint.vo.DmMeasurePointSaveReqVO;
import tech.qiantong.qknow.module.dm.dal.dataobject.dmMeasurePoint.DmMeasurePointDO;

/**
 * 物联网测点 Convert
 *
 * @author qknow
 * @date 2025-02-20
 */
@Mapper
public interface DmMeasurePointConvert {
    DmMeasurePointConvert INSTANCE = Mappers.getMapper(DmMeasurePointConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param dmMeasurePointPageReqVO 请求参数
     * @return DmMeasurePointDO
     */
    DmMeasurePointDO convertToDO(DmMeasurePointPageReqVO dmMeasurePointPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param dmMeasurePointSaveReqVO 保存请求参数
     * @return DmMeasurePointDO
     */
    DmMeasurePointDO convertToDO(DmMeasurePointSaveReqVO dmMeasurePointSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param dmMeasurePointDO 实体对象
     * @return DmMeasurePointRespVO
     */
    DmMeasurePointRespVO convertToRespVO(DmMeasurePointDO dmMeasurePointDO);

    /**
     * DOList 转换为 RespVOList
     * @param dmMeasurePointDOList 实体对象列表
     * @return List<DmMeasurePointRespVO>
     */
    List<DmMeasurePointRespVO> convertToRespVOList(List<DmMeasurePointDO> dmMeasurePointDOList);
}
