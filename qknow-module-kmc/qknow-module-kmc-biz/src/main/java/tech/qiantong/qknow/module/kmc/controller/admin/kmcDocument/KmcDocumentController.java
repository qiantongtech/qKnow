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

package tech.qiantong.qknow.module.kmc.controller.admin.kmcDocument;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
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
import tech.qiantong.qknow.module.kmc.controller.admin.kmcDocument.vo.KmcDocumentPageReqVO;
import tech.qiantong.qknow.module.kmc.controller.admin.kmcDocument.vo.KmcDocumentRespVO;
import tech.qiantong.qknow.module.kmc.controller.admin.kmcDocument.vo.KmcDocumentSaveReqVO;
import tech.qiantong.qknow.module.kmc.controller.admin.kmcDocument.vo.KmcDocumentTrackReqVO;
import tech.qiantong.qknow.module.kmc.convert.kmcDocument.KmcDocumentConvert;
import tech.qiantong.qknow.module.kmc.dal.dataobject.document.KmcDocumentDO;
import tech.qiantong.qknow.module.kmc.service.kmcDocument.IKmcDocumentService;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import tech.qiantong.qknow.thirdparty.domain.dify.enums.DocFormEnum;
import java.util.*;

/**
 * 知识文件Controller
 *
 * @author qknow
 * @date 2025-02-14
 */
@Tag(name = "知识文件")
@RestController
@RequestMapping("/kmcDocument/document")
@Validated
public class KmcDocumentController extends BaseController {

    @Resource
    private IKmcDocumentService kmcDocumentService;

    @Operation(summary = "查询知识文件列表")
    @PreAuthorize("@ss.hasPermi('kmcDocument:kmcDocument:document:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<KmcDocumentRespVO>> list(KmcDocumentPageReqVO kmcDocument) {
        PageResult<KmcDocumentDO> page = kmcDocumentService.getKmcDocumentPage(kmcDocument);
        return CommonResult.success(BeanUtils.toBean(page, KmcDocumentRespVO.class));
    }

    @Operation(summary = "导出知识文件列表")
    @PreAuthorize("@ss.hasPermi('kmcDocument:kmcDocument:document:export')")
    @Log(title = "知识文件", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, KmcDocumentPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<KmcDocumentDO> list = (List<KmcDocumentDO>) kmcDocumentService.getKmcDocumentPage(exportReqVO).getRows();
        ExcelUtil<KmcDocumentRespVO> util = new ExcelUtil<>(KmcDocumentRespVO.class);
        util.exportExcel(response, KmcDocumentConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入知识文件列表")
    @PreAuthorize("@ss.hasPermi('kmcDocument:kmcDocument:document:import')")
    @Log(title = "知识文件", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<KmcDocumentRespVO> util = new ExcelUtil<>(KmcDocumentRespVO.class);
        List<KmcDocumentRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = kmcDocumentService.importKmcDocument(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取知识文件详细信息")
    @PreAuthorize("@ss.hasPermi('kmcDocument:kmcDocument:document:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<KmcDocumentRespVO> getInfo(@PathVariable("id") Long id) {
        KmcDocumentDO kmcDocumentDO = kmcDocumentService.getKmcDocumentById(id);
        return CommonResult.success(BeanUtils.toBean(kmcDocumentDO, KmcDocumentRespVO.class));
    }

    @Operation(summary = "新增知识文件")
    @PreAuthorize("@ss.hasPermi('kmcDocument:kmcDocument:document:add')")
    @Log(title = "知识文件", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Integer> add(@Valid @RequestBody KmcDocumentSaveReqVO kmcDocument) {
        kmcDocument.setCreatorId(getUserId());
        kmcDocument.setCreateBy(getNickName());
        kmcDocument.setCreateTime(DateUtil.date());
        kmcDocument.setWorkspaceId(super.getWorkSpaceId());
        if (Objects.equals(kmcDocument.getDocForm(), DocFormEnum.QA_MODEL.getType())){
            Assert.notBlank(kmcDocument.getMode(),"对话模型不能为空");
            Assert.notBlank(kmcDocument.getChatModelProvider(),"对话模型不能为空");
        }
        return CommonResult.toAjax(kmcDocumentService.createKmcDocument(kmcDocument));
    }

    @Operation(summary = "修改知识文件")
    @PreAuthorize("@ss.hasPermi('kmcDocument:kmcDocument:document:edit')")
    @Log(title = "知识文件", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody KmcDocumentSaveReqVO kmcDocument) {
        kmcDocument.setUpdaterId(getUserId());
        kmcDocument.setUpdateBy(getNickName());
        kmcDocument.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(kmcDocumentService.updateKmcDocument(kmcDocument));
    }

    @Operation(summary = "删除知识文件")
    @PreAuthorize("@ss.hasPermi('kmcDocument:kmcDocument:document:remove')")
    @Log(title = "知识文件", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(kmcDocumentService.removeKmcDocument(Arrays.asList(ids)));
    }

    @Operation(summary = "根据条件查询知识文件列表")
    @GetMapping("/selectList")
    public CommonResult<List<KmcDocumentRespVO>> selectList(KmcDocumentPageReqVO kmcDocument) {
        List<KmcDocumentDO> list = kmcDocumentService.selectList(kmcDocument);
        return CommonResult.success(BeanUtils.toBean(list, KmcDocumentRespVO.class));
    }

    @Operation(summary = "获取多少种类型及每种类型下面的总文件数量")
    @GetMapping("/getFileTypes/{knowledgeBaseId}")
    public CommonResult<List<Map<String, Object>>> getFileTypes(@PathVariable("knowledgeBaseId")Long knowledgeBaseId) {
        List<Map<String, Object>> list = kmcDocumentService.getFileTypes(knowledgeBaseId);
        return CommonResult.success(list);
    }

    @Operation(summary = "文件跟踪统计")
    @PostMapping("/trackCount")
    public CommonResult<Boolean> trackCount(@Valid @RequestBody List<KmcDocumentTrackReqVO> documentTrackList) {
        return CommonResult.success(kmcDocumentService.trackCount(documentTrackList, getUserId(), getUsername()));
    }

    @Operation(summary = "获取该知识库下的第一个文档")
    @GetMapping("/getOne/{knowledgeBaseId}")
    public CommonResult<KmcDocumentRespVO> getOne(@PathVariable("knowledgeBaseId") Long knowledgeBaseId) {
        KmcDocumentDO kmcDocumentDO = kmcDocumentService.getKnowledgeBaseOne(knowledgeBaseId);
        return CommonResult.success(BeanUtils.toBean(kmcDocumentDO, KmcDocumentRespVO.class));
    }

    @Operation(summary = "检查文件名称是否有重复的")
    @GetMapping("/checkFileName")
    public CommonResult<String> checkFileName(Long id, String fileNames, Long knowledgeBaseId) {
        return CommonResult.success(kmcDocumentService.checkFileName(id, fileNames, knowledgeBaseId));
    }
}
