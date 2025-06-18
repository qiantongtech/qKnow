package tech.qiantong.qknow.module.ext.controller.admin.extStructTask;

import com.alibaba.fastjson2.JSONObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
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
import tech.qiantong.qknow.common.utils.StringUtils;
import tech.qiantong.qknow.common.utils.object.BeanUtils;
import tech.qiantong.qknow.common.utils.poi.ExcelUtil;
import tech.qiantong.qknow.module.ext.controller.admin.extStructTask.vo.ExtStructTaskPageReqVO;
import tech.qiantong.qknow.module.ext.controller.admin.extStructTask.vo.ExtStructTaskRespVO;
import tech.qiantong.qknow.module.ext.controller.admin.extStructTask.vo.ExtStructTaskSaveReqVO;
import tech.qiantong.qknow.module.ext.convert.extStructTask.ExtStructTaskConvert;
import tech.qiantong.qknow.module.ext.dal.dataobject.extNeo4j.ExtNeo4j;
import tech.qiantong.qknow.module.ext.dal.dataobject.extStructTask.ExtStructTask;
import tech.qiantong.qknow.module.ext.dal.dataobject.extStructTask.ExtStructTaskDO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extUnstructTask.ExtUnstructTaskDO;
import tech.qiantong.qknow.module.ext.dal.dataobject.extraction.ExtExtractionDO;
import tech.qiantong.qknow.module.ext.extEnum.ExtReleaseStatus;
import tech.qiantong.qknow.module.ext.service.extStructTask.IExtStructTaskService;
import tech.qiantong.qknow.module.ext.service.neo4j.service.ExtNeo4jService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 结构化抽取任务Controller
 *
 * @author qknow
 * @date 2025-02-25
 */
@Tag(name = "结构化抽取任务")
@RestController
@Slf4j
@RequestMapping("/ext/extStruct")
@Validated
public class ExtStructTaskController extends BaseController {
    @Resource
    private IExtStructTaskService extStructTaskService;
    @Resource
    private ExtNeo4jService extNeo4jService;

    /**
     * 执行抽取
     *
     * @param extStructTask
     * @return
     */
    @PostMapping("executeExtraction")
    public AjaxResult executeExtraction(@RequestBody ExtStructTaskPageReqVO extStructTask) {
        if (extStructTask.getId() == null) {
            return AjaxResult.error("id为空");
        }
        return extStructTaskService.executeExtraction(extStructTask);
    }

    /**
     * 获取结构化任务抽取结果
     *
     * @param taskId
     * @return
     */
    @GetMapping("getExtStructByTaskId")
    public AjaxResult getExtStructByTaskId(Long taskId) {
        return extStructTaskService.selectByTaskId(taskId, 1);
    }

