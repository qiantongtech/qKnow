/*
 * Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
 *
 * This file is part of qKnow Intelligent Agent Building Platform (Open Source Edition).
 *
 * qKnow is licensed under Apache License 2.0 with additional qKnow terms.
 * You may use qKnow for commercial purposes, but you may not remove, hide,
 * modify, or replace the qKnow logo, copyright notices, license notices,
 * or attribution information without a separate commercial license.
 *
 * White-label use, OEM distribution, rebranding, or presenting qKnow as
 * another product requires separate commercial authorization from
 * Jiangsu Qiantong Technology Co., Ltd.
 *
 * Business License: https://community.qknow.ai/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

package tech.qiantong.qknow.module.app.api.service.impl;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import tech.qiantong.qknow.module.app.api.service.IAppGraphApiService;
import tech.qiantong.qknow.module.app.controller.admin.appGraph.vo.AppGraphRelationshipSaveReqVO;
import tech.qiantong.qknow.module.app.controller.admin.appGraph.vo.AppGraphVO;
import tech.qiantong.qknow.module.app.service.appGraph.AppGraphService;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 图谱
 */
@Service
public class AppGraphApiServiceImpl implements IAppGraphApiService {
    @Resource
    private AppGraphService appGraphService;

    /**
     * 获取图谱数据
     *
     * @param appGraphVO
     * @return
     */
    public Map<String, Object> getGraph(AppGraphVO appGraphVO) {
        return appGraphService.getGraph(appGraphVO);
    }

    public Boolean addTripletRel(AppGraphRelationshipSaveReqVO graphRelationshipSaveReqVO) {
        List<AppGraphRelationshipSaveReqVO> appGraphRelationshipSaveReqVOS = Lists.newArrayList(graphRelationshipSaveReqVO);
        return appGraphService.addTripletRel(appGraphRelationshipSaveReqVOS);
    }


}
