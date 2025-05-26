package tech.qiantong.qknow.module.ext.controller.admin.extSchemaAttribute;

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
import tech.qiantong.qknow.common.core.domain.AjaxResult;
import tech.qiantong.qknow.common.core.page.PageParam;
import tech.qiantong.qknow.common.annotation.Log;
import tech.qiantong.qknow.common.core.controller.BaseController;
import tech.qiantong.qknow.common.core.domain.CommonResult;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.common.enums.BusinessType;
import tech.qiantong.qknow.common.utils.object.BeanUtils;
import tech.qiantong.qknow.common.utils.poi.ExcelUtil;
import tech.qiantong.qknow.common.exception.enums.GlobalErrorCodeConstants;
import tech.qiantong.qknow.module.ext.controller.admin.extSchemaAttribute.vo.ExtSchemaAttributePageReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extSchemaAttribute.vo.ExtSchemaAttributeRespVO;
import tech.qiantong.qknow.module.ext.controller.admin.extSchemaAttribute.vo.ExtSchemaAttributeSaveReqVO;
import tech.qiantong.qknow.module.ext.convert.extSchemaAttribute.ExtSchemaAttributeConvert;
import tech.qiantong.qknow.module.ext.dal.dataobject.extSchemaAttribute.ExtSchemaAttributeDO;
import tech.qiantong.qknow.module.ext.service.extSchemaAttribute.IExtSchemaAttributeService;

/**
 * 概念属性Controller
 *
 * @author qknow
 * @date 2025-02-17
 */
@Tag(name = "概念属性")
@RestController
@RequestMapping("/ext/attribute")
@Validated
public class ExtSchemaAttributeController extends BaseController {
    @Resource
    private IExtSchemaAttributeService extSchemaAttributeService;

    @Operation(summary = "查询概念属性列表")
    @PreAuthorize("@ss.hasPermi('ext:extSchemaAttribute:attribute:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<ExtSchemaAttributeRespVO>> list(ExtSchemaAttributePageReqVO extSchemaAttribute) {
        PageResult<ExtSchemaAttributeDO> page = extSchemaAttributeService.getExtSchemaAttributePage(extSchemaAttribute);
        return CommonResult.success(BeanUtils.toBean(page, ExtSchemaAttributeRespVO.class));
    }

    @Operation(summary = "导出概念属性列表")
    @PreAuthorize("@ss.hasPermi('ext:extSchemaAttribute:attribute:export')")
    @Log(title = "概念属性", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ExtSchemaAttributePageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ExtSchemaAttributeDO> list = (List<ExtSchemaAttributeDO>) extSchemaAttributeService.getExtSchemaAttributePage(exportReqVO).getRows();
        ExcelUtil<ExtSchemaAttributeRespVO> util = new ExcelUtil<>(ExtSchemaAttributeRespVO.class);
        util.exportExcel(response, ExtSchemaAttributeConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入概念属性列表")
    @PreAuthorize("@ss.hasPermi('ext:extSchemaAttribute:attribute:import')")
    @Log(title = "概念属性", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<ExtSchemaAttributeRespVO> util = new ExcelUtil<>(ExtSchemaAttributeRespVO.class);
        List<ExtSchemaAttributeRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = extSchemaAttributeService.importExtSchemaAttribute(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取概念属性详细信息")
    @PreAuthorize("@ss.hasPermi('ext:extSchemaAttribute:attribute:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<ExtSchemaAttributeRespVO> getInfo(@PathVariable("id") Long id) {
        ExtSchemaAttributeDO extSchemaAttributeDO = extSchemaAttributeService.getExtSchemaAttributeById(id);
        return CommonResult.success(BeanUtils.toBean(extSchemaAttributeDO, ExtSchemaAttributeRespVO.class));
    }

    @Operation(summary = "新增概念属性")
    @PreAuthorize("@ss.hasPermi('ext:extSchemaAttribute:attribute:add')")
    @Log(title = "概念属性", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody ExtSchemaAttributeSaveReqVO extSchemaAttribute) {
        extSchemaAttribute.setCreatorId(getUserId());
        extSchemaAttribute.setCreateBy(getNickName());
        extSchemaAttribute.setCreateTime(DateUtil.date());
        extSchemaAttribute.setWorkspaceId(super.getWorkSpaceId());
        return CommonResult.toAjax(extSchemaAttributeService.createExtSchemaAttribute(extSchemaAttribute));
    }

    @Operation(summary = "修改概念属性")
    @PreAuthorize("@ss.hasPermi('ext:extSchemaAttribute:attribute:edit')")
    @Log(title = "概念属性", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody ExtSchemaAttributeSaveReqVO extSchemaAttribute) {
        extSchemaAttribute.setUpdaterId(getUserId());
        extSchemaAttribute.setUpdateBy(getNickName());
        extSchemaAttribute.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(extSchemaAttributeService.updateExtSchemaAttribute(extSchemaAttribute));
    }

    @Operation(summary = "删除概念属性")
    @PreAuthorize("@ss.hasPermi('ext:extSchemaAttribute:attribute:remove')")
    @Log(title = "概念属性", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(extSchemaAttributeService.removeExtSchemaAttribute(Arrays.asList(ids)));
    }


    @Operation(summary = "查询全部概念属性数据")
    @GetMapping("/getAllExtSchemaAttributeList")
    public CommonResult<List<ExtSchemaAttributeDO>> getAllExtSchemaAttributeList(ExtSchemaAttributeDO extSchemaAttributeDO) {
        List<ExtSchemaAttributeDO> list = extSchemaAttributeService.getAllExtSchemaAttributeList(extSchemaAttributeDO);
        return CommonResult.success(list);
    }

}