    /**
     * 修改结构化任务 发布状态
     *
     * @param extStructTask
     * @return
     */
    @PostMapping("updateStructTaskPublishStatus")
    public AjaxResult updateStructTaskPublishStatus(@RequestBody ExtStructTaskSaveReqVO extStructTask) {
        ExtStructTaskDO taskById = extStructTaskService.getExtStructTaskById(extStructTask.getId());
        taskById.setPublishStatus(extStructTask.getPublishStatus());
        if(extStructTask.getPublishStatus().equals(ExtReleaseStatus.PUBLISHED.getStatus())){
            taskById.setPublishBy(getNickName());
            taskById.setPublisherId(getUserId());
            taskById.setPublishTime(new Date());
        }
        boolean updateById = extStructTaskService.updateById(taskById);
        if (updateById) {
            return AjaxResult.success("操作成功");
        } else {
            return AjaxResult.error("操作失败");
        }
    }

//    /**
//     * 发布
//     *
//     * @param jsonObject
//     * @return
//     */
//    @PostMapping("releaseByTaskId")
//    public AjaxResult releaseByTaskId(@RequestBody JSONObject jsonObject) {
//        Long taskId = jsonObject.getLong("taskId");
//        ExtStructTaskDO structTaskDO = extStructTaskService.getExtStructTaskById(taskId);
//        structTaskDO.setPublishStatus(1);
//        structTaskDO.setPublishTime(new Date());
//        structTaskDO.setPublisherId(getUserId());
//        structTaskDO.setPublishBy(getNickName());
//        structTaskDO.setUpdateTime(new Date());
//        structTaskDO.setUpdaterId(getUserId());
//        structTaskDO.setUpdateBy(getNickName());
//        return extStructTaskService.releaseByTaskId(structTaskDO);
//    }
//
//    /**
//     * 取消发布
//     *
//     * @param jsonObject
//     * @return
//     */
//    @PostMapping("cancelReleaseByTaskId")
//    public AjaxResult cancelReleaseByTaskId(@RequestBody JSONObject jsonObject) {
//        Long taskId = jsonObject.getLong("taskId");
//        ExtStructTaskDO structTaskDO = extStructTaskService.getExtStructTaskById(taskId);
//        structTaskDO.setPublishStatus(0);
//        structTaskDO.setPublishTime(new Date());
//        structTaskDO.setPublisherId(getUserId());
//        structTaskDO.setPublishBy(getNickName());
//        structTaskDO.setUpdateTime(new Date());
//        structTaskDO.setUpdaterId(getUserId());
//        structTaskDO.setUpdateBy(getNickName());
//        return extStructTaskService.cancelReleaseByTaskId(structTaskDO);
//    }
//
//    /**
//     * 删除节点的某个属性
//     *
//     * @param jsonObject
//     * @return
//     */
//    @PostMapping("deleteNodeAttribute")
//    public AjaxResult deleteNodeAttribute(@RequestBody JSONObject jsonObject) {
//        Long taskId = jsonObject.getLong("taskId");
//        return extNeo4jService.deleteNodeAttribute(taskId, jsonObject.getInteger("extractType"),
//                jsonObject.getString("tableName"), jsonObject.getInteger("dataId"), jsonObject.getString("attributeKey"));
//    }

//    /**
//     * 修改关系
//     *
//     * @param updateRelationship
//     * @return
//     */
//    @PostMapping("updateRelationship")
//    public AjaxResult updateRelationship(@RequestBody ExtNeo4j.UpdateRelationship updateRelationship) {
//        return extNeo4jService.updateRelationship(updateRelationship);
//    }

//    /**
//     * 删除关系
//     *
//     * @param deleteRelationship
//     * @return
//     */
//    @PostMapping("deleteRelationship")
//    public AjaxResult deleteRelationship(@RequestBody ExtNeo4j.DeleteRelationship deleteRelationship) {
//        Long relationshipId = deleteRelationship.getRelationshipId();
//        return extNeo4jService.deleteRelationship(relationshipId);
//    }

    /**
     * 修改节点的某个属性
     *
     * @param jsonObject
     * @return
     */
    @PostMapping("updateNodeAttribute")
    public AjaxResult updateNodeAttribute(@RequestBody JSONObject jsonObject) {
        Long taskId = jsonObject.getLong("taskId");
        return extNeo4jService.updateNodeAttribute(taskId, jsonObject.getString("tableName"), jsonObject.getInteger("dataId"),
                jsonObject.getString("attributeKey"), jsonObject.getString("attributeValue"));
    }

    /**
     * 根据属性ids获取属性列表
     *
     * @param attributeIds
     * @return
     */
    @GetMapping("getAttributeInformation")
    public AjaxResult getAttributeInformation(String attributeIds) {
        if (StringUtils.isBlank(attributeIds)) {
            return AjaxResult.error("attributeIds为空");
        }
        attributeIds = attributeIds.replace("attributeId", "");
        String[] split = attributeIds.split(",");
        List<String> list = Arrays.asList(split);
        return extStructTaskService.getAttributeInformation(list);
    }

