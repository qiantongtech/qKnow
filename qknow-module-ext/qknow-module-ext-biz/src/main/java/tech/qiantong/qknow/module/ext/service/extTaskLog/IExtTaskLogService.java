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

package tech.qiantong.qknow.module.ext.service.extTaskLog;


import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.module.ext.controller.admin.extTaskLog.vo.ExtTaskLogDetailPageReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extTaskLog.vo.ExtTaskLogPageReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extTaskLog.vo.ExtTaskLogSaveReqVO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extTaskLog.ExtTaskLogDO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extTaskLog.ExtTaskLogDetailDO;
import tech.qiantong.qknow.module.ext.extEnum.ExtLogStatusEnum;
import tech.qiantong.qknow.module.ext.extEnum.ExtTaskTypeEnum;

import java.util.List;

/**
 * 抽取任务执行日志Service接口
 *
 * @author qknow
 * @date 2025-12-03
 */
public interface IExtTaskLogService extends IService<ExtTaskLogDO> {

    /**
     * 获得抽取任务执行日志分页列表
     *
     * @param pageReqVO 分页请求
     * @return 抽取任务执行日志分页列表
     */
    PageResult<ExtTaskLogDO> getExtTaskLogPage(ExtTaskLogPageReqVO pageReqVO);

    /**
     * 获得抽取任务执行日志分页列表
     *
     * @param detailPageReqVO 分页请求
     * @return 抽取任务执行日志分页列表
     */
    PageResult<ExtTaskLogDetailDO> getLogDetailPage(ExtTaskLogDetailPageReqVO detailPageReqVO);

    /**
     * 创建抽取任务执行日志
     *
     * @param createReqVO 抽取任务执行日志信息
     * @return 抽取任务执行日志编号
     */
    Long createExtTaskLog(ExtTaskLogSaveReqVO createReqVO);

    /**
     * 获得抽取任务执行日志详情
     *
     * @param id 抽取任务执行日志编号
     * @return 抽取任务执行日志
     */
    ExtTaskLogDO getExtTaskLogById(Long id);

    /**
     * 开始执行日志
     *
     * @param workSpaceId 工作空间id
     * @param taskId      执行的任务id
     * @param taskName    任务名称
     * @param taskType    任务类型
     * @return 日志id
     */
    Long startInvoke(Long workSpaceId, Long taskId, String taskName, ExtTaskTypeEnum taskType);

    /**
     * 记录日志步骤
     *
     * @param logId 日志id
     * @param step  执行步骤
     */
    void recordStep(Long logId, String step);

    /**
     * 执行结束
     *
     * @param logId    日志id
     * @param status   日志的执行状态
     * @param errorMsg 错误日志
     */
    void endInvoke(Long logId, ExtLogStatusEnum status, String errorMsg);


    Integer removeExtTaskById(List<Long> list);
}
