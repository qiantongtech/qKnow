package tech.qiantong.qknow.module.ext.controller.admin.extDatasource;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.qiantong.qknow.common.core.controller.BaseController;
import tech.qiantong.qknow.common.core.domain.AjaxResult;
import tech.qiantong.qknow.module.ext.dal.dataobject.extDatasource.ExtDataSourceTable;
import tech.qiantong.qknow.module.ext.service.extDatasource.IExtDatasourceService;

import javax.annotation.Resource;

/**
 * 数据源Controller
 *
 * @author qknow
 * @date 2025-02-25
 */
@Tag(name = "数据源")
@RestController
@RequestMapping("/ext/datasource")
@Validated
public class ExtDatasourceController extends BaseController {
    @Resource
    private IExtDatasourceService extDatasourceService;

    /**
     * 根据数据源id, 数据id和表名获取行数据
     *
     * @param sourceTable
     * @return
     */
    @GetMapping("getTableDataByDataId")
    public AjaxResult getTableDataByDataId(ExtDataSourceTable sourceTable) {
        return extDatasourceService.getTableDataByDataId(sourceTable);
    }


//    /**
//     * 获取数据库表列表
//     *
//     * @param extDatasource
//     * @return
//     */
//    @GetMapping("getTableList")
//    public AjaxResult getTableList(ExtDatasource extDatasource) {
//        return extDatasourceService.getTableList(extDatasource);
//    }
//
//    /**
//     * 获取表结构详细信息
//     *
//     * @param sourceTable
//     * @return
//     */
//    @GetMapping("getTableData")
//    public AjaxResult getTableData(ExtDataSourceTable sourceTable) {
//        return extDatasourceService.getTableData(sourceTable);
//    }
//    /**
//     * 测试数据库连接
//     *
//     * @param id
//     * @return
//     */
//    @GetMapping("testConnection")
//    public AjaxResult testConnection(Long id) {
//        if (id == null) return AjaxResult.error("id为空");
//        return extDatasourceService.testConnection(id);
//    }
//
//    @Operation(summary = "查询数据源列表")
//    @PreAuthorize("@ss.hasPermi('ext:extDatasource:datasource:list')")
//    @GetMapping("/list")
//    public CommonResult<PageResult<ExtDatasourceRespVO>> list(ExtDatasourcePageReqVO extDatasource) {
//        PageResult<ExtDatasourceDO> page = extDatasourceService.getExtDatasourcePage(extDatasource);
//        return CommonResult.success(BeanUtils.toBean(page, ExtDatasourceRespVO.class));
//    }
//
//    @Operation(summary = "导出数据源列表")
//    @PreAuthorize("@ss.hasPermi('ext:extDatasource:datasource:export')")
//    @Log(title = "数据源", businessType = BusinessType.EXPORT)
//    @PostMapping("/export")
//    public void export(HttpServletResponse response, ExtDatasourcePageReqVO exportReqVO) {
//        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
//        List<ExtDatasourceDO> list = (List<ExtDatasourceDO>) extDatasourceService.getExtDatasourcePage(exportReqVO).getRows();
//        ExcelUtil<ExtDatasourceRespVO> util = new ExcelUtil<>(ExtDatasourceRespVO.class);
//        util.exportExcel(response, ExtDatasourceConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
//    }
//
//    @Operation(summary = "导入数据源列表")
//    @PreAuthorize("@ss.hasPermi('ext:extDatasource:datasource:import')")
//    @Log(title = "数据源", businessType = BusinessType.IMPORT)
//    @PostMapping("/importData")
//    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
//        ExcelUtil<ExtDatasourceRespVO> util = new ExcelUtil<>(ExtDatasourceRespVO.class);
//        List<ExtDatasourceRespVO> importExcelList = util.importExcel(file.getInputStream());
//        String operName = getUsername();
//        String message = extDatasourceService.importExtDatasource(importExcelList, updateSupport, operName);
//        return success(message);
//    }
//
//    @Operation(summary = "获取数据源详细信息")
//    @PreAuthorize("@ss.hasPermi('ext:extDatasource:datasource:query')")
//    @GetMapping(value = "/{id}")
//    public CommonResult<ExtDatasourceRespVO> getInfo(@PathVariable("id") Long id) {
//        ExtDatasourceDO extDatasourceDO = extDatasourceService.getExtDatasourceById(id);
//        return CommonResult.success(BeanUtils.toBean(extDatasourceDO, ExtDatasourceRespVO.class));
//    }
//
//    @Operation(summary = "新增数据源")
//    @PreAuthorize("@ss.hasPermi('ext:extDatasource:datasource:add')")
//    @Log(title = "数据源", businessType = BusinessType.INSERT)
//    @PostMapping
//    public CommonResult<Long> add(@Valid @RequestBody ExtDatasourceSaveReqVO extDatasource) {
//        extDatasource.setStatus(0);
//        return CommonResult.toAjax(extDatasourceService.createExtDatasource(extDatasource));
//    }
//
//    @Operation(summary = "修改数据源")
//    @PreAuthorize("@ss.hasPermi('ext:extDatasource:datasource:edit')")
//    @Log(title = "数据源", businessType = BusinessType.UPDATE)
//    @PutMapping
//    public CommonResult<Integer> edit(@Valid @RequestBody ExtDatasourceSaveReqVO extDatasource) {
//        return CommonResult.toAjax(extDatasourceService.updateExtDatasource(extDatasource));
//    }
//
//    @Operation(summary = "删除数据源")
//    @PreAuthorize("@ss.hasPermi('ext:extDatasource:datasource:remove')")
//    @Log(title = "数据源", businessType = BusinessType.DELETE)
//    @DeleteMapping("/{ids}")
//    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
//        return CommonResult.toAjax(extDatasourceService.removeExtDatasource(Arrays.asList(ids)));
//    }

}
