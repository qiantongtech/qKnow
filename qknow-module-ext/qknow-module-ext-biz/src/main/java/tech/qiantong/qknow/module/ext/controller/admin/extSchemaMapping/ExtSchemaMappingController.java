package tech.qiantong.qknow.module.ext.controller.admin.extSchemaMapping;

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
import tech.qiantong.qknow.common.core.page.PageParam;
import tech.qiantong.qknow.common.core.domain.AjaxResult;
import tech.qiantong.qknow.common.annotation.Log;
import tech.qiantong.qknow.common.core.controller.BaseController;
import tech.qiantong.qknow.common.core.domain.CommonResult;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.common.enums.BusinessType;
import tech.qiantong.qknow.common.utils.object.BeanUtils;
import tech.qiantong.qknow.common.utils.poi.ExcelUtil;
import tech.qiantong.qknow.common.exception.enums.GlobalErrorCodeConstants;
import tech.qiantong.qknow.module.ext.controller.admin.extSchemaMapping.vo.ExtSchemaMappingPageReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extSchemaMapping.vo.ExtSchemaMappingRespVO;
import tech.qiantong.qknow.module.ext.controller.admin.extSchemaMapping.vo.ExtSchemaMappingSaveReqVO;
import tech.qiantong.qknow.module.ext.convert.extSchemaMapping.ExtSchemaMappingConvert;
import tech.qiantong.qknow.module.ext.dal.dataobject.extSchemaMapping.ExtSchemaMappingDO;
import tech.qiantong.qknow.module.ext.service.extSchemaMapping.IExtSchemaMappingService;

/**
 * 概念映射Controller
 *
 * @author qknow
 * @date 2025-02-25
 */
@Tag(name = "概念映射")
@RestController
@RequestMapping("/ext/extSchema")
@Validated
public class ExtSchemaMappingController extends BaseController {
    @Resource
    private IExtSchemaMappingService extSchemaMappingService;

    @Operation(summary = "查询概念映射列表")
    @PreAuthorize("@ss.hasPermi('ext:extSchemaMapping:schema:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<ExtSchemaMappingRespVO>> list(ExtSchemaMappingPageReqVO extSchemaMapping) {
        PageResult<ExtSchemaMappingDO> page = extSchemaMappingService.getExtSchemaMappingPage(extSchemaMapping);
        return CommonResult.success(BeanUtils.toBean(page, ExtSchemaMappingRespVO.class));
    }

    @Operation(summary = "导出概念映射列表")
    @PreAuthorize("@ss.hasPermi('ext:extSchemaMapping:schema:export')")
    @Log(title = "概念映射", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ExtSchemaMappingPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ExtSchemaMappingDO> list = (List<ExtSchemaMappingDO>) extSchemaMappingService.getExtSchemaMappingPage(exportReqVO).getRows();
        ExcelUtil<ExtSchemaMappingRespVO> util = new ExcelUtil<>(ExtSchemaMappingRespVO.class);
        util.exportExcel(response, ExtSchemaMappingConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入概念映射列表")
    @PreAuthorize("@ss.hasPermi('ext:extSchemaMapping:schema:import')")
    @Log(title = "概念映射", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<ExtSchemaMappingRespVO> util = new ExcelUtil<>(ExtSchemaMappingRespVO.class);
        List<ExtSchemaMappingRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = extSchemaMappingService.importExtSchemaMapping(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取概念映射详细信息")
    @PreAuthorize("@ss.hasPermi('ext:extSchemaMapping:schema:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<ExtSchemaMappingRespVO> getInfo(@PathVariable("id") Long id) {
        ExtSchemaMappingDO extSchemaMappingDO = extSchemaMappingService.getExtSchemaMappingById(id);
        return CommonResult.success(BeanUtils.toBean(extSchemaMappingDO, ExtSchemaMappingRespVO.class));
    }

    @Operation(summary = "新增概念映射")
    @PreAuthorize("@ss.hasPermi('ext:extSchemaMapping:schema:add')")
    @Log(title = "概念映射", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody ExtSchemaMappingSaveReqVO extSchemaMapping) {
        return CommonResult.toAjax(extSchemaMappingService.createExtSchemaMapping(extSchemaMapping));
    }

    @Operation(summary = "修改概念映射")
    @PreAuthorize("@ss.hasPermi('ext:extSchemaMapping:schema:edit')")
    @Log(title = "概念映射", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody ExtSchemaMappingSaveReqVO extSchemaMapping) {
        return CommonResult.toAjax(extSchemaMappingService.updateExtSchemaMapping(extSchemaMapping));
    }

    @Operation(summary = "删除概念映射")
    @PreAuthorize("@ss.hasPermi('ext:extSchemaMapping:schema:remove')")
    @Log(title = "概念映射", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(extSchemaMappingService.removeExtSchemaMapping(Arrays.asList(ids)));
    }

}
