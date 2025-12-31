/*
 * Copyright © 2026 Qiantong Technology Co., Ltd.
 * qKnow Knowledge Platform
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please apply for brand customization authorization via official channels.
 *  *
 * More information: https://qknow.qiantong.tech/business.html
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2026 江苏千桐科技有限公司
 * qKnow 知识平台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qknow.qiantong.tech/business.html
 */

package tech.qiantong.qknow.module.app.controller.admin.appGraph;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.web.bind.annotation.*;
import tech.qiantong.qknow.common.core.controller.BaseController;
import tech.qiantong.qknow.common.core.domain.AjaxResult;
import tech.qiantong.qknow.common.core.domain.CommonResult;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.common.utils.DateUtils;
import tech.qiantong.qknow.module.app.controller.admin.appGraph.vo.AppGraphPageReqVO;
import tech.qiantong.qknow.module.app.controller.admin.appGraph.vo.AppGraphRelationshipSaveReqVO;
import tech.qiantong.qknow.module.app.controller.admin.appGraph.vo.AppGraphVO;
import tech.qiantong.qknow.module.app.controller.admin.appGraph.vo.DeleteNodeAttributeVO;
import tech.qiantong.qknow.module.app.service.appGraph.AppGraphService;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 图谱controller
 *
 * @author qknow
 * @date 2025-03-17
 */
@RestController
@RequestMapping("/app/graph")
public class AppGraphController extends BaseController {

    @Resource
    private AppGraphService appGraphService;

    /**
     * 获取图谱数据
     *
     * @param appGraphVO
     * @return
     */
    @GetMapping("/getGraph")
    public CommonResult<Map<String, Object>> getGraph(AppGraphVO appGraphVO) {
        return CommonResult.success(appGraphService.getGraph(appGraphVO));
    }

    /**
     * 获取实体分页
     *
     * @param appGraphVO
     * @return
     */
    @GetMapping("/getGraphPage")
    public CommonResult<PageResult<JSONObject>> getGraph(AppGraphPageReqVO appGraphVO) {
        return CommonResult.success(appGraphService.getGraphPage(appGraphVO));
    }

    /**
     * 统计 (实体数量,关系类型数量,三元组数量)
     *
     * @param appGraphVO
     * @return
     */
    @GetMapping("/getGraphDataStatistics")
    public CommonResult<Map<String, Object>> getGraphDataStatistics(AppGraphVO appGraphVO) {
        return CommonResult.success(appGraphService.getGraphDataStatistics(appGraphVO));
    }


    /**
     * 新增实体 TODO
     *
     * @param jsonArray
     * @return
     */
    @PostMapping("/addNode")
    public CommonResult<Boolean> addNode(@RequestBody JSONArray jsonArray) {
        Date date = DateUtils.getNowDate();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            if (jsonObject.get("id") != null) {
                //jsonObject.put("workspaceId", super.getWorkSpaceId());
                //jsonObject.put("creatorId", super.getUserId());
                //jsonObject.put("createBy", super.getUsername());
                jsonObject.put("createTime", date);
            }
        }
        return CommonResult.success(appGraphService.addNode(jsonArray));
    }

    /**
     * 根据起点id和结点id创建关系 TODO
     *
     * @param graphRelationshipSaveReqVOList
     * @return
     */
    @PostMapping("/addTripletRel")
    public CommonResult<Boolean> addTripletRel(@RequestBody List<AppGraphRelationshipSaveReqVO> graphRelationshipSaveReqVOList) {
        return CommonResult.success(appGraphService.addTripletRel(graphRelationshipSaveReqVOList));
    }

    /**
     * 根据节点id和属性的key删除属性
     *
     * @param deleteNodeAttributeVO
     * @return
     */
    @DeleteMapping("/deleteNodeAttributeById")
    public AjaxResult deleteNodeAttributeById(@RequestBody DeleteNodeAttributeVO deleteNodeAttributeVO) {
        return appGraphService.deleteNodeAttributeById(deleteNodeAttributeVO);
    }

    /**
     * 发布 / 取消发布
     *
     * @param appGraphVO
     * @return
     */
    @PostMapping("/updateReleaseStatus")
    public AjaxResult updateReleaseStatus(@RequestBody AppGraphVO appGraphVO) {
        return appGraphService.updateReleaseStatus(appGraphVO);
    }

    /**
     * 根据节点id删除对应的节点
     *
     * @param id
     * @return
     */
    @DeleteMapping("/deleteNode/{id}")
    public AjaxResult deleteNode(@PathVariable Long id) {
        return appGraphService.deleteNode(id);
    }

    /**
     * 根据节点ids删除对应的节点
     * @param ids
     * @return
     */
    @DeleteMapping("/deleteNodeByIds/{ids}")
    public CommonResult<Boolean> deleteNodeByIds(@PathVariable Long[] ids) {
        return CommonResult.success(appGraphService.deleteNodeByIds(ids));
    }

    /**
     * 删除关系
     *
     * @param id
     * @return
     */
    @DeleteMapping("/deleteRelationshipById/{id}")
    public AjaxResult deleteRelationshipById(@PathVariable Long id) {
        return appGraphService.deleteRelationshipById(id);
    }

    /**
     * 根据关系ids删除关系
     *
     * @param ids
     * @return
     */
    @DeleteMapping("/deleteRelationshipsByIds/{ids}")
    public AjaxResult deleteRelationshipsByIds(@PathVariable Long[] ids) {
        return appGraphService.deleteRelationshipsByIds(ids);
    }
}
