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

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.module.kg.controller.admin.knowledge.vo.KgKnowledgeDocumentPageReqVO;
import tech.qiantong.qknow.module.kg.controller.admin.knowledge.vo.KgKnowledgeDocumentRespVO;
import tech.qiantong.qknow.module.kg.controller.admin.knowledge.vo.KgKnowledgeDocumentSaveReqVO;
import tech.qiantong.qknow.module.kg.controller.admin.knowledge.vo.KgKnowledgeDocumentTrackReqVO;
import tech.qiantong.qknow.module.kg.dal.dataobject.knowledge.KgKnowledgeDocumentDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * 知识文件Service接口
 *
 * @author qknow
 * @date 2025-10-20
 */
public interface IKgKnowledgeDocumentService extends IService<KgKnowledgeDocumentDO> {

    /**
     * 获得知识文件分页列表
     *
     * @param pageReqVO 分页请求
     * @return 知识文件分页列表
     */
    PageResult<KgKnowledgeDocumentDO> getKgKnowledgeDocumentPage(KgKnowledgeDocumentPageReqVO pageReqVO);

    /**
     * 创建知识文件
     *
     * @param createReqVO 知识文件信息
     * @return 知识文件编号
     */
    Long createKgKnowledgeDocument(KgKnowledgeDocumentSaveReqVO createReqVO);

    /**
     * 更新知识文件
     *
     * @param updateReqVO 知识文件信息
     */
    int updateKgKnowledgeDocument(KgKnowledgeDocumentSaveReqVO updateReqVO);

    /**
     * 删除知识文件
     *
     * @param idList 知识文件编号
     */
    int removeKgKnowledgeDocument(Collection<Long> idList);

    /**
     * 获得知识文件详情
     *
     * @param id 知识文件编号
     * @return 知识文件
     */
    KgKnowledgeDocumentDO getKgKnowledgeDocumentById(Long id);

    /**
     * 获得全部知识文件列表
     *
     * @return 知识文件列表
     */
    List<KgKnowledgeDocumentDO> getKgKnowledgeDocumentList();

    /**
     * 获得全部知识文件 Map
     *
     * @return 知识文件 Map
     */
    Map<Long, KgKnowledgeDocumentDO> getKgKnowledgeDocumentMap();


    /**
     * 导入知识文件数据
     *
     * @param importExcelList 知识文件数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importKgKnowledgeDocument(List<KgKnowledgeDocumentRespVO> importExcelList, boolean isUpdateSupport, String operName);

    /**
     * 获取多少种类型及每种类型下面的总文件数量
     *
     * @return
     */
    List<Map<String, Object>> getFileTypes();

    /**
     * 追踪文件
     *
     * @param documentTrackList
     * @param userId
     * @param userName
     * @return
     */
    Boolean trackCount(List<KgKnowledgeDocumentTrackReqVO> documentTrackList, Long userId, String userName);
}
