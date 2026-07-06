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

package tech.qiantong.qknow.module.kg.controller.admin.knowledge;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
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
import tech.qiantong.qknow.common.utils.object.BeanUtils;
import tech.qiantong.qknow.common.utils.poi.ExcelUtil;
import tech.qiantong.qknow.module.kg.controller.admin.knowledge.vo.KgKnowledgeDocumentLogPageReqVO;
import tech.qiantong.qknow.module.kg.controller.admin.knowledge.vo.KgKnowledgeDocumentLogRespVO;
import tech.qiantong.qknow.module.kg.controller.admin.knowledge.vo.KgKnowledgeDocumentLogSaveReqVO;
import tech.qiantong.qknow.module.kg.convert.knowledge.KgKnowledgeDocumentLogConvert;
import tech.qiantong.qknow.module.kg.dal.dataobject.knowledge.KgKnowledgeDocumentLogDO;
import tech.qiantong.qknow.module.kg.service.knowledge.IKgKnowledgeDocumentLogService;

import java.util.Arrays;
import java.util.List;

/**
 * 文件操作日志Controller
 *
 * @author qknow
 * @date 2025-10-22
 */
@Tag(name = "文件操作日志")
@RestController
@RequestMapping("/kg/docmentLog")
@Validated
public class KgKnowledgeDocumentLogController extends BaseController {
    @Resource
    private IKgKnowledgeDocumentLogService kgKnowledgeDocumentLogService;

    @Operation(summary = "查询文件操作日志列表")
    @PreAuthorize("@ss.hasPermi('kg:knowledge:docmentlog:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<KgKnowledgeDocumentLogRespVO>> list(KgKnowledgeDocumentLogPageReqVO kgKnowledgeDocumentLog) {
        PageResult<KgKnowledgeDocumentLogDO> page = kgKnowledgeDocumentLogService.getKgKnowledgeDocumentLogPage(kgKnowledgeDocumentLog);
        return CommonResult.success(BeanUtils.toBean(page, KgKnowledgeDocumentLogRespVO.class));
    }

    @Operation(summary = "导出文件操作日志列表")
    @PreAuthorize("@ss.hasPermi('kg:knowledge:docmentlog:export')")
    @Log(title = "文件操作日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, KgKnowledgeDocumentLogPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<KgKnowledgeDocumentLogDO> list = (List<KgKnowledgeDocumentLogDO>) kgKnowledgeDocumentLogService.getKgKnowledgeDocumentLogPage(exportReqVO).getRows();
        ExcelUtil<KgKnowledgeDocumentLogRespVO> util = new ExcelUtil<>(KgKnowledgeDocumentLogRespVO.class);
        util.exportExcel(response, KgKnowledgeDocumentLogConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入文件操作日志列表")
    @PreAuthorize("@ss.hasPermi('kg:knowledge:docmentlog:import')")
    @Log(title = "文件操作日志", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<KgKnowledgeDocumentLogRespVO> util = new ExcelUtil<>(KgKnowledgeDocumentLogRespVO.class);
        List<KgKnowledgeDocumentLogRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = kgKnowledgeDocumentLogService.importKgKnowledgeDocumentLog(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取文件操作日志详细信息")
    @PreAuthorize("@ss.hasPermi('kg:knowledge:docmentlog:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<KgKnowledgeDocumentLogRespVO> getInfo(@PathVariable("id") Long id) {
        KgKnowledgeDocumentLogDO kgKnowledgeDocumentLogDO = kgKnowledgeDocumentLogService.getKgKnowledgeDocumentLogById(id);
        return CommonResult.success(BeanUtils.toBean(kgKnowledgeDocumentLogDO, KgKnowledgeDocumentLogRespVO.class));
    }

    @Operation(summary = "新增文件操作日志")
    @PreAuthorize("@ss.hasPermi('kg:knowledge:docmentlog:add')")
    @Log(title = "文件操作日志", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody KgKnowledgeDocumentLogSaveReqVO kgKnowledgeDocumentLog) {
        return CommonResult.toAjax(kgKnowledgeDocumentLogService.createKgKnowledgeDocumentLog(kgKnowledgeDocumentLog));
    }

    @Operation(summary = "修改文件操作日志")
    @PreAuthorize("@ss.hasPermi('kg:knowledge:docmentlog:edit')")
    @Log(title = "文件操作日志", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody KgKnowledgeDocumentLogSaveReqVO kgKnowledgeDocumentLog) {
        return CommonResult.toAjax(kgKnowledgeDocumentLogService.updateKgKnowledgeDocumentLog(kgKnowledgeDocumentLog));
    }

    @Operation(summary = "删除文件操作日志")
    @PreAuthorize("@ss.hasPermi('kg:knowledge:docmentlog:remove')")
    @Log(title = "文件操作日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(kgKnowledgeDocumentLogService.removeKgKnowledgeDocumentLog(Arrays.asList(ids)));
    }

}
