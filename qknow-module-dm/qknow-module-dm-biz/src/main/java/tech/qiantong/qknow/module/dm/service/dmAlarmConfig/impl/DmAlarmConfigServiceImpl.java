package tech.qiantong.qknow.module.dm.service.dmAlarmConfig.impl;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.Resource;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.common.utils.object.BeanUtils;
import tech.qiantong.qknow.common.utils.StringUtils;
import tech.qiantong.qknow.common.exception.ServiceException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qknow.module.dm.controller.admin.dmAlarmConfig.vo.DmAlarmConfigPageReqVO;
import tech.qiantong.qknow.module.dm.controller.admin.dmAlarmConfig.vo.DmAlarmConfigRespVO;
import tech.qiantong.qknow.module.dm.controller.admin.dmAlarmConfig.vo.DmAlarmConfigSaveReqVO;
import tech.qiantong.qknow.module.dm.dal.dataobject.dmAlarmConfig.DmAlarmConfigDO;
import tech.qiantong.qknow.module.dm.dal.mapper.dmAlarmConfig.DmAlarmConfigMapper;
import tech.qiantong.qknow.module.dm.service.dmAlarmConfig.IDmAlarmConfigService;
/**
 * 告警配置Service业务层处理
 *
 * @author qknow
 * @date 2025-02-19
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DmAlarmConfigServiceImpl  extends ServiceImpl<DmAlarmConfigMapper,DmAlarmConfigDO> implements IDmAlarmConfigService {
    @Resource
    private DmAlarmConfigMapper dmAlarmConfigMapper;

    @Override
    public PageResult<DmAlarmConfigDO> getDmAlarmConfigPage(DmAlarmConfigPageReqVO pageReqVO) {
        return dmAlarmConfigMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createDmAlarmConfig(DmAlarmConfigSaveReqVO createReqVO) {
        DmAlarmConfigDO dictType = BeanUtils.toBean(createReqVO, DmAlarmConfigDO.class);
        dmAlarmConfigMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDmAlarmConfig(DmAlarmConfigSaveReqVO updateReqVO) {
        // 更新告警配置
        DmAlarmConfigDO updateObj = BeanUtils.toBean(updateReqVO, DmAlarmConfigDO.class);

        UpdateWrapper<DmAlarmConfigDO> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", updateObj.getId());
        // 标识是否需要执行更新
        boolean needUpdate = false;

        // 判断 updateReqVO 中的字段值是否为空，如果为空才 set
        if (updateReqVO.getUpper() == null) {
            updateWrapper.set("upper", null);
            needUpdate = true;
        }
        if (updateReqVO.getLower() == null) {
            updateWrapper.set("lower", null);
            needUpdate = true;
        }

        // 如果需要更新，则执行更新操作
        if (needUpdate) {
            dmAlarmConfigMapper.update(null, updateWrapper);
        }
        return dmAlarmConfigMapper.updateById(updateObj);
    }
    @Override
    public int removeDmAlarmConfig(Collection<Long> idList) {
        // 批量删除告警配置
        return dmAlarmConfigMapper.deleteBatchIds(idList);
    }

    @Override
    public DmAlarmConfigDO getDmAlarmConfigById(Long id) {
        return dmAlarmConfigMapper.selectById(id);
    }

    @Override
    public List<DmAlarmConfigDO> getDmAlarmConfigList() {
        return dmAlarmConfigMapper.selectList();
    }

    @Override
    public Map<Long, DmAlarmConfigDO> getDmAlarmConfigMap() {
        List<DmAlarmConfigDO> dmAlarmConfigList = dmAlarmConfigMapper.selectList();
        return dmAlarmConfigList.stream()
                .collect(Collectors.toMap(
                        DmAlarmConfigDO::getId,
                        dmAlarmConfigDO -> dmAlarmConfigDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


    /**
     * 导入告警配置数据
     *
     * @param importExcelList 告警配置数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    @Override
    public String importDmAlarmConfig(List<DmAlarmConfigRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("导入数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (DmAlarmConfigRespVO respVO : importExcelList) {
            try {
                DmAlarmConfigDO dmAlarmConfigDO = BeanUtils.toBean(respVO, DmAlarmConfigDO.class);
                Long dmAlarmConfigId = respVO.getId();
                if (isUpdateSupport) {
                    if (dmAlarmConfigId != null) {
                        DmAlarmConfigDO existingDmAlarmConfig = dmAlarmConfigMapper.selectById(dmAlarmConfigId);
                        if (existingDmAlarmConfig != null) {
                            dmAlarmConfigMapper.updateById(dmAlarmConfigDO);
                            successNum++;
                            successMessages.add("数据更新成功，ID为 " + dmAlarmConfigId + " 的告警配置记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，ID为 " + dmAlarmConfigId + " 的告警配置记录不存在。");
                        }
                    } else {
                        failureNum++;
                        failureMessages.add("数据更新失败，某条记录的ID不存在。");
                    }
                } else {
                    QueryWrapper<DmAlarmConfigDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", dmAlarmConfigId);
                    DmAlarmConfigDO existingDmAlarmConfig = dmAlarmConfigMapper.selectOne(queryWrapper);
                    if (existingDmAlarmConfig == null) {
                        dmAlarmConfigMapper.insert(dmAlarmConfigDO);
                        successNum++;
                        successMessages.add("数据插入成功，ID为 " + dmAlarmConfigId + " 的告警配置记录。");
                    } else {
                        failureNum++;
                        failureMessages.add("数据插入失败，ID为 " + dmAlarmConfigId + " 的告警配置记录已存在。");
                    }
                }
            } catch (Exception e) {
                failureNum++;
                String errorMsg = "数据导入失败，错误信息：" + e.getMessage();
                failureMessages.add(errorMsg);
                log.error(errorMsg, e);
            }
        }
        StringBuilder resultMsg = new StringBuilder();
        if (failureNum > 0) {
            resultMsg.append("很抱歉，导入失败！共 ").append(failureNum).append(" 条数据格式不正确，错误如下：");
            resultMsg.append("<br/>").append(String.join("<br/>", failureMessages));
            throw new ServiceException(resultMsg.toString());
        } else {
            resultMsg.append("恭喜您，数据已全部导入成功！共 ").append(successNum).append(" 条。");
        }
        return resultMsg.toString();
    }
}
