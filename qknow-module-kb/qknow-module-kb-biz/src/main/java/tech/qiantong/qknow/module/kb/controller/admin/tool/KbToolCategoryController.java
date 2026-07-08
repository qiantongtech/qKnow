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

import cn.hutool.core.date.DateUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tech.qiantong.qknow.common.annotation.Log;
import tech.qiantong.qknow.common.core.controller.BaseController;
import tech.qiantong.qknow.common.core.domain.AjaxResult;
import tech.qiantong.qknow.common.core.domain.CommonResult;
import tech.qiantong.qknow.common.enums.BusinessType;
import tech.qiantong.qknow.common.utils.object.BeanUtils;
import tech.qiantong.qknow.module.kb.controller.admin.tool.vo.KbToolCategorySaveReqVO;
import tech.qiantong.qknow.module.kb.dal.dataobject.tool.KbToolCategoryDO;
import tech.qiantong.qknow.module.kb.service.tool.IKbToolCategoryService;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 工具分类Controller
 *
 * @author qknow
 * @date 2025-02-13
 */
@Tag(name = "工具分类")
@RestController
@RequestMapping("/kb/toolCategory")
@Validated
public class KbToolCategoryController extends BaseController {
    @Resource
    private IKbToolCategoryService toolCategoryService;

    @Operation(summary = "获取工具分类详细信息")
//    @PreAuthorize("@ss.hasPermi('kmc:kmcCategory:kmcCategory:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<KbToolCategoryDO> getInfo(@PathVariable("id") Long id) {
        KbToolCategoryDO kmcCategoryDO = toolCategoryService.getToolCategoryById(id);
        return CommonResult.success(BeanUtils.toBean(kmcCategoryDO, KbToolCategoryDO.class));
    }

    @Operation(summary = "新增工具分类")
//    @PreAuthorize("@ss.hasPermi('kmc:kmcCategory:kmcCategory:add')")
    @Log(title = "工具分类", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody KbToolCategorySaveReqVO category) {
        category.setCreatorId(getUserId());
        category.setCreateBy(getNickName());
        category.setCreateTime(DateUtil.date());
        category.setWorkspaceId(super.getWorkSpaceId());
        return CommonResult.toAjax(toolCategoryService.createToolCategory(category));
    }

    @Operation(summary = "修改工具分类")
    @PreAuthorize("@ss.hasPermi('kmc:kmcCategory:kmcCategory:edit')")
    @Log(title = "工具分类", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody KbToolCategorySaveReqVO category) {
        category.setUpdaterId(getUserId());
        category.setUpdateBy(getNickName());
        category.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(toolCategoryService.updateToolCategory(category));
    }

    @Operation(summary = "删除工具分类")
    @PreAuthorize("@ss.hasPermi('kmc:kmcCategory:kmcCategory:remove')")
    @Log(title = "工具分类", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(toolCategoryService.removeToolCategory(Arrays.asList(ids)));
    }

    @Operation(summary = "查询全部工具分类")
    @GetMapping("/allList")
    public CommonResult<List<KbToolCategoryDO>> getKmcCategoryAllList(KbToolCategoryDO categoryDO) {
        categoryDO.setDelFlag(false);
        List<KbToolCategoryDO> list = toolCategoryService.getToolCategoryAllList(categoryDO);
        return CommonResult.success(list);
    }

    @Operation(summary = "查询工具分类树列表")
    @GetMapping("/kmcCategoryTree")
    public AjaxResult kmcCategoryTree(KbToolCategoryDO categoryDO) {
        return success(toolCategoryService.selectCategoryTreeList(categoryDO));
    }

    @Operation(summary = "获取分类树形结构数据")
    @GetMapping("/tree")
    public CommonResult<List<Map<String, Object>>> tree() {
        List<Map<String, Object>> list = toolCategoryService.getTreeList();
        return CommonResult.success(list);
    }

}
