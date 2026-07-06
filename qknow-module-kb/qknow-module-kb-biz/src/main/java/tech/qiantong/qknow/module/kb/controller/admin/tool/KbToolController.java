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
import tech.qiantong.qknow.module.kb.controller.admin.tool.vo.KbToolPageReqVO;
import tech.qiantong.qknow.module.kb.controller.admin.tool.vo.KbToolRespVO;
import tech.qiantong.qknow.module.kb.controller.admin.tool.vo.KbToolSaveReqVO;
import tech.qiantong.qknow.module.kb.convert.tool.KbToolConvert;
import tech.qiantong.qknow.module.kb.dal.dataobject.tool.KbToolDO;
import tech.qiantong.qknow.module.kb.service.tool.IKbToolService;

import java.util.Arrays;
import java.util.List;

/**
 * 工具管理Controller
 *
 * @author qknow
 * @date 2026-03-19
 */
@Tag(name = "工具管理")
@RestController
@RequestMapping("/kb/tool")
@Validated
public class KbToolController extends BaseController {
    @Resource
    private IKbToolService kbToolService;

    @Operation(summary = "查询工具管理列表")
    @PreAuthorize("@ss.hasPermi('kb:tool:tool:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<KbToolRespVO>> list(KbToolPageReqVO kbTool) {
        PageResult<KbToolDO> page = kbToolService.getKbToolPage(kbTool);
        return CommonResult.success(BeanUtils.toBean(page, KbToolRespVO.class));
    }

    @Operation(summary = "导出工具管理列表")
    @PreAuthorize("@ss.hasPermi('kb:tool:tool:export')")
    @Log(title = "工具管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, KbToolPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<KbToolDO> list = (List<KbToolDO>) kbToolService.getKbToolPage(exportReqVO).getRows();
        ExcelUtil<KbToolRespVO> util = new ExcelUtil<>(KbToolRespVO.class);
        util.exportExcel(response, KbToolConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入工具管理列表")
    @PreAuthorize("@ss.hasPermi('kb:tool:tool:import')")
    @Log(title = "工具管理", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<KbToolRespVO> util = new ExcelUtil<>(KbToolRespVO.class);
        List<KbToolRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = kbToolService.importKbTool(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取工具管理详细信息")
    @PreAuthorize("@ss.hasPermi('kb:tool:tool:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<KbToolRespVO> getInfo(@PathVariable("id") Long id) {
        KbToolDO kbToolDO = kbToolService.getKbToolById(id);
        return CommonResult.success(BeanUtils.toBean(kbToolDO, KbToolRespVO.class));
    }

    @Operation(summary = "新增工具管理")
    @PreAuthorize("@ss.hasPermi('kb:tool:tool:add')")
    @Log(title = "工具管理", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody KbToolSaveReqVO kbTool) {
        kbTool.setWorkspaceId(super.getWorkSpaceId());
        kbTool.setCreatorId(getUserId());
        kbTool.setCreateBy(getUsername());
        return CommonResult.toAjax(kbToolService.createKbTool(kbTool));
    }

    @Operation(summary = "修改工具管理")
    @PreAuthorize("@ss.hasPermi('kb:tool:tool:edit')")
    @Log(title = "工具管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody KbToolSaveReqVO kbTool) {
        kbTool.setUpdateBy(getUsername());
        kbTool.setUpdaterId(getUserId());
        return CommonResult.toAjax(kbToolService.updateKbTool(kbTool));
    }

    @Operation(summary = "删除工具管理")
    @PreAuthorize("@ss.hasPermi('kb:tool:tool:remove')")
    @Log(title = "工具管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(kbToolService.removeKbTool(Arrays.asList(ids)));
    }

}
