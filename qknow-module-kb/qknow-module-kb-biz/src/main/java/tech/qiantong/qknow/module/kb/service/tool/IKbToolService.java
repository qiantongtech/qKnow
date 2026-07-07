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
import org.springframework.ai.tool.ToolCallbackProvider;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.module.kb.controller.admin.tool.vo.KbToolPageReqVO;
import tech.qiantong.qknow.module.kb.controller.admin.tool.vo.KbToolSaveReqVO;
import tech.qiantong.qknow.module.kb.dal.dataobject.tool.KbToolDO;

import java.util.Collection;

/**
 * 工具管理Service接口
 *
 * @author qknow
 * @date 2026-03-19
 */
public interface IKbToolService extends IService<KbToolDO> {

    /**
     * 获得工具管理分页列表
     *
     * @param pageReqVO 分页请求
     * @return 工具管理分页列表
     */
    PageResult<KbToolDO> getKbToolPage(KbToolPageReqVO pageReqVO);

    /**
     * 创建工具管理
     *
     * @param createReqVO 工具管理信息
     * @return 工具管理编号
     */
    Long createKbTool(KbToolSaveReqVO createReqVO);

    /**
     * 更新工具管理
     *
     * @param updateReqVO 工具管理信息
     */
    int updateKbTool(KbToolSaveReqVO updateReqVO);

    /**
     * 删除工具管理
     *
     * @param idList 工具管理编号
     */
    int removeKbTool(Collection<Long> idList);

    /**
     * 获得工具管理详情
     *
     * @param id 工具管理编号
     * @return 工具管理
     */
    KbToolDO getKbToolById(Long id);

    /**
     * 获取工具回调提供者
     *
     * @param toolIds 工具ID
     * @return 工具回调提供者
     */
    ToolCallbackProvider getToolCallbackProvider(String toolIds);

    /**
     * 更新工具状态
     *
     * @param kbTool 工具信息
     * @return 更新结果
     */
    Boolean updateStatus(KbToolSaveReqVO kbTool);
}
