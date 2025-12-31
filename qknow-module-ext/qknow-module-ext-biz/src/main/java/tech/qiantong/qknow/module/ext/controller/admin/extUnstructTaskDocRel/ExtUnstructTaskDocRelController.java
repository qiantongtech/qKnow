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

package tech.qiantong.qknow.module.ext.controller.admin.extUnstructTaskDocRel;

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
import tech.qiantong.qknow.common.exception.enums.GlobalErrorCodeConstants;
import tech.qiantong.qknow.module.ext.controller.admin.extUnstructTaskDocRel.vo.ExtUnstructTaskDocRelPageReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extUnstructTaskDocRel.vo.ExtUnstructTaskDocRelRespVO;
import tech.qiantong.qknow.module.ext.controller.admin.extUnstructTaskDocRel.vo.ExtUnstructTaskDocRelSaveReqVO;
import tech.qiantong.qknow.module.ext.convert.extUnstructTaskDocRel.ExtUnstructTaskDocRelConvert;
import tech.qiantong.qknow.module.ext.dal.dataobject.extUnstructTaskDocRel.ExtUnstructTaskDocRelDO;
import tech.qiantong.qknow.module.ext.service.extUnstructTaskDocRel.IExtUnstructTaskDocRelService;

/**
 * 任务文件关联Controller
 *
 * @author qknow
 * @date 2025-02-19
 */
@Tag(name = "任务文件关联")
@RestController
@RequestMapping("/ext/unstructTaskDocRel")
@Validated
public class ExtUnstructTaskDocRelController extends BaseController {
    @Resource
    private IExtUnstructTaskDocRelService extUnstructTaskDocRelService;

    @Operation(summary = "查询任务文件关联列表")
    @PreAuthorize("@ss.hasPermi('ext:extUnstructTaskDocRel:unstructtaskdocrel:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<ExtUnstructTaskDocRelRespVO>> list(ExtUnstructTaskDocRelPageReqVO extUnstructTaskDocRel) {
        PageResult<ExtUnstructTaskDocRelDO> page = extUnstructTaskDocRelService.getExtUnstructTaskDocRelPage(extUnstructTaskDocRel);
        return CommonResult.success(BeanUtils.toBean(page, ExtUnstructTaskDocRelRespVO.class));
    }

    @Operation(summary = "导出任务文件关联列表")
    @PreAuthorize("@ss.hasPermi('ext:extUnstructTaskDocRel:unstructtaskdocrel:export')")
    @Log(title = "任务文件关联", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ExtUnstructTaskDocRelPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ExtUnstructTaskDocRelDO> list = (List<ExtUnstructTaskDocRelDO>) extUnstructTaskDocRelService.getExtUnstructTaskDocRelPage(exportReqVO).getRows();
        ExcelUtil<ExtUnstructTaskDocRelRespVO> util = new ExcelUtil<>(ExtUnstructTaskDocRelRespVO.class);
        util.exportExcel(response, ExtUnstructTaskDocRelConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入任务文件关联列表")
    @PreAuthorize("@ss.hasPermi('ext:extUnstructTaskDocRel:unstructtaskdocrel:import')")
    @Log(title = "任务文件关联", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<ExtUnstructTaskDocRelRespVO> util = new ExcelUtil<>(ExtUnstructTaskDocRelRespVO.class);
        List<ExtUnstructTaskDocRelRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = extUnstructTaskDocRelService.importExtUnstructTaskDocRel(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取任务文件关联详细信息")
    @PreAuthorize("@ss.hasPermi('ext:extUnstructTaskDocRel:unstructtaskdocrel:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<ExtUnstructTaskDocRelRespVO> getInfo(@PathVariable("id") Long id) {
        ExtUnstructTaskDocRelDO extUnstructTaskDocRelDO = extUnstructTaskDocRelService.getExtUnstructTaskDocRelById(id);
        return CommonResult.success(BeanUtils.toBean(extUnstructTaskDocRelDO, ExtUnstructTaskDocRelRespVO.class));
    }

    @Operation(summary = "新增任务文件关联")
    @PreAuthorize("@ss.hasPermi('ext:extUnstructTaskDocRel:unstructtaskdocrel:add')")
    @Log(title = "任务文件关联", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody ExtUnstructTaskDocRelSaveReqVO extUnstructTaskDocRel) {
        extUnstructTaskDocRel.setWorkspaceId(super.getWorkSpaceId());
        extUnstructTaskDocRel.setCreatorId(getUserId());
        extUnstructTaskDocRel.setCreateBy(getNickName());
        extUnstructTaskDocRel.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(extUnstructTaskDocRelService.createExtUnstructTaskDocRel(extUnstructTaskDocRel));
    }

    @Operation(summary = "修改任务文件关联")
    @PreAuthorize("@ss.hasPermi('ext:extUnstructTaskDocRel:unstructtaskdocrel:edit')")
    @Log(title = "任务文件关联", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody ExtUnstructTaskDocRelSaveReqVO extUnstructTaskDocRel) {
        extUnstructTaskDocRel.setUpdaterId(getUserId());
        extUnstructTaskDocRel.setUpdateBy(getNickName());
        extUnstructTaskDocRel.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(extUnstructTaskDocRelService.updateExtUnstructTaskDocRel(extUnstructTaskDocRel));
    }

    @Operation(summary = "删除任务文件关联")
    @PreAuthorize("@ss.hasPermi('ext:extUnstructTaskDocRel:unstructtaskdocrel:remove')")
    @Log(title = "任务文件关联", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(extUnstructTaskDocRelService.removeExtUnstructTaskDocRel(Arrays.asList(ids)));
    }

}
