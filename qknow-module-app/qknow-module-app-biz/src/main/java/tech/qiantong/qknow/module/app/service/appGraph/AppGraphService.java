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
