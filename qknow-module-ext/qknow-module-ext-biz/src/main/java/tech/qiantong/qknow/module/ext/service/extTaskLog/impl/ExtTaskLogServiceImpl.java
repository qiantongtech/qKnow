/*
 * Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
 *
 * This file is part of qKnow Intelligent Agent Building Platform (Open Source Edition).
 *
 * qKnow is licensed under Apache License 2.0 with additional qKnow terms.
 * You may use qKnow for commercial purposes, but you may not remove, hide,
 * modify, or replace the qKnow logo, copyright notices, license notices,
 * or attribution information without a separate commercial license.
 *
 * White-label use, OEM distribution, rebranding, or presenting qKnow as
 * another product requires separate commercial authorization from
 * Jiangsu Qiantong Technology Co., Ltd.
 *
 * Business License: https://community.qknow.ai/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

package tech.qiantong.qknow.module.ext.service.extTaskLog.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.common.exception.ServiceException;
import tech.qiantong.qknow.common.utils.object.BeanUtils;
import tech.qiantong.qknow.module.ext.controller.admin.extTaskLog.vo.ExtTaskLogDetailPageReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extTaskLog.vo.ExtTaskLogPageReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extTaskLog.vo.ExtTaskLogSaveReqVO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extTaskLog.ExtTaskLogDO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extTaskLog.ExtTaskLogDetailDO;
import tech.qiantong.qknow.module.ext.dal.mapper.extTaskLog.ExtTaskLogDetailMapper;
import tech.qiantong.qknow.module.ext.dal.mapper.extTaskLog.ExtTaskLogMapper;
import tech.qiantong.qknow.module.ext.extEnum.ExtLogStatusEnum;
import tech.qiantong.qknow.module.ext.extEnum.ExtTaskTypeEnum;
import tech.qiantong.qknow.module.ext.service.extTaskLog.IExtTaskLogService;

import jakarta.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 抽取任务执行日志Service业务层处理
 *
 * @author qknow
 * @date 2025-12-03
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class ExtTaskLogServiceImpl extends ServiceImpl<ExtTaskLogMapper, ExtTaskLogDO> implements IExtTaskLogService {
    @Resource
    private ExtTaskLogMapper extTaskLogMapper;
    @Resource
    private ExtTaskLogDetailMapper logDetailMapper;

    @Override
    public PageResult<ExtTaskLogDO> getExtTaskLogPage(ExtTaskLogPageReqVO pageReqVO) {
        return extTaskLogMapper.selectPage(pageReqVO);
    }

    /**
     * 获得抽取任务执行日志分页列表
     *
     * @param detailPageReqVO 分页请求
     * @return 抽取任务执行日志分页列表
     */
    @Override
    public PageResult<ExtTaskLogDetailDO> getLogDetailPage(ExtTaskLogDetailPageReqVO detailPageReqVO) {
        return logDetailMapper.selectPage(detailPageReqVO);
    }

    @Override
    public Long createExtTaskLog(ExtTaskLogSaveReqVO createReqVO) {
        ExtTaskLogDO dictType = BeanUtils.toBean(createReqVO, ExtTaskLogDO.class);
        extTaskLogMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public ExtTaskLogDO getExtTaskLogById(Long id) {
        return extTaskLogMapper.selectById(id);
    }

    /**
     * 开始执行日志
     *
     * @param taskId   执行的任务id
     * @param taskName 任务名称
     * @param taskType 任务类型
     * @return 日志id
     */
    @Override
    public Long startInvoke(Long workSpaceId, Long taskId, String taskName, ExtTaskTypeEnum taskType) {
        ExtTaskLogDO entity = new ExtTaskLogDO();
        entity.setWorkspaceId(workSpaceId);
        entity.setTaskId(taskId);
        entity.setTaskName(taskName);
        entity.setTaskType(taskType.getValue());
        entity.setStatus(ExtLogStatusEnum.SUCCESS.getValue());// 默认是成功的状态
        entity.setStartTime(new Date());
        extTaskLogMapper.insert(entity);
        return entity.getId();
    }

    /**
     * 记录日志步骤
     *
     * @param logId 日志id
     * @param step  执行步骤
     */
    @Override
    public void recordStep(Long logId, String step) {
        if (step.length() > 2000) {
            step = step.substring(0, 2000);
        }
        ExtTaskLogDO taskLogDO = extTaskLogMapper.selectById(logId);
        if (Objects.isNull(taskLogDO)) {
            throw new ServiceException("获取日志对象出错");
        }
        ExtTaskLogDetailDO detailEntity = new ExtTaskLogDetailDO();
        detailEntity.setLogId(logId);
        detailEntity.setWorkspaceId(taskLogDO.getWorkspaceId());
        detailEntity.setStep(step);
        logDetailMapper.insert(detailEntity);
        // 同时修改下结束事件
        updateEndTime(logId);
    }

    /**
     * 执行结束
     *
     * @param logId    日志id
     * @param status   日志的执行状态
     * @param errorMsg 错误日志
     */
    @Override
    public void endInvoke(Long logId, ExtLogStatusEnum status, String errorMsg) {
        if (Objects.isNull(errorMsg)) {
            errorMsg = "";
        }
        if (errorMsg.length() > 2000) {
            errorMsg = errorMsg.substring(0, 2000);
        }
        LambdaUpdateWrapper<ExtTaskLogDO> updateWrapper = Wrappers.<ExtTaskLogDO>lambdaUpdate()
                .set(ExtTaskLogDO::getStatus, status.getValue())
                .set(ExtTaskLogDO::getErrorMsg, errorMsg)
                .set(ExtTaskLogDO::getEndTime, new Date())
                .eq(ExtTaskLogDO::getId, logId);
        super.update(updateWrapper);
    }

    @Override
    public Integer removeExtTaskById(List<Long> list) {
        return extTaskLogMapper.deleteByIds(list);
    }

    /**
     * 更新日志的结束时间
     *
     * @param logId 日志id
     */
    private void updateEndTime(Long logId) {
        LambdaUpdateWrapper<ExtTaskLogDO> updateWrapper = Wrappers.<ExtTaskLogDO>lambdaUpdate()
                .set(ExtTaskLogDO::getEndTime, new Date())
                .eq(ExtTaskLogDO::getId, logId);
        super.update(updateWrapper);
    }
}
