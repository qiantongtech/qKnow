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

import com.alibaba.fastjson2.JSONArray;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tech.qiantong.qknow.ai.enums.model.AiModelTypeEnum;
import tech.qiantong.qknow.common.annotation.Log;
import tech.qiantong.qknow.common.core.controller.BaseController;
import tech.qiantong.qknow.common.core.domain.CommonResult;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.common.core.utils.object.BeanUtils;
import tech.qiantong.qknow.common.enums.BusinessType;
import tech.qiantong.qknow.module.ai.controller.admin.modelMarket.vo.AiModelPageReqVO;
import tech.qiantong.qknow.module.ai.controller.admin.modelMarket.vo.AiModelSaveReqVO;
import tech.qiantong.qknow.module.ai.dal.dataobject.modelMarket.AiModelDO;
import tech.qiantong.qknow.module.ai.service.modelMarket.IAiModelService;

/**
 * AI 模型Controller
 *
 * @author qknow
 * @date 2025-12-23
 */
@Tag(name = "AI 模型")
@RestController
@RequestMapping("/ai/model")
@Validated
public class AiModelController extends BaseController {

    @Resource
    private IAiModelService aiModelService;

    @Operation(summary = "新增AI 模型")
    @PreAuthorize("@ss.hasPermi('ai:modelMarket:key:edit')")
    @Log(title = "AI 模型", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody AiModelSaveReqVO aiModel) {
        return CommonResult.toAjax(aiModelService.createAiModel(aiModel));
    }

    @Operation(summary = "删除AI 模型")
    @PreAuthorize("@ss.hasPermi('ai:modelMarket:key:edit')")
    @Log(title = "AI 模型", businessType = BusinessType.DELETE)
    @DeleteMapping("/{keyId}/{modelName}")
    public CommonResult<Integer> remove(@PathVariable Long keyId, @PathVariable String modelName) {
        return CommonResult.toAjax(aiModelService.removeAiModel(keyId, modelName));
    }

    @Operation(summary = "更改模型的启用状态")
    @PreAuthorize("@ss.hasPermi('ai:modelMarket:key:edit')")
    @PutMapping("changeModelEnable")
    public CommonResult<Boolean> changeModelEnable(@Valid @RequestBody AiModelSaveReqVO aiModel) {
        return CommonResult.toAjax(aiModelService.changeModelEnable(aiModel));
    }

    @Operation(summary = "获取平台下模型列表")
    @GetMapping("/getModelPage")
    public CommonResult<PageResult<AiModelPageReqVO>> getModelPage(AiModelPageReqVO modelPage) {
        PageResult<AiModelDO> page = aiModelService.getModelPage(modelPage);
        return CommonResult.success(BeanUtils.toBean(page, AiModelPageReqVO.class));
    }

    @Operation(summary = "获取模型字典")
    @GetMapping("getChatModelDict")
    public CommonResult<JSONArray> getChatModelDict() {
        JSONArray modelDict = aiModelService.getModelDict(AiModelTypeEnum.CHAT.getType());
        return CommonResult.success(modelDict);
    }

}
