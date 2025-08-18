package tech.qiantong.qknow.module.app.api.service;

import tech.qiantong.qknow.module.app.controller.admin.appGraph.vo.AppGraphRelationshipSaveReqVO;
import tech.qiantong.qknow.module.app.controller.admin.appGraph.vo.AppGraphVO;

import java.util.Map;

/**
 * 图谱
 */
public interface IAppGraphApiService {
    public Map<String, Object> getGraph(AppGraphVO appGraphVO);

    public Boolean addTripletRel(AppGraphRelationshipSaveReqVO graphRelationshipSaveReqVO);
}
