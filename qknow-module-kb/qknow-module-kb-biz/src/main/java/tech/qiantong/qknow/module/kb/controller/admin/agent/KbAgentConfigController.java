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

package tech.qiantong.qknow.module.kb.controller.admin.agent;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import tech.qiantong.qknow.common.annotation.Log;
import tech.qiantong.qknow.common.enums.BusinessType;
import tech.qiantong.qknow.common.exception.enums.GlobalErrorCodeConstants;
import tech.qiantong.qknow.common.utils.StringUtils;
import tech.qiantong.qknow.common.core.controller.BaseController;
import tech.qiantong.qknow.common.core.domain.AjaxResult;
import tech.qiantong.qknow.common.core.domain.CommonResult;
import tech.qiantong.qknow.common.core.page.PageParam;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.common.core.utils.object.BeanUtils;
import tech.qiantong.qknow.common.core.utils.poi.ExcelUtil;
import tech.qiantong.qknow.module.kb.controller.admin.agent.vo.*;
import tech.qiantong.qknow.module.kb.convert.agent.KbAgentConfigConvert;
import tech.qiantong.qknow.module.kb.dal.dataobject.agent.KbAgentConfigDO;
import tech.qiantong.qknow.module.kb.dal.dataobject.tool.KbToolMethodDO;
import tech.qiantong.qknow.module.kb.service.agent.IKbAgentConfigService;
import tech.qiantong.qknow.module.kb.service.tool.IKbToolMethodService;
import tech.qiantong.qknow.module.kmc.api.knowledgeBase.dto.KmcKnowledgeBaseRespDTO;
import tech.qiantong.qknow.module.kmc.api.service.IKmcApiService;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * agent配置Controller
 *
 * @author qknow
 * @date 2026-03-19
 */
@Tag(name = "agent配置")
@RestController
@RequestMapping("/kb/agent")
@Validated
public class KbAgentConfigController extends BaseController {
    @Resource
    private IKbAgentConfigService kbAgentConfigService;

    @Resource
    private IKbToolMethodService kbToolMethodService;

    @Resource
    private IKmcApiService kmcApiService;

    @Operation(summary = "查询agent配置列表")
//    @PreAuthorize("@ss.hasPermi('kb:agent:config:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<KbAgentConfigRespVO>> list(KbAgentConfigPageReqVO kbAgentConfig) {
        PageResult<KbAgentConfigDO> page = kbAgentConfigService.getKbAgentConfigPage(kbAgentConfig);
        return CommonResult.success(BeanUtils.toBean(page, KbAgentConfigRespVO.class));
    }

    @Operation(summary = "导出agent配置列表")
