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
import tech.qiantong.qknow.module.kg.controller.admin.knowledge.vo.KgKnowledgeDocumentPageReqVO;
import tech.qiantong.qknow.module.kg.controller.admin.knowledge.vo.KgKnowledgeDocumentRespVO;
import tech.qiantong.qknow.module.kg.controller.admin.knowledge.vo.KgKnowledgeDocumentSaveReqVO;
import tech.qiantong.qknow.module.kg.controller.admin.knowledge.vo.KgKnowledgeDocumentTrackReqVO;
import tech.qiantong.qknow.module.kg.convert.knowledge.KgKnowledgeDocumentConvert;
import tech.qiantong.qknow.module.kg.dal.dataobject.knowledge.KgKnowledgeDocumentDO;
import tech.qiantong.qknow.module.kg.service.knowledge.IKgKnowledgeDocumentService;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 知识文件Controller
 *
 * @author qknow
 * @date 2025-10-20
 */
@Tag(name = "知识文件")
@RestController
@RequestMapping("/kg/document")
@Validated
public class KgKnowledgeDocumentController extends BaseController {
    @Resource
    private IKgKnowledgeDocumentService kgKnowledgeDocumentService;

    @Operation(summary = "查询知识文件列表")
    @PreAuthorize("@ss.hasPermi('kg:knowledge:document:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<KgKnowledgeDocumentRespVO>> list(KgKnowledgeDocumentPageReqVO kgKnowledgeDocument) {
        PageResult<KgKnowledgeDocumentDO> page = kgKnowledgeDocumentService.getKgKnowledgeDocumentPage(kgKnowledgeDocument);
        return CommonResult.success(BeanUtils.toBean(page, KgKnowledgeDocumentRespVO.class));
    }

    @Operation(summary = "导出知识文件列表")
    @PreAuthorize("@ss.hasPermi('kg:knowledge:document:export')")
    @Log(title = "知识文件", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, KgKnowledgeDocumentPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<KgKnowledgeDocumentDO> list = (List<KgKnowledgeDocumentDO>) kgKnowledgeDocumentService.getKgKnowledgeDocumentPage(exportReqVO).getRows();
        ExcelUtil<KgKnowledgeDocumentRespVO> util = new ExcelUtil<>(KgKnowledgeDocumentRespVO.class);
        util.exportExcel(response, KgKnowledgeDocumentConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入知识文件列表")
    @PreAuthorize("@ss.hasPermi('kg:knowledge:document:import')")
    @Log(title = "知识文件", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<KgKnowledgeDocumentRespVO> util = new ExcelUtil<>(KgKnowledgeDocumentRespVO.class);
        List<KgKnowledgeDocumentRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = kgKnowledgeDocumentService.importKgKnowledgeDocument(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取知识文件详细信息")
    @PreAuthorize("@ss.hasPermi('kg:knowledge:document:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<KgKnowledgeDocumentRespVO> getInfo(@PathVariable("id") Long id) {
        KgKnowledgeDocumentDO kgKnowledgeDocumentDO = kgKnowledgeDocumentService.getKgKnowledgeDocumentById(id);
        return CommonResult.success(BeanUtils.toBean(kgKnowledgeDocumentDO, KgKnowledgeDocumentRespVO.class));
    }

    @Operation(summary = "新增知识文件")
    @PreAuthorize("@ss.hasPermi('kg:knowledge:document:add')")
    @Log(title = "知识文件", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody KgKnowledgeDocumentSaveReqVO kgKnowledgeDocument) {
        kgKnowledgeDocument.setWorkspaceId(super.getWorkSpaceId());
        kgKnowledgeDocument.setCreatorId(getUserId());
        kgKnowledgeDocument.setCreateBy(getNickName());
        return CommonResult.toAjax(kgKnowledgeDocumentService.createKgKnowledgeDocument(kgKnowledgeDocument));
    }

    @Operation(summary = "修改知识文件")
    @PreAuthorize("@ss.hasPermi('kg:knowledge:document:edit')")
    @Log(title = "知识文件", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody KgKnowledgeDocumentSaveReqVO kgKnowledgeDocument) {
        return CommonResult.toAjax(kgKnowledgeDocumentService.updateKgKnowledgeDocument(kgKnowledgeDocument));
    }

    @Operation(summary = "删除知识文件")
    @PreAuthorize("@ss.hasPermi('kg:knowledge:document:remove')")
    @Log(title = "知识文件", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(kgKnowledgeDocumentService.removeKgKnowledgeDocument(Arrays.asList(ids)));
    }

    @Operation(summary = "获取多少种类型及每种类型下面的总文件数量")
    @GetMapping("/getFileTypes")
    public CommonResult<List<Map<String, Object>>> getFileTypes() {
        List<Map<String, Object>> list = kgKnowledgeDocumentService.getFileTypes();
        return CommonResult.success(list);
    }

    @Operation(summary = "文件跟踪统计")
    @PostMapping("/trackCount")
    public CommonResult<Boolean> trackCount(@Valid @RequestBody List<KgKnowledgeDocumentTrackReqVO> documentTrackList) {
        return CommonResult.success(kgKnowledgeDocumentService.trackCount(documentTrackList, getUserId(), getUsername()));
    }
}
