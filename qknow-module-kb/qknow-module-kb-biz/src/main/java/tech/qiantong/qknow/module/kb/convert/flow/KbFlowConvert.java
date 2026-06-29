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

import cn.hutool.core.collection.CollUtil;
import tech.qiantong.qknow.module.kb.controller.admin.flow.vo.KbFlowVO;
import tech.qiantong.qknow.module.kb.service.flow.bo.KbFlowBO;

/**
 * bot流程节点 Convert
 *
 * @author qknow
 * @date 2026-03-18
 */
public class KbFlowConvert {

    /**
     * 转换为运行时对象
     *
     * @param flowVO 流程 vo 对象
     * @return 流程运行对象
     */
    public static KbFlowBO toFlowBO(KbFlowVO flowVO) {
        KbFlowBO result = new KbFlowBO();
        result.setBotId(flowVO.getBotId());
        result.setWorkspaceId(flowVO.getWorkspaceId());
        if (CollUtil.isNotEmpty(flowVO.getNodes())) {
            result.setNodeList(KbFlowNodeConvert.toDOList(flowVO));
        }
        if (CollUtil.isNotEmpty(flowVO.getEdges())) {
            result.setEdgeList(KbFlowEdgeConvert.toDOList(flowVO));
        }
        return result;
    }
}
