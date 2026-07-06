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
import tech.qiantong.qknow.module.kmc.controller.admin.knowledgeBase.vo.KmcKnowledgeRolePageReqVO;
import tech.qiantong.qknow.module.kmc.controller.admin.knowledgeBase.vo.KmcKnowledgeRoleRespVO;
import tech.qiantong.qknow.module.kmc.controller.admin.knowledgeBase.vo.KmcKnowledgeRoleSaveReqVO;
import tech.qiantong.qknow.module.kmc.convert.knowledgeBase.KmcKnowledgeRoleConvert;
import tech.qiantong.qknow.module.kmc.dal.dataobject.knowledgeBase.KmcKnowledgeRoleDO;
import tech.qiantong.qknow.module.kmc.service.knowledgeBase.IKmcKnowledgeRoleService;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * 知识库角色关联Controller
 *
 * @author qknow
 * @date 2025-07-24
 */
@Tag(name = "知识库角色关联")
@RestController
@RequestMapping("/kmc/role")
@Validated
public class KmcKnowledgeRoleController extends BaseController {
    @Resource
    private IKmcKnowledgeRoleService kmcKnowledgeRoleService;

    @Operation(summary = "查询知识库角色关联列表")
    @PreAuthorize("@ss.hasPermi('kmc:knowledgeBase:role:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<KmcKnowledgeRoleRespVO>> list(KmcKnowledgeRolePageReqVO kmcKnowledgeRole) {
        PageResult<KmcKnowledgeRoleDO> page = kmcKnowledgeRoleService.getKmcKnowledgeRolePage(kmcKnowledgeRole);
        return CommonResult.success(BeanUtils.toBean(page, KmcKnowledgeRoleRespVO.class));
    }

    @Operation(summary = "导出知识库角色关联列表")
    @PreAuthorize("@ss.hasPermi('kmc:knowledgeBase:role:export')")
    @Log(title = "知识库角色关联", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, KmcKnowledgeRolePageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<KmcKnowledgeRoleDO> list = (List<KmcKnowledgeRoleDO>) kmcKnowledgeRoleService.getKmcKnowledgeRolePage(exportReqVO).getRows();
        ExcelUtil<KmcKnowledgeRoleRespVO> util = new ExcelUtil<>(KmcKnowledgeRoleRespVO.class);
        util.exportExcel(response, KmcKnowledgeRoleConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入知识库角色关联列表")
    @PreAuthorize("@ss.hasPermi('kmc:knowledgeBase:role:import')")
    @Log(title = "知识库角色关联", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<KmcKnowledgeRoleRespVO> util = new ExcelUtil<>(KmcKnowledgeRoleRespVO.class);
        List<KmcKnowledgeRoleRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = kmcKnowledgeRoleService.importKmcKnowledgeRole(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取知识库角色关联详细信息")
    @PreAuthorize("@ss.hasPermi('kmc:knowledgeBase:role:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<KmcKnowledgeRoleRespVO> getInfo(@PathVariable("id") Long id) {
        KmcKnowledgeRoleDO kmcKnowledgeRoleDO = kmcKnowledgeRoleService.getKmcKnowledgeRoleById(id);
        return CommonResult.success(BeanUtils.toBean(kmcKnowledgeRoleDO, KmcKnowledgeRoleRespVO.class));
    }

    @Operation(summary = "新增知识库角色关联")
    @PreAuthorize("@ss.hasPermi('kmc:knowledgeBase:role:add')")
    @Log(title = "知识库角色关联", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody KmcKnowledgeRoleSaveReqVO kmcKnowledgeRole) {
        return CommonResult.toAjax(kmcKnowledgeRoleService.createKmcKnowledgeRole(kmcKnowledgeRole));
    }

    @Operation(summary = "修改知识库角色关联")
    @PreAuthorize("@ss.hasPermi('kmc:knowledgeBase:role:edit')")
    @Log(title = "知识库角色关联", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody KmcKnowledgeRoleSaveReqVO kmcKnowledgeRole) {
        return CommonResult.toAjax(kmcKnowledgeRoleService.updateKmcKnowledgeRole(kmcKnowledgeRole));
    }

    @Operation(summary = "删除知识库角色关联")
    @PreAuthorize("@ss.hasPermi('kmc:knowledgeBase:role:remove')")
    @Log(title = "知识库角色关联", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(kmcKnowledgeRoleService.removeKmcKnowledgeRole(Arrays.asList(ids)));
    }

}
