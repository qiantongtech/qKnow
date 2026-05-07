package tech.qiantong.qknow.module.kb.api.flow.service;


import reactor.core.publisher.Flux;
import tech.qiantong.qknow.common.core.domain.CommonResult;
import tech.qiantong.qknow.module.kb.api.flow.dto.KbRuntimeRespDTO;
import org.springframework.ai.chat.messages.Message;

import java.util.List;
import java.util.Map;

public interface IBotApiService {

    /**
     * 执行工作流
     *
     * @param botId       botId
     * @param workspaceId workspaceId
     * @param paramMap    参数map
     * @return 工作流执行结果
     */
    KbRuntimeRespDTO executeFlow(Long botId, Long workspaceId, Map<String, String> paramMap);

    /**
     * 流式执行工作流
     *
     * @param botId       botId
     * @param workspaceId workspaceId
     * @param paramMap    参数map
     * @return 执行结果
     */
    Flux<CommonResult<String>> executeFlowStream(Long botId, Long workspaceId, Map<String, String> paramMap);

    /**
     * 流式执行chatFlow
     *
     * @param botId       botId
     * @param workspaceId workspaceId
     * @param paramMap    参数map
     * @return 执行结果
     */
    Flux<CommonResult<String>> executeChatFlow(Long botId, Long workspaceId,
                                               Map<String, String> paramMap,
                                               List<Message> messageList);
}
