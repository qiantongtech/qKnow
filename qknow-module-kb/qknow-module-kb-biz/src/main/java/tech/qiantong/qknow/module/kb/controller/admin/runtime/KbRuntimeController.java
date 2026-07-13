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

package tech.qiantong.qknow.module.kb.controller.admin.runtime;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tech.qiantong.qknow.common.annotation.Log;
import tech.qiantong.qknow.common.enums.BusinessType;
import tech.qiantong.qknow.common.core.controller.BaseController;
import tech.qiantong.qknow.common.core.domain.CommonResult;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.common.core.utils.object.BeanUtils;
import tech.qiantong.qknow.module.kb.controller.admin.runtime.vo.KbRuntimePageReqVO;
import tech.qiantong.qknow.module.kb.controller.admin.runtime.vo.KbRuntimeRespVO;
import tech.qiantong.qknow.module.kb.dal.dataobject.runtime.KbRuntimeDO;
import tech.qiantong.qknow.module.kb.service.runtime.IKbRuntimeService;

import java.util.Arrays;

/**
 * bot运行Controller
 *
 * @author qknow
 * @date 2026-03-18
 */
@Tag(name = "bot运行")
@RestController
@RequestMapping("/kb/runtime")
@Validated
public class KbRuntimeController extends BaseController {
    @Resource
    private IKbRuntimeService kbRuntimeService;

    @Operation(summary = "查询bot运行列表")
//    @PreAuthorize("@ss.hasPermi('kb:kb:runtime:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<KbRuntimeRespVO>> list(KbRuntimePageReqVO kbRuntime) {
        PageResult<KbRuntimeDO> page = kbRuntimeService.getKbRuntimePage(kbRuntime);
        return CommonResult.success(BeanUtils.toBean(page, KbRuntimeRespVO.class));
    }

    @Operation(summary = "获取bot运行详细信息")
//    @PreAuthorize("@ss.hasPermi('kb:kb:runtime:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<KbRuntimeRespVO> getInfo(@PathVariable("id") Long id) {
        KbRuntimeDO kbRuntimeDO = kbRuntimeService.getKbRuntimeById(id);
        return CommonResult.success(BeanUtils.toBean(kbRuntimeDO, KbRuntimeRespVO.class));
    }

    @Operation(summary = "删除bot运行")
//    @PreAuthorize("@ss.hasPermi('kb:kb:runtime:remove')")
    @Log(title = "bot运行", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(kbRuntimeService.removeKbRuntime(Arrays.asList(ids)));
    }

}
