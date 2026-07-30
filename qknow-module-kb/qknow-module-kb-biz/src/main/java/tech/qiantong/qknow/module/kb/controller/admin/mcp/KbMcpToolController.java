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

package tech.qiantong.qknow.module.kb.controller.admin.mcp;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.qiantong.qknow.common.core.controller.BaseController;
import tech.qiantong.qknow.common.core.domain.CommonResult;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.common.core.utils.object.BeanUtils;
import tech.qiantong.qknow.module.kb.controller.admin.mcp.vo.KbMcpToolPageReqVO;
import tech.qiantong.qknow.module.kb.controller.admin.mcp.vo.KbMcpToolRespVO;
import tech.qiantong.qknow.module.kb.dal.dataobject.mcp.KbMcpToolDO;
import tech.qiantong.qknow.module.kb.service.mcp.IKbMcpToolService;

/**
 * mcp 工具Controller
 *
 * @author qknow
 * @date 2026-06-16
 */
@Tag(name = "mcp 工具")
@RestController
@RequestMapping("/kb/mcpTool")
@Validated
public class KbMcpToolController extends BaseController {

    @Resource
    private IKbMcpToolService kbMcpToolService;

    @Operation(summary = "查询mcp 工具列表")
//    @PreAuthorize("@ss.hasPermi('kb:mcp:mcptool:list')")
    @GetMapping("/mcpToolPage")
    public CommonResult<PageResult<KbMcpToolRespVO>> mcpToolPage(KbMcpToolPageReqVO kbMcpTool) {
        PageResult<KbMcpToolDO> page = kbMcpToolService.getKbMcpToolPage(kbMcpTool);
        return CommonResult.success(BeanUtils.toBean(page, KbMcpToolRespVO.class));
    }

    @Operation(summary = "获取mcp 工具详细信息")
//    @PreAuthorize("@ss.hasPermi('kb:mcp:mcptool:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<KbMcpToolRespVO> getInfo(@PathVariable("id") Long id) {
        KbMcpToolDO kbMcpToolDO = kbMcpToolService.getKbMcpToolById(id);
        return CommonResult.success(BeanUtils.toBean(kbMcpToolDO, KbMcpToolRespVO.class));
    }

}
