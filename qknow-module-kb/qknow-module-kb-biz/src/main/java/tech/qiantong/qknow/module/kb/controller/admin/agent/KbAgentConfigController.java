/*
 * Copyright (c) 2026 Jiangsu Qiantong Technology Co., Ltd.
 *  *
 * Software Name: qKnow Knowledge Platform (Business Edition)
 * Software Copyright Registration No. 15980140
 *  *
 * [RIGHTS AND LICENSE STATEMENT]
 * This file contains non-public commercial source code of which Jiangsu Qiantong
 * Technology Co., Ltd. lawfully possesses complete intellectual property rights.
 *  *
 * Access and use are limited to entities or individuals who have signed a valid
 * commercial license agreement, within the scope stipulated in the agreement.
 * The "accessibility" of this source code is premised on lawful authorization
 * and does not constitute any form of transfer of intellectual property rights
 * or implied licensing.
 *  *
 * [PROHIBITIONS]
 * Unless explicitly agreed in the license agreement, the following acts in any
 * form are strictly prohibited:
 * 1. Copying, disseminating, disclosing, selling, renting, or redistributing
 * this source code;
 * 2. Providing the software's functionality to third parties via SaaS, PaaS,
 * cloud hosting, or other means;
 * 3. Using this software or its derivative versions to develop products that
 * compete with the Right Holder;
 * 4. Providing or displaying this source code or related technical information
 * to unauthorized third parties;
 * 5. Tampering with, circumventing, or destroying copyright notices, license
 * verifications, or other technical protection measures.
 *  *
 * [LEGAL LIABILITY]
 * Any unauthorized use constitutes an infringement of trade secrets and
 * intellectual property rights.
 *  *
 * The Right Holder will strictly pursue liability for breach of contract and
 * infringement in accordance with the commercial agreement and laws such as
 * the "Copyright Law of the People's Republic of China" and the "Anti-Unfair
 * Competition Law".
 *  *
 * ============================================================================
 *  *
 * Copyright (c) 2026 江苏千桐科技有限公司
 *  *
 * 软件名称：qKnow 知识平台（商业版） | 软著登字第15980140号
 *  *
 * 【权利与授权声明】
 * 本文件属于江苏千桐科技有限公司依法享有完全知识产权的非公开商业源代码。
 * 仅限已签署有效商业授权合同的单位或个人在约定范围内查阅和使用。
 * 源代码的“可访问性”均以合法授权为前提，不构成任何形式的知识产权转让或默示授权。
 *  *
 * 【禁止事项】
 * 除授权合同明确约定外，严禁任何形式的：
 * 1. 复制、传播、披露、出售、出租或再分发本源代码；
 * 2. 通过 SaaS、PaaS、云托管等方式向第三方提供本软件功能；
 * 3. 将本软件或其衍生版本用于开发与权利人构成竞争的产品；
 * 4. 向未授权第三方提供或展示本源代码或相关技术信息；
 * 5. 篡改、规避或破坏版权标识、授权校验及其他技术保护措施。
 *  *
 * 【法律责任】
 * 任何未经授权的利用行为，均构成对商业秘密及知识产权的侵害。
 * 权利人将依据商业合同及《中华人民共和国著作权法》《反不正当竞争法》
 * 等法律法规，严厉追究违约与侵权责任。
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
