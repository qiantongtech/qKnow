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

package tech.qiantong.qknow.module.kmc.controller.admin.knowledgeBase;

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
import tech.qiantong.qknow.module.kmc.controller.admin.knowledgeBase.vo.KmcKnowledgeRecallLogPageReqVO;
import tech.qiantong.qknow.module.kmc.controller.admin.knowledgeBase.vo.KmcKnowledgeRecallLogRespVO;
import tech.qiantong.qknow.module.kmc.controller.admin.knowledgeBase.vo.KmcKnowledgeRecallLogSaveReqVO;
import tech.qiantong.qknow.module.kmc.convert.knowledgeBase.KmcKnowledgeRecallLogConvert;
import tech.qiantong.qknow.module.kmc.dal.dataobject.knowledgeBase.KmcKnowledgeRecallLogDO;
import tech.qiantong.qknow.module.kmc.service.knowledgeBase.IKmcKnowledgeRecallLogService;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * 召回记录Controller
 *
 * @author qknow
 * @date 2025-07-24
 */
@Tag(name = "召回记录")
@RestController
@RequestMapping("/kmc/log")
@Validated
public class KmcKnowledgeRecallLogController extends BaseController {
    @Resource
    private IKmcKnowledgeRecallLogService kmcKnowledgeRecallLogService;

    @Operation(summary = "查询召回记录列表")
    @GetMapping("/list")
    public CommonResult<PageResult<KmcKnowledgeRecallLogRespVO>> list(KmcKnowledgeRecallLogPageReqVO kmcKnowledgeRecallLog) {
        kmcKnowledgeRecallLog.setCreatorId(super.getUserId());// 只查看自己的召回记录
        PageResult<KmcKnowledgeRecallLogDO> page = kmcKnowledgeRecallLogService.getKmcKnowledgeRecallLogPage(kmcKnowledgeRecallLog);
        return CommonResult.success(BeanUtils.toBean(page, KmcKnowledgeRecallLogRespVO.class));
    }

    @Operation(summary = "导出召回记录列表")
    @Log(title = "召回记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, KmcKnowledgeRecallLogPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<KmcKnowledgeRecallLogDO> list = (List<KmcKnowledgeRecallLogDO>) kmcKnowledgeRecallLogService.getKmcKnowledgeRecallLogPage(exportReqVO).getRows();
        ExcelUtil<KmcKnowledgeRecallLogRespVO> util = new ExcelUtil<>(KmcKnowledgeRecallLogRespVO.class);
        util.exportExcel(response, KmcKnowledgeRecallLogConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入召回记录列表")
    @Log(title = "召回记录", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<KmcKnowledgeRecallLogRespVO> util = new ExcelUtil<>(KmcKnowledgeRecallLogRespVO.class);
        List<KmcKnowledgeRecallLogRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = kmcKnowledgeRecallLogService.importKmcKnowledgeRecallLog(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取召回记录详细信息")
    @GetMapping(value = "/{id}")
    public CommonResult<KmcKnowledgeRecallLogRespVO> getInfo(@PathVariable("id") Long id) {
        KmcKnowledgeRecallLogDO kmcKnowledgeRecallLogDO = kmcKnowledgeRecallLogService.getKmcKnowledgeRecallLogById(id);
        return CommonResult.success(BeanUtils.toBean(kmcKnowledgeRecallLogDO, KmcKnowledgeRecallLogRespVO.class));
    }

    @Operation(summary = "新增召回记录")
    @Log(title = "召回记录", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody KmcKnowledgeRecallLogSaveReqVO kmcKnowledgeRecallLog) {
        return CommonResult.toAjax(kmcKnowledgeRecallLogService.createKmcKnowledgeRecallLog(kmcKnowledgeRecallLog));
    }

    @Operation(summary = "修改召回记录")
    @Log(title = "召回记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody KmcKnowledgeRecallLogSaveReqVO kmcKnowledgeRecallLog) {
        return CommonResult.toAjax(kmcKnowledgeRecallLogService.updateKmcKnowledgeRecallLog(kmcKnowledgeRecallLog));
    }

    @Operation(summary = "删除召回记录")
    @Log(title = "召回记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(kmcKnowledgeRecallLogService.removeKmcKnowledgeRecallLog(Arrays.asList(ids)));
    }

    @Operation(summary = "获取知识库召回记录")
    @GetMapping("/getKnowledgeBaseRecallLogList")
    public CommonResult<List<KmcKnowledgeRecallLogRespVO>> getKnowledgeBaseRecallLogList(Long knowledgeBaseId) {
        List<KmcKnowledgeRecallLogDO> list = kmcKnowledgeRecallLogService.findByKnowledgeBaseId(knowledgeBaseId);
        return CommonResult.success(BeanUtils.toBean(list, KmcKnowledgeRecallLogRespVO.class));
    }

}
