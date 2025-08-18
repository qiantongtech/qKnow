package tech.qiantong.qknow.module.ext.controller.admin.extUnstructTaskText;

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
import tech.qiantong.qknow.module.ext.controller.admin.extUnstructTaskText.vo.ExtUnstructTaskTextPageReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extUnstructTaskText.vo.ExtUnstructTaskTextRespVO;
import tech.qiantong.qknow.module.ext.controller.admin.extUnstructTaskText.vo.ExtUnstructTaskTextSaveReqVO;
import tech.qiantong.qknow.module.ext.convert.extUnstructTaskText.ExtUnstructTaskTextConvert;
import tech.qiantong.qknow.module.ext.dal.dataobject.extUnstructTaskText.ExtUnstructTaskTextDO;
import tech.qiantong.qknow.module.ext.service.extUnstructTaskText.IExtUnstructTaskTextService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * 任务文件段落关联Controller
 *
 * @author qknow
 * @date 2025-02-21
 */
@Tag(name = "任务文件段落关联")
@RestController
@RequestMapping("/ext/text")
@Validated
public class ExtUnstructTaskTextController extends BaseController {
    @Resource
    private IExtUnstructTaskTextService extUnstructTaskTextService;

    @Operation(summary = "查询任务文件段落关联列表")
    @PreAuthorize("@ss.hasPermi('ext:extUnstructTaskText:t:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<ExtUnstructTaskTextRespVO>> list(ExtUnstructTaskTextPageReqVO extUnstructTaskText) {
        PageResult<ExtUnstructTaskTextDO> page = extUnstructTaskTextService.getExtUnstructTaskTextPage(extUnstructTaskText);
        return CommonResult.success(BeanUtils.toBean(page, ExtUnstructTaskTextRespVO.class));
    }

    @Operation(summary = "导出任务文件段落关联列表")
    @PreAuthorize("@ss.hasPermi('ext:extUnstructTaskText:t:export')")
    @Log(title = "任务文件段落关联", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ExtUnstructTaskTextPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ExtUnstructTaskTextDO> list = (List<ExtUnstructTaskTextDO>) extUnstructTaskTextService.getExtUnstructTaskTextPage(exportReqVO).getRows();
        ExcelUtil<ExtUnstructTaskTextRespVO> util = new ExcelUtil<>(ExtUnstructTaskTextRespVO.class);
        util.exportExcel(response, ExtUnstructTaskTextConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入任务文件段落关联列表")
    @PreAuthorize("@ss.hasPermi('ext:extUnstructTaskText:t:import')")
    @Log(title = "任务文件段落关联", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<ExtUnstructTaskTextRespVO> util = new ExcelUtil<>(ExtUnstructTaskTextRespVO.class);
        List<ExtUnstructTaskTextRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = extUnstructTaskTextService.importExtUnstructTaskText(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取任务文件段落关联详细信息")
    @PreAuthorize("@ss.hasPermi('ext:extUnstructTaskText:t:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<ExtUnstructTaskTextRespVO> getInfo(@PathVariable("id") Long id) {
        ExtUnstructTaskTextDO extUnstructTaskTextDO = extUnstructTaskTextService.getExtUnstructTaskTextById(id);
        return CommonResult.success(BeanUtils.toBean(extUnstructTaskTextDO, ExtUnstructTaskTextRespVO.class));
    }

    @Operation(summary = "新增任务文件段落关联")
    @PreAuthorize("@ss.hasPermi('ext:extUnstructTaskText:t:add')")
    @Log(title = "任务文件段落关联", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody ExtUnstructTaskTextSaveReqVO extUnstructTaskText) {
        return CommonResult.toAjax(extUnstructTaskTextService.createExtUnstructTaskText(extUnstructTaskText));
    }

    @Operation(summary = "修改任务文件段落关联")
    @PreAuthorize("@ss.hasPermi('ext:extUnstructTaskText:t:edit')")
    @Log(title = "任务文件段落关联", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody ExtUnstructTaskTextSaveReqVO extUnstructTaskText) {
        return CommonResult.toAjax(extUnstructTaskTextService.updateExtUnstructTaskText(extUnstructTaskText));
    }

    @Operation(summary = "删除任务文件段落关联")
    @PreAuthorize("@ss.hasPermi('ext:extUnstructTaskText:t:remove')")
    @Log(title = "任务文件段落关联", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(extUnstructTaskTextService.removeExtUnstructTaskText(Arrays.asList(ids)));
    }

}
