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

package tech.qiantong.qknow.module.kmc.service.kmcDocumentLog;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.module.kmc.controller.admin.kmcDocumentLog.vo.KmcDocumentLogPageReqVO;
import tech.qiantong.qknow.module.kmc.controller.admin.kmcDocumentLog.vo.KmcDocumentLogRespVO;
import tech.qiantong.qknow.module.kmc.controller.admin.kmcDocumentLog.vo.KmcDocumentLogSaveReqVO;
import tech.qiantong.qknow.module.kmc.dal.dataobject.document.KmcDocumentDO;
import tech.qiantong.qknow.module.kmc.dal.dataobject.kmcDocumentLog.KmcDocumentLogDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * 文件操作日志Service接口
 *
 * @author qknow
 * @date 2025-03-24
 */
public interface IKmcDocumentLogService extends IService<KmcDocumentLogDO> {

    /**
     * 获得文件操作日志分页列表
     *
     * @param pageReqVO 分页请求
     * @return 文件操作日志分页列表
     */
    PageResult<KmcDocumentLogDO> getKmcDocumentLogPage(KmcDocumentLogPageReqVO pageReqVO);

    /**
     * 创建文件操作日志
     *
     * @param createReqVO 文件操作日志信息
     * @return 文件操作日志编号
     */
    Long createKmcDocumentLog(KmcDocumentLogSaveReqVO createReqVO);

    /**
     * 更新文件操作日志
     *
     * @param updateReqVO 文件操作日志信息
     */
    int updateKmcDocumentLog(KmcDocumentLogSaveReqVO updateReqVO);

    /**
     * 删除文件操作日志
     *
     * @param idList 文件操作日志编号
     */
    int removeKmcDocumentLog(Collection<Long> idList);

    /**
     * 获得文件操作日志详情
     *
     * @param id 文件操作日志编号
     * @return 文件操作日志
     */
    KmcDocumentLogDO getKmcDocumentLogById(Long id);

    /**
     * 获得全部文件操作日志列表
     *
     * @return 文件操作日志列表
     */
    List<KmcDocumentLogDO> getKmcDocumentLogList();

    /**
     * 获得全部文件操作日志 Map
     *
     * @return 文件操作日志 Map
     */
    Map<Long, KmcDocumentLogDO> getKmcDocumentLogMap();


    /**
     * 导入文件操作日志数据
     *
     * @param importExcelList 文件操作日志数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importKmcDocumentLog(List<KmcDocumentLogRespVO> importExcelList, boolean isUpdateSupport, String operName);

    List<KmcDocumentDO> seletAllDocumentLogList(KmcDocumentLogDO kmcDocumentLog);
}
