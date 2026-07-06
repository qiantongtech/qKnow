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

package tech.qiantong.qknow.module.kmc.service.kmcDocument;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.module.kmc.controller.admin.kmcDocument.vo.KmcDocumentPageReqVO;
import tech.qiantong.qknow.module.kmc.controller.admin.kmcDocument.vo.KmcDocumentRespVO;
import tech.qiantong.qknow.module.kmc.controller.admin.kmcDocument.vo.KmcDocumentSaveReqVO;
import tech.qiantong.qknow.module.kmc.controller.admin.kmcDocument.vo.KmcDocumentTrackReqVO;
import tech.qiantong.qknow.module.kmc.dal.dataobject.document.KmcDocumentDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 知识文件Service接口
 *
 * @author qknow
 * @date 2025-02-14
 */
public interface IKmcDocumentService extends IService<KmcDocumentDO> {

    /**
     * 获得知识文件分页列表
     *
     * @param pageReqVO 分页请求
     * @return 知识文件分页列表
     */
    PageResult<KmcDocumentDO> getKmcDocumentPage(KmcDocumentPageReqVO pageReqVO);

    List<KmcDocumentDO> getKmcDocumentListByIds(List<Long> ids);

    /**
     * 创建知识文件
     *
     * @param createReqVO 知识文件信息
     * @return 知识文件编号
     */
    Integer createKmcDocument(KmcDocumentSaveReqVO createReqVO);

    /**
     * 更新知识文件
     *
     * @param updateReqVO 知识文件信息
     */
    int updateKmcDocument(KmcDocumentSaveReqVO updateReqVO);

    /**
     * 删除知识文件
     *
     * @param idList 知识文件编号
     */
    int removeKmcDocument(Collection<Long> idList);

    /**
     * 获得知识文件详情
     *
     * @param id 知识文件编号
     * @return 知识文件
     */
    KmcDocumentDO getKmcDocumentById(Long id);

    KmcDocumentDO getKmcDocumentByIdJoinSync(Long id);

    /**
     * 获得全部知识文件列表
     *
     * @return 知识文件列表
     */
    List<KmcDocumentDO> getKmcDocumentList();

    /**
     * 导入知识文件数据
     *
     * @param importExcelList 知识文件数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importKmcDocument(List<KmcDocumentRespVO> importExcelList, boolean isUpdateSupport, String operName);

    List<KmcDocumentDO> selectList(KmcDocumentPageReqVO kmcDocument);

    List<Map<String, Object>> getFileTypes(Long knowledgeBaseId);

    /**
     * 文件跟踪保存
     * @param documentTrackList
     * @return
     */
    Boolean trackCount(List<KmcDocumentTrackReqVO> documentTrackList, Long userId, String userName);

    /**
     * 获取该知识库下的第一个文档
     * @param knowledgeBaseId
     * @return
     */
    KmcDocumentDO getKnowledgeBaseOne(Long knowledgeBaseId);

    /**
     * 检查文件名称是否有重复的
     * @param id
     * @param fileNames
     * @param knowledgeBaseId
     * @return
     */
    String checkFileName(Long id, String fileNames, Long knowledgeBaseId);

    /**
     * 获取知识库下的文件数量
     * @param knowledgeBaseIdList
     * @return
     */
    Map<Long, Integer> getFileCount(List<Long> knowledgeBaseIdList);
}
