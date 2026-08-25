/*
 * Copyright (c) 2026 Jiangsu Qiantong Technology Co., Ltd.
 */
package tech.qiantong.qknow.module.kg.controller.admin.sync;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tech.qiantong.qknow.common.annotation.Log;
import tech.qiantong.qknow.common.core.controller.BaseController;
import tech.qiantong.qknow.common.core.domain.AjaxResult;
import tech.qiantong.qknow.common.enums.BusinessType;
import tech.qiantong.qknow.module.kg.controller.admin.sync.vo.KgStorageBrowseReqVO;
import tech.qiantong.qknow.module.kg.controller.admin.sync.vo.KgStorageImportReqVO;
import tech.qiantong.qknow.module.kg.controller.admin.sync.vo.KgStorageSelectionReqVO;
import tech.qiantong.qknow.module.kg.service.sync.KgStorageBrowserService;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

/**
 * 知识图谱第三方存储同步 Controller
 */
@Tag(name = "知识图谱第三方存储同步")
@RestController
@RequestMapping("/kg/sync")
@Validated
public class KgSyncController extends BaseController {

    @Resource
    private KgStorageBrowserService kgStorageBrowserService;

    @Operation(summary = "测试第三方存储连接")
    @PreAuthorize("@ss.hasPermi('kg:knowledge:document:list')")
    @PostMapping("/testConnection")
    public AjaxResult testStorageConnection(@Valid @RequestBody KgStorageBrowseReqVO request) {
        kgStorageBrowserService.testConnection(request.getConnection());
        return AjaxResult.success("连接测试成功");
    }

    @Operation(summary = "读取第三方存储目录树")
    @PreAuthorize("@ss.hasPermi('kg:knowledge:document:list')")
    @PostMapping("/fileTree")
    public AjaxResult fileTree(@Valid @RequestBody KgStorageBrowseReqVO request) {
        return AjaxResult.success(kgStorageBrowserService.listDirectories(request));
    }

    @Operation(summary = "读取第三方存储文件列表")
    @PreAuthorize("@ss.hasPermi('kg:knowledge:document:list')")
    @PostMapping("/fileList")
    public AjaxResult fileList(@Valid @RequestBody KgStorageBrowseReqVO request) {
        return AjaxResult.success(kgStorageBrowserService.listFiles(request));
    }

    @Operation(summary = "准备第三方存储文件预览")
    @PreAuthorize("@ss.hasPermi('kg:knowledge:document:list')")
    @PostMapping("/filePreview")
    public AjaxResult filePreview(@Valid @RequestBody KgStorageBrowseReqVO request) {
        AjaxResult result = AjaxResult.success();
        result.put("fileUrl", kgStorageBrowserService.preparePreview(request));
        return result;
    }

    @Operation(summary = "下载第三方存储文件或目录")
    @PreAuthorize("@ss.hasPermi('kg:knowledge:document:list')")
    @PostMapping("/fileDownload")
    public void fileDownload(
            @Valid @RequestBody KgStorageBrowseReqVO request,
            HttpServletResponse response) {
        kgStorageBrowserService.download(request, response);
    }

    @Operation(summary = "解析第三方存储选中项并过滤不支持的文件")
    @PreAuthorize("@ss.hasPermi('kg:knowledge:document:list')")
    @PostMapping("/resolveCandidates")
    public AjaxResult resolveCandidates(@Valid @RequestBody KgStorageSelectionReqVO request) {
        return AjaxResult.success(kgStorageBrowserService.resolveCandidates(request));
    }

    @Operation(summary = "导入第三方存储文件到知识图谱")
    @PreAuthorize("@ss.hasPermi('kg:knowledge:document:add')")
    @Log(title = "第三方存储同步", businessType = BusinessType.IMPORT)
    @PostMapping("/importDocuments")
    public AjaxResult importDocuments(@Valid @RequestBody KgStorageImportReqVO request) {
        int count = kgStorageBrowserService.importDocuments(
                request, getUserId(), getNickName(), super.getWorkSpaceId());
        return AjaxResult.success("成功导入 " + count + " 个文件", count);
    }

}
