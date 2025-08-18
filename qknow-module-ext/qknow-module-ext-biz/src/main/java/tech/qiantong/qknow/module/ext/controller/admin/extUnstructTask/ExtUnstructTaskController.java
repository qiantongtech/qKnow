package tech.qiantong.qknow.module.ext.controller.admin.extUnstructTask;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSONObject;
import com.google.common.collect.Maps;
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
import tech.qiantong.qknow.module.ext.controller.admin.extUnstructTask.vo.ExtUnstructTaskPageReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extUnstructTask.vo.ExtUnstructTaskRespVO;
import tech.qiantong.qknow.module.ext.controller.admin.extUnstructTask.vo.ExtUnstructTaskSaveReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extUnstructTaskDocRel.vo.ExtUnstructTaskDocRelPageReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extUnstructTaskText.vo.ExtUnstructTaskTextPageReqVO;
import tech.qiantong.qknow.module.ext.convert.extUnstructTask.ExtUnstructTaskConvert;
import tech.qiantong.qknow.module.ext.dal.dataobject.extUnstructTask.ExtUnstructTaskDO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extUnstructTaskDocRel.ExtUnstructTaskDocRelDO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extUnstructTaskText.ExtUnstructTaskTextDO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extraction.ExtExtractionDO;
import tech.qiantong.qknow.module.ext.dal.dataobject.unstructTaskRelation.ExtUnstructTaskRelationDO;
import tech.qiantong.qknow.module.ext.extEnum.ExtReleaseStatus;
import tech.qiantong.qknow.module.ext.extEnum.ExtTaskStatus;
import tech.qiantong.qknow.module.ext.service.extUnstructTask.IExtUnstructTaskService;
import tech.qiantong.qknow.module.ext.service.extUnstructTaskDocRel.IExtUnstructTaskDocRelService;
import tech.qiantong.qknow.module.ext.service.extUnstructTaskText.IExtUnstructTaskTextService;
import tech.qiantong.qknow.module.ext.service.neo4j.service.ExtNeo4jService;
import tech.qiantong.qknow.module.ext.service.unstructTaskRelation.IExtUnstructTaskRelationService;
import tech.qiantong.qknow.module.kmc.api.service.IKmcApiService;
import tech.qiantong.qknow.module.kmc.dal.dataobject.document.KmcDocumentDO;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 非结构化抽取任务Controller
 *
 * @author qknow
 * @date 2025-02-18
 */
@Tag(name = "非结构化抽取任务")
@RestController
@RequestMapping("/ext/unstructTask")
@Validated
public class ExtUnstructTaskController extends BaseController {
    @Resource
    private IExtUnstructTaskService extUnstructTaskService;
    @Resource
    private IExtUnstructTaskDocRelService extUnstructTaskDocRelService;
    @Resource
    private ExtNeo4jService extNeo4jService;
    @Resource
    private IExtUnstructTaskTextService extUnstructTaskTextService;
    @Resource
    private IKmcApiService kmcApiService;
    @Resource
    private IExtUnstructTaskRelationService extUnstructTaskRelationService;

    /**
     * 执行抽取 具体任务异步执行
     *
     * @param extUnstructTask
     * @return
     */
    @PostMapping("/executeExtraction")
    public AjaxResult executeExtraction(@RequestBody ExtUnstructTaskSaveReqVO extUnstructTask) {
        return extUnstructTaskService.executeExtraction(extUnstructTask);
    }

    /**
     * 修改结构化任务 发布状态
     *
     * @param taskSaveReqVO
     * @return
     */
    @PostMapping("updateUnStructTaskPublishStatus")
    public AjaxResult updateUnStructTaskPublishStatus(@RequestBody ExtUnstructTaskSaveReqVO taskSaveReqVO) {
        ExtUnstructTaskDO taskById = extUnstructTaskService.getExtUnstructTaskById(taskSaveReqVO.getId());
        taskById.setPublishStatus(taskSaveReqVO.getPublishStatus());
        if(taskSaveReqVO.getPublishStatus().equals(ExtReleaseStatus.PUBLISHED.getStatus())){
            taskById.setPublishBy(getNickName());
            taskById.setPublisherId(getUserId());
            taskById.setPublishTime(new Date());
        }
        boolean updateById = extUnstructTaskService.updateById(taskById);
        if (updateById) {
            return AjaxResult.success("操作成功");
        } else {
            return AjaxResult.error("操作失败");
        }
    }

    /**
     * 获取neo4j全部数据, 实体数组和关系数组
     *
     * @return
     */
    @GetMapping("/getExtractionAllData")
    public AjaxResult getExtractionAllData() {
        return extNeo4jService.getExtractionAllData();
    }

//    /**
//     * 获取neo4j全部数据, 三元组格式
//     *
//     * @return
//     */
//    @GetMapping("/getExtExtractionAll")
//    public AjaxResult getExtExtractionAll() {
//        return extNeo4jService.getExtExtractionAll();
//    }

