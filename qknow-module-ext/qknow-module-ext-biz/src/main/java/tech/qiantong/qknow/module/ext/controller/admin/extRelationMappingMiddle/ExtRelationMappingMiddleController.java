package tech.qiantong.qknow.module.ext.controller.admin.extRelationMappingMiddle;

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
import tech.qiantong.qknow.module.ext.controller.admin.extRelationMappingMiddle.vo.ExtRelationMappingMiddlePageReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extRelationMappingMiddle.vo.ExtRelationMappingMiddleRespVO;
import tech.qiantong.qknow.module.ext.controller.admin.extRelationMappingMiddle.vo.ExtRelationMappingMiddleSaveReqVO;
import tech.qiantong.qknow.module.ext.convert.extRelationMappingMiddle.ExtRelationMappingMiddleConvert;
import tech.qiantong.qknow.module.ext.dal.dataobject.extRelationMappingMiddle.ExtRelationMappingMiddleDO;
import tech.qiantong.qknow.module.ext.service.extRelationMappingMiddle.IExtRelationMappingMiddleService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * 关系映射中间Controller
 *
 * @author qknow
 * @date 2025-12-16
 */
@Tag(name = "关系映射中间")
@RestController
@RequestMapping("/ext/extRelationMiddle")
@Validated
public class ExtRelationMappingMiddleController extends BaseController {
    @Resource
    private IExtRelationMappingMiddleService extRelationMappingMiddleService;

    @Operation(summary = "查询关系映射中间列表")
    @PreAuthorize("@ss.hasPermi('ext:extRelationMappingMiddle:relationmiddle:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<ExtRelationMappingMiddleRespVO>> list(ExtRelationMappingMiddlePageReqVO extRelationMappingMiddle) {
        PageResult<ExtRelationMappingMiddleDO> page = extRelationMappingMiddleService.getExtRelationMappingMiddlePage(extRelationMappingMiddle);
        return CommonResult.success(BeanUtils.toBean(page, ExtRelationMappingMiddleRespVO.class));
    }

    @Operation(summary = "导出关系映射中间列表")
    @PreAuthorize("@ss.hasPermi('ext:extRelationMappingMiddle:relationmiddle:export')")
    @Log(title = "关系映射中间", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ExtRelationMappingMiddlePageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ExtRelationMappingMiddleDO> list = (List<ExtRelationMappingMiddleDO>) extRelationMappingMiddleService.getExtRelationMappingMiddlePage(exportReqVO).getRows();
        ExcelUtil<ExtRelationMappingMiddleRespVO> util = new ExcelUtil<>(ExtRelationMappingMiddleRespVO.class);
        util.exportExcel(response, ExtRelationMappingMiddleConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入关系映射中间列表")
    @PreAuthorize("@ss.hasPermi('ext:extRelationMappingMiddle:relationmiddle:import')")
    @Log(title = "关系映射中间", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<ExtRelationMappingMiddleRespVO> util = new ExcelUtil<>(ExtRelationMappingMiddleRespVO.class);
        List<ExtRelationMappingMiddleRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = extRelationMappingMiddleService.importExtRelationMappingMiddle(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取关系映射中间详细信息")
    @PreAuthorize("@ss.hasPermi('ext:extRelationMappingMiddle:relationmiddle:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<ExtRelationMappingMiddleRespVO> getInfo(@PathVariable("id") Long id) {
        ExtRelationMappingMiddleDO extRelationMappingMiddleDO = extRelationMappingMiddleService.getExtRelationMappingMiddleById(id);
        return CommonResult.success(BeanUtils.toBean(extRelationMappingMiddleDO, ExtRelationMappingMiddleRespVO.class));
    }

    @Operation(summary = "新增关系映射中间")
    @PreAuthorize("@ss.hasPermi('ext:extRelationMappingMiddle:relationmiddle:add')")
    @Log(title = "关系映射中间", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody ExtRelationMappingMiddleSaveReqVO extRelationMappingMiddle) {
        return CommonResult.toAjax(extRelationMappingMiddleService.createExtRelationMappingMiddle(extRelationMappingMiddle));
    }

    @Operation(summary = "修改关系映射中间")
    @PreAuthorize("@ss.hasPermi('ext:extRelationMappingMiddle:relationmiddle:edit')")
    @Log(title = "关系映射中间", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody ExtRelationMappingMiddleSaveReqVO extRelationMappingMiddle) {
        return CommonResult.toAjax(extRelationMappingMiddleService.updateExtRelationMappingMiddle(extRelationMappingMiddle));
    }

    @Operation(summary = "删除关系映射中间")
    @PreAuthorize("@ss.hasPermi('ext:extRelationMappingMiddle:relationmiddle:remove')")
    @Log(title = "关系映射中间", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(extRelationMappingMiddleService.removeExtRelationMappingMiddle(Arrays.asList(ids)));
    }

}
