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

package tech.qiantong.qknow.module.kg.controller.admin.knowledge;

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
import tech.qiantong.qknow.module.kg.controller.admin.knowledge.vo.KgKnowledgeCategoryPageReqVO;
import tech.qiantong.qknow.module.kg.controller.admin.knowledge.vo.KgKnowledgeCategoryReqVO;
import tech.qiantong.qknow.module.kg.controller.admin.knowledge.vo.KgKnowledgeCategoryRespVO;
import tech.qiantong.qknow.module.kg.controller.admin.knowledge.vo.KgKnowledgeCategorySaveReqVO;
import tech.qiantong.qknow.module.kg.convert.knowledge.KgKnowledgeCategoryConvert;
import tech.qiantong.qknow.module.kg.dal.dataobject.knowledge.KgKnowledgeCategoryDO;
import tech.qiantong.qknow.module.kg.service.knowledge.IKgKnowledgeCategoryService;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * 知识分类Controller
 *
 * @author qknow
 * @date 2025-10-20
 */
@Tag(name = "知识分类")
@RestController
@RequestMapping("/kg/category")
@Validated
public class KgKnowledgeCategoryController extends BaseController {
    @Resource
    private IKgKnowledgeCategoryService kgKnowledgeCategoryService;

    @Operation(summary = "查询知识分类列表")
    @PreAuthorize("@ss.hasPermi('kg:knowledge:category:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<KgKnowledgeCategoryRespVO>> list(KgKnowledgeCategoryPageReqVO kgKnowledgeCategory) {
        PageResult<KgKnowledgeCategoryDO> page = kgKnowledgeCategoryService.getKgKnowledgeCategoryPage(kgKnowledgeCategory);
        return CommonResult.success(BeanUtils.toBean(page, KgKnowledgeCategoryRespVO.class));
    }

    @Operation(summary = "导出知识分类列表")
    @PreAuthorize("@ss.hasPermi('kg:knowledge:category:export')")
    @Log(title = "知识分类", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, KgKnowledgeCategoryPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<KgKnowledgeCategoryDO> list = (List<KgKnowledgeCategoryDO>) kgKnowledgeCategoryService.getKgKnowledgeCategoryPage(exportReqVO).getRows();
        ExcelUtil<KgKnowledgeCategoryRespVO> util = new ExcelUtil<>(KgKnowledgeCategoryRespVO.class);
        util.exportExcel(response, KgKnowledgeCategoryConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入知识分类列表")
    @PreAuthorize("@ss.hasPermi('kg:knowledge:category:import')")
    @Log(title = "知识分类", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<KgKnowledgeCategoryRespVO> util = new ExcelUtil<>(KgKnowledgeCategoryRespVO.class);
        List<KgKnowledgeCategoryRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = kgKnowledgeCategoryService.importKgKnowledgeCategory(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取知识分类详细信息")
    @PreAuthorize("@ss.hasPermi('kg:knowledge:category:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<KgKnowledgeCategoryRespVO> getInfo(@PathVariable("id") Long id) {
        KgKnowledgeCategoryDO kgKnowledgeCategoryDO = kgKnowledgeCategoryService.getKgKnowledgeCategoryById(id);
        return CommonResult.success(BeanUtils.toBean(kgKnowledgeCategoryDO, KgKnowledgeCategoryRespVO.class));
    }

    @Operation(summary = "新增知识分类")
    @PreAuthorize("@ss.hasPermi('kg:knowledge:category:add')")
    @Log(title = "知识分类", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody KgKnowledgeCategorySaveReqVO kgKnowledgeCategory) {
        kgKnowledgeCategory.setWorkspaceId(super.getWorkSpaceId());
        kgKnowledgeCategory.setCreatorId(super.getUserId());
        kgKnowledgeCategory.setCreateBy(super.getUsername());
        return CommonResult.toAjax(kgKnowledgeCategoryService.createKgKnowledgeCategory(kgKnowledgeCategory));
    }

    @Operation(summary = "修改知识分类")
    @PreAuthorize("@ss.hasPermi('kg:knowledge:category:edit')")
    @Log(title = "知识分类", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody KgKnowledgeCategorySaveReqVO kgKnowledgeCategory) {
        return CommonResult.toAjax(kgKnowledgeCategoryService.updateKgKnowledgeCategory(kgKnowledgeCategory));
    }

    @Operation(summary = "删除知识分类")
    @PreAuthorize("@ss.hasPermi('kg:knowledge:category:remove')")
    @Log(title = "知识分类", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(kgKnowledgeCategoryService.removeKgKnowledgeCategory(Arrays.asList(ids)));
    }

    @Operation(summary = "查询全部知识分类")
    @GetMapping("/allList")
    public CommonResult<List<KgKnowledgeCategoryRespVO>> getKmcCategoryAllList(KgKnowledgeCategoryReqVO kgCategoryReq) {
        List<KgKnowledgeCategoryDO> list = kgKnowledgeCategoryService.getKgKnowledgeCategoryList(kgCategoryReq);
        return CommonResult.success(BeanUtils.toBean(list, KgKnowledgeCategoryRespVO.class));
    }
}
