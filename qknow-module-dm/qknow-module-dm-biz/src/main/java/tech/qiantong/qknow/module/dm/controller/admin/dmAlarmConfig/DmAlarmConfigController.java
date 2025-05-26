package tech.qiantong.qknow.module.dm.controller.admin.dmAlarmConfig;

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
import tech.qiantong.qknow.module.dm.controller.admin.dmAlarmConfig.vo.DmAlarmConfigPageReqVO;
import tech.qiantong.qknow.module.dm.controller.admin.dmAlarmConfig.vo.DmAlarmConfigRespVO;
import tech.qiantong.qknow.module.dm.controller.admin.dmAlarmConfig.vo.DmAlarmConfigSaveReqVO;
import tech.qiantong.qknow.module.dm.convert.dmAlarmConfig.DmAlarmConfigConvert;
import tech.qiantong.qknow.module.dm.dal.dataobject.dmAlarmConfig.DmAlarmConfigDO;
import tech.qiantong.qknow.module.dm.service.dmAlarmConfig.IDmAlarmConfigService;

/**
 * 告警配置Controller
 *
 * @author qknow
 * @date 2025-02-19
 */
@Tag(name = "告警配置")
@RestController
@RequestMapping("/dm/config")
@Validated
public class DmAlarmConfigController extends BaseController {
    @Resource
    private IDmAlarmConfigService dmAlarmConfigService;

    @Operation(summary = "查询告警配置列表")
    @PreAuthorize("@ss.hasPermi('dm:dmAlarmConfig:config:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<DmAlarmConfigRespVO>> list(DmAlarmConfigPageReqVO dmAlarmConfig) {
        PageResult<DmAlarmConfigDO> page = dmAlarmConfigService.getDmAlarmConfigPage(dmAlarmConfig);
        return CommonResult.success(BeanUtils.toBean(page, DmAlarmConfigRespVO.class));
    }

    @Operation(summary = "导出告警配置列表")
    @PreAuthorize("@ss.hasPermi('dm:dmAlarmConfig:config:export')")
    @Log(title = "告警配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DmAlarmConfigPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DmAlarmConfigDO> list = (List<DmAlarmConfigDO>) dmAlarmConfigService.getDmAlarmConfigPage(exportReqVO).getRows();
        ExcelUtil<DmAlarmConfigRespVO> util = new ExcelUtil<>(DmAlarmConfigRespVO.class);
        util.exportExcel(response, DmAlarmConfigConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入告警配置列表")
    @PreAuthorize("@ss.hasPermi('dm:dmAlarmConfig:config:import')")
    @Log(title = "告警配置", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<DmAlarmConfigRespVO> util = new ExcelUtil<>(DmAlarmConfigRespVO.class);
        List<DmAlarmConfigRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = dmAlarmConfigService.importDmAlarmConfig(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取告警配置详细信息")
    @PreAuthorize("@ss.hasPermi('dm:dmAlarmConfig:config:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<DmAlarmConfigRespVO> getInfo(@PathVariable("id") Long id) {
        DmAlarmConfigDO dmAlarmConfigDO = dmAlarmConfigService.getDmAlarmConfigById(id);
        return CommonResult.success(BeanUtils.toBean(dmAlarmConfigDO, DmAlarmConfigRespVO.class));
    }

    @Operation(summary = "新增告警配置")
    @PreAuthorize("@ss.hasPermi('dm:dmAlarmConfig:config:add')")
    @Log(title = "告警配置", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody DmAlarmConfigSaveReqVO dmAlarmConfig) {
        dmAlarmConfig.setCreatorId(getUserId());
        dmAlarmConfig.setCreateBy(getNickName());
        dmAlarmConfig.setCreateTime(DateUtil.date());
        dmAlarmConfig.setWorkspaceId(super.getWorkSpaceId());
        return CommonResult.toAjax(dmAlarmConfigService.createDmAlarmConfig(dmAlarmConfig));
    }

    @Operation(summary = "修改告警配置")
    @PreAuthorize("@ss.hasPermi('dm:dmAlarmConfig:config:edit')")
    @Log(title = "告警配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody DmAlarmConfigSaveReqVO dmAlarmConfig) {
        dmAlarmConfig.setUpdaterId(getUserId());
        dmAlarmConfig.setUpdateBy(getNickName());
        dmAlarmConfig.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(dmAlarmConfigService.updateDmAlarmConfig(dmAlarmConfig));
    }

    @Operation(summary = "删除告警配置")
    @PreAuthorize("@ss.hasPermi('dm:dmAlarmConfig:config:remove')")
    @Log(title = "告警配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(dmAlarmConfigService.removeDmAlarmConfig(Arrays.asList(ids)));
    }

}
