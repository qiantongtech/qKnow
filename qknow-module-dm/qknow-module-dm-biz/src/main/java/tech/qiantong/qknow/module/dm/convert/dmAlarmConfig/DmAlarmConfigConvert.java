package tech.qiantong.qknow.module.dm.convert.dmAlarmConfig;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qknow.module.dm.controller.admin.dmAlarmConfig.vo.DmAlarmConfigPageReqVO;
import tech.qiantong.qknow.module.dm.controller.admin.dmAlarmConfig.vo.DmAlarmConfigRespVO;
import tech.qiantong.qknow.module.dm.controller.admin.dmAlarmConfig.vo.DmAlarmConfigSaveReqVO;
import tech.qiantong.qknow.module.dm.dal.dataobject.dmAlarmConfig.DmAlarmConfigDO;

/**
 * 告警配置 Convert
 *
 * @author qknow
 * @date 2025-02-19
 */
@Mapper
public interface DmAlarmConfigConvert {
    DmAlarmConfigConvert INSTANCE = Mappers.getMapper(DmAlarmConfigConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param dmAlarmConfigPageReqVO 请求参数
     * @return DmAlarmConfigDO
     */
    DmAlarmConfigDO convertToDO(DmAlarmConfigPageReqVO dmAlarmConfigPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param dmAlarmConfigSaveReqVO 保存请求参数
     * @return DmAlarmConfigDO
     */
    DmAlarmConfigDO convertToDO(DmAlarmConfigSaveReqVO dmAlarmConfigSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param dmAlarmConfigDO 实体对象
     * @return DmAlarmConfigRespVO
     */
    DmAlarmConfigRespVO convertToRespVO(DmAlarmConfigDO dmAlarmConfigDO);

    /**
     * DOList 转换为 RespVOList
     * @param dmAlarmConfigDOList 实体对象列表
     * @return List<DmAlarmConfigRespVO>
     */
    List<DmAlarmConfigRespVO> convertToRespVOList(List<DmAlarmConfigDO> dmAlarmConfigDOList);
}
