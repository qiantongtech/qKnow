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

import com.alibaba.fastjson2.JSONArray;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.module.kmc.api.knowledgeBase.dto.KmcKnowledgeBaseRespDTO;
import tech.qiantong.qknow.module.kmc.controller.admin.knowledgeBase.vo.*;
import tech.qiantong.qknow.module.kmc.dal.dataobject.knowledgeBase.KmcKnowledgeBaseDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 知识库Service接口
 *
 * @author qknow
 * @date 2025-07-22
 */
public interface IKmcKnowledgeBaseService extends IService<KmcKnowledgeBaseDO> {

    /**
     * 获得知识库分页列表
     *
     * @param pageReqVO 分页请求
     * @return 知识库分页列表
     */
    PageResult<KmcKnowledgeBaseDO> getKmcKnowledgeBasePage(KmcKnowledgeBasePageReqVO pageReqVO, Long userId);

    /**
     * 创建知识库
     *
     * @param createReqVO 知识库信息
     * @return 知识库编号
     */
    Long createKmcKnowledgeBase(KmcKnowledgeBaseSaveReqVO createReqVO);

    /**
     * 更新知识库
     *
     * @param updateReqVO 知识库信息
     */
    int updateKmcKnowledgeBase(KmcKnowledgeBaseSaveReqVO updateReqVO);

    /**
     * 删除知识库
     *
     * @param idList 知识库编号
     */
    int removeKmcKnowledgeBase(Collection<Long> idList);

    /**
     * 获得知识库详情
     *
     * @param id 知识库编号
     * @return 知识库
     */
    KmcKnowledgeBaseDO getKmcKnowledgeBaseById(Long id);

    /**
     * 获得知识库
     *
     * @return 知识库
     */
    List<KmcKnowledgeBaseDO> getKmcKnowledgeBaseByIds(List<Long> ids);

    /**
     * 获得全部知识库列表
     *
     * @return 知识库列表
     */
    List<KmcKnowledgeBaseDO> getKmcKnowledgeBaseList();

    /**
     * 获得全部知识库 Map
     *
     * @return 知识库 Map
     */
    Map<Long, KmcKnowledgeBaseDO> getKmcKnowledgeBaseMap();


    /**
     * 导入知识库数据
     *
     * @param importExcelList 知识库数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    String importKmcKnowledgeBase(List<KmcKnowledgeBaseRespVO> importExcelList, boolean isUpdateSupport, String operName);

    /**
     * 获取Embedding模型
     *
     * @return 结果
     */
    JSONArray getTextEmbedding();

    /**
     * 获取Rerank模型
     *
     * @return 结果
     */
    JSONArray getRerank();

    /**
     * 召回测试
     *
     * @return 知识库检索结果列表
     */
    List<RetrieveResultRespVO> recallTest(RetrieveResultReqVO retrieveResultReqVO);

    /**
     * 设置知识库权限角色
     *
     * @param kmcKnowledgeRoleSaveReqVO
     * @return
     */
    Boolean updateKnowledgeBaseRole(KmcKnowledgeRoleSaveReqVO kmcKnowledgeRoleSaveReqVO);

    /**
     * 根据权限获取知识库列表
     *
     * @param userId
     * @return
     */
    public List<KmcKnowledgeBaseDO> getKmcKnowledgeBaseList(Long userId, Boolean isValid);

    /**
     * 启用禁用知识库
     *
     * @param kmcKnowledgeSaveReqVO
     * @return
     */
    Integer changeKnowledgeValid(KmcKnowledgeBaseSaveReqVO kmcKnowledgeSaveReqVO);

    /**
     * 获取知识库(包含文件数量）
     *
     * @param pageReqVO
     * @param userId
     * @return
     */
    PageResult<KmcKnowledgeBaseRespVO> getKmcKnowledgeBasePageWithFileCount(KmcKnowledgeBasePageReqVO pageReqVO, Long userId);

    /**
     * 知识库内容检索
     *
     * @param knowledgeBaseId 知识库id
     * @param query           查询内容
     * @return 知识库检索结果列表
     */
    List<RetrieveResultRespVO> search(Long knowledgeBaseId, String query);

}
