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

package tech.qiantong.qknow.module.kb.controller.admin.skills;

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
import tech.qiantong.qknow.module.kb.controller.admin.skills.vo.KbSkillsBatchImportReqVO;
import tech.qiantong.qknow.module.kb.controller.admin.skills.vo.KbSkillsPageReqVO;
import tech.qiantong.qknow.module.kb.controller.admin.skills.vo.KbSkillsRespVO;
import tech.qiantong.qknow.module.kb.controller.admin.skills.vo.KbSkillsSaveReqVO;
import tech.qiantong.qknow.module.kb.convert.skills.KbSkillsConvert;
import tech.qiantong.qknow.module.kb.dal.dataobject.skills.KbSkillsDO;
import tech.qiantong.qknow.module.kb.service.skills.IKbSkillsService;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * skillsController
 *
 * @author qknow
 * @date 2026-06-17
 */
@Tag(name = "skills")
@RestController
@RequestMapping("/kb/skills")
@Validated
public class KbSkillsController extends BaseController {
    @Resource
    private IKbSkillsService kbSkillsService;

    @Operation(summary = "查询skills列表")
    @PreAuthorize("@ss.hasPermi('kb:skills:skills:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<KbSkillsRespVO>> list(KbSkillsPageReqVO kbSkills) {
        PageResult<KbSkillsDO> page = kbSkillsService.getKbSkillsPage(kbSkills);
        return CommonResult.success(BeanUtils.toBean(page, KbSkillsRespVO.class));
    }

    @Operation(summary = "导出skills列表")
    @PreAuthorize("@ss.hasPermi('kb:skills:skills:export')")
    @Log(title = "skills", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, KbSkillsPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<KbSkillsDO> list = (List<KbSkillsDO>) kbSkillsService.getKbSkillsPage(exportReqVO).getRows();
        ExcelUtil<KbSkillsRespVO> util = new ExcelUtil<>(KbSkillsRespVO.class);
        util.exportExcel(response, KbSkillsConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入skills列表")
    @PreAuthorize("@ss.hasPermi('kb:skills:skills:import')")
    @Log(title = "skills", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<KbSkillsRespVO> util = new ExcelUtil<>(KbSkillsRespVO.class);
        List<KbSkillsRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = kbSkillsService.importKbSkills(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取skills详细信息")
    @PreAuthorize("@ss.hasPermi('kb:skills:skills:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<KbSkillsRespVO> getInfo(@PathVariable("id") Long id) {
        KbSkillsDO kbSkillsDO = kbSkillsService.getKbSkillsById(id);
        return CommonResult.success(BeanUtils.toBean(kbSkillsDO, KbSkillsRespVO.class));
    }

    @Operation(summary = "新增skills")
    @PreAuthorize("@ss.hasPermi('kb:skills:skills:add')")
    @Log(title = "skills", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody KbSkillsSaveReqVO kbSkills) {
        kbSkills.setWorkspaceId(super.getWorkSpaceId());
        kbSkills.setCreatorId(super.getUserId());
        kbSkills.setCreateBy(super.getUsername());
        return CommonResult.toAjax(kbSkillsService.createKbSkills(kbSkills));
    }

    @Operation(summary = "修改skills")
    @PreAuthorize("@ss.hasPermi('kb:skills:skills:edit')")
    @Log(title = "skills", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody KbSkillsSaveReqVO kbSkills) {
        return CommonResult.toAjax(kbSkillsService.updateKbSkills(kbSkills));
    }

    @Operation(summary = "删除skills")
    @PreAuthorize("@ss.hasPermi('kb:skills:skills:remove')")
    @Log(title = "skills", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(kbSkillsService.removeKbSkills(Arrays.asList(ids)));
    }

    @Operation(summary = "批量导入skills（从ZIP）")
    @PreAuthorize("@ss.hasPermi('kb:skills:skills:add')")
    @Log(title = "skills", businessType = BusinessType.IMPORT)
    @PostMapping("/batchImport")
    public CommonResult<Map<String, Object>> batchImport(@Valid @RequestBody KbSkillsBatchImportReqVO reqVO) {
        reqVO.setWorkspaceId(super.getWorkSpaceId());
        reqVO.setCreatorId(super.getUserId());
        reqVO.setCreateBy(super.getUsername());
        Map<String, Object> result = kbSkillsService.batchCreateKbSkills(reqVO);
        return CommonResult.success(result);
    }

    @Operation(summary = "预览 skills 的 SKILL.md")
    @PreAuthorize("@ss.hasPermi('kb:skills:skills:query')")
    @GetMapping("/preview/{id}")
    public CommonResult<String> previewSkillMd(@PathVariable("id") Long id) {
        String content = kbSkillsService.previewSkillMd(id);
        return CommonResult.success(content);
    }

    @Operation(summary = "下载 skill 为 ZIP")
    @PreAuthorize("@ss.hasPermi('kb:skills:skills:query')")
    @Log(title = "skills", businessType = BusinessType.EXPORT)
    @GetMapping("/download/{id}")
    public void downloadSkill(@PathVariable("id") Long id, HttpServletResponse response) {
        kbSkillsService.downloadSkill(id, response);
    }

}
