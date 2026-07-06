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

package tech.qiantong.qknow.module.kmc.controller.admin.knowledgeBase;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
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
import tech.qiantong.qknow.common.core.domain.entity.SysRole;
import tech.qiantong.qknow.common.core.domain.entity.SysUser;
import tech.qiantong.qknow.common.core.page.PageParam;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.common.enums.BusinessType;
import tech.qiantong.qknow.common.core.utils.object.BeanUtils;
import tech.qiantong.qknow.common.core.utils.poi.ExcelUtil;
import tech.qiantong.qknow.module.kmc.controller.admin.knowledgeBase.vo.*;
import tech.qiantong.qknow.module.kmc.convert.knowledgeBase.KmcKnowledgeBaseConvert;
import tech.qiantong.qknow.module.kmc.dal.dataobject.knowledgeBase.KmcKnowledgeBaseDO;
import tech.qiantong.qknow.module.kmc.dal.dataobject.knowledgeBase.KmcKnowledgeRoleDO;
import tech.qiantong.qknow.module.kmc.service.kmcDocument.IKmcDocumentService;
import tech.qiantong.qknow.module.kmc.service.knowledgeBase.IKmcKnowledgeBaseService;
import tech.qiantong.qknow.module.kmc.service.knowledgeBase.IKmcKnowledgeRoleService;
import tech.qiantong.qknow.module.system.service.ISysRoleService;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 知识库Controller
 *
 * @author qknow
 * @date 2025-07-22
 */
@Tag(name = "知识库")
@RestController
@RequestMapping("/kmc/knowledgeBase")
@Validated
public class KmcKnowledgeBaseController extends BaseController {
    @Resource
    private IKmcKnowledgeBaseService kmcKnowledgeBaseService;
    @Resource
    private ISysRoleService sysRoleService;
    @Resource
    private IKmcDocumentService kmcDocumentService;
    @Resource
    private IKmcKnowledgeRoleService kmcKnowledgeRoleService;

    @Operation(summary = "查询知识库列表")
    @PreAuthorize("@ss.hasPermi('kmc:knowledgeBase:knowledgebase:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<KmcKnowledgeBaseRespVO>> list(KmcKnowledgeBasePageReqVO kmcKnowledgeBase) {
        return CommonResult.success(kmcKnowledgeBaseService.getKmcKnowledgeBasePageWithFileCount(kmcKnowledgeBase, super.getUserId()));
    }

