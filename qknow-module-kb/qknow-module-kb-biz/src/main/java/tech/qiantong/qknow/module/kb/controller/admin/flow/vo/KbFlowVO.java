package tech.qiantong.qknow.module.kb.controller.admin.flow.vo;

import com.alibaba.fastjson2.JSONObject;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class KbFlowVO implements Serializable {
    /**
     * botId
     */
    @NotNull(message = "botId不能为空")
    private Long botId;

    /**
     * 工作区id
     */
    private Long workspaceId;

    /**
     * 节点列表
     */
    @NotEmpty(message = "节点列表不能为空")
    private List<JSONObject> nodes;

    /**
     * 边列表
     */
    @NotEmpty(message = "边列表不能为空")
    private List<JSONObject> edges;
}
