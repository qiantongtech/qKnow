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
import tech.qiantong.qknow.ai.utils.AiErrorTranslator;
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
import tech.qiantong.qknow.module.kb.dal.dataobject.bot.KbBotDO;
import tech.qiantong.qknow.module.kb.dal.dataobject.mcp.KbMcpConfigDO;
import tech.qiantong.qknow.module.kb.dal.dataobject.mcp.KbMcpToolDO;
import tech.qiantong.qknow.module.kb.dal.dataobject.skills.KbSkillsDO;
import tech.qiantong.qknow.module.kb.dal.dataobject.tool.KbToolMethodDO;
import tech.qiantong.qknow.module.kb.service.agent.IKbAgentConfigService;
import tech.qiantong.qknow.module.kb.service.bot.IKbBotService;
import tech.qiantong.qknow.module.kb.service.mcp.IKbMcpConfigService;
import tech.qiantong.qknow.module.kb.service.skills.IKbSkillsService;
import tech.qiantong.qknow.module.kb.service.tool.IKbToolMethodService;
import tech.qiantong.qknow.module.kmc.api.knowledgeBase.dto.KmcKnowledgeBaseRespDTO;
import tech.qiantong.qknow.module.kmc.api.service.IKmcApiService;

import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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

    @Resource
    private IKbSkillsService kbSkillsService;

    @Resource
    private IKbMcpConfigService kbMcpConfigService;
    @Resource
    private IKbBotService kbBotService;

    @Operation(summary = "查询agent配置列表")
//    @PreAuthorize("@ss.hasPermi('kb:agent:config:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<KbAgentConfigRespVO>> list(KbAgentConfigPageReqVO kbAgentConfig) {
        kbAgentConfig.setWorkspaceId(super.getWorkSpaceId());
        PageResult<KbAgentConfigDO> page = kbAgentConfigService.getKbAgentConfigPage(kbAgentConfig);
        PageResult<KbAgentConfigRespVO> result = BeanUtils.toBean(page, KbAgentConfigRespVO.class);
        fillCallableBotConfigs(page.getList(), result.getList());
        return CommonResult.success(result);
    }

    @Operation(summary = "导出agent配置列表")
//    @PreAuthorize("@ss.hasPermi('kb:agent:config:export')")
    @Log(title = "agent配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, KbAgentConfigPageReqVO exportReqVO) {
        exportReqVO.setWorkspaceId(super.getWorkSpaceId());
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<KbAgentConfigDO> list = (List<KbAgentConfigDO>) kbAgentConfigService.getKbAgentConfigPage(exportReqVO).getRows();
        ExcelUtil<KbAgentConfigRespVO> util = new ExcelUtil<>(KbAgentConfigRespVO.class);
        List<KbAgentConfigRespVO> respList = KbAgentConfigConvert.INSTANCE.convertToRespVOList(list);
        fillCallableBotConfigs(list, respList);
        util.exportExcel(response, respList, "应用管理数据");
    }

    @Operation(summary = "导入agent配置列表")
