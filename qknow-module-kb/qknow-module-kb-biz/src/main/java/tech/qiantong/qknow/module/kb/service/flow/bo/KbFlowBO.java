package tech.qiantong.qknow.module.kb.service.flow.bo;

import lombok.Data;
import tech.qiantong.qknow.module.kb.dal.dataobject.flow.KbFlowEdgeDO;
import tech.qiantong.qknow.module.kb.dal.dataobject.flow.KbFlowNodeDO;

import java.util.List;

@Data
public class KbFlowBO {

    /**
     * botId
     */
    private Long botId;

    /**
     * 工作区id
     */
    private Long workspaceId;

    /**
     * 节点列表
     */
    private List<KbFlowNodeDO> nodeList;

    /**
     * 边列表
     */
    private List<KbFlowEdgeDO> edgeList;
}
