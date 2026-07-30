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
import io.modelcontextprotocol.spec.McpSchema;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.module.kb.controller.admin.mcp.vo.KbMcpToolPageReqVO;
import tech.qiantong.qknow.module.kb.dal.dataobject.mcp.KbMcpToolDO;

/**
 * mcp 工具Service接口
 *
 * @author qknow
 * @date 2026-06-16
 */
public interface IKbMcpToolService extends IService<KbMcpToolDO> {

    /**
     * 获得mcp 工具分页列表
     *
     * @param pageReqVO 分页请求
     * @return mcp 工具分页列表
     */
    PageResult<KbMcpToolDO> getKbMcpToolPage(KbMcpToolPageReqVO pageReqVO);

    /**
     * 获得mcp 工具详情
     *
     * @param id mcp 工具编号
     * @return mcp 工具
     */
    KbMcpToolDO getKbMcpToolById(Long id);

    /**
     * 同步工具
     *
     * @param mcpId           mcpId
     * @param listToolsResult 工具列表
     * @return Boolean
     */
    Boolean syncTool(Long mcpId, McpSchema.ListToolsResult listToolsResult);

}