//    @PreAuthorize("@ss.hasPermi('kb:agent:config:import')")
    @Log(title = "agent配置", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<KbAgentConfigRespVO> util = new ExcelUtil<>(KbAgentConfigRespVO.class);
        List<KbAgentConfigRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        importExcelList.forEach(item -> {
            item.setWorkspaceId(getWorkSpaceId());
            item.setCreatorId(getUserId());
            item.setUpdaterId(getUserId());
        });
        String message = kbAgentConfigService.importKbAgentConfig(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取 agent 配置详细信息")
//    @PreAuthorize("@ss.hasPermi('kb:agent:config:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<KbAgentConfigRespVO> getInfo(@PathVariable("id") Long id) {
        KbAgentConfigDO kbAgentConfigDO = kbAgentConfigService.getKbAgentConfigById(id);
        if (kbAgentConfigDO == null || !getWorkSpaceId().equals(kbAgentConfigDO.getWorkspaceId())) {
            return CommonResult.success(null);
        }
        KbAgentConfigRespVO respVO = BeanUtils.toBean(kbAgentConfigDO, KbAgentConfigRespVO.class);
        fillCallableBotConfig(kbAgentConfigDO, respVO);
        return CommonResult.success(respVO);
    }

    @Operation(summary = "根据 botId 获取 agent 配置")
//    @PreAuthorize("@ss.hasPermi('kb:agent:config:query')")
    @GetMapping(value = "/byBot/{botId}")
    public CommonResult<KbAgentConfigRespVO> getByBotId(@PathVariable("botId") Long botId) {
        KbAgentConfigDO kbAgentConfigDO = kbAgentConfigService.getKbAgentConfigByBotId(botId);
        if (kbAgentConfigDO == null || !getWorkSpaceId().equals(kbAgentConfigDO.getWorkspaceId())) {
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
        // 填充 skills 名称列表
        if (StringUtils.isNotBlank(kbAgentConfigDO.getSkillIds())) {
            Set<String> skillIdSet = StringUtils.str2Set(kbAgentConfigDO.getSkillIds(), ",");
            List<KbSkillsDO> skillsList = kbSkillsService.listByIds(skillIdSet);
            respVO.setSkillNames(skillsList.stream().map(KbSkillsDO::getName).toList());
        }

        // 填充 mcpConfig 名称列表
        if (StringUtils.isNotBlank(kbAgentConfigDO.getMcpConfigIds())) {
            Set<String> mcpSet = StringUtils.str2Set(kbAgentConfigDO.getMcpConfigIds(), ",");
            List<KbMcpConfigDO> mcpList = kbMcpConfigService.listByIds(mcpSet);
            respVO.setMcpConfigNames(mcpList.stream().map(KbMcpConfigDO::getName).toList());
        }
        fillCallableBotConfig(kbAgentConfigDO, respVO);
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
        return CommonResult.success(kbAgentConfigService.createKbAgentConfig(kbAgentConfig));
    }

    @Operation(summary = "修改agent配置")
//    @PreAuthorize("@ss.hasPermi('kb:agent:config:edit')")
    @Log(title = "agent配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody KbAgentConfigSaveReqVO kbAgentConfig) {
        kbAgentConfig.setWorkspaceId(super.getWorkSpaceId());
        kbAgentConfig.setUpdaterId(super.getUserId());
        kbAgentConfig.setUpdateBy(super.getUsername());
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
                    .onErrorResume(e -> Flux.just(CommonResult.error(
                            GlobalErrorCodeConstants.ERROR.getCode(), resolveErrorMessage(e))));
        } catch (Exception e) {
            return Flux.just(CommonResult.error(
                    GlobalErrorCodeConstants.ERROR.getCode(), resolveErrorMessage(e)));
        }
    }

    private String resolveErrorMessage(Throwable throwable) {
        Throwable current = throwable;
        while (current != null) {
            if (StringUtils.isNotBlank(current.getMessage())) {
                return translateAgentError(current.getMessage());
            }
            current = current.getCause();
        }
        return "Agent 执行失败，请检查模型配置、工具和 MCP 连接";
    }

    /**
     * 将 Agent 执行异常消息翻译为用户可见的中文描述。
     * 业务层会包装 "Agent 执行失败：" 前缀，需剥离后再翻译模型厂商报错。
     */
    private String translateAgentError(String message) {
        String prefix = "Agent 执行失败：";
        if (message.startsWith(prefix)) {
            String detail = message.substring(prefix.length());
            // 已知的模型厂商报错走翻译，其它业务文案保留原文
            if (AiErrorTranslator.matches(detail)) {
                return prefix + AiErrorTranslator.translate(detail);
            }
            return message;
        }
        if (AiErrorTranslator.matches(message)) {
            return AiErrorTranslator.translate(message);
        }
        return message;
    }

    private void fillCallableBotConfig(KbAgentConfigDO configDO, KbAgentConfigRespVO respVO) {
        String callableBotIds = configDO.getCallableBotIds();
        respVO.setCallableBotIds(callableBotIds);
        if (StringUtils.isBlank(callableBotIds)) {
            return;
        }
        try {
            List<Long> orderedIds = Arrays.stream(callableBotIds.split(","))
                    .map(String::trim)
                    .filter(value -> StringUtils.isNotBlank(value))
                    .map(Long::parseLong)
                    .distinct()
                    .toList();
            Map<Long, KbBotDO> callableBotMap = new LinkedHashMap<>();
            for (KbBotDO callableBot : kbBotService.listByIds(orderedIds)) {
                if (getWorkSpaceId().equals(callableBot.getWorkspaceId())) {
                    callableBotMap.put(callableBot.getId(), callableBot);
                }
            }
            respVO.setCallableBotNames(orderedIds.stream()
                    .map(id -> callableBotMap.containsKey(id)
                            ? callableBotMap.get(id).getName() : "Bot " + id)
                    .toList());
            respVO.setCallableBotTypes(orderedIds.stream()
                    .map(id -> callableBotMap.containsKey(id)
                            ? callableBotMap.get(id).getType() : null)
                    .toList());
        } catch (Exception e) {
            // 兼容历史异常配置，不影响 Agent 其他配置的读取。
        }
    }

    private void fillCallableBotConfigs(List<KbAgentConfigDO> configList, List<KbAgentConfigRespVO> respList) {
        Map<Long, KbAgentConfigDO> configMap = configList.stream()
                .collect(java.util.stream.Collectors.toMap(KbAgentConfigDO::getId, item -> item));
        for (KbAgentConfigRespVO respVO : respList) {
            KbAgentConfigDO configDO = configMap.get(respVO.getId());
            if (configDO != null) {
                fillCallableBotConfig(configDO, respVO);
            }
        }
    }

}
