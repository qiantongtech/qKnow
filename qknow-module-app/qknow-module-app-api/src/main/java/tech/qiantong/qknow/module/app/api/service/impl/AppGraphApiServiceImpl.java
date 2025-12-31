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

package tech.qiantong.qknow.module.app.api.service.impl;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import tech.qiantong.qknow.module.app.api.service.IAppGraphApiService;
import tech.qiantong.qknow.module.app.controller.admin.appGraph.vo.AppGraphRelationshipSaveReqVO;
import tech.qiantong.qknow.module.app.controller.admin.appGraph.vo.AppGraphVO;
import tech.qiantong.qknow.module.app.service.appGraph.AppGraphService;

import javax.annotation.Resource;
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
