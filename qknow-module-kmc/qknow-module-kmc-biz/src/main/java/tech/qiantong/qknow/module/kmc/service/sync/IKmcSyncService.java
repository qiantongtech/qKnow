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

package tech.qiantong.qknow.module.kmc.service.sync;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.module.kmc.controller.admin.sync.vo.KmcSyncPageReqVO;
import tech.qiantong.qknow.module.kmc.controller.admin.sync.vo.KmcSyncRespVO;
import tech.qiantong.qknow.module.kmc.controller.admin.sync.vo.KmcSyncSaveReqVO;
import tech.qiantong.qknow.module.kmc.dal.dataobject.document.KmcDocumentDO;
import tech.qiantong.qknow.module.kmc.dal.dataobject.sync.KmcSyncDO;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
/**
 * 文件同步Service接口
 *
 * @author qknow
 * @date 2025-03-18
 */
public interface IKmcSyncService extends IService<KmcSyncDO> {

    /**
     * 获得文件同步分页列表
     *
     * @param pageReqVO 分页请求
     * @return 文件同步分页列表
     */
    PageResult<KmcSyncDO> getKmcSyncPage(KmcSyncPageReqVO pageReqVO);

    /**
     * 创建文件同步
     *
     * @param createReqVO 文件同步信息
     * @return 文件同步编号
     */
    Long createKmcSync(KmcSyncSaveReqVO createReqVO);

    /**
     * 更新文件同步
     *
     * @param updateReqVO 文件同步信息
     */
    int updateKmcSync(KmcSyncSaveReqVO updateReqVO);

    /**
     * 删除文件同步
     *
     * @param idList 文件同步编号
     */
    int removeKmcSync(Collection<Long> idList);

    /**
     * 获得文件同步详情
     *
     * @param id 文件同步编号
     * @return 文件同步
     */
    KmcSyncDO getKmcSyncById(Long id);

    /**
     * 导入文件同步数据
     *
     * @param importExcelList 文件同步数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importKmcSync(List<KmcSyncRespVO> importExcelList, boolean isUpdateSupport, String operName);

    /**
     * 根据文档id获取文档列表
     * @param idList
     * @return
     */
    List<KmcSyncDO> getSyncByDocumentIdList(Collection<? extends Serializable> idList, String datasetId);

    /**
     * 根据知识库id获取文档列表
     */
    List<KmcSyncDO> getSyncList(List<String> idList, String datasetId);

    /**
     * 同步创建
     * @param kmcDocumentDO
     * @return
     */
    Boolean syncToCreate(KmcDocumentDO kmcDocumentDO);

    /**
     * 同步更新
     * @param kmcDocumentDO
     * @return
     */
    Boolean syncToUpdate(KmcDocumentDO kmcDocumentDO);

    /**
     * 同步删除
     * @param kmcDocumentDO kmc文档对象
     * @return 操作是否成功
     */
    Boolean syncToRemove(KmcDocumentDO kmcDocumentDO);
}