    @Operation(summary = "导出知识库列表")
    @PreAuthorize("@ss.hasPermi('kmc:knowledgeBase:knowledgebase:export')")
    @Log(title = "知识库", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, KmcKnowledgeBasePageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<KmcKnowledgeBaseDO> list = (List<KmcKnowledgeBaseDO>) kmcKnowledgeBaseService.getKmcKnowledgeBasePage(exportReqVO, super.getUserId()).getRows();
        ExcelUtil<KmcKnowledgeBaseRespVO> util = new ExcelUtil<>(KmcKnowledgeBaseRespVO.class);
        util.exportExcel(response, KmcKnowledgeBaseConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入知识库列表")
    @PreAuthorize("@ss.hasPermi('kmc:knowledgeBase:knowledgebase:import')")
    @Log(title = "知识库", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<KmcKnowledgeBaseRespVO> util = new ExcelUtil<>(KmcKnowledgeBaseRespVO.class);
        List<KmcKnowledgeBaseRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = kmcKnowledgeBaseService.importKmcKnowledgeBase(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取知识库详细信息")
    @PreAuthorize("@ss.hasPermi('kmc:knowledgeBase:knowledgebase:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<KmcKnowledgeBaseRespVO> getInfo(@PathVariable("id") Long id) {
        KmcKnowledgeBaseDO kmcKnowledgeBaseDO = kmcKnowledgeBaseService.getKmcKnowledgeBaseById(id);
        return CommonResult.success(BeanUtils.toBean(kmcKnowledgeBaseDO, KmcKnowledgeBaseRespVO.class));
    }

    @Operation(summary = "新增知识库")
    @PreAuthorize("@ss.hasPermi('kmc:knowledgeBase:knowledgebase:add')")
    @Log(title = "知识库", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody KmcKnowledgeBaseSaveReqVO kmcKnowledgeBase) {
        kmcKnowledgeBase.setWorkspaceId(super.getWorkSpaceId());
        kmcKnowledgeBase.setCreatorId(getUserId());
        kmcKnowledgeBase.setCreateBy(getNickName());
        return CommonResult.success(kmcKnowledgeBaseService.createKmcKnowledgeBase(kmcKnowledgeBase));
    }

    @Operation(summary = "修改知识库")
    @PreAuthorize("@ss.hasPermi('kmc:knowledgeBase:knowledgebase:edit')")
    @Log(title = "知识库", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody KmcKnowledgeBaseSaveReqVO kmcKnowledgeBase) {
        kmcKnowledgeBase.setUpdaterId(getUserId());
        kmcKnowledgeBase.setUpdateBy(getNickName());
        return CommonResult.toAjax(kmcKnowledgeBaseService.updateKmcKnowledgeBase(kmcKnowledgeBase));
    }

    @Operation(summary = "删除知识库")
    @PreAuthorize("@ss.hasPermi('kmc:knowledgeBase:knowledgebase:remove')")
    @Log(title = "知识库", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(kmcKnowledgeBaseService.removeKmcKnowledgeBase(Arrays.asList(ids)));
    }

    @Operation(summary = "获取Embedding模型")
    @GetMapping("/getTextEmbedding")
    public CommonResult<JSONArray> getTextEmbedding() {
        return CommonResult.success(kmcKnowledgeBaseService.getTextEmbedding());
    }

    @Operation(summary = "获取rerank模型")
    @GetMapping("/getRerank")
    public CommonResult<JSONArray> getRerank() {
        return CommonResult.success(kmcKnowledgeBaseService.getRerank());
    }

    @Operation(summary = "召回测试")
    @PostMapping("/recallTest")
    public CommonResult<List<RetrieveResultRespVO>> recallTest(@Valid @RequestBody RetrieveResultReqVO retrieveResultReqVO) {
        retrieveResultReqVO.setWorkspaceId(getWorkSpaceId());
        return CommonResult.success(kmcKnowledgeBaseService.recallTest(retrieveResultReqVO));
    }

    @PreAuthorize("@ss.hasPermi('kmc:knowledgeBase:role:list')")
    @Operation(summary = "获取知识库权限角色")
    @GetMapping("/role/{id}")
    public CommonResult<JSONObject> getKnowledgeBaseRole(@PathVariable("id") Long id) {
        // 绑定的关系数据
        List<KmcKnowledgeRoleDO> byKnowledgeBaseId = kmcKnowledgeRoleService.findByKnowledgeBaseId(id);
        // 所有的角色数据
        List<SysRole> sysRoles = sysRoleService.selectRoleAll();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("roleList", SysUser.isAdmin(getUserId()) ? sysRoles : sysRoles.stream().filter(r -> !r.isAdmin()).collect(Collectors.toList()));
        jsonObject.put("knowledgeRoleList", byKnowledgeBaseId);
        return CommonResult.success(jsonObject);
    }

    @Operation(summary = "修改知识库权限角色")
    @PutMapping("/role")
    public CommonResult<Boolean> updateKnowledgeBaseRole(@Validated @RequestBody KmcKnowledgeRoleSaveReqVO updateReqVO) {
        updateReqVO.setWorkspaceId(getWorkSpaceId());
        return CommonResult.success(kmcKnowledgeBaseService.updateKnowledgeBaseRole(updateReqVO));
    }

    @Operation(summary = "根据权限获取知识库列表")
    @GetMapping("/getKmcKnowledgeBaseList")
    public CommonResult<List<KmcKnowledgeBaseRespVO>> getKmcKnowledgeBaseList(Boolean isValid) {
        List<KmcKnowledgeBaseDO> kmcKnowledgeBaseDOList = kmcKnowledgeBaseService.getKmcKnowledgeBaseList(getUserId(), isValid);
        return CommonResult.success(BeanUtils.toBean(kmcKnowledgeBaseDOList, KmcKnowledgeBaseRespVO.class));
    }

    @Operation(summary = "启用禁用知识库")
    @PutMapping("/changeKnowledgeValid")
    public CommonResult<Integer> changeKnowledgeValid(KmcKnowledgeBaseSaveReqVO kmcKnowledgeSaveReqVO) {
        return CommonResult.success(kmcKnowledgeBaseService.changeKnowledgeValid(kmcKnowledgeSaveReqVO));
    }
}
