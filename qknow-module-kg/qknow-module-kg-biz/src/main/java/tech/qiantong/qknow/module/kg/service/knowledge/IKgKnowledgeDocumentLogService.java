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

package tech.qiantong.qknow.module.kg.service.knowledge;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.module.kg.controller.admin.knowledge.vo.KgKnowledgeDocumentLogSaveReqVO;
import tech.qiantong.qknow.module.kg.controller.admin.knowledge.vo.KgKnowledgeDocumentLogPageReqVO;
import tech.qiantong.qknow.module.kg.controller.admin.knowledge.vo.KgKnowledgeDocumentLogRespVO;
import tech.qiantong.qknow.module.kg.dal.dataobject.knowledge.KgKnowledgeDocumentLogDO;
/**
 * 文件操作日志Service接口
 *
 * @author qknow
 * @date 2025-10-22
 */
public interface IKgKnowledgeDocumentLogService extends IService<KgKnowledgeDocumentLogDO> {

    /**
     * 获得文件操作日志分页列表
     *
     * @param pageReqVO 分页请求
     * @return 文件操作日志分页列表
     */
    PageResult<KgKnowledgeDocumentLogDO> getKgKnowledgeDocumentLogPage(KgKnowledgeDocumentLogPageReqVO pageReqVO);

    /**
     * 创建文件操作日志
     *
     * @param createReqVO 文件操作日志信息
     * @return 文件操作日志编号
     */
    Long createKgKnowledgeDocumentLog(KgKnowledgeDocumentLogSaveReqVO createReqVO);

    /**
     * 更新文件操作日志
     *
     * @param updateReqVO 文件操作日志信息
     */
    int updateKgKnowledgeDocumentLog(KgKnowledgeDocumentLogSaveReqVO updateReqVO);

    /**
     * 删除文件操作日志
     *
     * @param idList 文件操作日志编号
     */
    int removeKgKnowledgeDocumentLog(Collection<Long> idList);

    /**
     * 获得文件操作日志详情
     *
     * @param id 文件操作日志编号
     * @return 文件操作日志
     */
    KgKnowledgeDocumentLogDO getKgKnowledgeDocumentLogById(Long id);

    /**
     * 获得全部文件操作日志列表
     *
     * @return 文件操作日志列表
     */
    List<KgKnowledgeDocumentLogDO> getKgKnowledgeDocumentLogList();

    /**
     * 获得全部文件操作日志 Map
     *
     * @return 文件操作日志 Map
     */
    Map<Long, KgKnowledgeDocumentLogDO> getKgKnowledgeDocumentLogMap();


    /**
     * 导入文件操作日志数据
     *
     * @param importExcelList 文件操作日志数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importKgKnowledgeDocumentLog(List<KgKnowledgeDocumentLogRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
