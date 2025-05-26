package tech.qiantong.qknow.module.dm.controller.admin.dmMeasurePoint;

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
import tech.qiantong.qknow.module.dm.controller.admin.dmMeasurePoint.vo.DmMeasurePointPageReqVO;
import tech.qiantong.qknow.module.dm.controller.admin.dmMeasurePoint.vo.DmMeasurePointRespVO;
import tech.qiantong.qknow.module.dm.controller.admin.dmMeasurePoint.vo.DmMeasurePointSaveReqVO;
import tech.qiantong.qknow.module.dm.convert.dmMeasurePoint.DmMeasurePointConvert;
import tech.qiantong.qknow.module.dm.dal.dataobject.dmMeasurePoint.DmMeasurePointDO;
import tech.qiantong.qknow.module.dm.service.dmMeasurePoint.IDmMeasurePointService;

/**
 * 物联网测点Controller
 *
 * @author qknow
 * @date 2025-02-20
 */
@Tag(name = "物联网测点")
@RestController
@RequestMapping("/dm/point")
@Validated
public class DmMeasurePointController extends BaseController {
    @Resource
    private IDmMeasurePointService dmMeasurePointService;

    @Operation(summary = "查询物联网测点列表")
    @PreAuthorize("@ss.hasPermi('dm:dmMeasurePoint:point:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<DmMeasurePointRespVO>> list(DmMeasurePointPageReqVO dmMeasurePoint) {
        PageResult<DmMeasurePointDO> page = dmMeasurePointService.getDmMeasurePointPage(dmMeasurePoint);
        return CommonResult.success(BeanUtils.toBean(page, DmMeasurePointRespVO.class));
    }

    @Operation(summary = "导出物联网测点列表")
    @PreAuthorize("@ss.hasPermi('dm:dmMeasurePoint:point:export')")
    @Log(title = "物联网测点", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DmMeasurePointPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DmMeasurePointDO> list = (List<DmMeasurePointDO>) dmMeasurePointService.getDmMeasurePointPage(exportReqVO).getRows();
        ExcelUtil<DmMeasurePointRespVO> util = new ExcelUtil<>(DmMeasurePointRespVO.class);
        util.exportExcel(response, DmMeasurePointConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入物联网测点列表")
    @PreAuthorize("@ss.hasPermi('dm:dmMeasurePoint:point:import')")
    @Log(title = "物联网测点", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<DmMeasurePointRespVO> util = new ExcelUtil<>(DmMeasurePointRespVO.class);
        List<DmMeasurePointRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = dmMeasurePointService.importDmMeasurePoint(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取物联网测点详细信息")
    @PreAuthorize("@ss.hasPermi('dm:dmMeasurePoint:point:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<DmMeasurePointRespVO> getInfo(@PathVariable("id") Long id) {
        DmMeasurePointDO dmMeasurePointDO = dmMeasurePointService.getDmMeasurePointById(id);
        return CommonResult.success(BeanUtils.toBean(dmMeasurePointDO, DmMeasurePointRespVO.class));
    }

    @Operation(summary = "新增物联网测点")
    @PreAuthorize("@ss.hasPermi('dm:dmMeasurePoint:point:add')")
    @Log(title = "物联网测点", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody DmMeasurePointSaveReqVO dmMeasurePoint) {
        dmMeasurePoint.setCreatorId(getUserId());
        dmMeasurePoint.setCreateBy(getNickName());
        dmMeasurePoint.setCreateTime(DateUtil.date());
        dmMeasurePoint.setWorkspaceId(super.getWorkSpaceId());
        return CommonResult.toAjax(dmMeasurePointService.createDmMeasurePoint(dmMeasurePoint));
    }

    @Operation(summary = "修改物联网测点")
    @PreAuthorize("@ss.hasPermi('dm:dmMeasurePoint:point:edit')")
    @Log(title = "物联网测点", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody DmMeasurePointSaveReqVO dmMeasurePoint) {
        dmMeasurePoint.setUpdaterId(getUserId());
        dmMeasurePoint.setUpdateBy(getNickName());
        dmMeasurePoint.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(dmMeasurePointService.updateDmMeasurePoint(dmMeasurePoint));
    }

    @Operation(summary = "删除物联网测点")
    @PreAuthorize("@ss.hasPermi('dm:dmMeasurePoint:point:remove')")
    @Log(title = "物联网测点", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(dmMeasurePointService.removeDmMeasurePoint(Arrays.asList(ids)));
    }

}
