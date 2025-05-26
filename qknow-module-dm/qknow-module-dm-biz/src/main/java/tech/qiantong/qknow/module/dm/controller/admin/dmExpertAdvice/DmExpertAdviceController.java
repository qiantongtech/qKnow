package tech.qiantong.qknow.module.dm.controller.admin.dmExpertAdvice;

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
import tech.qiantong.qknow.module.dm.controller.admin.dmExpertAdvice.vo.DmExpertAdvicePageReqVO;
import tech.qiantong.qknow.module.dm.controller.admin.dmExpertAdvice.vo.DmExpertAdviceRespVO;
import tech.qiantong.qknow.module.dm.controller.admin.dmExpertAdvice.vo.DmExpertAdviceSaveReqVO;
import tech.qiantong.qknow.module.dm.convert.dmExpertAdvice.DmExpertAdviceConvert;
import tech.qiantong.qknow.module.dm.dal.dataobject.dmExpertAdvice.DmExpertAdviceDO;
import tech.qiantong.qknow.module.dm.service.dmExpertAdvice.IDmExpertAdviceService;

/**
 * 专家经验Controller
 *
 * @author qknow
 * @date 2025-02-20
 */
@Tag(name = "专家经验")
@RestController
@RequestMapping("/dm/advice")
@Validated
public class DmExpertAdviceController extends BaseController {
    @Resource
    private IDmExpertAdviceService dmExpertAdviceService;

    @Operation(summary = "查询专家经验列表")
    @PreAuthorize("@ss.hasPermi('dm:dmExpertAdvice:advice:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<DmExpertAdviceRespVO>> list(DmExpertAdvicePageReqVO dmExpertAdvice) {
        PageResult<DmExpertAdviceDO> page = dmExpertAdviceService.getDmExpertAdvicePage(dmExpertAdvice);
        return CommonResult.success(BeanUtils.toBean(page, DmExpertAdviceRespVO.class));
    }

    @Operation(summary = "导出专家经验列表")
    @PreAuthorize("@ss.hasPermi('dm:dmExpertAdvice:advice:export')")
    @Log(title = "专家经验", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DmExpertAdvicePageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DmExpertAdviceDO> list = (List<DmExpertAdviceDO>) dmExpertAdviceService.getDmExpertAdvicePage(exportReqVO).getRows();
        ExcelUtil<DmExpertAdviceRespVO> util = new ExcelUtil<>(DmExpertAdviceRespVO.class);
        util.exportExcel(response, DmExpertAdviceConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入专家经验列表")
    @PreAuthorize("@ss.hasPermi('dm:dmExpertAdvice:advice:import')")
    @Log(title = "专家经验", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<DmExpertAdviceRespVO> util = new ExcelUtil<>(DmExpertAdviceRespVO.class);
        List<DmExpertAdviceRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = dmExpertAdviceService.importDmExpertAdvice(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取专家经验详细信息")
    @PreAuthorize("@ss.hasPermi('dm:dmExpertAdvice:advice:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<DmExpertAdviceRespVO> getInfo(@PathVariable("id") Long id) {
        DmExpertAdviceDO dmExpertAdviceDO = dmExpertAdviceService.getDmExpertAdviceById(id);
        return CommonResult.success(BeanUtils.toBean(dmExpertAdviceDO, DmExpertAdviceRespVO.class));
    }

    @Operation(summary = "新增专家经验")
    @PreAuthorize("@ss.hasPermi('dm:dmExpertAdvice:advice:add')")
    @Log(title = "专家经验", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody DmExpertAdviceSaveReqVO dmExpertAdvice) {
        dmExpertAdvice.setCreatorId(getUserId());
        dmExpertAdvice.setCreateBy(getNickName());
        dmExpertAdvice.setCreateTime(DateUtil.date());
        dmExpertAdvice.setWorkspaceId(super.getWorkSpaceId());
        return CommonResult.toAjax(dmExpertAdviceService.createDmExpertAdvice(dmExpertAdvice));
    }

    @Operation(summary = "修改专家经验")
    @PreAuthorize("@ss.hasPermi('dm:dmExpertAdvice:advice:edit')")
    @Log(title = "专家经验", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody DmExpertAdviceSaveReqVO dmExpertAdvice) {
        dmExpertAdvice.setUpdaterId(getUserId());
        dmExpertAdvice.setUpdateBy(getNickName());
        dmExpertAdvice.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(dmExpertAdviceService.updateDmExpertAdvice(dmExpertAdvice));
    }

    @Operation(summary = "删除专家经验")
    @PreAuthorize("@ss.hasPermi('dm:dmExpertAdvice:advice:remove')")
    @Log(title = "专家经验", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(dmExpertAdviceService.removeDmExpertAdvice(Arrays.asList(ids)));
    }

}