    /**
     * 根据任务Id查询neo4j三元组数据
     *
     * @param extExtractionDO
     * @return
     */
    @GetMapping("/getExtExtraction")
    public AjaxResult getExtExtraction(ExtExtractionDO extExtractionDO) {
        return extUnstructTaskService.getExtExtraction(extExtractionDO);
    }

    /**
     * 发布
     *
     * @param jsonObject
     * @return
     */
    @PostMapping("releaseByTaskId")
    public AjaxResult releaseByTaskId(@RequestBody JSONObject jsonObject) {
        Long taskId = jsonObject.getLong("taskId");
        ExtUnstructTaskDO structTaskDO = extUnstructTaskService.getExtUnstructTaskById(taskId);
        structTaskDO.setPublishStatus(1);
        structTaskDO.setPublishTime(new Date());
        structTaskDO.setPublisherId(getUserId());
        structTaskDO.setPublishBy(getNickName());
        structTaskDO.setUpdateTime(new Date());
        structTaskDO.setUpdaterId(getUserId());
        structTaskDO.setUpdateBy(getNickName());
        return extUnstructTaskService.releaseByTaskId(structTaskDO);
    }

    /**
     * 取消发布
     *
     * @param jsonObject
     * @return
     */
    @PostMapping("cancelReleaseByTaskId")
    public AjaxResult cancelReleaseByTaskId(@RequestBody JSONObject jsonObject) {
        Long taskId = jsonObject.getLong("taskId");
        ExtUnstructTaskDO unstructTaskById = extUnstructTaskService.getExtUnstructTaskById(taskId);
        unstructTaskById.setPublishStatus(0);
        unstructTaskById.setPublishTime(new Date());
        unstructTaskById.setPublisherId(getUserId());
        unstructTaskById.setPublishBy(getNickName());
        unstructTaskById.setUpdateTime(new Date());
        unstructTaskById.setUpdaterId(getUserId());
        unstructTaskById.setUpdateBy(getNickName());
        return extUnstructTaskService.cancelReleaseByTaskId(unstructTaskById);
    }

    /**
     * 根据节点id删除对应的节点
     *
     * @param id
     * @return
     */
    @PostMapping("/deleteNode")
    public AjaxResult deleteNode(Long id) {
        return extNeo4jService.deleteNode(id);
    }

//    @GetMapping("/deleteExtExtraction")
//    public AjaxResult deleteExtExtraction(Long taskId) {
//        ExtExtractionDO extractionDO = new ExtExtractionDO();
//        extractionDO.setTaskId(taskId);
//        return extNeo4jService.deleteExtExtraction(extractionDO);
//    }

    /**
     * 获取实体相关的文字段落和文档信息
     *
     * @param extExtractionDO
     * @return
     */
    @GetMapping("/getTaskTextList")
    public AjaxResult getTaskTextList(ExtUnstructTaskTextPageReqVO extExtractionDO) {
        ExtUnstructTaskTextPageReqVO textPageReqVO = new ExtUnstructTaskTextPageReqVO();
        textPageReqVO.setTaskId(extExtractionDO.getTaskId());
        PageResult<ExtUnstructTaskTextDO> textPage = extUnstructTaskTextService.getExtUnstructTaskTextPage(textPageReqVO);
        List<ExtUnstructTaskTextPageReqVO> textPageReqVOList = BeanUtils.toBean(textPage.getRows(), ExtUnstructTaskTextPageReqVO.class);

        List<Long> docIds = textPageReqVOList.stream()
                .map(e -> e.getDocId())  // 假设每个对象有一个 getDocId() 方法
                .collect(Collectors.toList());
        List<KmcDocumentDO> documentListByIds = new ArrayList<KmcDocumentDO>();
        if (docIds.size() > 0) {
            documentListByIds = kmcApiService.getKmcDocumentListByIds(docIds);
        }

        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("textListByTaskId", textPageReqVOList);
        hashMap.put("docListByTaskId", documentListByIds);
        return AjaxResult.success(hashMap);
    }

    @Operation(summary = "查询非结构化抽取任务列表")
    @PreAuthorize("@ss.hasPermi('ext:extUnstructTask:unstructtask:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<ExtUnstructTaskRespVO>> list(ExtUnstructTaskPageReqVO extUnstructTask) {
        PageResult<ExtUnstructTaskDO> page = extUnstructTaskService.getExtUnstructTaskPage(extUnstructTask);
        return CommonResult.success(BeanUtils.toBean(page, ExtUnstructTaskRespVO.class));
    }

