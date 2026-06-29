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

package tech.qiantong.qknow.module.kb.controller.admin.bot;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tech.qiantong.qknow.common.annotation.Log;
import tech.qiantong.qknow.common.enums.BusinessType;
import tech.qiantong.qknow.common.core.controller.BaseController;
import tech.qiantong.qknow.common.core.domain.CommonResult;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.common.core.utils.object.BeanUtils;
import tech.qiantong.qknow.module.kb.controller.admin.bot.vo.KbBotPageReqVO;
import tech.qiantong.qknow.module.kb.controller.admin.bot.vo.KbBotRespVO;
import tech.qiantong.qknow.module.kb.controller.admin.bot.vo.KbBotSaveReqVO;
import tech.qiantong.qknow.module.kb.dal.dataobject.bot.KbBotDO;
import tech.qiantong.qknow.module.kb.service.bot.IKbBotService;

import java.util.Arrays;

/**
 * bot 管理Controller
 *
 * @author qknow
 * @date 2026-03-18
 */
@Tag(name = "bot 管理")
@RestController
@RequestMapping("/kb/bot")
@Validated
public class KbBotController extends BaseController {
    @Resource
    private IKbBotService kbBotService;

    @Operation(summary = "查询bot 管理列表")
    @PreAuthorize("@ss.hasPermi('kb:bot:bot:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<KbBotRespVO>> list(KbBotPageReqVO kbBot) {
        PageResult<KbBotDO> page = kbBotService.getKbBotPage(kbBot);
        PageResult<KbBotRespVO> kbBotRespVOPageResult = BeanUtils.toBean(page, KbBotRespVO.class);
        kbBotRespVOPageResult.getList().forEach(item -> {
            item.setNodeNum((int) (item.getId()+item.getBuiltinFlag())/3 +1);
            item.setApiKeyNum(2);
        });
        return CommonResult.success(kbBotRespVOPageResult);
    }

    @Operation(summary = "获取bot 管理详细信息")
    @PreAuthorize("@ss.hasPermi('kb:bot:bot:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<KbBotRespVO> getInfo(@PathVariable("id") Long id) {
        KbBotDO kbBotDO = kbBotService.getKbBotById(id);
        return CommonResult.success(BeanUtils.toBean(kbBotDO, KbBotRespVO.class));
    }

    @Operation(summary = "新增bot 管理")
    @PreAuthorize("@ss.hasPermi('kb:bot:bot:add')")
    @Log(title = "bot 管理", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody KbBotSaveReqVO kbBot) {
        kbBot.setWorkspaceId(super.getWorkSpaceId());
        return CommonResult.toAjax(kbBotService.createKbBot(kbBot));
    }

    @Operation(summary = "修改bot 管理")
    @PreAuthorize("@ss.hasPermi('kb:bot:bot:edit')")
    @Log(title = "bot 管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody KbBotSaveReqVO kbBot) {
        return CommonResult.toAjax(kbBotService.updateKbBot(kbBot));
    }

    @Operation(summary = "删除bot 管理")
    @PreAuthorize("@ss.hasPermi('kb:bot:bot:remove')")
    @Log(title = "bot 管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.success(kbBotService.removeKbBot(Arrays.asList(ids)));
    }

    @Operation(summary = "复制bot 管理")
    @PreAuthorize("@ss.hasPermi('kb:bot:bot:add')")
    @Log(title = "bot 管理", businessType = BusinessType.INSERT)
    @PostMapping("/copyBot")
    public CommonResult<Long> copyBot(@Valid @RequestBody KbBotSaveReqVO kbBot) {
        kbBot.setWorkspaceId(super.getWorkSpaceId());
        return CommonResult.success(kbBotService.copyKbBot(kbBot));
    }
}
