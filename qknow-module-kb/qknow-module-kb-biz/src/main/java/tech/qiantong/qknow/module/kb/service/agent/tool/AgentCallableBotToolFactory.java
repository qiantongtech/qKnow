package tech.qiantong.qknow.module.kb.service.agent.tool;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.function.FunctionToolCallback;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.qiantong.qknow.common.exception.ServiceException;
import tech.qiantong.qknow.module.kb.controller.admin.flow.vo.KbFlowVO;
import tech.qiantong.qknow.module.kb.controller.admin.runtime.vo.KbRuntimeRespVO;
import tech.qiantong.qknow.module.kb.dal.dataobject.agent.KbAgentConfigDO;
import tech.qiantong.qknow.module.kb.dal.dataobject.bot.KbBotDO;
import tech.qiantong.qknow.module.kb.dal.dataobject.flow.KbFlowNodeDO;
import tech.qiantong.qknow.module.kb.dal.enums.BotTypeEnums;
import tech.qiantong.qknow.module.kb.dal.enums.FlowNodeTypeEnums;
import tech.qiantong.qknow.module.kb.dal.enums.RuntimeStatusEnums;
import tech.qiantong.qknow.module.kb.dal.mapper.agent.KbAgentConfigMapper;
import tech.qiantong.qknow.module.kb.service.agent.IKbAgentConfigService;
import tech.qiantong.qknow.module.kb.service.bot.IKbBotService;
import tech.qiantong.qknow.module.kb.service.flow.IKbFlowNodeService;
import tech.qiantong.qknow.module.kb.service.flow.IKbFlowService;
import tech.qiantong.qknow.mybatis.core.query.LambdaQueryWrapperX;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 将工作流和子 Agent 暴露为主 Agent 可自主调用的工具。
 */
@Slf4j
@Component
public class AgentCallableBotToolFactory {

    private static final String WORKFLOW_TOOL_NAME_PREFIX = "workflow_";
    private static final String AGENT_TOOL_NAME_PREFIX = "agent_";

    @Resource
    private IKbBotService botService;

    @Resource
    private IKbFlowNodeService flowNodeService;

    @Resource
    private IKbFlowService flowService;

    @Resource
    private KbAgentConfigMapper agentConfigMapper;

    /** 使用延迟获取避免 ToolFactory 与 AgentConfigService 形成初始化时循环依赖。 */
    @Autowired
    private ObjectProvider<IKbAgentConfigService> agentConfigServiceProvider;

    /**
     * 按配置顺序创建工具，仅允许加载当前工作空间内有效的工作流或 Agent。
     *
     * @param allowAgentCallables false 时忽略子 Agent，供一级子 Agent 执行时阻断继续委派
     */
    public List<ToolCallback> createToolCallbacks(String callableBotIds, Long workspaceId,
            Long ownerBotId, boolean allowAgentCallables) {
        List<Long> idList = parseIds(callableBotIds);
        if (idList.isEmpty()) {
            return Collections.emptyList();
        }

        Map<Long, KbBotDO> botMap = botService.listByIds(idList).stream()
                .collect(Collectors.toMap(KbBotDO::getId, bot -> bot, (left, right) -> left));
        List<ToolCallback> callbacks = new ArrayList<>(idList.size());
        for (Long callableBotId : idList) {
            KbBotDO callableBot = botMap.get(callableBotId);
            // 一级子 Agent 不继续注册 Agent 工具，直接截断潜在的 A -> B -> A 调用链。
            if (!allowAgentCallables && callableBot != null
                    && Objects.equals(BotTypeEnums.AGENT.getCode(), callableBot.getType())) {
                continue;
            }
            validateCommon(callableBot, callableBotId, workspaceId, ownerBotId);

            if (Objects.equals(BotTypeEnums.WORK_FLOW.getCode(), callableBot.getType())) {
                callbacks.add(createWorkflowTool(callableBot));
                continue;
            }
            if (Objects.equals(BotTypeEnums.AGENT.getCode(), callableBot.getType())) {
                callbacks.add(createAgentTool(callableBot));
                continue;
            }
            throw new ServiceException("Agent 目前只支持导入工作流和 Agent：" + callableBot.getName());
        }
        return callbacks;
    }

    private ToolCallback createWorkflowTool(KbBotDO workflow) {
        KbFlowNodeDO startNode = findStartNode(workflow.getId());
        if (startNode == null) {
            throw new ServiceException("工作流“" + workflow.getName() + "”没有开始节点");
        }
        Function<Map<String, Object>, String> executeFunction = params -> executeWorkflow(workflow, startNode, params);
        return FunctionToolCallback
                .builder(WORKFLOW_TOOL_NAME_PREFIX + workflow.getId(), executeFunction)
                .description(buildWorkflowDescription(workflow, startNode))
                .inputSchema(buildInputSchema(startNode))
                .inputType(Map.class)
                .build();
    }