    /**
     * 获取结构化抽取任务详细信息
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        if (id == null) {
            return AjaxResult.error("id为空");
        }
        return extStructTaskService.getInfo(id);
    }


    /**
     * 新建结构化抽取任务
     *
     * @param extStructTask
     * @return
     */
    @PostMapping("addDataMapping")
    public AjaxResult addDataMapping(@RequestBody ExtStructTask extStructTask) {
        log.info("extStructTask:{}", JSONObject.toJSONString(extStructTask));
        extStructTask.setWorkspaceId(super.getWorkSpaceId());
        extStructTask.setUpdateBy(getNickName());
        extStructTask.setUpdaterId(getUserId());
        return extStructTaskService.addDataMapping(extStructTask);
    }

    /**
     * 修改结构化抽取任务
     *
     * @param extStructTask
     * @return
     */
    @PostMapping("updateDataMapping")
    public AjaxResult updateDataMapping(@RequestBody ExtStructTask extStructTask) {
        if (extStructTask.getTaskId() == null) {
            return AjaxResult.error("任务id为空");
        }
        if (extStructTask.getDataSourceId() == null) {
            return AjaxResult.error("数据源为空");
        }
        log.info("extStructTask:{}", JSONObject.toJSONString(extStructTask));
        extStructTask.setWorkspaceId(super.getWorkSpaceId());
        extStructTask.setUpdateBy(getNickName());
        extStructTask.setUpdaterId(getUserId());
        return extStructTaskService.updateDataMapping(extStructTask);
    }

    @Operation(summary = "编辑结构化抽取任务")
    @PreAuthorize("@ss.hasPermi('ext:extStructTask:struct:edit')")
    @Log(title = "结构化抽取任务", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody ExtStructTaskSaveReqVO extStructTask) {
        return CommonResult.toAjax(extStructTaskService.updateExtStructTask(extStructTask));
    }

    @Operation(summary = "查询结构化抽取任务列表")
    @PreAuthorize("@ss.hasPermi('ext:extStructTask:struct:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<ExtStructTaskRespVO>> list(ExtStructTaskPageReqVO extStructTask) {
        PageResult<ExtStructTaskDO> page = extStructTaskService.getExtStructTaskPage(extStructTask);
        return CommonResult.success(BeanUtils.toBean(page, ExtStructTaskRespVO.class));
    }

    @Operation(summary = "导出结构化抽取任务列表")
    @PreAuthorize("@ss.hasPermi('ext:extStructTask:struct:export')")
    @Log(title = "结构化抽取任务", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ExtStructTaskPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ExtStructTaskDO> list = (List<ExtStructTaskDO>) extStructTaskService.getExtStructTaskPage(exportReqVO).getRows();
        ExcelUtil<ExtStructTaskRespVO> util = new ExcelUtil<>(ExtStructTaskRespVO.class);
        util.exportExcel(response, ExtStructTaskConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入结构化抽取任务列表")
    @PreAuthorize("@ss.hasPermi('ext:extStructTask:struct:import')")
    @Log(title = "结构化抽取任务", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<ExtStructTaskRespVO> util = new ExcelUtil<>(ExtStructTaskRespVO.class);
        List<ExtStructTaskRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = extStructTaskService.importExtStructTask(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "新增结构化抽取任务")
    @PreAuthorize("@ss.hasPermi('ext:extStructTask:struct:add')")
    @Log(title = "结构化抽取任务", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody ExtStructTaskSaveReqVO extStructTask) {
        return CommonResult.toAjax(extStructTaskService.createExtStructTask(extStructTask));
    }

    @Operation(summary = "删除结构化抽取任务")
    @PreAuthorize("@ss.hasPermi('ext:extStructTask:struct:remove')")
    @Log(title = "结构化抽取任务", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        for (Long id : ids) {
            ExtStructTaskDO structTaskById = extStructTaskService.getExtStructTaskById(id);
            //删除neo4j中之前抽取相关的数据, 如果有的话
            ExtExtractionDO extractionDO = new ExtExtractionDO();
            extractionDO.setTaskId(structTaskById.getId());
            extNeo4jService.deleteExtStruck(extractionDO);
        }
        return CommonResult.toAjax(extStructTaskService.removeExtStructTask(Arrays.asList(ids)));
    }

}