//    @PreAuthorize("@ss.hasPermi('kb:agent:config:export')")
    @Log(title = "agent配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, KbAgentConfigPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<KbAgentConfigDO> list = (List<KbAgentConfigDO>) kbAgentConfigService.getKbAgentConfigPage(exportReqVO).getRows();
        ExcelUtil<KbAgentConfigRespVO> util = new ExcelUtil<>(KbAgentConfigRespVO.class);
        util.exportExcel(response, KbAgentConfigConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入agent配置列表")
//    @PreAuthorize("@ss.hasPermi('kb:agent:config:import')")
    @Log(title = "agent配置", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<KbAgentConfigRespVO> util = new ExcelUtil<>(KbAgentConfigRespVO.class);
        List<KbAgentConfigRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = kbAgentConfigService.importKbAgentConfig(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取 agent 配置详细信息")
//    @PreAuthorize("@ss.hasPermi('kb:agent:config:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<KbAgentConfigRespVO> getInfo(@PathVariable("id") Long id) {
        KbAgentConfigDO kbAgentConfigDO = kbAgentConfigService.getKbAgentConfigById(id);
        return CommonResult.success(BeanUtils.toBean(kbAgentConfigDO, KbAgentConfigRespVO.class));
    }

    @Operation(summary = "根据 botId 获取 agent 配置")
//    @PreAuthorize("@ss.hasPermi('kb:agent:config:query')")
    @GetMapping(value = "/byBot/{botId}")
    public CommonResult<KbAgentConfigRespVO> getByBotId(@PathVariable("botId") Long botId) {
        KbAgentConfigDO kbAgentConfigDO = kbAgentConfigService.getKbAgentConfigByBotId(botId);
        if (kbAgentConfigDO == null) {
            return CommonResult.success(null);
        }
        // 转为 VO
        KbAgentConfigRespVO respVO = BeanUtils.toBean(kbAgentConfigDO, KbAgentConfigRespVO.class);
        // 填充知识库名称列表
        if (StringUtils.isNotBlank(kbAgentConfigDO.getKnowledgeIds())) {
            Set<String> knowledgeIdSet = StringUtils.str2Set(kbAgentConfigDO.getKnowledgeIds(), ",");
            List<KmcKnowledgeBaseRespDTO> knowledgeList = kmcApiService.getKnowledgeBaseByIds(
                knowledgeIdSet.stream().map(Long::parseLong).toList()
            );
            respVO.setKnowledgeNames(knowledgeList.stream().map(KmcKnowledgeBaseRespDTO::getName).toList());
        }
        // 填充工具方法名称列表
        if (StringUtils.isNotBlank(kbAgentConfigDO.getToolMethodIds())) {
            Set<String> methodIdSet = StringUtils.str2Set(kbAgentConfigDO.getToolMethodIds(), ",");
            List<KbToolMethodDO> toolList = kbToolMethodService.listByIds(methodIdSet);
            respVO.setToolMethodNames(toolList.stream().map(KbToolMethodDO::getName).toList());
        }
        return CommonResult.success(respVO);
    }

    @Operation(summary = "新增agent配置")
//    @PreAuthorize("@ss.hasPermi('kb:agent:config:add')")
    @Log(title = "agent配置", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody KbAgentConfigSaveReqVO kbAgentConfig) {
        kbAgentConfig.setWorkspaceId(super.getWorkSpaceId());
        kbAgentConfig.setCreatorId(super.getUserId());
        kbAgentConfig.setCreateBy(super.getUsername());
        return CommonResult.toAjax(kbAgentConfigService.createKbAgentConfig(kbAgentConfig));
    }

    @Operation(summary = "修改agent配置")
//    @PreAuthorize("@ss.hasPermi('kb:agent:config:edit')")
    @Log(title = "agent配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody KbAgentConfigSaveReqVO kbAgentConfig) {
        return CommonResult.toAjax(kbAgentConfigService.updateKbAgentConfig(kbAgentConfig));
    }

    @Operation(summary = "删除agent配置")
//    @PreAuthorize("@ss.hasPermi('kb:agent:config:remove')")
    @Log(title = "agent配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(kbAgentConfigService.removeKbAgentConfig(Arrays.asList(ids)));
    }

    @Operation(summary = "调试agent")
    @PostMapping(value = "/testChatMessages", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<CommonResult<KbChatMessageSendRespVO>> runChatMessages(@Valid @RequestBody KbAgentConfigReqVO kbAgentConfig) {
        kbAgentConfig.setCreatorId(getUserId());
        kbAgentConfig.setWorkspaceId(getWorkSpaceId());
        kbAgentConfig.setCreateTime(new Date());
        try {
            return kbAgentConfigService.chatMessage(kbAgentConfig)
                    .map(CommonResult::success)
                    // 错误转换
                    .onErrorResume(e -> Flux.just(CommonResult.error(GlobalErrorCodeConstants.ERROR.getCode(), e.getMessage())));
        } catch (Exception e) {
            return Flux.just(CommonResult.error(GlobalErrorCodeConstants.ERROR.getCode(), e.getMessage()));
        }
    }

}
