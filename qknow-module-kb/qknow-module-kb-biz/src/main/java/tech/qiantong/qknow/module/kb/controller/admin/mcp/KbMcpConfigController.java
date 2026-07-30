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
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tech.qiantong.qknow.common.annotation.Log;
import tech.qiantong.qknow.common.core.controller.BaseController;
import tech.qiantong.qknow.common.core.domain.CommonResult;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.common.core.utils.object.BeanUtils;
import tech.qiantong.qknow.common.enums.BusinessType;
import tech.qiantong.qknow.module.kb.controller.admin.mcp.vo.KbMcpConfigPageReqVO;
import tech.qiantong.qknow.module.kb.controller.admin.mcp.vo.KbMcpConfigRespVO;
import tech.qiantong.qknow.module.kb.controller.admin.mcp.vo.KbMcpConfigSaveReqVO;
import tech.qiantong.qknow.module.kb.dal.dataobject.mcp.KbMcpConfigDO;
import tech.qiantong.qknow.module.kb.service.mcp.IKbMcpConfigService;

import java.util.Arrays;

/**
 * mcp 配置Controller
 *
 * @author qknow
 * @date 2026-06-16
 */
@Tag(name = "mcp 配置")
@RestController
@RequestMapping("/kb/mcpConfig")
@Validated
public class KbMcpConfigController extends BaseController {
    @Resource
    private IKbMcpConfigService kbMcpConfigService;

    @Operation(summary = "查询mcp 配置列表")
    @PreAuthorize("@ss.hasPermi('kb:mcp:mcpconfig:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<KbMcpConfigRespVO>> list(KbMcpConfigPageReqVO kbMcpConfig) {
        PageResult<KbMcpConfigDO> page = kbMcpConfigService.getKbMcpConfigPage(kbMcpConfig);
        return CommonResult.success(BeanUtils.toBean(page, KbMcpConfigRespVO.class));
    }

    @Operation(summary = "获取mcp 配置详细信息")
    @PreAuthorize("@ss.hasPermi('kb:mcp:mcpconfig:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<KbMcpConfigRespVO> getInfo(@PathVariable("id") Long id) {
        KbMcpConfigDO kbMcpConfigDO = kbMcpConfigService.getKbMcpConfigById(id);
        return CommonResult.success(BeanUtils.toBean(kbMcpConfigDO, KbMcpConfigRespVO.class));
    }

    @Operation(summary = "新增mcp 配置")
    @PreAuthorize("@ss.hasPermi('kb:mcp:mcpconfig:add')")
    @Log(title = "mcp 配置", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody KbMcpConfigSaveReqVO kbMcpConfig) {
        return CommonResult.toAjax(kbMcpConfigService.createKbMcpConfig(kbMcpConfig));
    }

    @Operation(summary = "修改mcp 配置")
    @PreAuthorize("@ss.hasPermi('kb:mcp:mcpconfig:edit')")
    @Log(title = "mcp 配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody KbMcpConfigSaveReqVO kbMcpConfig) {
        return CommonResult.toAjax(kbMcpConfigService.updateKbMcpConfig(kbMcpConfig));
    }

    @Operation(summary = "删除mcp 配置")
    @PreAuthorize("@ss.hasPermi('kb:mcp:mcpconfig:remove')")
    @Log(title = "mcp 配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(kbMcpConfigService.removeKbMcpConfig(Arrays.asList(ids)));
    }

    @Operation(summary = "启动服务")
//    @PreAuthorize("@ss.hasPermi('kb:mcp:mcpconfig:remove')")
    @Log(title = "启动服务", businessType = BusinessType.UPDATE)
    @PostMapping("/updateMcpStatus")
    public CommonResult<Boolean> updateMcpStatus(@RequestBody KbMcpConfigSaveReqVO kbMcpConfig) {
        Boolean result = kbMcpConfigService.updateMcpStatus(kbMcpConfig.getId(), kbMcpConfig.getStatus());
        return CommonResult.toAjax(result);
    }

    @Operation(summary = "同步 mcp 工具列表")
//    @PreAuthorize("@ss.hasPermi('kb:mcp:mcpconfig:remove')")
    @Log(title = "同步 mcp 工具列表", businessType = BusinessType.UPDATE)
    @PostMapping("/syncMcpTool")
    public CommonResult<Boolean> syncMcpTool(@RequestBody KbMcpConfigSaveReqVO kbMcpConfig) {
        Boolean result = kbMcpConfigService.syncMcpTool(kbMcpConfig.getId());
        return CommonResult.toAjax(result);
    }

}
