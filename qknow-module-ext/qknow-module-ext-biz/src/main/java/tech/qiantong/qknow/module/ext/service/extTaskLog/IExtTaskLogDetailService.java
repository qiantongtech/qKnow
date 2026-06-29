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
import tech.qiantong.qknow.module.ext.controller.admin.extTaskLog.vo.ExtTaskLogDetailRespVO;
import tech.qiantong.qknow.module.ext.controller.admin.extTaskLog.vo.ExtTaskLogDetailSaveReqVO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extTaskLog.ExtTaskLogDetailDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * 抽取任务执行日志详情Service接口
 *
 * @author qknow
 * @date 2025-12-03
 */
public interface IExtTaskLogDetailService extends IService<ExtTaskLogDetailDO> {

    /**
     * 获得抽取任务执行日志详情分页列表
     *
     * @param pageReqVO 分页请求
     * @return 抽取任务执行日志详情分页列表
     */
    PageResult<ExtTaskLogDetailDO> getExtTaskLogDetailPage(ExtTaskLogDetailPageReqVO pageReqVO);

    /**
     * 创建抽取任务执行日志详情
     *
     * @param createReqVO 抽取任务执行日志详情信息
     * @return 抽取任务执行日志详情编号
     */
    Long createExtTaskLogDetail(ExtTaskLogDetailSaveReqVO createReqVO);

    /**
     * 更新抽取任务执行日志详情
     *
     * @param updateReqVO 抽取任务执行日志详情信息
     */
    int updateExtTaskLogDetail(ExtTaskLogDetailSaveReqVO updateReqVO);

    /**
     * 删除抽取任务执行日志详情
     *
     * @param idList 抽取任务执行日志详情编号
     */
    int removeExtTaskLogDetail(Collection<Long> idList);

    /**
     * 获得抽取任务执行日志详情详情
     *
     * @param id 抽取任务执行日志详情编号
     * @return 抽取任务执行日志详情
     */
    ExtTaskLogDetailDO getExtTaskLogDetailById(Long id);

    /**
     * 获得全部抽取任务执行日志详情列表
     *
     * @return 抽取任务执行日志详情列表
     */
    List<ExtTaskLogDetailDO> getExtTaskLogDetailList();

    /**
     * 获得全部抽取任务执行日志详情 Map
     *
     * @return 抽取任务执行日志详情 Map
     */
    Map<Long, ExtTaskLogDetailDO> getExtTaskLogDetailMap();


    /**
     * 导入抽取任务执行日志详情数据
     *
     * @param importExcelList 抽取任务执行日志详情数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importExtTaskLogDetail(List<ExtTaskLogDetailRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
