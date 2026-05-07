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
