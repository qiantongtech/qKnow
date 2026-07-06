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

package tech.qiantong.qknow.module.kb.controller.admin.tool;

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
import tech.qiantong.qknow.module.kb.controller.admin.tool.vo.KbToolMethodPageReqVO;
import tech.qiantong.qknow.module.kb.controller.admin.tool.vo.KbToolMethodRespVO;
import tech.qiantong.qknow.module.kb.controller.admin.tool.vo.KbToolMethodSaveReqVO;
import tech.qiantong.qknow.module.kb.convert.tool.KbToolMethodConvert;
import tech.qiantong.qknow.module.kb.dal.dataobject.tool.KbToolMethodDO;
import tech.qiantong.qknow.module.kb.service.tool.IKbToolMethodService;

import java.util.Arrays;
import java.util.List;

/**
 * 工具方法Controller
 *
 * @author qknow
 * @date 2026-03-19
 */
@Tag(name = "工具方法")
@RestController
@RequestMapping("/kb/method")
@Validated
public class KbToolMethodController extends BaseController {
    @Resource
    private IKbToolMethodService kbToolMethodService;

    @Operation(summary = "查询工具方法列表")
//    @PreAuthorize("@ss.hasPermi('kb:tool:method:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<KbToolMethodRespVO>> list(KbToolMethodPageReqVO kbToolMethod) {
        PageResult<KbToolMethodDO> page = kbToolMethodService.getKbToolMethodPage(kbToolMethod);
        return CommonResult.success(BeanUtils.toBean(page, KbToolMethodRespVO.class));
    }

    @Operation(summary = "导出工具方法列表")
//    @PreAuthorize("@ss.hasPermi('kb:tool:method:export')")
    @Log(title = "工具方法", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, KbToolMethodPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<KbToolMethodDO> list = (List<KbToolMethodDO>) kbToolMethodService.getKbToolMethodPage(exportReqVO).getRows();
        ExcelUtil<KbToolMethodRespVO> util = new ExcelUtil<>(KbToolMethodRespVO.class);
        util.exportExcel(response, KbToolMethodConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入工具方法列表")
//    @PreAuthorize("@ss.hasPermi('kb:tool:method:import')")
    @Log(title = "工具方法", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<KbToolMethodRespVO> util = new ExcelUtil<>(KbToolMethodRespVO.class);
        List<KbToolMethodRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = kbToolMethodService.importKbToolMethod(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取工具方法详细信息")
//    @PreAuthorize("@ss.hasPermi('kb:tool:method:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<KbToolMethodRespVO> getInfo(@PathVariable("id") Long id) {
        KbToolMethodDO kbToolMethodDO = kbToolMethodService.getKbToolMethodById(id);
        return CommonResult.success(BeanUtils.toBean(kbToolMethodDO, KbToolMethodRespVO.class));
    }

    @Operation(summary = "新增工具方法")
//    @PreAuthorize("@ss.hasPermi('kb:tool:method:add')")
    @Log(title = "工具方法", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody KbToolMethodSaveReqVO kbToolMethod) {
        kbToolMethod.setWorkspaceId(super.getWorkSpaceId());
        kbToolMethod.setCreatorId(getUserId());
        kbToolMethod.setCreateBy(getUsername());
        return CommonResult.toAjax(kbToolMethodService.createKbToolMethod(kbToolMethod));
    }

    @Operation(summary = "修改工具方法")
//    @PreAuthorize("@ss.hasPermi('kb:tool:method:edit')")
    @Log(title = "工具方法", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody KbToolMethodSaveReqVO kbToolMethod) {
        kbToolMethod.setUpdaterId(getUserId());
        kbToolMethod.setUpdateBy(getUsername());
        return CommonResult.toAjax(kbToolMethodService.updateKbToolMethod(kbToolMethod));
    }

    @Operation(summary = "删除工具方法")
//    @PreAuthorize("@ss.hasPermi('kb:tool:method:remove')")
    @Log(title = "工具方法", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(kbToolMethodService.removeKbToolMethod(Arrays.asList(ids)));
    }

}
