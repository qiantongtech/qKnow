package tech.qiantong.qknow.module.kb.service.flow.bo;

import lombok.Data;
import tech.qiantong.qknow.module.kb.dal.enums.RuntimeStatusEnums;

import java.util.List;
import java.util.Map;

/**
 * 节点执行结果 BO
 */
@Data
public class NodeRunResultBO {

    /**
     * 运行状态
     */
    private Integer status;

    /**
     * 节点 uuid
     */
    private String nodeUuid;

    /**
     * 节点名称
     */
    private String nodeName;

    /**
     * 节点的输入数据
     */
    private Map<String, Object> input;

    /**
     * 执行结果数据
     */
    private Map<String, Object> output;

    /**
     * 步骤号
     */
    private Integer step;

    /**
     * 错误信息（失败时）
     */
    private String errorMessage;

    /**
     * 下一个要执行的节点 IDs（用于分支场景）
     */
    private List<String> nextNodeIds;

    /**
     * 执行耗时（毫秒）
     */
    private Long duration;

    /**
     * 创建成功的结果
     */
    public static NodeRunResultBO success(String nodeUuid, String nodeName, Map<String, Object> data) {
        NodeRunResultBO result = new NodeRunResultBO();
        result.setStatus(RuntimeStatusEnums.SUCCESS.getCode());
        result.setNodeUuid(nodeUuid);
        result.setNodeName(nodeName);
        result.setOutput(data);
        return result;
    }

    /**
     * 创建失败的结果
     */
    public static NodeRunResultBO failure(String nodeUuid, String nodeName, String errorMessage) {
        NodeRunResultBO result = new NodeRunResultBO();
        result.setStatus(RuntimeStatusEnums.ERROR.getCode());
        result.setNodeUuid(nodeUuid);
        result.setNodeName(nodeName);
        result.setErrorMessage(errorMessage);
        return result;
    }
}
