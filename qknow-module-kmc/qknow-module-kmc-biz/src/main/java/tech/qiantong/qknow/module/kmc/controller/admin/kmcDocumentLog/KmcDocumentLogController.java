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

package tech.qiantong.qknow.module.kmc.controller.admin.kmcDocumentLog;

import cn.hutool.core.date.DateUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import tech.qiantong.qknow.module.kmc.controller.admin.kmcDocumentLog.vo.KmcDocumentLogPageReqVO;
import tech.qiantong.qknow.module.kmc.controller.admin.kmcDocumentLog.vo.KmcDocumentLogRespVO;
import tech.qiantong.qknow.module.kmc.controller.admin.kmcDocumentLog.vo.KmcDocumentLogSaveReqVO;
import tech.qiantong.qknow.module.kmc.convert.kmcDocumentLog.KmcDocumentLogConvert;
import tech.qiantong.qknow.module.kmc.dal.dataobject.document.KmcDocumentDO;
import tech.qiantong.qknow.module.kmc.dal.dataobject.kmcDocumentLog.KmcDocumentLogDO;
import tech.qiantong.qknow.module.kmc.service.kmcDocumentLog.IKmcDocumentLogService;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * 文件操作日志Controller
 *
 * @author qknow
 * @date 2025-03-24
 */
@Tag(name = "文件操作日志")
@RestController
@RequestMapping("/kmc/kmcDocumentLog")
@Validated
public class KmcDocumentLogController extends BaseController {
    @Resource
    private IKmcDocumentLogService kmcDocumentLogService;

    @Operation(summary = "查询文件操作日志列表")
//    @PreAuthorize("@ss.hasPermi('kmc:kmcDocumentLog:documentlog:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<KmcDocumentLogRespVO>> list(KmcDocumentLogPageReqVO kmcDocumentLog) {
        PageResult<KmcDocumentLogDO> page = kmcDocumentLogService.getKmcDocumentLogPage(kmcDocumentLog);
        return CommonResult.success(BeanUtils.toBean(page, KmcDocumentLogRespVO.class));
    }

    @Operation(summary = "导出文件操作日志列表")
//    @PreAuthorize("@ss.hasPermi('kmc:kmcDocumentLog:documentlog:export')")
    @Log(title = "文件操作日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, KmcDocumentLogPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<KmcDocumentLogDO> list = (List<KmcDocumentLogDO>) kmcDocumentLogService.getKmcDocumentLogPage(exportReqVO).getRows();
        ExcelUtil<KmcDocumentLogRespVO> util = new ExcelUtil<>(KmcDocumentLogRespVO.class);
        util.exportExcel(response, KmcDocumentLogConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入文件操作日志列表")
//    @PreAuthorize("@ss.hasPermi('kmc:kmcDocumentLog:documentlog:import')")
    @Log(title = "文件操作日志", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<KmcDocumentLogRespVO> util = new ExcelUtil<>(KmcDocumentLogRespVO.class);
        List<KmcDocumentLogRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = kmcDocumentLogService.importKmcDocumentLog(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取文件操作日志详细信息")
//    @PreAuthorize("@ss.hasPermi('kmc:kmcDocumentLog:documentlog:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<KmcDocumentLogRespVO> getInfo(@PathVariable("id") Long id) {
        KmcDocumentLogDO kmcDocumentLogDO = kmcDocumentLogService.getKmcDocumentLogById(id);
        return CommonResult.success(BeanUtils.toBean(kmcDocumentLogDO, KmcDocumentLogRespVO.class));
    }

    @Operation(summary = "新增文件操作日志")
//    @PreAuthorize("@ss.hasPermi('kmc:kmcDocumentLog:documentlog:add')")
    @Log(title = "文件操作日志", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody KmcDocumentLogSaveReqVO kmcDocumentLog) {
        kmcDocumentLog.setUserName(getUsername());
        kmcDocumentLog.setUserId(getUserId());
        kmcDocumentLog.setCreatorId(getUserId());
        kmcDocumentLog.setCreateBy(getNickName());
        kmcDocumentLog.setCreateTime(DateUtil.date());
        kmcDocumentLog.setWorkspaceId(super.getWorkSpaceId());
        return CommonResult.toAjax(kmcDocumentLogService.createKmcDocumentLog(kmcDocumentLog));
    }

    @Operation(summary = "修改文件操作日志")
//    @PreAuthorize("@ss.hasPermi('kmc:kmcDocumentLog:documentlog:edit')")
    @Log(title = "文件操作日志", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody KmcDocumentLogSaveReqVO kmcDocumentLog) {
        kmcDocumentLog.setUpdaterId(getUserId());
        kmcDocumentLog.setUpdateBy(getNickName());
        kmcDocumentLog.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(kmcDocumentLogService.updateKmcDocumentLog(kmcDocumentLog));
    }

    @Operation(summary = "删除文件操作日志")
//    @PreAuthorize("@ss.hasPermi('kmc:kmcDocumentLog:documentlog:remove')")
    @Log(title = "文件操作日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(kmcDocumentLogService.removeKmcDocumentLog(Arrays.asList(ids)));
    }

    @Operation(summary = "获取每个用户不同的关注度列表")
//    @PreAuthorize("@ss.hasPermi('kmc:kmcDocumentLog:documentlog:list')")
    @GetMapping("/getUserAttention")
    public CommonResult<List<KmcDocumentDO>> getUserAttention(KmcDocumentLogDO kmcDocumentLog) {
        kmcDocumentLog.setUserName(getUsername());
        kmcDocumentLog.setUserId(getUserId());
        List<KmcDocumentDO> list = kmcDocumentLogService.seletAllDocumentLogList(kmcDocumentLog);
        return CommonResult.success(list);
    }

}
