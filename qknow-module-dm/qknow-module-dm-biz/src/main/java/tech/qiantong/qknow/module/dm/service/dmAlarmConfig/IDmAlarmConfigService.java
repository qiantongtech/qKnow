package tech.qiantong.qknow.module.dm.service.dmAlarmConfig;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.module.dm.controller.admin.dmAlarmConfig.vo.DmAlarmConfigSaveReqVO;
import tech.qiantong.qknow.module.dm.controller.admin.dmAlarmConfig.vo.DmAlarmConfigPageReqVO;
import tech.qiantong.qknow.module.dm.controller.admin.dmAlarmConfig.vo.DmAlarmConfigRespVO;
import tech.qiantong.qknow.module.dm.dal.dataobject.dmAlarmConfig.DmAlarmConfigDO;
/**
 * 告警配置Service接口
 *
 * @author qknow
 * @date 2025-02-19
 */
public interface IDmAlarmConfigService extends IService<DmAlarmConfigDO> {

    /**
     * 获得告警配置分页列表
     *
     * @param pageReqVO 分页请求
     * @return 告警配置分页列表
     */
    PageResult<DmAlarmConfigDO> getDmAlarmConfigPage(DmAlarmConfigPageReqVO pageReqVO);

    /**
     * 创建告警配置
     *
     * @param createReqVO 告警配置信息
     * @return 告警配置编号
     */
    Long createDmAlarmConfig(DmAlarmConfigSaveReqVO createReqVO);

    /**
     * 更新告警配置
     *
     * @param updateReqVO 告警配置信息
     */
    int updateDmAlarmConfig(DmAlarmConfigSaveReqVO updateReqVO);

    /**
     * 删除告警配置
     *
     * @param idList 告警配置编号
     */
    int removeDmAlarmConfig(Collection<Long> idList);

    /**
     * 获得告警配置详情
     *
     * @param id 告警配置编号
     * @return 告警配置
     */
    DmAlarmConfigDO getDmAlarmConfigById(Long id);

    /**
     * 获得全部告警配置列表
     *
     * @return 告警配置列表
     */
    List<DmAlarmConfigDO> getDmAlarmConfigList();

    /**
     * 获得全部告警配置 Map
     *
     * @return 告警配置 Map
     */
    Map<Long, DmAlarmConfigDO> getDmAlarmConfigMap();


    /**
     * 导入告警配置数据
     *
     * @param importExcelList 告警配置数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importDmAlarmConfig(List<DmAlarmConfigRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