    @Operation(summary = "导出非结构化抽取任务列表")
    @PreAuthorize("@ss.hasPermi('ext:extUnstructTask:unstructtask:export')")
    @Log(title = "非结构化抽取任务", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ExtUnstructTaskPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ExtUnstructTaskDO> list = (List<ExtUnstructTaskDO>) extUnstructTaskService.getExtUnstructTaskPage(exportReqVO).getRows();
        ExcelUtil<ExtUnstructTaskRespVO> util = new ExcelUtil<>(ExtUnstructTaskRespVO.class);
        util.exportExcel(response, ExtUnstructTaskConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入非结构化抽取任务列表")
    @PreAuthorize("@ss.hasPermi('ext:extUnstructTask:unstructtask:import')")
    @Log(title = "非结构化抽取任务", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<ExtUnstructTaskRespVO> util = new ExcelUtil<>(ExtUnstructTaskRespVO.class);
        List<ExtUnstructTaskRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = extUnstructTaskService.importExtUnstructTask(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取非结构化抽取任务详细信息")
    @PreAuthorize("@ss.hasPermi('ext:extUnstructTask:unstructtask:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<ExtUnstructTaskRespVO> getInfo(@PathVariable("id") Long id) {
        ExtUnstructTaskDO extUnstructTaskDO = extUnstructTaskService.getExtUnstructTaskById(id);
        ExtUnstructTaskRespVO taskRespVO = BeanUtils.toBean(extUnstructTaskDO, ExtUnstructTaskRespVO.class);

        ExtUnstructTaskDocRelPageReqVO docRelPageReqVO = new ExtUnstructTaskDocRelPageReqVO();
        docRelPageReqVO.setTaskId(id);
        PageResult<ExtUnstructTaskDocRelDO> docRelPage = extUnstructTaskDocRelService.getExtUnstructTaskDocRelPage(docRelPageReqVO);
        List<ExtUnstructTaskDocRelPageReqVO> relPageReqVOS = BeanUtils.toBean(docRelPage.getRows(), ExtUnstructTaskDocRelPageReqVO.class);
        taskRespVO.setRelPageReqVOS(relPageReqVOS);
        List<ExtUnstructTaskRelationDO> relationDOList = extUnstructTaskRelationService.findByTaskId(id);
        taskRespVO.setRelRelationDOList(relationDOList);
        return CommonResult.success(taskRespVO);
    }

    @Operation(summary = "新增非结构化抽取任务")
    @PreAuthorize("@ss.hasPermi('ext:extUnstructTask:unstructtask:add')")
    @Log(title = "非结构化抽取任务", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody ExtUnstructTaskSaveReqVO extUnstructTask) {
        Long userId = getUserId();
        extUnstructTask.setWorkspaceId(super.getWorkSpaceId());
        extUnstructTask.setCreatorId(getUserId());
        extUnstructTask.setCreateBy(getNickName());
        extUnstructTask.setCreateTime(DateUtil.date());
        extUnstructTask.setStatus(ExtTaskStatus.UNEXECUTED.getValue());
        extUnstructTask.setPublishStatus(ExtReleaseStatus.UNPUBLISHED.getStatus());
//        extUnstructTask.setPublishTime(new Date());
//        extUnstructTask.setPublisherId(userId);
//        extUnstructTask.setPublishBy(getNickName());
        extUnstructTask.setUpdateTime(new Date());
        return CommonResult.toAjax(extUnstructTaskService.createExtUnstructTask(extUnstructTask));
    }

    @Operation(summary = "修改非结构化抽取任务")
    @PreAuthorize("@ss.hasPermi('ext:extUnstructTask:unstructtask:edit')")
    @Log(title = "非结构化抽取任务", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody ExtUnstructTaskSaveReqVO extUnstructTask) {
        extUnstructTask.setUpdaterId(getUserId());
        extUnstructTask.setUpdateBy(getNickName());
        extUnstructTask.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(extUnstructTaskService.updateExtUnstructTask(extUnstructTask));
    }

    @Operation(summary = "删除非结构化抽取任务")
    @PreAuthorize("@ss.hasPermi('ext:extUnstructTask:unstructtask:remove')")
    @Log(title = "非结构化抽取任务", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        for (Long id : ids) {
            ExtUnstructTaskDO unstructTaskDO = extUnstructTaskService.getExtUnstructTaskById(id);
            //删除neo4j中之前抽取相关的数据, 如果有的话
            ExtExtractionDO extractionDO = new ExtExtractionDO();
            extractionDO.setTaskId(unstructTaskDO.getId());
            extNeo4jService.deleteExtUnStruck(extractionDO);
        }
        return CommonResult.toAjax(extUnstructTaskService.removeExtUnstructTask(Arrays.asList(ids)));
    }

    @GetMapping("/getTextList/{ids}")
    public CommonResult<Map<String, Object>> getTextList(@PathVariable Long[] ids) {
        List<ExtUnstructTaskTextDO> unstructTaskTextDOList = extUnstructTaskTextService.listByIds(Arrays.asList(ids));
        List<ExtUnstructTaskTextPageReqVO> textPageReqVOList = BeanUtils.toBean(unstructTaskTextDOList, ExtUnstructTaskTextPageReqVO.class);

        List<Long> docIds = textPageReqVOList.stream()
                .map(ExtUnstructTaskTextPageReqVO::getDocId)  // 假设每个对象有一个 getDocId() 方法
                .collect(Collectors.toList());
        List<KmcDocumentDO> documentListByIds = kmcApiService.getKmcDocumentListByIds(docIds);

        Map<String, Object> map = Maps.newHashMap();
        map.put("textListByTaskId", textPageReqVOList);
        map.put("docListByTaskId", documentListByIds);
        return CommonResult.success(map);
    }
}
