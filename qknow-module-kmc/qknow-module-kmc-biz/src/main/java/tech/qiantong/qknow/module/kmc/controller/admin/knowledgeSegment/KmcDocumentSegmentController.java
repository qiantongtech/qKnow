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

package tech.qiantong.qknow.module.kmc.controller.admin.knowledgeSegment;

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
import tech.qiantong.qknow.common.enums.BusinessType;
import tech.qiantong.qknow.common.core.controller.BaseController;
import tech.qiantong.qknow.common.core.domain.AjaxResult;
import tech.qiantong.qknow.common.core.domain.CommonResult;
import tech.qiantong.qknow.common.core.page.PageParam;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.common.core.utils.object.BeanUtils;
import tech.qiantong.qknow.common.core.utils.poi.ExcelUtil;
import tech.qiantong.qknow.module.kmc.controller.admin.knowledgeSegment.vo.KmcDocumentSegmentPageReqVO;
import tech.qiantong.qknow.module.kmc.controller.admin.knowledgeSegment.vo.KmcDocumentSegmentRespVO;
import tech.qiantong.qknow.module.kmc.controller.admin.knowledgeSegment.vo.KmcDocumentSegmentSaveReqVO;
import tech.qiantong.qknow.module.kmc.convert.knowledgeSegment.KmcDocumentSegmentConvert;
import tech.qiantong.qknow.module.kmc.dal.dataobject.knowledgeSegment.KmcDocumentSegmentDO;
import tech.qiantong.qknow.module.kmc.service.knowledgeSegment.IKmcDocumentSegmentService;

import java.util.Collections;
import java.util.List;

/**
 * 文件分段Controller
 *
 * @author qknow
 * @date 2025-08-28
 */
@Tag(name = "文件分段")
@RestController
@RequestMapping("/kmc/knowledgeSegment")
@Validated
public class KmcDocumentSegmentController extends BaseController {
    @Resource
    private IKmcDocumentSegmentService kmcDocumentSegmentService;

    @Operation(summary = "查询文件分段列表")
    @PreAuthorize("@ss.hasPermi('kmc:knowledgeSegment:knowledgesegment:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<KmcDocumentSegmentRespVO>> list(KmcDocumentSegmentPageReqVO kmcDocumentSegment) {
        PageResult<KmcDocumentSegmentDO> page = kmcDocumentSegmentService.getKmcDocumentSegmentPage(kmcDocumentSegment);
        return CommonResult.success(BeanUtils.toBean(page, KmcDocumentSegmentRespVO.class));
    }

    @Operation(summary = "查询文件分段列表树形结构")
    @PreAuthorize("@ss.hasPermi('kmc:knowledgeSegment:knowledgesegment:list')")
    @GetMapping("/listTree")
    public CommonResult<PageResult<KmcDocumentSegmentRespVO>> listTree(KmcDocumentSegmentPageReqVO kmcDocumentSegment) {
        PageResult<KmcDocumentSegmentDO> page = kmcDocumentSegmentService.getKmcDocumentSegmentTreePage(kmcDocumentSegment);
        return CommonResult.success(BeanUtils.toBean(page, KmcDocumentSegmentRespVO.class));
    }

    @Operation(summary = "根据文件id获取所有顶层分段节点")
    @PreAuthorize("@ss.hasPermi('kmc:knowledgeSegment:knowledgesegment:list')")
        @GetMapping("/getAllLevelNodes")
    public CommonResult<List<KmcDocumentSegmentDO>> getAllLevelNodes(KmcDocumentSegmentPageReqVO kmcDocumentSegment) {
        List<KmcDocumentSegmentDO> allLevelNodes = kmcDocumentSegmentService.getAllLevelNodes(kmcDocumentSegment.getDocumentId());
        return CommonResult.success(allLevelNodes);
    }

    @Operation(summary = "导出文件分段列表")
    @PreAuthorize("@ss.hasPermi('kmc:knowledgeSegment:knowledgesegment:export')")
    @Log(title = "文件分段", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, KmcDocumentSegmentPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<KmcDocumentSegmentDO> list = (List<KmcDocumentSegmentDO>) kmcDocumentSegmentService.getKmcDocumentSegmentPage(exportReqVO).getRows();
        ExcelUtil<KmcDocumentSegmentRespVO> util = new ExcelUtil<>(KmcDocumentSegmentRespVO.class);
        util.exportExcel(response, KmcDocumentSegmentConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入文件分段列表")
    @PreAuthorize("@ss.hasPermi('kmc:knowledgeSegment:knowledgesegment:import')")
    @Log(title = "文件分段", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<KmcDocumentSegmentRespVO> util = new ExcelUtil<>(KmcDocumentSegmentRespVO.class);
        List<KmcDocumentSegmentRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = kmcDocumentSegmentService.importKmcDocumentSegment(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取文件分段详细信息")
    @PreAuthorize("@ss.hasPermi('kmc:knowledgeSegment:knowledgesegment:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<KmcDocumentSegmentRespVO> getInfo(@PathVariable("id") Long id) {
        KmcDocumentSegmentDO kmcDocumentSegmentDO = kmcDocumentSegmentService.getKmcDocumentSegmentById(id);
        return CommonResult.success(BeanUtils.toBean(kmcDocumentSegmentDO, KmcDocumentSegmentRespVO.class));
    }

    @Operation(summary = "新增文件分段")
    @PreAuthorize("@ss.hasPermi('kmc:knowledgeSegment:knowledgesegment:add')")
    @Log(title = "文件分段", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody KmcDocumentSegmentSaveReqVO kmcDocumentSegment) {
        kmcDocumentSegment.setWorkspaceId(super.getWorkSpaceId());
        return CommonResult.toAjax(kmcDocumentSegmentService.createKmcDocumentSegment(kmcDocumentSegment));
    }

    @Operation(summary = "修改文件分段")
    @PreAuthorize("@ss.hasPermi('kmc:knowledgeSegment:knowledgesegment:edit')")
    @Log(title = "文件分段", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody KmcDocumentSegmentSaveReqVO kmcDocumentSegment) {
        kmcDocumentSegment.setWorkspaceId(super.getWorkSpaceId());
        return CommonResult.toAjax(kmcDocumentSegmentService.updateKmcDocumentSegment(kmcDocumentSegment));
    }

    @Operation(summary = "删除文件分段")
    @PreAuthorize("@ss.hasPermi('kmc:knowledgeSegment:knowledgesegment:remove')")
    @Log(title = "文件分段", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long ids) {
        int i = kmcDocumentSegmentService.removeKmcDocumentSegment(Collections.singletonList(ids));
        return CommonResult.success(i);
    }
}
