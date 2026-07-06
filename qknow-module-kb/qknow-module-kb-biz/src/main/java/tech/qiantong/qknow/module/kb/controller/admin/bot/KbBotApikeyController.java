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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tech.qiantong.qknow.common.annotation.Log;
import tech.qiantong.qknow.common.core.controller.BaseController;
import tech.qiantong.qknow.common.core.domain.CommonResult;
import tech.qiantong.qknow.common.core.domain.model.LoginUser;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.common.enums.BusinessType;
import tech.qiantong.qknow.common.utils.object.BeanUtils;
import tech.qiantong.qknow.module.kb.controller.admin.bot.vo.KbBotApikeyPageReqVO;
import tech.qiantong.qknow.module.kb.controller.admin.bot.vo.KbBotApikeyRespVO;
import tech.qiantong.qknow.module.kb.controller.admin.bot.vo.KbBotApikeySaveReqVO;
import tech.qiantong.qknow.module.kb.dal.dataobject.bot.KbBotApikeyDO;
import tech.qiantong.qknow.module.kb.service.bot.IKbBotApikeyService;

import java.util.Arrays;

/**
 * bot访问密钥Controller
 *
 * @author qknow
 * @date 2026-04-24
 */
@Tag(name = "bot访问密钥")
@RestController
@RequestMapping("/kb/apikey")
@Validated
public class KbBotApikeyController extends BaseController {
    @Resource
    private IKbBotApikeyService kbBotApikeyService;

    @Operation(summary = "查询bot访问密钥列表")
//    @PreAuthorize("@ss.hasPermi('kb:botApikey:apikey:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<KbBotApikeyRespVO>> list(KbBotApikeyPageReqVO kbBotApikey) {
        PageResult<KbBotApikeyDO> page = kbBotApikeyService.getKbBotApikeyPage(kbBotApikey);
        return CommonResult.success(BeanUtils.toBean(page, KbBotApikeyRespVO.class));
    }

    @Operation(summary = "生成访问密钥")
//    @PreAuthorize("@ss.hasPermi('kb:botApikey:apikey:add')")
    @Log(title = "生成访问密钥", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Boolean> generate(@Valid @RequestBody KbBotApikeySaveReqVO kbBotApikey) {
        LoginUser currentUser = super.getLoginUser();
        kbBotApikey.setCreatorId(getUserId());
        kbBotApikey.setWorkspaceId(getWorkSpaceId());

        return CommonResult.toAjax(kbBotApikeyService.generate(kbBotApikey,currentUser));
    }

    @Operation(summary = "删除bot访问密钥")
//    @PreAuthorize("@ss.hasPermi('kb:botApikey:apikey:remove')")
    @Log(title = "bot访问密钥", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(kbBotApikeyService.removeKbBotApikey(Arrays.asList(ids)));
    }

}
