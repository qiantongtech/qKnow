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

package tech.qiantong.qknow.module.kmc.controller.admin.kmcCategory;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import cn.hutool.core.date.DateUtil;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.qiantong.qknow.common.core.domain.AjaxResult;
import tech.qiantong.qknow.common.core.page.PageParam;
import tech.qiantong.qknow.common.annotation.Log;
import tech.qiantong.qknow.common.core.controller.BaseController;
import tech.qiantong.qknow.common.core.domain.CommonResult;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.common.enums.BusinessType;
import tech.qiantong.qknow.common.utils.object.BeanUtils;
import tech.qiantong.qknow.common.utils.poi.ExcelUtil;
import tech.qiantong.qknow.module.kmc.controller.admin.kmcCategory.vo.KmcCategoryPageReqVO;
import tech.qiantong.qknow.module.kmc.controller.admin.kmcCategory.vo.KmcCategoryRespVO;
import tech.qiantong.qknow.module.kmc.controller.admin.kmcCategory.vo.KmcCategorySaveReqVO;
import tech.qiantong.qknow.module.kmc.convert.kmcCategory.KmcCategoryConvert;
import tech.qiantong.qknow.module.kmc.dal.dataobject.kmcCategory.KmcCategoryDO;
import tech.qiantong.qknow.module.kmc.service.kmcCategory.IKmcCategoryService;

/**
 * 知识分类Controller
 *
 * @author qknow
 * @date 2025-02-13
 */
@Tag(name = "知识分类")
@RestController
@RequestMapping("/kmc/kmcCategory")
@Validated
public class KmcCategoryController extends BaseController {
    @Resource
    private IKmcCategoryService kmcCategoryService;

    @Operation(summary = "查询知识分类列表")
    @PreAuthorize("@ss.hasPermi('kmc:kmcCategory:kmcCategory:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<KmcCategoryRespVO>> list(KmcCategoryPageReqVO kmcCategory) {
        PageResult<KmcCategoryDO> page = kmcCategoryService.getKmcCategoryPage(kmcCategory);
        return CommonResult.success(BeanUtils.toBean(page, KmcCategoryRespVO.class));
    }

    @Operation(summary = "导出知识分类列表")
    @PreAuthorize("@ss.hasPermi('kmc:kmcCategory:kmcCategory:export')")
    @Log(title = "知识分类", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, KmcCategoryPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<KmcCategoryDO> list = (List<KmcCategoryDO>) kmcCategoryService.getKmcCategoryPage(exportReqVO).getRows();
        ExcelUtil<KmcCategoryRespVO> util = new ExcelUtil<>(KmcCategoryRespVO.class);
        util.exportExcel(response, KmcCategoryConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入知识分类列表")
    @PreAuthorize("@ss.hasPermi('kmc:kmcCategory:kmcCategory:import')")
    @Log(title = "知识分类", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<KmcCategoryRespVO> util = new ExcelUtil<>(KmcCategoryRespVO.class);
        List<KmcCategoryRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = kmcCategoryService.importKmcCategory(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取知识分类详细信息")
    @PreAuthorize("@ss.hasPermi('kmc:kmcCategory:kmcCategory:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<KmcCategoryRespVO> getInfo(@PathVariable("id") Long id) {
        KmcCategoryDO kmcCategoryDO = kmcCategoryService.getKmcCategoryById(id);
        return CommonResult.success(BeanUtils.toBean(kmcCategoryDO, KmcCategoryRespVO.class));
    }

    @Operation(summary = "新增知识分类")
    @PreAuthorize("@ss.hasPermi('kmc:kmcCategory:kmcCategory:add')")
    @Log(title = "知识分类", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody KmcCategorySaveReqVO kmcCategory) {
        kmcCategory.setCreatorId(getUserId());
        kmcCategory.setCreateBy(getNickName());
        kmcCategory.setCreateTime(DateUtil.date());
        kmcCategory.setWorkspaceId(super.getWorkSpaceId());
        return CommonResult.toAjax(kmcCategoryService.createKmcCategory(kmcCategory));
    }

    @Operation(summary = "修改知识分类")
    @PreAuthorize("@ss.hasPermi('kmc:kmcCategory:kmcCategory:edit')")
    @Log(title = "知识分类", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody KmcCategorySaveReqVO kmcCategory) {
        kmcCategory.setUpdaterId(getUserId());
        kmcCategory.setUpdateBy(getNickName());
        kmcCategory.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(kmcCategoryService.updateKmcCategory(kmcCategory));
    }

    @Operation(summary = "删除知识分类")
    @PreAuthorize("@ss.hasPermi('kmc:kmcCategory:kmcCategory:remove')")
    @Log(title = "知识分类", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(kmcCategoryService.removeKmcCategory(Arrays.asList(ids)));
    }

    @Operation(summary = "查询全部知识分类")
    @GetMapping("/allList")
    public CommonResult<List<KmcCategoryDO>> getKmcCategoryAllList(KmcCategoryDO kmcCategoryDO) {
        kmcCategoryDO.setDelFlag(false);
        List<KmcCategoryDO> list = kmcCategoryService.getKmcCategoryAllList(kmcCategoryDO);
        return CommonResult.success(list);
    }

    /**
     * 获取知识分类树列表
     */
    @Operation(summary = "查询知识分类树列表")
    @GetMapping("/kmcCategoryTree")
    public AjaxResult kmcCategoryTree(KmcCategoryDO kmcCategoryDO)
    {
        return success(kmcCategoryService.selectCategoryTreeList(kmcCategoryDO));
    }

}
