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

package tech.qiantong.qknow.module.ext.controller.admin.extSchemaAttribute;

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
import tech.qiantong.qknow.module.ext.controller.admin.extSchemaAttribute.vo.ExtSchemaAttributePageReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extSchemaAttribute.vo.ExtSchemaAttributeRespVO;
import tech.qiantong.qknow.module.ext.controller.admin.extSchemaAttribute.vo.ExtSchemaAttributeSaveReqVO;
import tech.qiantong.qknow.module.ext.convert.extSchemaAttribute.ExtSchemaAttributeConvert;
import tech.qiantong.qknow.module.ext.dal.dataobject.extSchemaAttribute.ExtSchemaAttributeDO;
import tech.qiantong.qknow.module.ext.service.extSchemaAttribute.IExtSchemaAttributeService;

/**
 * 概念属性Controller
 *
 * @author qknow
 * @date 2025-02-17
 */
@Tag(name = "概念属性")
@RestController
@RequestMapping("/ext/attribute")
@Validated
public class ExtSchemaAttributeController extends BaseController {
    @Resource
    private IExtSchemaAttributeService extSchemaAttributeService;

    @Operation(summary = "查询概念属性列表")
    @GetMapping("/list")
    public CommonResult<PageResult<ExtSchemaAttributeRespVO>> list(ExtSchemaAttributePageReqVO extSchemaAttribute) {
        PageResult<ExtSchemaAttributeDO> page = extSchemaAttributeService.getExtSchemaAttributePage(extSchemaAttribute);
        return CommonResult.success(BeanUtils.toBean(page, ExtSchemaAttributeRespVO.class));
    }

    @Operation(summary = "导出概念属性列表")
    @Log(title = "概念属性", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ExtSchemaAttributePageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ExtSchemaAttributeDO> list = (List<ExtSchemaAttributeDO>) extSchemaAttributeService.getExtSchemaAttributePage(exportReqVO).getRows();
        ExcelUtil<ExtSchemaAttributeRespVO> util = new ExcelUtil<>(ExtSchemaAttributeRespVO.class);
        util.exportExcel(response, ExtSchemaAttributeConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入概念属性列表")
    @Log(title = "概念属性", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<ExtSchemaAttributeRespVO> util = new ExcelUtil<>(ExtSchemaAttributeRespVO.class);
        List<ExtSchemaAttributeRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = extSchemaAttributeService.importExtSchemaAttribute(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取概念属性详细信息")
    @GetMapping(value = "/{id}")
    public CommonResult<ExtSchemaAttributeRespVO> getInfo(@PathVariable("id") Long id) {
        ExtSchemaAttributeDO extSchemaAttributeDO = extSchemaAttributeService.getExtSchemaAttributeById(id);
        return CommonResult.success(BeanUtils.toBean(extSchemaAttributeDO, ExtSchemaAttributeRespVO.class));
    }

    @Operation(summary = "新增概念属性")
    @Log(title = "概念属性", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody ExtSchemaAttributeSaveReqVO extSchemaAttribute) {
        extSchemaAttribute.setCreatorId(getUserId());
        extSchemaAttribute.setCreateBy(getNickName());
        extSchemaAttribute.setCreateTime(DateUtil.date());
        extSchemaAttribute.setWorkspaceId(super.getWorkSpaceId());
        return CommonResult.toAjax(extSchemaAttributeService.createExtSchemaAttribute(extSchemaAttribute));
    }

    @Operation(summary = "修改概念属性")
    @Log(title = "概念属性", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody ExtSchemaAttributeSaveReqVO extSchemaAttribute) {
        extSchemaAttribute.setUpdaterId(getUserId());
        extSchemaAttribute.setUpdateBy(getNickName());
        extSchemaAttribute.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(extSchemaAttributeService.updateExtSchemaAttribute(extSchemaAttribute));
    }

    @Operation(summary = "删除概念属性")
    @Log(title = "概念属性", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(extSchemaAttributeService.removeExtSchemaAttribute(Arrays.asList(ids)));
    }


    @Operation(summary = "查询全部概念属性数据")
    @GetMapping("/getAllExtSchemaAttributeList")
    public CommonResult<List<ExtSchemaAttributeDO>> getAllExtSchemaAttributeList(ExtSchemaAttributeDO extSchemaAttributeDO) {
        List<ExtSchemaAttributeDO> list = extSchemaAttributeService.getAllExtSchemaAttributeList(extSchemaAttributeDO);
        return CommonResult.success(list);
    }

}
