package tech.qiantong.qknow.module.dm.controller.admin.datasource;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;
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
import tech.qiantong.qknow.common.database.core.DbTable;
import tech.qiantong.qknow.common.enums.BusinessType;
import tech.qiantong.qknow.common.utils.object.BeanUtils;
import tech.qiantong.qknow.common.utils.poi.ExcelUtil;
import tech.qiantong.qknow.module.dm.api.datasource.dto.DatasourceCreaTeTableReqDTO;
import tech.qiantong.qknow.module.dm.api.model.dto.DmModelColumnReqDTO;
import tech.qiantong.qknow.module.dm.controller.admin.datasource.vo.DmDatasourcePageReqVO;
import tech.qiantong.qknow.module.dm.controller.admin.datasource.vo.DmDatasourceRespVO;
import tech.qiantong.qknow.module.dm.controller.admin.datasource.vo.DmDatasourceSaveReqVO;
import tech.qiantong.qknow.module.dm.convert.datasource.DmDatasourceConvert;
import tech.qiantong.qknow.module.dm.dal.dataobject.datasource.DmDatasourceDO;
import tech.qiantong.qknow.module.dm.service.datasource.IDmDatasourceService;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 数据源Controller
 *
 * @author lhs
 * @date 2025-01-21
 */
@Tag(name = "数据源")
@RestController
@RequestMapping("/dm/dmDatasource")
@Validated
public class DmDatasourceController extends BaseController {
    @Resource
    private IDmDatasourceService daDatasourceService;

    @Operation(summary = "查询数据源列表")
    @PreAuthorize("@ss.hasPermi('dm:datasource:datasource:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<DmDatasourceRespVO>> list(DmDatasourcePageReqVO daDatasource) {
        PageResult<DmDatasourceDO> page = daDatasourceService.getDmDatasourcePage(daDatasource);
        return CommonResult.success(BeanUtils.toBean(page, DmDatasourceRespVO.class));
    }

    @Operation(summary = "查询数据资产的数据源连接信息")
    @PreAuthorize("@ss.hasPermi('dm:datasource:datasource:list')")
    @GetMapping("/getDataSourceByAsset")
    public AjaxResult getDataSourceByAsset(DmDatasourceRespVO daAsset) {
        List<DmDatasourceDO> daAssetDOS = daDatasourceService.getDataSourceByAsset(daAsset);
        return AjaxResult.success(daAssetDOS);
    }

    @Operation(summary = "查询数据源列表")
    @PreAuthorize("@ss.hasPermi('dm:datasource:datasource:list')")
    @GetMapping("/getDaDatasourceList")
    public CommonResult<List<DmDatasourceRespVO>> getDaDatasourceList(DmDatasourcePageReqVO daDatasource) {
        List<DmDatasourceDO> page = daDatasourceService.getDaDatasourceList(daDatasource);
        return CommonResult.success(BeanUtils.toBean(page, DmDatasourceRespVO.class));
    }

    @Operation(summary = "导出数据源列表")
    @PreAuthorize("@ss.hasPermi('dm:datasource:datasource:export')")
    @Log(title = "数据源", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DmDatasourcePageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DmDatasourceDO> list = (List<DmDatasourceDO>) daDatasourceService.getDmDatasourcePage(exportReqVO).getRows();
        ExcelUtil<DmDatasourceRespVO> util = new ExcelUtil<>(DmDatasourceRespVO.class);
        util.exportExcel(response, DmDatasourceConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入数据源列表")
    @PreAuthorize("@ss.hasPermi('dm:datasource:datasource:import')")
    @Log(title = "数据源", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<DmDatasourceRespVO> util = new ExcelUtil<>(DmDatasourceRespVO.class);
        List<DmDatasourceRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = daDatasourceService.importDaDatasource(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取数据源详细信息")
    @PreAuthorize("@ss.hasPermi('dm:datasource:datasource:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<DmDatasourceRespVO> getInfo(@PathVariable("id") Long id) {
        DmDatasourceDO daDatasourceDO = daDatasourceService.getDaDatasourceById(id);
        return CommonResult.success(BeanUtils.toBean(daDatasourceDO, DmDatasourceRespVO.class));
    }

    @Operation(summary = "新增数据源")
    @PreAuthorize("@ss.hasPermi('dm:datasource:datasource:add')")
    @Log(title = "数据源", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody DmDatasourceSaveReqVO daDatasource) {
        daDatasource.setCreatorId(getUserId());
        daDatasource.setCreateBy(getNickName());
        daDatasource.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(daDatasourceService.createDaDatasource(daDatasource));
    }

    @Operation(summary = "修改数据源")
    @PreAuthorize("@ss.hasPermi('dm:datasource:datasource:edit')")
    @Log(title = "数据源", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody DmDatasourceSaveReqVO daDatasource) {
        daDatasource.setUpdaterId(getUserId());
        daDatasource.setUpdateBy(getNickName());
        daDatasource.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(daDatasourceService.updateDaDatasource(daDatasource));
    }

    @Operation(summary = "删除数据源")
    @PreAuthorize("@ss.hasPermi('dm:datasource:datasource:remove')")
    @Log(title = "数据源", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(daDatasourceService.removeDaDatasource(Arrays.asList(ids)));
    }

    @Operation(summary = "测试连接")
    @PreAuthorize("@ss.hasPermi('dm:datasource:datasource:remove')")
    @Log(title = "测试连接", businessType = BusinessType.DELETE)
    @GetMapping("clientsTest/{id}")
    public AjaxResult clientsTest(@PathVariable("id") Long ids) {
        return daDatasourceService.clientsTest(ids);
    }


    @Operation(summary = "获取数据源里面的数据表")
    @PreAuthorize("@ss.hasPermi('dm:datasource:datasource:query')")
    @GetMapping(value = "/tableList/{id}")
    public AjaxResult getTableList(@PathVariable("id") Long id) {
        List<DbTable> tables = daDatasourceService.getDbTables(id);
        return success(tables);
    }

    @Operation(summary = "获取数据源里面的数据表的数据字段")
    @PreAuthorize("@ss.hasPermi('dm:datasource:datasource:query')")
    @PostMapping(value = "/columnsList")
    public AjaxResult getColumnsList(@RequestBody JSONObject jsonObject) {
        List<DmModelColumnReqDTO> columns = daDatasourceService.getColumnsList(jsonObject);
        return success(columns);
    }

    @Operation(summary = "创建任务临时表")
    @PostMapping(value = "/createTaskTempTable")
    public AjaxResult createTaskTempTable(@RequestBody Map<String,Object> map) {
        DatasourceCreaTeTableReqDTO daDatasource = daDatasourceService.createTaskTempTable(map);
        return success(daDatasource);
    }
}
