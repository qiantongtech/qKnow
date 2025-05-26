package tech.qiantong.qknow.module.kmc.controller.admin.kmcDocument;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import cn.hutool.core.date.DateUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
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
import tech.qiantong.qknow.common.utils.PdfConverUtil;
import tech.qiantong.qknow.common.utils.TxtConverUtil;
import tech.qiantong.qknow.common.utils.object.BeanUtils;
import tech.qiantong.qknow.common.utils.poi.ExcelUtil;
import tech.qiantong.qknow.module.kmc.controller.admin.kmcDocument.vo.KmcDocumentPageReqVO;
import tech.qiantong.qknow.module.kmc.controller.admin.kmcDocument.vo.KmcDocumentRespVO;
import tech.qiantong.qknow.module.kmc.controller.admin.kmcDocument.vo.KmcDocumentSaveReqVO;
import tech.qiantong.qknow.module.kmc.convert.kmcDocument.KmcDocumentConvert;
import tech.qiantong.qknow.module.kmc.dal.dataobject.document.KmcDocumentDO;
import tech.qiantong.qknow.module.kmc.service.kmcDocument.IKmcDocumentService;

/**
 * 知识文件Controller
 *
 * @author qknow
 * @date 2025-02-14
 */
@Tag(name = "知识文件")
@RestController
@RequestMapping("/kmcDocument/document")
@Validated
public class KmcDocumentController extends BaseController {
    @Resource
    private IKmcDocumentService kmcDocumentService;

    @Value("${upload.filePath}")
    private String uploadFilePath;

    @Operation(summary = "查询知识文件列表")
    @PreAuthorize("@ss.hasPermi('kmcDocument:kmcDocument:document:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<KmcDocumentRespVO>> list(KmcDocumentPageReqVO kmcDocument) {
        PageResult<KmcDocumentDO> page = kmcDocumentService.getKmcDocumentPage(kmcDocument);
        return CommonResult.success(BeanUtils.toBean(page, KmcDocumentRespVO.class));
    }

    @Operation(summary = "导出知识文件列表")
    @PreAuthorize("@ss.hasPermi('kmcDocument:kmcDocument:document:export')")
    @Log(title = "知识文件", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, KmcDocumentPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<KmcDocumentDO> list = (List<KmcDocumentDO>) kmcDocumentService.getKmcDocumentPage(exportReqVO).getRows();
        ExcelUtil<KmcDocumentRespVO> util = new ExcelUtil<>(KmcDocumentRespVO.class);
        util.exportExcel(response, KmcDocumentConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入知识文件列表")
    @PreAuthorize("@ss.hasPermi('kmcDocument:kmcDocument:document:import')")
    @Log(title = "知识文件", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<KmcDocumentRespVO> util = new ExcelUtil<>(KmcDocumentRespVO.class);
        List<KmcDocumentRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = kmcDocumentService.importKmcDocument(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取知识文件详细信息")
    @PreAuthorize("@ss.hasPermi('kmcDocument:kmcDocument:document:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<KmcDocumentRespVO> getInfo(@PathVariable("id") Long id) {
        KmcDocumentDO kmcDocumentDO = kmcDocumentService.getKmcDocumentById(id);
        return CommonResult.success(BeanUtils.toBean(kmcDocumentDO, KmcDocumentRespVO.class));
    }

    @Operation(summary = "新增知识文件")
    @PreAuthorize("@ss.hasPermi('kmcDocument:kmcDocument:document:add')")
    @Log(title = "知识文件", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody KmcDocumentSaveReqVO kmcDocument) {
        kmcDocument.setCreatorId(getUserId());
        kmcDocument.setCreateBy(getNickName());
        kmcDocument.setCreateTime(DateUtil.date());
        kmcDocument.setWorkspaceId(super.getWorkSpaceId());
        return CommonResult.toAjax(kmcDocumentService.createKmcDocument(kmcDocument));
    }

    @Operation(summary = "修改知识文件")
    @PreAuthorize("@ss.hasPermi('kmcDocument:kmcDocument:document:edit')")
    @Log(title = "知识文件", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody KmcDocumentSaveReqVO kmcDocument) {
        kmcDocument.setUpdaterId(getUserId());
        kmcDocument.setUpdateBy(getNickName());
        kmcDocument.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(kmcDocumentService.updateKmcDocument(kmcDocument));
    }

    @Operation(summary = "删除知识文件")
    @PreAuthorize("@ss.hasPermi('kmcDocument:kmcDocument:document:remove')")
    @Log(title = "知识文件", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(kmcDocumentService.removeKmcDocument(Arrays.asList(ids)));
    }

    /**
     * 获取文件的base64编码
     */
    @PostMapping("/getPdfPreview" )
    public CommonResult<Map<String, String>> getPdfPreview(@RequestBody String url) {
        Map<String, String> stringMap = new HashMap<>();
        JSONObject object = JSONUtil.parseObj(url);
        String path = object.getStr("url");
        try {
            logger.info("接收到的数据：" + path);
            File fileByHttpUrl = kmcDocumentService.getFileByHttpURL(path);
            // 获取文件类型
            String fileName = fileByHttpUrl.getName();
            String fileType = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());

            FileInputStream fileInputStream = new FileInputStream(fileByHttpUrl);
            String fileBase64 = "";
            File file = new File("");
            // 判断文件类型然后调用相应的方法
            if ("docx".equals(fileType)){
                file = PdfConverUtil.wordTopdfByAspose(fileInputStream, uploadFilePath);
            }else if ("excel".equals(fileType)){
                file = PdfConverUtil.excelToPdf(fileInputStream, uploadFilePath);
            }else if ("ppt".equals(fileType)){
                file = PdfConverUtil.pptToPdf(fileInputStream,uploadFilePath);
            }else if ("pptx".equals(fileType)){
                file = PdfConverUtil.pptxToPdf(fileInputStream,uploadFilePath);
            }else if ("txt".equals(fileType)){
                file = TxtConverUtil.txtToPdf(fileInputStream,uploadFilePath);
            }else if ("pdf".equals(fileType)){
                file = fileByHttpUrl;
            }
            byte[] bytes = PdfConverUtil.readBytesFromFile(file.getAbsolutePath());
            fileBase64 = Base64.encodeBase64String(bytes);
            file.delete();
            stringMap.put("fileBase64",fileBase64);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("系统错误：", e);
        }
        return CommonResult.success(stringMap);
    }

    @Operation(summary = "根据条件查询知识文件列表")
    @GetMapping("/selectList")
    public CommonResult<List<KmcDocumentRespVO>> selectList(KmcDocumentPageReqVO kmcDocument) {
        List<KmcDocumentDO> list = kmcDocumentService.selectList(kmcDocument);
        return CommonResult.success(BeanUtils.toBean(list, KmcDocumentRespVO.class));
    }

    @Operation(summary = "获取多少种类型及每种类型下面的总文件数量")
    @GetMapping("/getFileTypes")
    public CommonResult<List<Map<String, Object>>> getFileTypes() {
        List<Map<String, Object>> list = kmcDocumentService.getFileTypes();
        return CommonResult.success(list);
    }

}
