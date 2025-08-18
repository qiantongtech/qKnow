package tech.qiantong.qknow.module.ext.controller.admin.extAttributeMapping;

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
import tech.qiantong.qknow.module.ext.controller.admin.extAttributeMapping.vo.ExtAttributeMappingPageReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extAttributeMapping.vo.ExtAttributeMappingRespVO;
import tech.qiantong.qknow.module.ext.controller.admin.extAttributeMapping.vo.ExtAttributeMappingSaveReqVO;
import tech.qiantong.qknow.module.ext.convert.extAttributeMapping.ExtAttributeMappingConvert;
import tech.qiantong.qknow.module.ext.dal.dataobject.extAttributeMapping.ExtAttributeMappingDO;
import tech.qiantong.qknow.module.ext.service.extAttributeMapping.IExtAttributeMappingService;

/**
 * 属性映射Controller
 *
 * @author qknow
 * @date 2025-02-25
 */
@Tag(name = "属性映射")
@RestController
@RequestMapping("/ext/extAttribute")
@Validated
public class ExtAttributeMappingController extends BaseController {
    @Resource
    private IExtAttributeMappingService extAttributeMappingService;

    @Operation(summary = "查询属性映射列表")
    @PreAuthorize("@ss.hasPermi('ext:extAttributeMapping:mapping:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<ExtAttributeMappingRespVO>> list(ExtAttributeMappingPageReqVO extAttributeMapping) {
        PageResult<ExtAttributeMappingDO> page = extAttributeMappingService.getExtAttributeMappingPage(extAttributeMapping);
        return CommonResult.success(BeanUtils.toBean(page, ExtAttributeMappingRespVO.class));
    }

    @Operation(summary = "导出属性映射列表")
    @PreAuthorize("@ss.hasPermi('ext:extAttributeMapping:mapping:export')")
    @Log(title = "属性映射", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ExtAttributeMappingPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ExtAttributeMappingDO> list = (List<ExtAttributeMappingDO>) extAttributeMappingService.getExtAttributeMappingPage(exportReqVO).getRows();
        ExcelUtil<ExtAttributeMappingRespVO> util = new ExcelUtil<>(ExtAttributeMappingRespVO.class);
        util.exportExcel(response, ExtAttributeMappingConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入属性映射列表")
    @PreAuthorize("@ss.hasPermi('ext:extAttributeMapping:mapping:import')")
    @Log(title = "属性映射", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<ExtAttributeMappingRespVO> util = new ExcelUtil<>(ExtAttributeMappingRespVO.class);
        List<ExtAttributeMappingRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = extAttributeMappingService.importExtAttributeMapping(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取属性映射详细信息")
    @PreAuthorize("@ss.hasPermi('ext:extAttributeMapping:mapping:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<ExtAttributeMappingRespVO> getInfo(@PathVariable("id") Long id) {
        ExtAttributeMappingDO extAttributeMappingDO = extAttributeMappingService.getExtAttributeMappingById(id);
        return CommonResult.success(BeanUtils.toBean(extAttributeMappingDO, ExtAttributeMappingRespVO.class));
    }

    @Operation(summary = "新增属性映射")
    @PreAuthorize("@ss.hasPermi('ext:extAttributeMapping:mapping:add')")
    @Log(title = "属性映射", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody ExtAttributeMappingSaveReqVO extAttributeMapping) {
        return CommonResult.toAjax(extAttributeMappingService.createExtAttributeMapping(extAttributeMapping));
    }

    @Operation(summary = "修改属性映射")
    @PreAuthorize("@ss.hasPermi('ext:extAttributeMapping:mapping:edit')")
    @Log(title = "属性映射", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody ExtAttributeMappingSaveReqVO extAttributeMapping) {
        return CommonResult.toAjax(extAttributeMappingService.updateExtAttributeMapping(extAttributeMapping));
    }

    @Operation(summary = "删除属性映射")
    @PreAuthorize("@ss.hasPermi('ext:extAttributeMapping:mapping:remove')")
    @Log(title = "属性映射", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(extAttributeMappingService.removeExtAttributeMapping(Arrays.asList(ids)));
    }

}
