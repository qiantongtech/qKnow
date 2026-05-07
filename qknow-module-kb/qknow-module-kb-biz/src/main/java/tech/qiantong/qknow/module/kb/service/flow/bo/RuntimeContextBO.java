package tech.qiantong.qknow.module.kb.service.flow.bo;

import com.alibaba.fastjson2.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.ai.chat.messages.Message;
import reactor.core.publisher.Flux;
import tech.qiantong.qknow.common.core.domain.CommonResult;
import tech.qiantong.qknow.module.kb.dal.dataobject.flow.KbFlowNodeDO;
import tech.qiantong.qknow.module.kb.dal.dataobject.runtime.KbRuntimeDO;
import tech.qiantong.qknow.module.kb.dal.enums.BotExecuteModeEnum;
import tech.qiantong.qknow.module.kb.dal.enums.BotTypeEnums;

import java.util.List;
import java.util.Map;

/**
 * 流程运行时上下文对象
 */
@Data
public class RuntimeContextBO {

    /**
     * 运行时对象
     */
    private KbRuntimeDO runtimeDO;

    /**
     * 存储执行过程中的变量数据
     */
    private JSONObject variables;

    /**
     * 运行模式 test/stream/block
     */
    private BotExecuteModeEnum executeMode;

    /**
     * bot 类型
     */
    private BotTypeEnums botType;

    /**
     * 节点map
     */
    Map<String, KbFlowNodeDO> nodeMap;

    /**
     * 输出流 map
     */
    Map<String, Flux<String>> fluxMap;

    /**
     * 对话消息列表
     */
    List<Message> messageList;

    private Flux<CommonResult<String>> resultFlux;

    public RuntimeContextBO(KbRuntimeDO runtimeDO, JSONObject variables) {
        this.runtimeDO = runtimeDO;
        this.variables = variables;
    }
}
