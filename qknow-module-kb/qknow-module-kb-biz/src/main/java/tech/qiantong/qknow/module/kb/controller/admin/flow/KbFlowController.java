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

package tech.qiantong.qknow.module.kb.controller.admin.flow;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import tech.qiantong.qknow.common.annotation.Log;
import tech.qiantong.qknow.common.enums.BusinessType;
import tech.qiantong.qknow.common.core.controller.BaseController;
import tech.qiantong.qknow.common.core.domain.CommonResult;
import tech.qiantong.qknow.module.kb.controller.admin.flow.vo.KbFlowVO;
import tech.qiantong.qknow.module.kb.controller.admin.runtime.vo.KbRuntimeRespVO;
import tech.qiantong.qknow.module.kb.service.flow.IKbFlowService;

/**
 * bot流程节点Controller
 *
 * @author qknow
 * @date 2026-03-18
 */
@Tag(name = "bot流程节点")
@RestController
@RequestMapping("/kb/flow")
@Validated
public class KbFlowController extends BaseController {

    @Resource
    private IKbFlowService flowService;

    @Operation(summary = "获取流程")
    @GetMapping
    public CommonResult<KbFlowVO> getFlow(@NotNull Long botId) {
        return CommonResult.success(flowService.queryFlow(botId));
    }

    @Operation(summary = "新增或修改bot流程")
    @Log(title = "bot流程", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Boolean> submitFlow(@Valid @RequestBody KbFlowVO flowVO) {
        flowVO.setWorkspaceId(super.getWorkSpaceId());
        return CommonResult.toAjax(flowService.submitFlow(flowVO));
    }

    @Operation(summary = "测试运行流程")
    @Log(title = "bot流程", businessType = BusinessType.UPDATE)
    @PostMapping(value = "/testExecuteFlow", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<CommonResult<String>> testExecuteFlow(@RequestBody JSONObject jsonObject) {
        KbFlowVO kbFlowVO = jsonObject.getObject("flow", KbFlowVO.class);
        kbFlowVO.setWorkspaceId(super.getWorkSpaceId());
        JSONObject input = jsonObject.getJSONObject("input");
        return flowService.testExecuteFlow(kbFlowVO, input);
    }

    @Operation(summary = "正式运行流程")
    @Log(title = "bot流程", businessType = BusinessType.FORCE)
    @PostMapping("/executeFlow")
    public CommonResult<KbRuntimeRespVO> executeFlow(@RequestBody JSONObject jsonObject) {
        KbFlowVO kbFlowVO = new KbFlowVO();
        kbFlowVO.setBotId(jsonObject.getLong("botId"));
        kbFlowVO.setWorkspaceId(super.getWorkSpaceId());
        JSONObject input = jsonObject.getJSONObject("input");
        return CommonResult.success(flowService.executeFlow(kbFlowVO, input));
    }

    @Operation(summary = "正式运行流程(流式输出)")
    @Log(title = "bot流程", businessType = BusinessType.UPDATE)
    @PostMapping(value = "/executeFlowStream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<CommonResult<String>> executeFlowStream(@RequestBody JSONObject jsonObject) {
        KbFlowVO kbFlowVO = new KbFlowVO();
        kbFlowVO.setBotId(jsonObject.getLong("botId"));
        kbFlowVO.setWorkspaceId(super.getWorkSpaceId());
        JSONObject input = jsonObject.getJSONObject("input");
        return flowService.executeFlowStream(kbFlowVO, input);
    }

    // todo
//    @Operation(summary = "正式运行chatFlow")
//    @Log(title = "bot流程", businessType = BusinessType.UPDATE)
//    @PostMapping(value = "/executeChatFlow", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//    public Flux<CommonResult<String>> executeChatFlow(@RequestBody JSONObject jsonObject) {
//        KbFlowVO kbFlowVO = new KbFlowVO();
//        kbFlowVO.setBotId(jsonObject.getLong("botId"));
//        kbFlowVO.setWorkspaceId(super.getWorkSpaceId());
//        JSONObject input = jsonObject.getJSONObject("input");
//        Long conversationId = jsonObject.getLong("conversationId");
//
//        return flowService.executeChatFlow(kbFlowVO, input, conversationId);
//    }


    @Operation(summary = "测试运行chatFlow")
    @Log(title = "bot流程", businessType = BusinessType.UPDATE)
    @PostMapping(value = "/testExecuteChatFlow", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<CommonResult<String>> testExecuteChatFlow(@RequestBody JSONObject jsonObject) {
        KbFlowVO kbFlowVO = jsonObject.getObject("flow", KbFlowVO.class);
        kbFlowVO.setWorkspaceId(super.getWorkSpaceId());
        JSONObject input = jsonObject.getJSONObject("input");
        JSONArray messageList = jsonObject.getJSONArray("messageList");
        return flowService.testExecuteChatFlow(kbFlowVO, input,messageList);
    }

}
