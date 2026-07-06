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
import tech.qiantong.qknow.module.kg.controller.admin.knowledge.vo.KgKnowledgeCategoryPageReqVO;
import tech.qiantong.qknow.module.kg.controller.admin.knowledge.vo.KgKnowledgeCategoryReqVO;
import tech.qiantong.qknow.module.kg.controller.admin.knowledge.vo.KgKnowledgeCategoryRespVO;
import tech.qiantong.qknow.module.kg.controller.admin.knowledge.vo.KgKnowledgeCategorySaveReqVO;
import tech.qiantong.qknow.module.kg.dal.dataobject.knowledge.KgKnowledgeCategoryDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * 知识分类Service接口
 *
 * @author qknow
 * @date 2025-10-20
 */
public interface IKgKnowledgeCategoryService extends IService<KgKnowledgeCategoryDO> {

    /**
     * 获得知识分类分页列表
     *
     * @param pageReqVO 分页请求
     * @return 知识分类分页列表
     */
    PageResult<KgKnowledgeCategoryDO> getKgKnowledgeCategoryPage(KgKnowledgeCategoryPageReqVO pageReqVO);

    /**
     * 创建知识分类
     *
     * @param createReqVO 知识分类信息
     * @return 知识分类编号
     */
    Long createKgKnowledgeCategory(KgKnowledgeCategorySaveReqVO createReqVO);

    /**
     * 更新知识分类
     *
     * @param updateReqVO 知识分类信息
     */
    int updateKgKnowledgeCategory(KgKnowledgeCategorySaveReqVO updateReqVO);

    /**
     * 删除知识分类
     *
     * @param idList 知识分类编号
     */
    int removeKgKnowledgeCategory(Collection<Long> idList);

    /**
     * 获得知识分类详情
     *
     * @param id 知识分类编号
     * @return 知识分类
     */
    KgKnowledgeCategoryDO getKgKnowledgeCategoryById(Long id);

    /**
     * 获得全部知识分类列表
     *
     * @return 知识分类列表
     */
    List<KgKnowledgeCategoryDO> getKgKnowledgeCategoryList();

    /**
     * 获得全部知识分类 Map
     *
     * @return 知识分类 Map
     */
    Map<Long, KgKnowledgeCategoryDO> getKgKnowledgeCategoryMap();


    /**
     * 导入知识分类数据
     *
     * @param importExcelList 知识分类数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importKgKnowledgeCategory(List<KgKnowledgeCategoryRespVO> importExcelList, boolean isUpdateSupport, String operName);

    /**
     * 查询知识分类列表
     *
     * @param kgKnowledgeCategoryReqVO 查询条件
     * @return 知识分类列表
     */
    List<KgKnowledgeCategoryDO> getKgKnowledgeCategoryList(KgKnowledgeCategoryReqVO kgKnowledgeCategoryReqVO);
}
