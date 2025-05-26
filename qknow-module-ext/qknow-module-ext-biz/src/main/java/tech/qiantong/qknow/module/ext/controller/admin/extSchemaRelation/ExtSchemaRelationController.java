package tech.qiantong.qknow.module.ext.controller.admin.extSchemaRelation;

import cn.hutool.core.date.DateUtil;
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
import tech.qiantong.qknow.module.ext.controller.admin.extSchemaRelation.vo.ExtSchemaRelationPageReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extSchemaRelation.vo.ExtSchemaRelationRespVO;
import tech.qiantong.qknow.module.ext.controller.admin.extSchemaRelation.vo.ExtSchemaRelationSaveReqVO;
import tech.qiantong.qknow.module.ext.convert.extSchemaRelation.ExtSchemaRelationConvert;
import tech.qiantong.qknow.module.ext.dal.dataobject.extSchemaRelation.ExtSchemaRelationDO;
import tech.qiantong.qknow.module.ext.service.extSchemaRelation.IExtSchemaRelationService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * 关系配置Controller
 *
 * @author qknow
 * @date 2025-02-18
 */
@Tag(name = "关系配置")
@RestController
@RequestMapping("/ext/relation")
@Validated
public class ExtSchemaRelationController extends BaseController {
    @Resource
    private IExtSchemaRelationService extSchemaRelationService;

    @Operation(summary = "查询关系配置列表")
    @PreAuthorize("@ss.hasPermi('ext:extSchemaRelation:relation:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<ExtSchemaRelationRespVO>> list(ExtSchemaRelationPageReqVO extSchemaRelation) {
        PageResult<ExtSchemaRelationDO> page = extSchemaRelationService.getExtSchemaRelationPage(extSchemaRelation);
        return CommonResult.success(BeanUtils.toBean(page, ExtSchemaRelationRespVO.class));
    }

    @Operation(summary = "导出关系配置列表")
    @PreAuthorize("@ss.hasPermi('ext:extSchemaRelation:relation:export')")
    @Log(title = "关系配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ExtSchemaRelationPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ExtSchemaRelationDO> list = (List<ExtSchemaRelationDO>) extSchemaRelationService.getExtSchemaRelationPage(exportReqVO).getRows();
        ExcelUtil<ExtSchemaRelationRespVO> util = new ExcelUtil<>(ExtSchemaRelationRespVO.class);
        util.exportExcel(response, ExtSchemaRelationConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入关系配置列表")
    @PreAuthorize("@ss.hasPermi('ext:extSchemaRelation:relation:import')")
    @Log(title = "关系配置", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<ExtSchemaRelationRespVO> util = new ExcelUtil<>(ExtSchemaRelationRespVO.class);
        List<ExtSchemaRelationRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = extSchemaRelationService.importExtSchemaRelation(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取关系配置详细信息")
    @PreAuthorize("@ss.hasPermi('ext:extSchemaRelation:relation:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<ExtSchemaRelationRespVO> getInfo(@PathVariable("id") Long id) {
        ExtSchemaRelationDO extSchemaRelationDO = extSchemaRelationService.getExtSchemaRelationById(id);
        return CommonResult.success(BeanUtils.toBean(extSchemaRelationDO, ExtSchemaRelationRespVO.class));
    }

    @Operation(summary = "根据id集合获取详细信息")
    @PreAuthorize("@ss.hasPermi('ext:extSchemaRelation:relation:query')")
    @GetMapping(value = "/listByIds/{ids}")
    public CommonResult<List<ExtSchemaRelationRespVO>> listByIds(@PathVariable Long[] ids) {
        List<ExtSchemaRelationDO> schemaRelationDOList = extSchemaRelationService.listByIds(Arrays.asList(ids));
        return CommonResult.success(BeanUtils.toBean(schemaRelationDOList, ExtSchemaRelationRespVO.class));
    }

    @Operation(summary = "新增关系配置")
    @PreAuthorize("@ss.hasPermi('ext:extSchemaRelation:relation:add')")
    @Log(title = "关系配置", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody ExtSchemaRelationSaveReqVO extSchemaRelation) {
        extSchemaRelation.setWorkspaceId(super.getWorkSpaceId());
        extSchemaRelation.setCreatorId(getUserId());
        extSchemaRelation.setCreateBy(getNickName());
        extSchemaRelation.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(extSchemaRelationService.createExtSchemaRelation(extSchemaRelation));
    }

    @Operation(summary = "修改关系配置")
    @PreAuthorize("@ss.hasPermi('ext:extSchemaRelation:relation:edit')")
    @Log(title = "关系配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody ExtSchemaRelationSaveReqVO extSchemaRelation) {
        extSchemaRelation.setUpdaterId(getUserId());
        extSchemaRelation.setUpdateBy(getNickName());
        extSchemaRelation.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(extSchemaRelationService.updateExtSchemaRelation(extSchemaRelation));
    }

    @Operation(summary = "删除关系配置")
    @PreAuthorize("@ss.hasPermi('ext:extSchemaRelation:relation:remove')")
    @Log(title = "关系配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(extSchemaRelationService.removeExtSchemaRelation(Arrays.asList(ids)));
    }

}
