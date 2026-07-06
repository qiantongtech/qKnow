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

package tech.qiantong.qknow.module.kb.service.flow.bo;


import tech.qiantong.qknow.module.kb.dal.dataobject.flow.KbFlowEdgeDO;
import tech.qiantong.qknow.module.kb.dal.dataobject.flow.KbFlowNodeDO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 开始节点
 */
public class StartNodeBO extends BaseNodeBO {

    /**
     * 构造函数
     *
     * @param nodeDefinition 节点定义
     * @param edgeList       所有边列表
     */
    public StartNodeBO(KbFlowNodeDO nodeDefinition, List<KbFlowEdgeDO> edgeList) {
        super(nodeDefinition, edgeList);
    }

    /**
     * 执行开始节点
     *
     * @param inputData 输入数据
     * @param context   工作流上下文
     * @return 执行结果
     */
    @Override
    protected NodeRunResultBO executeLogic(Map<String, Object> inputData, RuntimeContextBO context) {
        KbFlowNodeDO nodeDefinition = super.getNodeDefinition();
        Map<String, Object> outputData = new HashMap<>();
        for (Map.Entry<String, Object> entry : inputData.entrySet()){
            outputData.put("node_1."+entry.getKey(), entry.getValue());
            outputData.put(nodeDefinition.getUuid()+"."+entry.getKey(), entry.getValue());
        }
        return NodeRunResultBO.success(nodeDefinition.getUuid(), nodeDefinition.getName(), outputData);
    }
}
