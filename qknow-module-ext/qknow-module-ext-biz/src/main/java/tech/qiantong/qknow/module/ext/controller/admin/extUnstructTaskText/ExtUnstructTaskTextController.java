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

package tech.qiantong.qknow.module.ext.controller.admin.extUnstructTaskText;

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
import tech.qiantong.qknow.common.utils.object.BeanUtils;
import tech.qiantong.qknow.common.utils.poi.ExcelUtil;
import tech.qiantong.qknow.module.ext.controller.admin.extUnstructTaskText.vo.ExtUnstructTaskTextPageReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extUnstructTaskText.vo.ExtUnstructTaskTextRespVO;
import tech.qiantong.qknow.module.ext.controller.admin.extUnstructTaskText.vo.ExtUnstructTaskTextSaveReqVO;
import tech.qiantong.qknow.module.ext.convert.extUnstructTaskText.ExtUnstructTaskTextConvert;
import tech.qiantong.qknow.module.ext.dal.dataobject.extUnstructTaskText.ExtUnstructTaskTextDO;
import tech.qiantong.qknow.module.ext.service.extUnstructTaskText.IExtUnstructTaskTextService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * 任务文件段落关联Controller
 *
 * @author qknow
 * @date 2025-02-21
 */
@Tag(name = "任务文件段落关联")
@RestController
@RequestMapping("/ext/text")
@Validated
public class ExtUnstructTaskTextController extends BaseController {
    @Resource
    private IExtUnstructTaskTextService extUnstructTaskTextService;

    @Operation(summary = "查询任务文件段落关联列表")
    @PreAuthorize("@ss.hasPermi('ext:extUnstructTaskText:t:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<ExtUnstructTaskTextRespVO>> list(ExtUnstructTaskTextPageReqVO extUnstructTaskText) {
        PageResult<ExtUnstructTaskTextDO> page = extUnstructTaskTextService.getExtUnstructTaskTextPage(extUnstructTaskText);
        return CommonResult.success(BeanUtils.toBean(page, ExtUnstructTaskTextRespVO.class));
    }

    @Operation(summary = "导出任务文件段落关联列表")
    @PreAuthorize("@ss.hasPermi('ext:extUnstructTaskText:t:export')")
    @Log(title = "任务文件段落关联", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ExtUnstructTaskTextPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ExtUnstructTaskTextDO> list = (List<ExtUnstructTaskTextDO>) extUnstructTaskTextService.getExtUnstructTaskTextPage(exportReqVO).getRows();
        ExcelUtil<ExtUnstructTaskTextRespVO> util = new ExcelUtil<>(ExtUnstructTaskTextRespVO.class);
        util.exportExcel(response, ExtUnstructTaskTextConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入任务文件段落关联列表")
    @PreAuthorize("@ss.hasPermi('ext:extUnstructTaskText:t:import')")
    @Log(title = "任务文件段落关联", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<ExtUnstructTaskTextRespVO> util = new ExcelUtil<>(ExtUnstructTaskTextRespVO.class);
        List<ExtUnstructTaskTextRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = extUnstructTaskTextService.importExtUnstructTaskText(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取任务文件段落关联详细信息")
    @PreAuthorize("@ss.hasPermi('ext:extUnstructTaskText:t:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<ExtUnstructTaskTextRespVO> getInfo(@PathVariable("id") Long id) {
        ExtUnstructTaskTextDO extUnstructTaskTextDO = extUnstructTaskTextService.getExtUnstructTaskTextById(id);
        return CommonResult.success(BeanUtils.toBean(extUnstructTaskTextDO, ExtUnstructTaskTextRespVO.class));
    }

    @Operation(summary = "新增任务文件段落关联")
    @PreAuthorize("@ss.hasPermi('ext:extUnstructTaskText:t:add')")
    @Log(title = "任务文件段落关联", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody ExtUnstructTaskTextSaveReqVO extUnstructTaskText) {
        return CommonResult.toAjax(extUnstructTaskTextService.createExtUnstructTaskText(extUnstructTaskText));
    }

    @Operation(summary = "修改任务文件段落关联")
    @PreAuthorize("@ss.hasPermi('ext:extUnstructTaskText:t:edit')")
    @Log(title = "任务文件段落关联", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody ExtUnstructTaskTextSaveReqVO extUnstructTaskText) {
        return CommonResult.toAjax(extUnstructTaskTextService.updateExtUnstructTaskText(extUnstructTaskText));
    }

    @Operation(summary = "删除任务文件段落关联")
    @PreAuthorize("@ss.hasPermi('ext:extUnstructTaskText:t:remove')")
    @Log(title = "任务文件段落关联", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(extUnstructTaskTextService.removeExtUnstructTaskText(Arrays.asList(ids)));
    }

}
