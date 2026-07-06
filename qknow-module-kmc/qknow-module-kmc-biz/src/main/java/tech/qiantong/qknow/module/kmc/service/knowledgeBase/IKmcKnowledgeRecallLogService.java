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

package tech.qiantong.qknow.module.kmc.service.knowledgeBase;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.module.kmc.controller.admin.knowledgeBase.vo.KmcKnowledgeRecallLogPageReqVO;
import tech.qiantong.qknow.module.kmc.controller.admin.knowledgeBase.vo.KmcKnowledgeRecallLogRespVO;
import tech.qiantong.qknow.module.kmc.controller.admin.knowledgeBase.vo.KmcKnowledgeRecallLogSaveReqVO;
import tech.qiantong.qknow.module.kmc.dal.dataobject.knowledgeBase.KmcKnowledgeRecallLogDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * 召回记录Service接口
 *
 * @author qknow
 * @date 2025-07-24
 */
public interface IKmcKnowledgeRecallLogService extends IService<KmcKnowledgeRecallLogDO> {

    /**
     * 获得召回记录分页列表
     *
     * @param pageReqVO 分页请求
     * @return 召回记录分页列表
     */
    PageResult<KmcKnowledgeRecallLogDO> getKmcKnowledgeRecallLogPage(KmcKnowledgeRecallLogPageReqVO pageReqVO);

    /**
     * 创建召回记录
     *
     * @param createReqVO 召回记录信息
     * @return 召回记录编号
     */
    Long createKmcKnowledgeRecallLog(KmcKnowledgeRecallLogSaveReqVO createReqVO);

    /**
     * 更新召回记录
     *
     * @param updateReqVO 召回记录信息
     */
    int updateKmcKnowledgeRecallLog(KmcKnowledgeRecallLogSaveReqVO updateReqVO);

    /**
     * 删除召回记录
     *
     * @param idList 召回记录编号
     */
    int removeKmcKnowledgeRecallLog(Collection<Long> idList);

    /**
     * 获得召回记录详情
     *
     * @param id 召回记录编号
     * @return 召回记录
     */
    KmcKnowledgeRecallLogDO getKmcKnowledgeRecallLogById(Long id);

    /**
     * 获得全部召回记录列表
     *
     * @return 召回记录列表
     */
    List<KmcKnowledgeRecallLogDO> getKmcKnowledgeRecallLogList();

    /**
     * 获得全部召回记录 Map
     *
     * @return 召回记录 Map
     */
    Map<Long, KmcKnowledgeRecallLogDO> getKmcKnowledgeRecallLogMap();


    /**
     * 导入召回记录数据
     *
     * @param importExcelList 召回记录数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importKmcKnowledgeRecallLog(List<KmcKnowledgeRecallLogRespVO> importExcelList, boolean isUpdateSupport, String operName);

    /**
     * 获取知识库召回记录
     * @param knowledgeBaseId
     * @return
     */
    List<KmcKnowledgeRecallLogDO> findByKnowledgeBaseId(Long knowledgeBaseId);
}
