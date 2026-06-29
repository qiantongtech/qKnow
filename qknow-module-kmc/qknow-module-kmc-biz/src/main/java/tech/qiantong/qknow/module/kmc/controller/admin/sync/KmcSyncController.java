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

package tech.qiantong.qknow.module.kmc.controller.admin.sync;

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
import tech.qiantong.qknow.module.kmc.controller.admin.sync.vo.KmcSyncPageReqVO;
import tech.qiantong.qknow.module.kmc.controller.admin.sync.vo.KmcSyncRespVO;
import tech.qiantong.qknow.module.kmc.controller.admin.sync.vo.KmcSyncSaveReqVO;
import tech.qiantong.qknow.module.kmc.convert.sync.KmcSyncConvert;
import tech.qiantong.qknow.module.kmc.dal.dataobject.sync.KmcSyncDO;
import tech.qiantong.qknow.module.kmc.service.sync.IKmcSyncService;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * 文件同步Controller
 *
 * @author qknow
 * @date 2025-03-18
 */
@Tag(name = "文件同步")
@RestController
@RequestMapping("/kmc/sync")
@Validated
public class KmcSyncController extends BaseController {
    @Resource
    private IKmcSyncService kmcSyncService;

    @Operation(summary = "查询文件同步列表")
    @PreAuthorize("@ss.hasPermi('kmc:sync:sync:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<KmcSyncRespVO>> list(KmcSyncPageReqVO kmcSync) {
        PageResult<KmcSyncDO> page = kmcSyncService.getKmcSyncPage(kmcSync);
        return CommonResult.success(BeanUtils.toBean(page, KmcSyncRespVO.class));
    }

    @Operation(summary = "导出文件同步列表")
    @PreAuthorize("@ss.hasPermi('kmc:sync:sync:export')")
    @Log(title = "文件同步", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, KmcSyncPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<KmcSyncDO> list = (List<KmcSyncDO>) kmcSyncService.getKmcSyncPage(exportReqVO).getRows();
        ExcelUtil<KmcSyncRespVO> util = new ExcelUtil<>(KmcSyncRespVO.class);
        util.exportExcel(response, KmcSyncConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入文件同步列表")
    @PreAuthorize("@ss.hasPermi('kmc:sync:sync:import')")
    @Log(title = "文件同步", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<KmcSyncRespVO> util = new ExcelUtil<>(KmcSyncRespVO.class);
        List<KmcSyncRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = kmcSyncService.importKmcSync(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取文件同步详细信息")
    @PreAuthorize("@ss.hasPermi('kmc:sync:sync:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<KmcSyncRespVO> getInfo(@PathVariable("id") Long id) {
        KmcSyncDO kmcSyncDO = kmcSyncService.getKmcSyncById(id);
        return CommonResult.success(BeanUtils.toBean(kmcSyncDO, KmcSyncRespVO.class));
    }

    @Operation(summary = "新增文件同步")
    @PreAuthorize("@ss.hasPermi('kmc:sync:sync:add')")
    @Log(title = "文件同步", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody KmcSyncSaveReqVO kmcSync) {
        return CommonResult.toAjax(kmcSyncService.createKmcSync(kmcSync));
    }

    @Operation(summary = "修改文件同步")
    @PreAuthorize("@ss.hasPermi('kmc:sync:sync:edit')")
    @Log(title = "文件同步", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody KmcSyncSaveReqVO kmcSync) {
        return CommonResult.toAjax(kmcSyncService.updateKmcSync(kmcSync));
    }

    @Operation(summary = "删除文件同步")
    @PreAuthorize("@ss.hasPermi('kmc:sync:sync:remove')")
    @Log(title = "文件同步", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(kmcSyncService.removeKmcSync(Arrays.asList(ids)));
    }

}