    private ToolCallback createAgentTool(KbBotDO agentBot) {
        KbAgentConfigDO agentConfig = findAgentConfig(agentBot.getId());
        if (agentConfig == null || Boolean.FALSE.equals(agentConfig.getValidFlag())
                || StrUtil.isBlank(agentConfig.getModelConfig())) {
            throw new ServiceException("Agent“" + agentBot.getName() + "”尚未完成有效配置");
        }

        Function<Map<String, Object>, String> executeFunction = params -> {
            Object questionValue = params == null ? null : params.get("question");
            String question = questionValue == null ? null : String.valueOf(questionValue);
            if (StrUtil.isBlank(question)) {
                throw new ServiceException("调用 Agent“" + agentBot.getName() + "”时问题不能为空");
            }
            if (question.length() > 16000) {
                throw new ServiceException("调用 Agent“" + agentBot.getName() + "”时问题不能超过16000个字符");
            }
            return agentConfigServiceProvider.getObject()
                    .executeSubAgent(agentBot.getId(), question, agentBot.getWorkspaceId());
        };
        return FunctionToolCallback
                .builder(AGENT_TOOL_NAME_PREFIX + agentBot.getId(), executeFunction)
                .description(buildAgentDescription(agentBot))
                .inputSchema(buildAgentInputSchema())
                .inputType(Map.class)
                .build();
    }

    private void validateCommon(KbBotDO callableBot, Long callableBotId, Long workspaceId, Long ownerBotId) {
        if (callableBot == null) {
            throw new ServiceException("可调用 Bot 不存在：" + callableBotId);
        }
        if (workspaceId != null && !Objects.equals(workspaceId, callableBot.getWorkspaceId())) {
            throw new ServiceException("可调用 Bot 不属于当前工作空间：" + callableBot.getName());
        }
        if (!Boolean.TRUE.equals(callableBot.getValidFlag())) {
            throw new ServiceException("可调用 Bot 已停用：" + callableBot.getName());
        }
        if (Objects.equals(ownerBotId, callableBot.getId())) {
            throw new ServiceException("Agent 不能导入并调用自身：" + callableBot.getName());
        }
    }

    private KbAgentConfigDO findAgentConfig(Long botId) {
        return agentConfigMapper.selectOne(new LambdaQueryWrapperX<KbAgentConfigDO>()
                .eq(KbAgentConfigDO::getBotId, botId));
    }

    private String executeWorkflow(KbBotDO workflow, KbFlowNodeDO startNode, Map<String, Object> params) {
        JSONObject input = applyDefaultsAndValidate(startNode, params);
        KbFlowVO flowVO = new KbFlowVO();
        flowVO.setBotId(workflow.getId());
        flowVO.setWorkspaceId(workflow.getWorkspaceId());

        log.info("Agent 调用工作流: workflowId={}, name={}", workflow.getId(), workflow.getName());
        KbRuntimeRespVO result = flowService.executeFlow(flowVO, input);
        if (result == null || !Objects.equals(RuntimeStatusEnums.SUCCESS.getCode(), result.getStatus())) {
            StringBuilder message = new StringBuilder("工作流“" + workflow.getName() + "”执行失败");
            // 带上工作流的链式错误详情，让 Agent 能感知具体根因而不是只看到"执行失败"
            String detail = result == null ? null : result.getOutput();
            if (StrUtil.isNotBlank(detail)) {
                message.append("：").append(detail);
            }
            throw new ServiceException(message.toString());
        }
        return StrUtil.isBlank(result.getOutput()) ? "工作流执行成功，但没有返回内容" : result.getOutput();
    }

    private KbFlowNodeDO findStartNode(Long workflowId) {
        return flowNodeService.listByBotId(workflowId).stream()
                .filter(node -> Objects.equals(FlowNodeTypeEnums.START.getCode(), node.getType()))
                .findFirst()
                .orElse(null);
    }

    private String buildWorkflowDescription(KbBotDO workflow, KbFlowNodeDO startNode) {
        StringBuilder description = new StringBuilder("工作流业务名称：“")
                .append(workflow.getName()).append("”。执行该工作流");
        if (StrUtil.isNotBlank(workflow.getDescription())) {
            description.append("。用途：").append(workflow.getDescription());
        } else {
            description.append("。未填写用途描述，请根据工作流业务名称和输入参数判断是否调用");
        }

        JSONArray fields = parseInputFields(startNode);
        if (!fields.isEmpty()) {
            description.append("。输入参数：");
            for (int i = 0; i < fields.size(); i++) {
                JSONObject field = fields.getJSONObject(i);
                if (i > 0) {
                    description.append("；");
                }
                description.append(field.getString("name"));
                String label = field.getString("label");
                if (StrUtil.isNotBlank(label)) {
                    description.append("（").append(label).append("）");
                }
                if (Boolean.TRUE.equals(field.getBoolean("required"))) {
                    description.append("[必填]");
                }
            }
        }
        return description.toString();
    }

