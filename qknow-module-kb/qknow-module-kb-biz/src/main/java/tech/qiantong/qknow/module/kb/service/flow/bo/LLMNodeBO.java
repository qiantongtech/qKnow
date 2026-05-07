package tech.qiantong.qknow.module.kb.service.flow.bo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import reactor.core.publisher.Flux;
import tech.qiantong.qknow.common.exception.ServiceException;
import tech.qiantong.qknow.module.ai.api.modelMarket.IAiModelApiService;
import tech.qiantong.qknow.module.kb.dal.dataobject.flow.KbFlowEdgeDO;
import tech.qiantong.qknow.module.kb.dal.dataobject.flow.KbFlowNodeDO;
import tech.qiantong.qknow.module.kb.dal.enums.BotExecuteModeEnum;
import tech.qiantong.qknow.module.kb.dal.enums.FlowNodeTypeEnums;

import java.util.*;

public class LLMNodeBO extends BaseNodeBO {

    private final IAiModelApiService aiModelService;


    public LLMNodeBO(KbFlowNodeDO nodeDefinition, List<KbFlowEdgeDO> edgeList, IAiModelApiService aiModelService) {
        super(nodeDefinition, edgeList);
        this.aiModelService = aiModelService;
    }

    /**
     * 执行具体的节点逻辑
     *
     * @param inputData 输入数据
     * @param context   工作流上下文
     * @return 执行结果
     */
    @Override
    protected NodeRunResultBO executeLogic(Map<String, Object> inputData, RuntimeContextBO context) {
        KbFlowNodeDO nodeDefinition = super.getNodeDefinition();
        JSONObject configJson = JSONObject.parseObject(nodeDefinition.getConfig());
        JSONArray inputDefArray = JSONArray.parse(nodeDefinition.getInput());

        Message[] messageArray = getMessageArray(configJson, inputDefArray, context);

        Long provider = configJson.getLong("provider");
        String model = configJson.getString("model");
        if (Objects.isNull(provider) || StrUtil.isEmpty(model)) {
            throw new ServiceException("模型选择错误");
        }
        StringBuilder outSb = new StringBuilder();
        ChatModel chatModel = aiModelService.getChatModel(provider, model);
        if (isStream(context)) {
            Map<String, Flux<String>> fluxMap = context.getFluxMap();
            if (Objects.isNull(fluxMap)) {
                fluxMap = new HashMap<>();
            }
            Flux<String> flux = chatModel.stream(messageArray);
            fluxMap.put(nodeDefinition.getUuid(), flux);
            context.setFluxMap(fluxMap);
        } else {
            String call = chatModel.call(messageArray);
            outSb.append(call);
        }

        Map<String, Object> outputData = new HashMap<>();
        outputData.put(nodeDefinition.getUuid() + ".text", outSb.toString());
        return NodeRunResultBO.success(nodeDefinition.getUuid(), nodeDefinition.getName(), outputData);
    }

    /**
     * 设置大模型对话上下文
     *
     * @param configJson    llm 配置json 文件
     * @param inputDefArray 输入定义
     * @param context       上下文
     * @return 需要添加大模型的上下文信息
     */
    private Message[] getMessageArray(JSONObject configJson, JSONArray inputDefArray, RuntimeContextBO context) {
        List<Message> messageList = new ArrayList<>();
        if (inputDefArray.size() == 0) {
            String promptFormat = configJson.getString("prompt");
            messageList.add(new SystemMessage(super.format(promptFormat, context)));
        } else {
            String promptFormat = configJson.getString("prompt");
            messageList.add(new SystemMessage(super.format(promptFormat, context)));
            for (int i = 0; i < inputDefArray.size(); i++) {
                JSONObject inputDef = inputDefArray.getJSONObject(i);
                String role = inputDef.getString("role");
                String content = inputDef.getString("content");
                if (Objects.equals(role, "assistant")) {
                    messageList.add(new AssistantMessage(super.format(content, context)));
                } else {
                    messageList.add(new UserMessage(super.format(content, context)));
                }
            }
        }
        if (CollUtil.isNotEmpty(context.getMessageList())) {
            messageList.addAll(context.getMessageList());
        }
        return messageList.toArray(new Message[0]);
    }

    /**
     * 判断是否是流式输出
     *
     * @param context 当前运行上下文
     * @return 是否应该是流式输出
     */
    private boolean isStream(RuntimeContextBO context) {
        if (Objects.equals(context.getExecuteMode(), BotExecuteModeEnum.BLOCK)) {
            return false;
        }
        // 否则查找所有指向的下一个节点
        List<String> nextNodeIdList = super.getEdgeList().stream()
                .filter(edge -> Objects.equals(edge.getSourceNodeUuid(), super.getNodeDefinition().getUuid()))
                .map(KbFlowEdgeDO::getTargetNodeUuid)
                .toList();
        if (CollUtil.isEmpty(nextNodeIdList)) {
            return true;
        }
        KbFlowNodeDO nextNode = context.getNodeMap().get(nextNodeIdList.get(0));
        return Objects.equals(FlowNodeTypeEnums.REPLY.getCode(), nextNode.getType());
    }

}
