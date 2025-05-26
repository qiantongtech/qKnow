package tech.qiantong.qknow.module.ext.controller.admin.extRelationMapping;

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
import tech.qiantong.qknow.module.ext.controller.admin.extRelationMapping.vo.ExtRelationMappingPageReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extRelationMapping.vo.ExtRelationMappingRespVO;
import tech.qiantong.qknow.module.ext.controller.admin.extRelationMapping.vo.ExtRelationMappingSaveReqVO;
import tech.qiantong.qknow.module.ext.convert.extRelationMapping.ExtRelationMappingConvert;
import tech.qiantong.qknow.module.ext.dal.dataobject.extRelationMapping.ExtRelationMappingDO;
import tech.qiantong.qknow.module.ext.service.extRelationMapping.IExtRelationMappingService;

/**
 * 关系映射Controller
 *
 * @author qknow
 * @date 2025-02-25
 */
@Tag(name = "关系映射")
@RestController
@RequestMapping("/ext/extRelation")
@Validated
public class ExtRelationMappingController extends BaseController {
    @Resource
    private IExtRelationMappingService extRelationMappingService;

    @Operation(summary = "查询关系映射列表")
    @PreAuthorize("@ss.hasPermi('ext:extRelationMapping:relation:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<ExtRelationMappingRespVO>> list(ExtRelationMappingPageReqVO extRelationMapping) {
        PageResult<ExtRelationMappingDO> page = extRelationMappingService.getExtRelationMappingPage(extRelationMapping);
        return CommonResult.success(BeanUtils.toBean(page, ExtRelationMappingRespVO.class));
    }

    @Operation(summary = "导出关系映射列表")
    @PreAuthorize("@ss.hasPermi('ext:extRelationMapping:relation:export')")
    @Log(title = "关系映射", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ExtRelationMappingPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ExtRelationMappingDO> list = (List<ExtRelationMappingDO>) extRelationMappingService.getExtRelationMappingPage(exportReqVO).getRows();
        ExcelUtil<ExtRelationMappingRespVO> util = new ExcelUtil<>(ExtRelationMappingRespVO.class);
        util.exportExcel(response, ExtRelationMappingConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入关系映射列表")
    @PreAuthorize("@ss.hasPermi('ext:extRelationMapping:relation:import')")
    @Log(title = "关系映射", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<ExtRelationMappingRespVO> util = new ExcelUtil<>(ExtRelationMappingRespVO.class);
        List<ExtRelationMappingRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = extRelationMappingService.importExtRelationMapping(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取关系映射详细信息")
    @PreAuthorize("@ss.hasPermi('ext:extRelationMapping:relation:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<ExtRelationMappingRespVO> getInfo(@PathVariable("id") Long id) {
        ExtRelationMappingDO extRelationMappingDO = extRelationMappingService.getExtRelationMappingById(id);
        return CommonResult.success(BeanUtils.toBean(extRelationMappingDO, ExtRelationMappingRespVO.class));
    }

    @Operation(summary = "新增关系映射")
    @PreAuthorize("@ss.hasPermi('ext:extRelationMapping:relation:add')")
    @Log(title = "关系映射", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody ExtRelationMappingSaveReqVO extRelationMapping) {
        return CommonResult.toAjax(extRelationMappingService.createExtRelationMapping(extRelationMapping));
    }

    @Operation(summary = "修改关系映射")
    @PreAuthorize("@ss.hasPermi('ext:extRelationMapping:relation:edit')")
    @Log(title = "关系映射", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody ExtRelationMappingSaveReqVO extRelationMapping) {
        return CommonResult.toAjax(extRelationMappingService.updateExtRelationMapping(extRelationMapping));
    }

    @Operation(summary = "删除关系映射")
    @PreAuthorize("@ss.hasPermi('ext:extRelationMapping:relation:remove')")
    @Log(title = "关系映射", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(extRelationMappingService.removeExtRelationMapping(Arrays.asList(ids)));
    }

}
