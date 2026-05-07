/*
 * Copyright (c) 2026 Jiangsu Qiantong Technology Co., Ltd.
 *  *
 * Software Name: qKnow Knowledge Platform (Business Edition)
 * Software Copyright Registration No. 15980140
 *  *
 * [RIGHTS AND LICENSE STATEMENT]
 * This file contains non-public commercial source code of which Jiangsu Qiantong
 * Technology Co., Ltd. lawfully possesses complete intellectual property rights.
 *  *
 * Access and use are limited to entities or individuals who have signed a valid
 * commercial license agreement, within the scope stipulated in the agreement.
 * The "accessibility" of this source code is premised on lawful authorization
 * and does not constitute any form of transfer of intellectual property rights
 * or implied licensing.
 *  *
 * [PROHIBITIONS]
 * Unless explicitly agreed in the license agreement, the following acts in any
 * form are strictly prohibited:
 * 1. Copying, disseminating, disclosing, selling, renting, or redistributing
 * this source code;
 * 2. Providing the software's functionality to third parties via SaaS, PaaS,
 * cloud hosting, or other means;
 * 3. Using this software or its derivative versions to develop products that
 * compete with the Right Holder;
 * 4. Providing or displaying this source code or related technical information
 * to unauthorized third parties;
 * 5. Tampering with, circumventing, or destroying copyright notices, license
 * verifications, or other technical protection measures.
 *  *
 * [LEGAL LIABILITY]
 * Any unauthorized use constitutes an infringement of trade secrets and
 * intellectual property rights.
 *  *
 * The Right Holder will strictly pursue liability for breach of contract and
 * infringement in accordance with the commercial agreement and laws such as
 * the "Copyright Law of the People's Republic of China" and the "Anti-Unfair
 * Competition Law".
 *  *
 * ============================================================================
 *  *
 * Copyright (c) 2026 江苏千桐科技有限公司
 *  *
 * 软件名称：qKnow 知识平台（商业版） | 软著登字第15980140号
 *  *
 * 【权利与授权声明】
 * 本文件属于江苏千桐科技有限公司依法享有完全知识产权的非公开商业源代码。
 * 仅限已签署有效商业授权合同的单位或个人在约定范围内查阅和使用。
 * 源代码的“可访问性”均以合法授权为前提，不构成任何形式的知识产权转让或默示授权。
 *  *
 * 【禁止事项】
 * 除授权合同明确约定外，严禁任何形式的：
 * 1. 复制、传播、披露、出售、出租或再分发本源代码；
 * 2. 通过 SaaS、PaaS、云托管等方式向第三方提供本软件功能；
 * 3. 将本软件或其衍生版本用于开发与权利人构成竞争的产品；
 * 4. 向未授权第三方提供或展示本源代码或相关技术信息；
 * 5. 篡改、规避或破坏版权标识、授权校验及其他技术保护措施。
 *  *
 * 【法律责任】
 * 任何未经授权的利用行为，均构成对商业秘密及知识产权的侵害。
 * 权利人将依据商业合同及《中华人民共和国著作权法》《反不正当竞争法》
 * 等法律法规，严厉追究违约与侵权责任。
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