    private String buildAgentDescription(KbBotDO agentBot) {
        StringBuilder description = new StringBuilder("Agent 业务名称：“")
                .append(agentBot.getName()).append("”。将一个完整、明确的问题委派给该专业 Agent");
        if (StrUtil.isNotBlank(agentBot.getDescription())) {
            description.append("。用途：").append(agentBot.getDescription());
        } else {
            description.append("。未填写用途描述，请根据 Agent 业务名称判断是否调用");
        }
        description.append("。该 Agent 只返回任务结果，由当前 Agent 负责向用户组织最终回答");
        return description.toString();
    }

    private String buildAgentInputSchema() {
        JSONObject schema = new JSONObject();
        schema.put("type", "object");
        JSONObject properties = new JSONObject();
        JSONObject question = new JSONObject();
        question.put("type", "string");
        question.put("description", "需要委派给该 Agent 的完整问题或任务要求");
        question.put("maxLength", 16000);
        properties.put("question", question);
        schema.put("properties", properties);
        schema.put("required", List.of("question"));
        schema.put("additionalProperties", false);
        return schema.toJSONString();
    }

    static String buildInputSchema(KbFlowNodeDO startNode) {
        JSONObject schema = new JSONObject();
        schema.put("type", "object");
        JSONObject properties = new JSONObject();
        JSONArray required = new JSONArray();

        JSONArray fields = parseInputFields(startNode);
        for (int i = 0; i < fields.size(); i++) {
            JSONObject field = fields.getJSONObject(i);
            String name = field.getString("name");
            if (StrUtil.isBlank(name)) {
                continue;
            }

            JSONObject property = new JSONObject();
            String fieldType = field.getString("type");
            if (Objects.equals("number", fieldType)) {
                property.put("type", "number");
            } else {
                property.put("type", "string");
                Integer maxLength = field.getInteger("maxLength");
                if (maxLength != null && maxLength > 0) {
                    property.put("maxLength", maxLength);
                }
            }

            String label = field.getString("label");
            property.put("description", StrUtil.isBlank(label) ? name : label);
            Object defaultValue = field.get("defaultValue");
            if (hasValue(defaultValue)) {
                property.put("default", defaultValue);
            }
            properties.put(name, property);
            if (Boolean.TRUE.equals(field.getBoolean("required")) && !hasValue(defaultValue)) {
                required.add(name);
            }
        }

        schema.put("properties", properties);
        if (!required.isEmpty()) {
            schema.put("required", required);
        }
        schema.put("additionalProperties", false);
        return schema.toJSONString();
    }

    static JSONObject applyDefaultsAndValidate(KbFlowNodeDO startNode, Map<String, Object> params) {
        Map<String, Object> normalized = new LinkedHashMap<>();
        if (params != null) {
            normalized.putAll(params);
        }

        JSONArray fields = parseInputFields(startNode);
        for (int i = 0; i < fields.size(); i++) {
            JSONObject field = fields.getJSONObject(i);
            String name = field.getString("name");
            if (StrUtil.isBlank(name)) {
                continue;
            }
            Object value = normalized.get(name);
            if (!hasValue(value) && hasValue(field.get("defaultValue"))) {
                normalized.put(name, field.get("defaultValue"));
                value = normalized.get(name);
            }
            if (Boolean.TRUE.equals(field.getBoolean("required")) && !hasValue(value)) {
                String label = field.getString("label");
                throw new ServiceException("工作流缺少必填参数：" + (StrUtil.isBlank(label) ? name : label));
            }
        }

        JSONObject input = new JSONObject();
        input.putAll(normalized);
        return input;
    }

    private static JSONArray parseInputFields(KbFlowNodeDO startNode) {
        if (startNode == null || StrUtil.isBlank(startNode.getInput())) {
            return new JSONArray();
        }
        try {
            JSONArray fields = JSONArray.parseArray(startNode.getInput());
            return fields == null ? new JSONArray() : fields;
        } catch (Exception e) {
            throw new ServiceException("工作流开始节点的输入参数配置不是有效的 JSON");
        }
    }

    private static boolean hasValue(Object value) {
        if (value == null || (value instanceof String text && StrUtil.isBlank(text))) {
            return false;
        }
        if (value instanceof java.util.Collection<?> collection) {
            return !collection.isEmpty();
        }
        if (value instanceof Map<?, ?> map) {
            return !map.isEmpty();
        }
        return true;
    }

    private List<Long> parseIds(String callableBotIds) {
        if (StrUtil.isBlank(callableBotIds)) {
            return Collections.emptyList();
        }
        try {
            List<Long> ids = new ArrayList<>();
            for (String value : callableBotIds.split(",")) {
                value = value.trim();
                if (StrUtil.isNotBlank(value)) {
                    Long id = Long.valueOf(value);
                    if (!ids.contains(id)) {
                        ids.add(id);
                    }
                }
            }
            return ids;
        } catch (NumberFormatException e) {
            throw new ServiceException("可调用Bot ids格式错误");
        }
    }
}

