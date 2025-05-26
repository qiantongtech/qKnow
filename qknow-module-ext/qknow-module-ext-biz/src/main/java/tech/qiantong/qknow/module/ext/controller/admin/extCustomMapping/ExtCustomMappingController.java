package tech.qiantong.qknow.module.ext.controller.admin.extCustomMapping;

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
import tech.qiantong.qknow.module.ext.controller.admin.extCustomMapping.vo.ExtCustomMappingPageReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extCustomMapping.vo.ExtCustomMappingRespVO;
import tech.qiantong.qknow.module.ext.controller.admin.extCustomMapping.vo.ExtCustomMappingSaveReqVO;
import tech.qiantong.qknow.module.ext.convert.extCustomMapping.ExtCustomMappingConvert;
import tech.qiantong.qknow.module.ext.dal.dataobject.extCustomMapping.ExtCustomMappingDO;
import tech.qiantong.qknow.module.ext.service.extCustomMapping.IExtCustomMappingService;

/**
 * 自定义映射Controller
 *
 * @author qknow
 * @date 2025-02-25
 */
@Tag(name = "自定义映射")
@RestController
@RequestMapping("/ext/extCustom")
@Validated
public class ExtCustomMappingController extends BaseController {
    @Resource
    private IExtCustomMappingService extCustomMappingService;

    @Operation(summary = "查询自定义映射列表")
    @PreAuthorize("@ss.hasPermi('ext:extCustomMapping:mapping:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<ExtCustomMappingRespVO>> list(ExtCustomMappingPageReqVO extCustomMapping) {
        PageResult<ExtCustomMappingDO> page = extCustomMappingService.getExtCustomMappingPage(extCustomMapping);
        return CommonResult.success(BeanUtils.toBean(page, ExtCustomMappingRespVO.class));
    }

    @Operation(summary = "导出自定义映射列表")
    @PreAuthorize("@ss.hasPermi('ext:extCustomMapping:mapping:export')")
    @Log(title = "自定义映射", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ExtCustomMappingPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ExtCustomMappingDO> list = (List<ExtCustomMappingDO>) extCustomMappingService.getExtCustomMappingPage(exportReqVO).getRows();
        ExcelUtil<ExtCustomMappingRespVO> util = new ExcelUtil<>(ExtCustomMappingRespVO.class);
        util.exportExcel(response, ExtCustomMappingConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入自定义映射列表")
    @PreAuthorize("@ss.hasPermi('ext:extCustomMapping:mapping:import')")
    @Log(title = "自定义映射", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<ExtCustomMappingRespVO> util = new ExcelUtil<>(ExtCustomMappingRespVO.class);
        List<ExtCustomMappingRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = extCustomMappingService.importExtCustomMapping(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取自定义映射详细信息")
    @PreAuthorize("@ss.hasPermi('ext:extCustomMapping:mapping:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<ExtCustomMappingRespVO> getInfo(@PathVariable("id") Long id) {
        ExtCustomMappingDO extCustomMappingDO = extCustomMappingService.getExtCustomMappingById(id);
        return CommonResult.success(BeanUtils.toBean(extCustomMappingDO, ExtCustomMappingRespVO.class));
    }

    @Operation(summary = "新增自定义映射")
    @PreAuthorize("@ss.hasPermi('ext:extCustomMapping:mapping:add')")
    @Log(title = "自定义映射", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody ExtCustomMappingSaveReqVO extCustomMapping) {
        return CommonResult.toAjax(extCustomMappingService.createExtCustomMapping(extCustomMapping));
    }

    @Operation(summary = "修改自定义映射")
    @PreAuthorize("@ss.hasPermi('ext:extCustomMapping:mapping:edit')")
    @Log(title = "自定义映射", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody ExtCustomMappingSaveReqVO extCustomMapping) {
        return CommonResult.toAjax(extCustomMappingService.updateExtCustomMapping(extCustomMapping));
    }

    @Operation(summary = "删除自定义映射")
    @PreAuthorize("@ss.hasPermi('ext:extCustomMapping:mapping:remove')")
    @Log(title = "自定义映射", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(extCustomMappingService.removeExtCustomMapping(Arrays.asList(ids)));
    }

}
