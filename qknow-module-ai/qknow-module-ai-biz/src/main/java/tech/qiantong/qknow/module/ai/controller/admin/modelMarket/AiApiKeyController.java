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

package tech.qiantong.qknow.module.ai.controller.admin.modelMarket;

import com.alibaba.fastjson2.JSONObject;
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
import tech.qiantong.qknow.common.utils.object.BeanUtils;
import tech.qiantong.qknow.module.ai.controller.admin.modelMarket.vo.AiApiKeyPageReqVO;
import tech.qiantong.qknow.module.ai.controller.admin.modelMarket.vo.AiApiKeyRespVO;
import tech.qiantong.qknow.module.ai.controller.admin.modelMarket.vo.AiApiKeySaveReqVO;
import tech.qiantong.qknow.module.ai.dal.dataobject.modelMarket.AiApiKeyDO;
import tech.qiantong.qknow.module.ai.dal.enums.ApiKeyStatus;
import tech.qiantong.qknow.module.ai.service.modelMarket.IAiApiKeyService;

import java.util.List;

/**
 * API秘钥Controller
 * <p>
 * service.modelMarket
 *
 * @author qknow
 * @date 2025-12-23
 */
@Tag(name = "API秘钥")
@RestController
@RequestMapping("/ai/key")
@Validated
public class AiApiKeyController extends BaseController {

    @Resource
    private IAiApiKeyService aiApiKeyService;

    @Operation(summary = "查询API秘钥列表")
    @PreAuthorize("@ss.hasPermi('ai:modelMarket:key:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<AiApiKeyRespVO>> list(AiApiKeyPageReqVO aiApiKey) {
        PageResult<AiApiKeyPageReqVO> page = aiApiKeyService.getAiApiKeyPage(aiApiKey);
        return CommonResult.success(BeanUtils.toBean(page, AiApiKeyRespVO.class));
    }

    @Operation(summary = "查询API秘钥列表")
    @PreAuthorize("@ss.hasPermi('ai:modelMarket:key:list')")
    @GetMapping("/listByPlatform")
    public CommonResult<List<AiApiKeyRespVO>> listByPlatform(String platform) {
        List<AiApiKeyDO> result = aiApiKeyService.listByPlatform(platform);
        return CommonResult.success(BeanUtils.toBean(result, AiApiKeyRespVO.class));
    }

    @Operation(summary = "查询我的模型列表")
    @PreAuthorize("@ss.hasPermi('ai:modelMarket:key:list')")
    @GetMapping("myModelPage")
    public CommonResult<PageResult<AiApiKeyRespVO>> myModelPage(AiApiKeyPageReqVO aiApiKey) {
        aiApiKey.setStatus(ApiKeyStatus.CONFIG.getCode());
        PageResult<AiApiKeyPageReqVO> page = aiApiKeyService.queryMyModelPage(aiApiKey);
        return CommonResult.success(BeanUtils.toBean(page, AiApiKeyRespVO.class));
    }

    @Operation(summary = "获取API秘钥详细信息")
    @PreAuthorize("@ss.hasPermi('ai:modelMarket:key:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<AiApiKeyRespVO> getInfo(@PathVariable("id") Long id) {
        AiApiKeyDO aiApiKeyDO = aiApiKeyService.getAiApiKeyById(id);
        return CommonResult.success(BeanUtils.toBean(aiApiKeyDO, AiApiKeyRespVO.class));
    }

    @Operation(summary = "获取API秘钥详细信息")
    @PreAuthorize("@ss.hasPermi('ai:modelMarket:key:query')")
    @GetMapping(value = "/getByPlatform")
    public CommonResult<AiApiKeyRespVO> getByPlatform(String platform) {
        AiApiKeyDO aiApiKeyDO = aiApiKeyService.queryByPlatform(platform);
        return CommonResult.success(BeanUtils.toBean(aiApiKeyDO, AiApiKeyRespVO.class));
    }

    @Operation(summary = "新增API秘钥")
    @PreAuthorize("@ss.hasPermi('ai:modelMarket:key:edit')")
    @Log(title = "API秘钥", businessType = BusinessType.UPDATE)
    @PostMapping
    public CommonResult<Boolean> save(@Valid @RequestBody AiApiKeySaveReqVO aiApiKey) {
        aiApiKey.setWorkspaceId(super.getWorkSpaceId());
        return CommonResult.toAjax(aiApiKeyService.saveAiApiKey(aiApiKey));
    }

    @Operation(summary = "修改API秘钥")
    @PreAuthorize("@ss.hasPermi('ai:modelMarket:key:edit')")
    @Log(title = "API秘钥", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody AiApiKeySaveReqVO aiApiKey) {
        return CommonResult.toAjax(aiApiKeyService.updateAiApiKey(aiApiKey));
    }

    @Operation(summary = "移除API秘钥")
    @PreAuthorize("@ss.hasPermi('ai:modelMarket:key:edit')")
    @DeleteMapping(value = "/{id}")
    public CommonResult<Boolean> removeKey(@PathVariable("id") Long id) {
        return CommonResult.toAjax(aiApiKeyService.removeKey(id));
    }

    @Operation(summary = "移除API秘钥")
    @PreAuthorize("@ss.hasPermi('ai:modelMarket:key:edit')")
    @PostMapping("/submitBatch")
    public CommonResult<Boolean> submitBatch(@RequestBody JSONObject jsonObject) {
        String platform = jsonObject.getString("platform");
        List<AiApiKeySaveReqVO> keyList = jsonObject.getList("keyList", AiApiKeySaveReqVO.class);
        Boolean result = aiApiKeyService.submitBatch(platform, keyList,super.getWorkSpaceId());
        return CommonResult.toAjax(result);
    }

}
