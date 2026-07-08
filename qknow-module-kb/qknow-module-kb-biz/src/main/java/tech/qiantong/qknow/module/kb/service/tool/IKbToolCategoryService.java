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

package tech.qiantong.qknow.module.kb.service.tool;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qknow.module.kb.controller.admin.tool.vo.KbToolCategorySaveReqVO;
import tech.qiantong.qknow.module.kb.dal.dataobject.tool.KbToolCategoryDO;
import tech.qiantong.qknow.module.kb.domain.TreeSelectsTool;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 知识分类Service接口
 *
 * @author qknow
 * @date 2025-02-13
 */
public interface IKbToolCategoryService extends IService<KbToolCategoryDO> {

    /**
     * 创建知识分类
     *
     * @param createReqVO 知识分类信息
     * @return 知识分类编号
     */
    Long createToolCategory(KbToolCategorySaveReqVO createReqVO);

    /**
     * 更新知识分类
     *
     * @param updateReqVO 知识分类信息
     */
    int updateToolCategory(KbToolCategorySaveReqVO updateReqVO);

    /**
     * 删除知识分类
     *
     * @param idList 知识分类编号
     */
    int removeToolCategory(Collection<Long> idList);

    /**
     * 获得知识分类详情
     *
     * @param id 知识分类编号
     * @return 知识分类
     */
    KbToolCategoryDO getToolCategoryById(Long id);

    /**
     * 获得全部知识分类列表
     *
     * @return 知识分类列表
     */
    List<KbToolCategoryDO> getToolCategoryAllList(KbToolCategoryDO KbToolCategoryDO);

    /**
     * 获取知识分类树列表
     *
     * @return 知识分类树列表
     */
    List<TreeSelectsTool> selectCategoryTreeList(KbToolCategoryDO KbToolCategoryDO);

    /**
     * 构建工具分类树
     *
     * @param kmcCategoryDO 工具分类列表
     * @return 树列表
     */
    List<TreeSelectsTool> buildToolCategoryTreeSelect(List<KbToolCategoryDO> kmcCategoryDO);

    /**
     * 获取工具分类树列表
     *
     * @return 工具分类树列表
     */
    List<Map<String, Object>> getTreeList();

    /**
     * 获取工具分类Map
     *
     * @return 工具分类Map
     */
    Map<Long, String> getCategoryMap();
}
