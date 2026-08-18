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

package tech.qiantong.qknow.module.kmc.service.knowledgeSegment;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.module.kmc.controller.admin.knowledgeSegment.vo.KmcDocumentSegmentPageReqVO;
import tech.qiantong.qknow.module.kmc.controller.admin.knowledgeSegment.vo.KmcDocumentSegmentRespVO;
import tech.qiantong.qknow.module.kmc.controller.admin.knowledgeSegment.vo.KmcDocumentSegmentSaveReqVO;
import tech.qiantong.qknow.module.kmc.dal.dataobject.knowledgeSegment.KmcDocumentSegmentDO;
import org.springframework.ai.vectorstore.weaviate.WeaviateVectorStore;
import tech.qiantong.qknow.module.kmc.dal.dataobject.document.KmcDocumentDO;
import tech.qiantong.qknow.module.kmc.dal.dataobject.knowledgeBase.KmcKnowledgeBaseDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * 文件分段Service接口
 *
 * @author qknow
 * @date 2025-08-28
 */
public interface IKmcDocumentSegmentService extends IService<KmcDocumentSegmentDO> {

    /**
     * 获得文件分段分页列表
     *
     * @param pageReqVO 分页请求
     * @return 文件分段分页列表
     */
    PageResult<KmcDocumentSegmentDO> getKmcDocumentSegmentPage(KmcDocumentSegmentPageReqVO pageReqVO);


    /**
     * 获得文件分段分页列表树形结构
     *
     * @param pageReqVO 分页请求
     * @return 文件分段分页列表
     */
    PageResult<KmcDocumentSegmentDO> getKmcDocumentSegmentTreePage(KmcDocumentSegmentPageReqVO pageReqVO);

    /**
     * 根据文件id获取所有顶层分段节点
     * @param documentId 文件id
     * @return 顶层分段节点
     */
    List<KmcDocumentSegmentDO> getAllLevelNodes(Long documentId);

    /**
     * 创建文件分段
     *
     * @param createReqVO 文件分段信息
     * @return 文件分段编号
     */
    Long createKmcDocumentSegment(KmcDocumentSegmentSaveReqVO createReqVO);

    /**
     * 创建文段分段
     *
     * @param vectorStore 向量数据库
     * @param knowledgeBaseDO 知识库
     * @param kmcDocument 文件信息
     * @param segmentDO 文件分段信息
     * @return 分段信息id
     */
    Long createKmcDocumentSegment(WeaviateVectorStore vectorStore,
                                  KmcKnowledgeBaseDO knowledgeBaseDO,
                                  KmcDocumentDO kmcDocument,
                                  KmcDocumentSegmentDO segmentDO);

    /**
     * 获取分段数量
     *
     * @param documentId 文件id
     * @return 分段数量
     */
    Long getSegmentCount(Long documentId);

    /**
     * 更新文件分段
     *
     * @param updateReqVO 文件分段信息
     */
    int updateKmcDocumentSegment(KmcDocumentSegmentSaveReqVO updateReqVO);

    /**
     * 删除文件分段
     *
     * @param idList 文件分段编号
     */
    int removeKmcDocumentSegment(Collection<Long> idList);




    /**
     * 获得文件分段详情
     *
     * @param id 文件分段编号
     * @return 文件分段
     */
    KmcDocumentSegmentDO getKmcDocumentSegmentById(Long id);

    /**
     * 获得全部文件分段列表
     *
     * @return 文件分段列表
     */
    List<KmcDocumentSegmentDO> getKmcDocumentSegmentList();

    /**
     * 获得全部文件分段 Map
     *
     * @return 文件分段 Map
     */
    Map<Long, KmcDocumentSegmentDO> getKmcDocumentSegmentMap();


    /**
     * 导入文件分段数据
     *
     * @param importExcelList 文件分段数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importKmcDocumentSegment(List<KmcDocumentSegmentRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
