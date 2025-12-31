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

package tech.qiantong.qknow.module.ext.controller.admin.extSchema;

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
import tech.qiantong.qknow.module.ext.controller.admin.extSchema.vo.ExtSchemaPageReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extSchema.vo.ExtSchemaRespVO;
import tech.qiantong.qknow.module.ext.controller.admin.extSchema.vo.ExtSchemaSaveReqVO;
import tech.qiantong.qknow.module.ext.convert.extSchema.ExtSchemaConvert;
import tech.qiantong.qknow.module.ext.dal.dataobject.extSchema.ExtSchemaDO;
import tech.qiantong.qknow.module.ext.service.extSchema.IExtSchemaService;

/**
 * 概念配置Controller
 *
 * @author qknow
 * @date 2025-02-17
 */
@Tag(name = "概念配置")
@RestController
@RequestMapping("/ext/schema")
@Validated
public class ExtSchemaController extends BaseController {
    @Resource
    private IExtSchemaService extSchemaService;

    @Operation(summary = "查询概念配置列表")
//    @PreAuthorize("@ss.hasPermi('ext:extSchema:schema:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<ExtSchemaRespVO>> list(ExtSchemaPageReqVO extSchema) {
        PageResult<ExtSchemaDO> page = extSchemaService.getExtSchemaPage(extSchema);
        return CommonResult.success(BeanUtils.toBean(page, ExtSchemaRespVO.class));
    }

    @Operation(summary = "导出概念配置列表")
    @PreAuthorize("@ss.hasPermi('ext:extSchema:schema:export')")
    @Log(title = "概念配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ExtSchemaPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ExtSchemaDO> list = (List<ExtSchemaDO>) extSchemaService.getExtSchemaPage(exportReqVO).getRows();
        ExcelUtil<ExtSchemaRespVO> util = new ExcelUtil<>(ExtSchemaRespVO.class);
        util.exportExcel(response, ExtSchemaConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入概念配置列表")
    @PreAuthorize("@ss.hasPermi('ext:extSchema:schema:import')")
    @Log(title = "概念配置", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<ExtSchemaRespVO> util = new ExcelUtil<>(ExtSchemaRespVO.class);
        List<ExtSchemaRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = extSchemaService.importExtSchema(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取概念配置详细信息")
    @PreAuthorize("@ss.hasPermi('ext:extSchema:schema:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<ExtSchemaRespVO> getInfo(@PathVariable("id") Long id) {
        ExtSchemaDO extSchemaDO = extSchemaService.getExtSchemaById(id);
        return CommonResult.success(BeanUtils.toBean(extSchemaDO, ExtSchemaRespVO.class));
    }

    @Operation(summary = "新增概念配置")
    @PreAuthorize("@ss.hasPermi('ext:extSchema:schema:add')")
    @Log(title = "概念配置", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody ExtSchemaSaveReqVO extSchema) {
        ExtSchemaPageReqVO extSchemaPageReqVO = new ExtSchemaPageReqVO();
        extSchemaPageReqVO.setExactName(extSchema.getName());
        PageResult<ExtSchemaDO> page = extSchemaService.getExtSchemaPage(extSchemaPageReqVO);
        if (!page.getRows().isEmpty() || page.getTotal() > 0) {
            return CommonResult.error(GlobalErrorCodeConstants.ERROR.getCode(),"概念名称已存在，不可重复添加");
        }
        extSchema.setCreatorId(getUserId());
        extSchema.setCreateBy(getNickName());
        extSchema.setCreateTime(DateUtil.date());
        extSchema.setWorkspaceId(super.getWorkSpaceId());
        return CommonResult.toAjax(extSchemaService.createExtSchema(extSchema));
    }

    @Operation(summary = "修改概念配置")
    @PreAuthorize("@ss.hasPermi('ext:extSchema:schema:edit')")
    @Log(title = "概念配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody ExtSchemaSaveReqVO extSchema) {
        extSchema.setUpdaterId(getUserId());
        extSchema.setUpdateBy(getNickName());
        extSchema.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(extSchemaService.updateExtSchema(extSchema));
    }

    @Operation(summary = "删除概念配置")
    @PreAuthorize("@ss.hasPermi('ext:extSchema:schema:remove')")
    @Log(title = "概念配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(extSchemaService.removeExtSchema(Arrays.asList(ids)));
    }

    @Operation(summary = "查询全部概念配置")
    @GetMapping("/allList")
    public CommonResult<List<ExtSchemaDO>> getExtSchemaAllList(ExtSchemaDO extSchemaDO) {
        List<ExtSchemaDO> list = extSchemaService.getExtSchemaAllList(extSchemaDO);
        return CommonResult.success(list);
    }

}
