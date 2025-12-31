/*
 * Copyright © 2026 Qiantong Technology Co., Ltd.
 * qKnow Knowledge Platform
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please apply for brand customization authorization via official channels.
 *  *
 * More information: https://qknow.qiantong.tech/business.html
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2026 江苏千桐科技有限公司
 * qKnow 知识平台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qknow.qiantong.tech/business.html
 */

package tech.qiantong.qknow.module.system.controller.admin.system.message;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tech.qiantong.qknow.common.annotation.Log;
import tech.qiantong.qknow.common.core.controller.BaseController;
import tech.qiantong.qknow.common.core.domain.CommonResult;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.common.enums.BusinessType;
import tech.qiantong.qknow.common.utils.object.BeanUtils;
import tech.qiantong.qknow.common.utils.poi.ExcelUtil;
import tech.qiantong.qknow.module.system.controller.admin.system.message.vo.MessageTemplatePageReqVO;
import tech.qiantong.qknow.module.system.controller.admin.system.message.vo.MessageTemplateRespVO;
import tech.qiantong.qknow.module.system.controller.admin.system.message.vo.MessageTemplateSaveReqVO;
import tech.qiantong.qknow.module.system.convert.message.MessageTemplateConvert;
import tech.qiantong.qknow.module.system.dal.dataobject.message.MessageTemplateDO;
import tech.qiantong.qknow.module.system.service.message.IMessageTemplateService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 消息模板Controller
 *
 * @author qknow
 * @date 2024-10-31
 */
@Tag(name = "消息模板")
@RestController
@RequestMapping("/system/messageTemplate")
@Validated
public class MessageTemplateController extends BaseController {
    @Resource
    private IMessageTemplateService messageTemplateService;

    @Operation(summary = "查询消息模板列表")
    @PreAuthorize("@ss.hasPermi('system:message:messageTemplate:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<MessageTemplateRespVO>> list(MessageTemplatePageReqVO messageTemplate) {
        startPage();
        PageResult<MessageTemplateDO> page = messageTemplateService.getMessageTemplatePage(messageTemplate);
        return CommonResult.success(BeanUtils.toBean(page, MessageTemplateRespVO.class));
    }

    @Operation(summary = "导出消息模板列表")
    @PreAuthorize("@ss.hasPermi('system:message:messageTemplate:export')")
    @Log(title = "消息模板", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MessageTemplatePageReqVO messageTemplate) {
        List<MessageTemplateDO> list = (List<MessageTemplateDO>) messageTemplateService.getMessageTemplatePage(messageTemplate).getRows();
        ExcelUtil<MessageTemplateRespVO> util = new ExcelUtil<>(MessageTemplateRespVO.class);
        util.exportExcel(response, MessageTemplateConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "获取消息模板详细信息")
    @PreAuthorize("@ss.hasPermi('system:message:messageTemplate:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<MessageTemplateRespVO> getInfo(@PathVariable("id") Long id) {
            MessageTemplateDO messageTemplateDO = messageTemplateService.getById(id);
        return CommonResult.success(BeanUtils.toBean(messageTemplateDO, MessageTemplateRespVO.class));
    }

    @Operation(summary = "新增消息模板")
    @PreAuthorize("@ss.hasPermi('system:message:messageTemplate:add')")
    @Log(title = "消息模板", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Boolean> add(@Valid @RequestBody MessageTemplateSaveReqVO messageTemplate) {
        MessageTemplateDO messageTemplateDO = BeanUtils.toBean(messageTemplate, MessageTemplateDO.class);
        messageTemplateDO.setCreatorId(getUserId());
        messageTemplateDO.setCreateBy(getNickName());
        return CommonResult.toAjax(messageTemplateService.save(messageTemplateDO));
    }

    @Operation(summary = "修改消息模板")
    @PreAuthorize("@ss.hasPermi('system:message:messageTemplate:edit')")
    @Log(title = "消息模板", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Boolean> edit(@Valid @RequestBody MessageTemplateSaveReqVO messageTemplate) {
        MessageTemplateDO messageTemplateDO = BeanUtils.toBean(messageTemplate, MessageTemplateDO.class);
        messageTemplateDO.setUpdaterId(getUserId());
        messageTemplateDO.setUpdateBy(getNickName());
        messageTemplateDO.setUpdateTime(new Date());
        return CommonResult.toAjax(messageTemplateService.updateById(messageTemplateDO));
    }

    @Operation(summary = "删除消息模板")
    @PreAuthorize("@ss.hasPermi('system:message:messageTemplate:remove')")
    @Log(title = "消息模板", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Boolean> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(messageTemplateService.removeByIds(Arrays.asList(ids)));
    }
}
