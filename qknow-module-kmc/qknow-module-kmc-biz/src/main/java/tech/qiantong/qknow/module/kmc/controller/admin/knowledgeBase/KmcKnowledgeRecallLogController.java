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

package tech.qiantong.qknow.module.kmc.controller.admin.knowledgeBase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.qiantong.qknow.common.annotation.Log;
import tech.qiantong.qknow.common.core.controller.BaseController;
import tech.qiantong.qknow.common.core.domain.AjaxResult;
import tech.qiantong.qknow.common.core.domain.CommonResult;
import tech.qiantong.qknow.common.core.page.PageParam;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.common.enums.BusinessType;
import tech.qiantong.qknow.common.core.utils.object.BeanUtils;
import tech.qiantong.qknow.common.core.utils.poi.ExcelUtil;
import tech.qiantong.qknow.module.kmc.controller.admin.knowledgeBase.vo.KmcKnowledgeRecallLogPageReqVO;
import tech.qiantong.qknow.module.kmc.controller.admin.knowledgeBase.vo.KmcKnowledgeRecallLogRespVO;
import tech.qiantong.qknow.module.kmc.controller.admin.knowledgeBase.vo.KmcKnowledgeRecallLogSaveReqVO;
import tech.qiantong.qknow.module.kmc.convert.knowledgeBase.KmcKnowledgeRecallLogConvert;
import tech.qiantong.qknow.module.kmc.dal.dataobject.knowledgeBase.KmcKnowledgeRecallLogDO;
import tech.qiantong.qknow.module.kmc.service.knowledgeBase.IKmcKnowledgeRecallLogService;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * 召回记录Controller
 *
 * @author qknow
 * @date 2025-07-24
 */
@Tag(name = "召回记录")
@RestController
@RequestMapping("/kmc/log")
@Validated
public class KmcKnowledgeRecallLogController extends BaseController {
    @Resource
    private IKmcKnowledgeRecallLogService kmcKnowledgeRecallLogService;

    @Operation(summary = "查询召回记录列表")
    @GetMapping("/list")
    public CommonResult<PageResult<KmcKnowledgeRecallLogRespVO>> list(KmcKnowledgeRecallLogPageReqVO kmcKnowledgeRecallLog) {
        kmcKnowledgeRecallLog.setCreatorId(super.getUserId());// 只查看自己的召回记录
        PageResult<KmcKnowledgeRecallLogDO> page = kmcKnowledgeRecallLogService.getKmcKnowledgeRecallLogPage(kmcKnowledgeRecallLog);
        return CommonResult.success(BeanUtils.toBean(page, KmcKnowledgeRecallLogRespVO.class));
    }

    @Operation(summary = "导出召回记录列表")
    @Log(title = "召回记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, KmcKnowledgeRecallLogPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<KmcKnowledgeRecallLogDO> list = (List<KmcKnowledgeRecallLogDO>) kmcKnowledgeRecallLogService.getKmcKnowledgeRecallLogPage(exportReqVO).getRows();
        ExcelUtil<KmcKnowledgeRecallLogRespVO> util = new ExcelUtil<>(KmcKnowledgeRecallLogRespVO.class);
        util.exportExcel(response, KmcKnowledgeRecallLogConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入召回记录列表")
    @Log(title = "召回记录", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<KmcKnowledgeRecallLogRespVO> util = new ExcelUtil<>(KmcKnowledgeRecallLogRespVO.class);
        List<KmcKnowledgeRecallLogRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = kmcKnowledgeRecallLogService.importKmcKnowledgeRecallLog(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取召回记录详细信息")
    @GetMapping(value = "/{id}")
    public CommonResult<KmcKnowledgeRecallLogRespVO> getInfo(@PathVariable("id") Long id) {
        KmcKnowledgeRecallLogDO kmcKnowledgeRecallLogDO = kmcKnowledgeRecallLogService.getKmcKnowledgeRecallLogById(id);
        return CommonResult.success(BeanUtils.toBean(kmcKnowledgeRecallLogDO, KmcKnowledgeRecallLogRespVO.class));
    }

    @Operation(summary = "新增召回记录")
    @Log(title = "召回记录", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody KmcKnowledgeRecallLogSaveReqVO kmcKnowledgeRecallLog) {
        return CommonResult.toAjax(kmcKnowledgeRecallLogService.createKmcKnowledgeRecallLog(kmcKnowledgeRecallLog));
    }

    @Operation(summary = "修改召回记录")
    @Log(title = "召回记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody KmcKnowledgeRecallLogSaveReqVO kmcKnowledgeRecallLog) {
        return CommonResult.toAjax(kmcKnowledgeRecallLogService.updateKmcKnowledgeRecallLog(kmcKnowledgeRecallLog));
    }

    @Operation(summary = "删除召回记录")
    @Log(title = "召回记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(kmcKnowledgeRecallLogService.removeKmcKnowledgeRecallLog(Arrays.asList(ids)));
    }

    @Operation(summary = "获取知识库召回记录")
    @GetMapping("/getKnowledgeBaseRecallLogList")
    public CommonResult<List<KmcKnowledgeRecallLogRespVO>> getKnowledgeBaseRecallLogList(Long knowledgeBaseId) {
        List<KmcKnowledgeRecallLogDO> list = kmcKnowledgeRecallLogService.findByKnowledgeBaseId(knowledgeBaseId);
        return CommonResult.success(BeanUtils.toBean(list, KmcKnowledgeRecallLogRespVO.class));
    }

}
