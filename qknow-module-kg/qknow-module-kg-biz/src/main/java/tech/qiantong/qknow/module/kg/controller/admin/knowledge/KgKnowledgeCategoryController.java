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
import tech.qiantong.qknow.module.kg.controller.admin.knowledge.vo.KgKnowledgeCategoryPageReqVO;
import tech.qiantong.qknow.module.kg.controller.admin.knowledge.vo.KgKnowledgeCategoryReqVO;
import tech.qiantong.qknow.module.kg.controller.admin.knowledge.vo.KgKnowledgeCategoryRespVO;
import tech.qiantong.qknow.module.kg.controller.admin.knowledge.vo.KgKnowledgeCategorySaveReqVO;
import tech.qiantong.qknow.module.kg.convert.knowledge.KgKnowledgeCategoryConvert;
import tech.qiantong.qknow.module.kg.dal.dataobject.knowledge.KgKnowledgeCategoryDO;
import tech.qiantong.qknow.module.kg.service.knowledge.IKgKnowledgeCategoryService;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * 知识分类Controller
 *
 * @author qknow
 * @date 2025-10-20
 */
@Tag(name = "知识分类")
@RestController
@RequestMapping("/kg/category")
@Validated
public class KgKnowledgeCategoryController extends BaseController {
    @Resource
    private IKgKnowledgeCategoryService kgKnowledgeCategoryService;

    @Operation(summary = "查询知识分类列表")
    @PreAuthorize("@ss.hasPermi('kg:knowledge:category:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<KgKnowledgeCategoryRespVO>> list(KgKnowledgeCategoryPageReqVO kgKnowledgeCategory) {
        PageResult<KgKnowledgeCategoryDO> page = kgKnowledgeCategoryService.getKgKnowledgeCategoryPage(kgKnowledgeCategory);
        return CommonResult.success(BeanUtils.toBean(page, KgKnowledgeCategoryRespVO.class));
    }

    @Operation(summary = "导出知识分类列表")
    @PreAuthorize("@ss.hasPermi('kg:knowledge:category:export')")
    @Log(title = "知识分类", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, KgKnowledgeCategoryPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<KgKnowledgeCategoryDO> list = (List<KgKnowledgeCategoryDO>) kgKnowledgeCategoryService.getKgKnowledgeCategoryPage(exportReqVO).getRows();
        ExcelUtil<KgKnowledgeCategoryRespVO> util = new ExcelUtil<>(KgKnowledgeCategoryRespVO.class);
        util.exportExcel(response, KgKnowledgeCategoryConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入知识分类列表")
    @PreAuthorize("@ss.hasPermi('kg:knowledge:category:import')")
    @Log(title = "知识分类", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<KgKnowledgeCategoryRespVO> util = new ExcelUtil<>(KgKnowledgeCategoryRespVO.class);
        List<KgKnowledgeCategoryRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = kgKnowledgeCategoryService.importKgKnowledgeCategory(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取知识分类详细信息")
    @PreAuthorize("@ss.hasPermi('kg:knowledge:category:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<KgKnowledgeCategoryRespVO> getInfo(@PathVariable("id") Long id) {
        KgKnowledgeCategoryDO kgKnowledgeCategoryDO = kgKnowledgeCategoryService.getKgKnowledgeCategoryById(id);
        return CommonResult.success(BeanUtils.toBean(kgKnowledgeCategoryDO, KgKnowledgeCategoryRespVO.class));
    }

    @Operation(summary = "新增知识分类")
    @PreAuthorize("@ss.hasPermi('kg:knowledge:category:add')")
    @Log(title = "知识分类", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody KgKnowledgeCategorySaveReqVO kgKnowledgeCategory) {
        kgKnowledgeCategory.setWorkspaceId(super.getWorkSpaceId());
        kgKnowledgeCategory.setCreatorId(super.getUserId());
        kgKnowledgeCategory.setCreateBy(super.getUsername());
        return CommonResult.toAjax(kgKnowledgeCategoryService.createKgKnowledgeCategory(kgKnowledgeCategory));
    }

    @Operation(summary = "修改知识分类")
    @PreAuthorize("@ss.hasPermi('kg:knowledge:category:edit')")
    @Log(title = "知识分类", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody KgKnowledgeCategorySaveReqVO kgKnowledgeCategory) {
        return CommonResult.toAjax(kgKnowledgeCategoryService.updateKgKnowledgeCategory(kgKnowledgeCategory));
    }

    @Operation(summary = "删除知识分类")
    @PreAuthorize("@ss.hasPermi('kg:knowledge:category:remove')")
    @Log(title = "知识分类", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(kgKnowledgeCategoryService.removeKgKnowledgeCategory(Arrays.asList(ids)));
    }

    @Operation(summary = "查询全部知识分类")
    @GetMapping("/allList")
    public CommonResult<List<KgKnowledgeCategoryRespVO>> getKmcCategoryAllList(KgKnowledgeCategoryReqVO kgCategoryReq) {
        List<KgKnowledgeCategoryDO> list = kgKnowledgeCategoryService.getKgKnowledgeCategoryList(kgCategoryReq);
        return CommonResult.success(BeanUtils.toBean(list, KgKnowledgeCategoryRespVO.class));
    }
}
