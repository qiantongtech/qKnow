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

package tech.qiantong.qknow.module.ext.controller.admin.extTaskLog;

import cn.hutool.core.lang.Assert;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tech.qiantong.qknow.common.annotation.Log;
import tech.qiantong.qknow.common.core.controller.BaseController;
import tech.qiantong.qknow.common.core.domain.CommonResult;
import tech.qiantong.qknow.common.core.page.PageParam;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.common.enums.BusinessType;
import tech.qiantong.qknow.common.utils.object.BeanUtils;
import tech.qiantong.qknow.common.utils.poi.ExcelUtil;
import tech.qiantong.qknow.module.ext.controller.admin.extTaskLog.vo.ExtTaskLogDetailPageReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extTaskLog.vo.ExtTaskLogPageReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extTaskLog.vo.ExtTaskLogRespVO;
import tech.qiantong.qknow.module.ext.convert.extTaskLog.ExtTaskLogConvert;
import tech.qiantong.qknow.module.ext.dal.dataobject.extTaskLog.ExtTaskLogDO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extTaskLog.ExtTaskLogDetailDO;
import tech.qiantong.qknow.module.ext.service.extTaskLog.IExtTaskLogService;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Arrays;
import java.util.List;

/**
 * 抽取任务执行日志Controller
 *
 * @author qknow
 * @date 2025-12-03
 */
@Tag(name = "抽取任务执行日志")
@RestController
@RequestMapping("/ext/taskLog")
@Validated
public class ExtTaskLogController extends BaseController {
    @Resource
    private IExtTaskLogService extTaskLogService;

    @Operation(summary = "查询抽取任务执行日志列表")
    @PreAuthorize("@ss.hasAnyPermi('ext:extTasklog:tasklog:list,ext:extUnstructTask:unstructtask:taskLog')")
    @GetMapping("/list")
    public CommonResult<PageResult<ExtTaskLogRespVO>> list(ExtTaskLogPageReqVO extTaskLog) {
        PageResult<ExtTaskLogDO> page = extTaskLogService.getExtTaskLogPage(extTaskLog);
        return CommonResult.success(BeanUtils.toBean(page, ExtTaskLogRespVO.class));
    }

    @Operation(summary = "抽取任务详情分页")
    @PreAuthorize("@ss.hasAnyPermi('ext:extTasklog:tasklog:list,ext:extUnstructTask:unstructtask:taskLog')")
    @GetMapping("detail/page")
    public CommonResult<PageResult<ExtTaskLogDetailPageReqVO>> detailPage(ExtTaskLogDetailPageReqVO extTaskLogDetail) {
        Assert.notNull(extTaskLogDetail.getLogId(),"日志id 不能为空");
        PageResult<ExtTaskLogDetailDO> page = extTaskLogService.getLogDetailPage(extTaskLogDetail);
        return CommonResult.success(BeanUtils.toBean(page, ExtTaskLogDetailPageReqVO.class));
    }

    @Operation(summary = "导出抽取任务执行日志列表")
    @PreAuthorize("@ss.hasPermi('ext:extTasklog:tasklog:export')")
    @Log(title = "抽取任务执行日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ExtTaskLogPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ExtTaskLogDO> list = (List<ExtTaskLogDO>) extTaskLogService.getExtTaskLogPage(exportReqVO).getRows();
        ExcelUtil<ExtTaskLogRespVO> util = new ExcelUtil<>(ExtTaskLogRespVO.class);
        util.exportExcel(response, ExtTaskLogConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "获取抽取任务执行日志详细信息")
    @PreAuthorize("@ss.hasAnyPermi('ext:extTasklog:tasklog:list,ext:extUnstructTask:unstructtask:taskLog')")
    @GetMapping(value = "/{id}")
    public CommonResult<ExtTaskLogRespVO> getInfo(@PathVariable("id") Long id) {
        ExtTaskLogDO extTaskLogDO = extTaskLogService.getExtTaskLogById(id);
        return CommonResult.success(BeanUtils.toBean(extTaskLogDO, ExtTaskLogRespVO.class));
    }

    @Operation(summary = "删除抽取日志")
    @Log(title = "关系映射", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(extTaskLogService.removeExtTaskById(Arrays.asList(ids)));
    }
}
