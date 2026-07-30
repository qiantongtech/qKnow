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


package tech.qiantong.qknow.module.kb.service.mcp;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.ai.tool.ToolCallbackProvider;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.module.kb.controller.admin.mcp.vo.KbMcpConfigPageReqVO;
import tech.qiantong.qknow.module.kb.controller.admin.mcp.vo.KbMcpConfigSaveReqVO;
import tech.qiantong.qknow.module.kb.dal.dataobject.mcp.KbMcpConfigDO;

import java.util.Collection;

/**
 * mcp 配置Service接口
 *
 * @author qknow
 * @date 2026-06-16
 */
public interface IKbMcpConfigService extends IService<KbMcpConfigDO> {

    /**
     * 获得mcp 配置分页列表
     *
     * @param pageReqVO 分页请求
     * @return mcp 配置分页列表
     */
    PageResult<KbMcpConfigDO> getKbMcpConfigPage(KbMcpConfigPageReqVO pageReqVO);

    /**
     * 创建mcp 配置
     *
     * @param createReqVO mcp 配置信息
     * @return mcp 配置编号
     */
    Long createKbMcpConfig(KbMcpConfigSaveReqVO createReqVO);

    /**
     * 更新mcp 配置
     *
     * @param updateReqVO mcp 配置信息
     */
    int updateKbMcpConfig(KbMcpConfigSaveReqVO updateReqVO);

    /**
     * 删除mcp 配置
     *
     * @param idList mcp 配置编号
     */
    int removeKbMcpConfig(Collection<Long> idList);

    /**
     * 获得mcp 配置详情
     *
     * @param id mcp 配置编号
     * @return mcp 配置
     */
    KbMcpConfigDO getKbMcpConfigById(Long id);

    /**
     * 同步mcp工具列表
     *
     * @param mcpId mcpId
     * @return 操作是否成功
     */
    Boolean syncMcpTool(Long mcpId);

    /**
     * 获取mcp回调提供者(异步，流式接口使用)
     *
     * @param mcpIds mcpIds, 多个用逗号分隔
     * @return mcp回调提供者
     */
    ToolCallbackProvider getMcpAsyncCallbackProvider(String mcpIds);

    /**
     * 更新mcp状态
     *
     * @param mcpId mcpId
     * @param newStatus 新状态
     * @return 操作是否成功
     */
    Boolean updateMcpStatus(Long mcpId, Integer newStatus);
}
