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

package tech.qiantong.qknow.module.kb.convert.flow;

import com.alibaba.fastjson2.JSONObject;
import tech.qiantong.qknow.module.kb.controller.admin.flow.vo.KbFlowVO;
import tech.qiantong.qknow.module.kb.dal.dataobject.flow.KbFlowEdgeDO;

import java.util.ArrayList;
import java.util.List;

/**
 * bot流程关系 Convert
 *
 * @author qknow
 * @date 2026-03-18
 */
public class KbFlowEdgeConvert {
    /**
     * JSONObject 转 KbFlowEdgeDO
     *
     * @param edgeJson edgeJson 的 json 数据
     * @return KbFlowEdgeDO
     */
    public static KbFlowEdgeDO toDO(JSONObject edgeJson) {
        KbFlowEdgeDO edge = new KbFlowEdgeDO();
        edge.setSourceNodeUuid(edgeJson.getString("source"));
        edge.setTargetNodeUuid(edgeJson.getString("target"));
        edge.setSourceHandle(edgeJson.getString("sourceHandle"));
        edge.setStyle(edgeJson.toJSONString());
        return edge;
    }

    /**
     * KbFlowVO 转 KbFlowEdgeDO 列表
     *
     * @param flowVO flowVO
     * @return KbFlowEdgeDO
     */
    public static List<KbFlowEdgeDO> toDOList(KbFlowVO flowVO) {
        List<KbFlowEdgeDO> resultList = new ArrayList<>(flowVO.getEdges().size());
        for (JSONObject edgeJson : flowVO.getEdges()) {
            KbFlowEdgeDO edge = toDO(edgeJson);
            edge.setBotId(flowVO.getBotId());
            edge.setWorkspaceId(flowVO.getWorkspaceId());
            resultList.add(edge);
        }
        return resultList;
    }
}
