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

package tech.qiantong.qknow.module.app.service.appGraph;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import tech.qiantong.qknow.common.core.domain.AjaxResult;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.module.app.controller.admin.appGraph.vo.AppGraphPageReqVO;
import tech.qiantong.qknow.module.app.controller.admin.appGraph.vo.AppGraphRelationshipSaveReqVO;
import tech.qiantong.qknow.module.app.controller.admin.appGraph.vo.AppGraphVO;
import tech.qiantong.qknow.module.app.controller.admin.appGraph.vo.DeleteNodeAttributeVO;

import java.util.List;
import java.util.Map;

public interface AppGraphService {
    public Map<String, Object> getGraph(AppGraphVO appGraphVO);

    public PageResult<JSONObject> getGraphPage(AppGraphPageReqVO appGraphVO);

    public Map<String, Object> getGraphDataStatistics(AppGraphVO appGraphVO);

    /**
     * 新增实体
     * @param jsonArray
     * @return
     */
    public Boolean addNode(JSONArray jsonArray);

    /**
     * 编辑三元组
     * @param graphRelationshipSaveReqVOList
     * @return
     */
    public Boolean addTripletRel(List<AppGraphRelationshipSaveReqVO> graphRelationshipSaveReqVOList);

    public AjaxResult deleteNodeAttributeById(DeleteNodeAttributeVO deleteNodeAttributeVO);

    public AjaxResult updateReleaseStatus(AppGraphVO appGraphVO);

    public AjaxResult deleteNode(Long id);

    /**
     * 批量删除节点
     * @param ids
     * @return
     */
    public Boolean deleteNodeByIds(Long[] ids);

    public AjaxResult deleteRelationshipById(Long id);

    public AjaxResult deleteRelationshipsByIds(Long[] ids);
}
