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

package tech.qiantong.qknow.module.kb.service.agent.impl;

import com.alibaba.cloud.ai.graph.NodeOutput;
import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import com.alibaba.cloud.ai.graph.agent.hook.Hook;
import com.alibaba.cloud.ai.graph.agent.hook.modelcalllimit.ModelCallLimitHook;
import com.alibaba.cloud.ai.graph.agent.hook.skills.SkillsAgentHook;
import com.alibaba.cloud.ai.graph.exception.GraphRunnerException;
import com.alibaba.cloud.ai.graph.skills.registry.SkillRegistry;
import com.alibaba.cloud.ai.graph.streaming.OutputType;
import com.alibaba.cloud.ai.graph.streaming.StreamingOutput;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.metadata.ChatGenerationMetadata;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.tool.StaticToolCallbackProvider;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.function.FunctionToolCallback;
import org.springframework.ai.tool.resolution.ToolCallbackResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import tech.qiantong.qknow.ai.enums.model.MessageTypeEnums;
import tech.qiantong.qknow.ai.service.IChatModelService;
import tech.qiantong.qknow.common.exception.ServiceException;
import tech.qiantong.qknow.common.utils.DateUtils;
import tech.qiantong.qknow.common.utils.StringUtils;
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.common.core.utils.object.BeanUtils;
import tech.qiantong.qknow.module.ai.api.modelMarket.IAiModelApiService;
import tech.qiantong.qknow.module.kb.controller.admin.agent.vo.*;
import tech.qiantong.qknow.module.kb.dal.dataobject.agent.KbAgentConfigDO;
import tech.qiantong.qknow.module.kb.dal.dataobject.skills.KbSkillsDO;
import tech.qiantong.qknow.module.kb.dal.dataobject.tool.KbToolMethodDO;
import tech.qiantong.qknow.module.kb.dal.mapper.agent.KbAgentConfigMapper;
import tech.qiantong.qknow.module.kb.service.agent.TargetedSkillRegistry;
import tech.qiantong.qknow.module.kb.service.agent.IKbAgentConfigService;
import tech.qiantong.qknow.module.kb.service.agent.tool.AgentCallableBotToolFactory;
import tech.qiantong.qknow.module.kb.service.agent.tool.ReadSkillReferenceTool;
import tech.qiantong.qknow.module.kb.service.skills.IKbSkillsService;
import tech.qiantong.qknow.module.kb.service.mcp.IKbMcpConfigService;
import tech.qiantong.qknow.module.kb.service.tool.IKbToolMethodService;
import tech.qiantong.qknow.module.kb.service.tool.IKbToolService;
import tech.qiantong.qknow.module.kb.tool.function.SearchKnowledgeTool;
import tech.qiantong.qknow.module.kb.tool.function.query.knowledgeQuery;
import tech.qiantong.qknow.module.kb.utils.NodeUtils;
import tech.qiantong.qknow.module.kmc.api.knowledgeBase.dto.KmcKnowledgeBaseRespDTO;
import tech.qiantong.qknow.module.kmc.api.service.IKmcApiService;
import tech.qiantong.qknow.mybatis.core.query.LambdaQueryWrapperX;

import java.io.File;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
/**
 * agent配置Service业务层处理
 *
 * @author qknow
 * @date 2026-03-19
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class KbAgentConfigServiceImpl  extends ServiceImpl<KbAgentConfigMapper,KbAgentConfigDO> implements IKbAgentConfigService {
    private static final int DEFAULT_HISTORY_ROUNDS = 1;
    private static final int MAX_HISTORY_ROUNDS = 20;

    @Resource
    private KbAgentConfigMapper kbAgentConfigMapper;
    @Resource
    private IKbToolMethodService kbToolMethodService;
    @Resource
    private IAiModelApiService aiModelService;
    @Resource
    private IKmcApiService kmcApiService;
    @Resource
    private IKbMcpConfigService mcpConfigService;
    @Resource
    private IKbToolService kbToolService;

    @Resource
    private IKbSkillsService kbSkillsService;

    @Resource
    private AgentCallableBotToolFactory agentCallableBotToolFactory;

    @Resource
    private ToolCallbackResolver resolver;

    @Value("${dromara.x-file-storage.local-plus[0].storage-path}")
    private String storagePath;

    @Override
    public PageResult<KbAgentConfigDO> getKbAgentConfigPage(KbAgentConfigPageReqVO pageReqVO) {
        return kbAgentConfigMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createKbAgentConfig(KbAgentConfigSaveReqVO createReqVO) {
        agentCallableBotToolFactory.createToolCallbacks(createReqVO.getCallableBotIds(),
                createReqVO.getWorkspaceId(), createReqVO.getBotId(), true);
        KbAgentConfigDO dictType = BeanUtils.toBean(createReqVO, KbAgentConfigDO.class);
        kbAgentConfigMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateKbAgentConfig(KbAgentConfigSaveReqVO updateReqVO) {
        if (updateReqVO.getId() == null) {
            throw new ServiceException("Agent 配置ID不能为空");
        }
        KbAgentConfigDO existingConfig = kbAgentConfigMapper.selectById(updateReqVO.getId());
        if (existingConfig == null || !Objects.equals(existingConfig.getWorkspaceId(), updateReqVO.getWorkspaceId())) {
            throw new ServiceException("Agent 配置不存在或无权修改");
        }
        if (!Objects.equals(existingConfig.getBotId(), updateReqVO.getBotId())) {
            throw new ServiceException("不能修改 Agent 配置所属的 Bot");
        }
        String callableBotIds = updateReqVO.getCallableBotIds() == null
                ? existingConfig.getCallableBotIds() : updateReqVO.getCallableBotIds();
        agentCallableBotToolFactory.createToolCallbacks(callableBotIds,
                updateReqVO.getWorkspaceId(), updateReqVO.getBotId(), true);

        // 更新agent配置
        KbAgentConfigDO updateObj = BeanUtils.toBean(updateReqVO, KbAgentConfigDO.class);
        updateObj.setCallableBotIds(callableBotIds);
        return kbAgentConfigMapper.updateById(updateObj);
    }
    @Override
    public int removeKbAgentConfig(Collection<Long> idList) {
        // 批量删除agent配置
        return kbAgentConfigMapper.deleteBatchIds(idList);
    }

    @Override
    public KbAgentConfigDO getKbAgentConfigById(Long id) {
        return kbAgentConfigMapper.selectById(id);
    }

    @Override
    public KbAgentConfigDO getKbAgentConfigByBotId(Long botId) {
        LambdaQueryWrapperX<KbAgentConfigDO> queryWrapper = new LambdaQueryWrapperX<>();
        queryWrapper.eq(KbAgentConfigDO::getBotId, botId);
        return kbAgentConfigMapper.selectOne(queryWrapper);
    }

    @Override
    public List<KbAgentConfigDO> getKbAgentConfigList() {
        return kbAgentConfigMapper.selectList();
    }

    @Override
    public Map<Long, KbAgentConfigDO> getKbAgentConfigMap() {
        List<KbAgentConfigDO> kbAgentConfigList = kbAgentConfigMapper.selectList();
        return kbAgentConfigList.stream()
                .collect(Collectors.toMap(
                        KbAgentConfigDO::getId,
                        kbAgentConfigDO -> kbAgentConfigDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }

    /**
     * 导入agent配置数据
     *
     * @param importExcelList agent配置数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    @Override
    public String importKbAgentConfig(List<KbAgentConfigRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("导入数据不能为空！");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (KbAgentConfigRespVO respVO : importExcelList) {
                try {
                    KbAgentConfigDO kbAgentConfigDO = BeanUtils.toBean(respVO, KbAgentConfigDO.class);
                    agentCallableBotToolFactory.createToolCallbacks(respVO.getCallableBotIds(),
                            respVO.getWorkspaceId(), respVO.getBotId(), true);
                    Long kbAgentConfigId = respVO.getId();
                    if (isUpdateSupport) {
                        if (kbAgentConfigId != null) {
                            KbAgentConfigDO existingKbAgentConfig = kbAgentConfigMapper.selectById(kbAgentConfigId);
                            if (existingKbAgentConfig != null
                                    && Objects.equals(existingKbAgentConfig.getWorkspaceId(), respVO.getWorkspaceId())) {
                                kbAgentConfigDO.setUpdateBy(operName);
                                kbAgentConfigMapper.updateById(kbAgentConfigDO);
                                successNum++;
                                successMessages.add("数据更新成功，ID为 " + kbAgentConfigId + " 的agent配置记录。");
                            } else {
                                failureNum++;
                                failureMessages.add("数据更新失败，ID为 " + kbAgentConfigId + " 的agent配置记录不存在或无权修改。");
                            }
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，某条记录的ID不存在。");
                        }
                    } else {
                        QueryWrapper<KbAgentConfigDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", kbAgentConfigId);
                        KbAgentConfigDO existingKbAgentConfig = kbAgentConfigMapper.selectOne(queryWrapper);
                        if (existingKbAgentConfig == null) {
                            kbAgentConfigDO.setCreateBy(operName);
                            kbAgentConfigMapper.insert(kbAgentConfigDO);
                            successNum++;
                            successMessages.add("数据插入成功，ID为 " + kbAgentConfigId + " 的agent配置记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据插入失败，ID为 " + kbAgentConfigId + " 的agent配置记录已存在。");
                        }
                    }
                } catch (Exception e) {
                    failureNum++;
                    String errorMsg = "数据导入失败，错误信息：" + e.getMessage();
                    failureMessages.add(errorMsg);
                    log.error(errorMsg, e);
                }
            }
            StringBuilder resultMsg = new StringBuilder();
            if (failureNum > 0) {
                resultMsg.append("很抱歉，导入失败！共 ").append(failureNum).append(" 条数据格式不正确，错误如下：");
                resultMsg.append("<br/>").append(String.join("<br/>", failureMessages));
                throw new ServiceException(resultMsg.toString());
            } else {
                resultMsg.append("恭喜您，数据已全部导入成功！共 ").append(successNum).append(" 条。");
            }
            return resultMsg.toString();
        }

    @Override
    public Flux<KbChatMessageSendRespVO> chatMessage(KbAgentConfigReqVO kbAgentConfig) throws GraphRunnerException {
        return chatMessage(kbAgentConfig, true);
    }

    private Flux<KbChatMessageSendRespVO> chatMessage(KbAgentConfigReqVO kbAgentConfig,
                                                       boolean allowAgentCallables) throws GraphRunnerException {
        // 获取模型配置
        String modelConfig = kbAgentConfig.getModelConfig();
        if (StringUtils.isNull(modelConfig)) {
            throw new GraphRunnerException("模型配置不能为空！");
        }
        JSONObject jsonObject = JSONObject.parseObject(modelConfig);
        if (StringUtils.isNull(jsonObject.getString("modelId")) || StringUtils.isNull(jsonObject.getString("modelName"))) {
            throw new GraphRunnerException("模型不能为空！");
        }
        ThinkingOptions thinkingOptions = getThinkingOptions(kbAgentConfig.getParameters());
        ChatModel chatModel = aiModelService.getChatModel(
                Long.parseLong(jsonObject.getString("modelId")),
                jsonObject.getString("modelName"),
                thinkingOptions.enabled(),
                thinkingOptions.effort()
        );

        // 获取知识库
        List<ToolCallback> tools = Lists.newArrayList();
        boolean hasKnowledgeTools = false;
        if (StringUtils.isNotEmpty(kbAgentConfig.getKnowledgeIds())) {
            Set<String> idSet = StringUtils.str2Set(kbAgentConfig.getKnowledgeIds(), ",");
            List<KmcKnowledgeBaseRespDTO> knowledgeBaseList = kmcApiService
                    .getKnowledgeBaseByIds(idSet.stream().map(Long::parseLong).toList())
                    .stream()
                    .filter(knowledgeBase -> Objects.equals(kbAgentConfig.getWorkspaceId(), knowledgeBase.getWorkspaceId()))
                    .filter(knowledgeBase -> Boolean.TRUE.equals(knowledgeBase.getValidFlag()))
                    .toList();
            knowledgeBaseList.forEach(knowledgeBase -> {
                FunctionToolCallback<knowledgeQuery, String> toolCallback = FunctionToolCallback.builder("knowledgeBase" + knowledgeBase.getId(),
                         new SearchKnowledgeTool(kmcApiService, knowledgeBase.getId()))
                        .inputType(knowledgeQuery.class)
                        .description("当需要查询" + knowledgeBase.getName() + "相关的信息时调用")
                        .build();
                tools.add(toolCallback);
            });
            hasKnowledgeTools = !knowledgeBaseList.isEmpty();
        }

        List<ToolCallback> callableBotTools = agentCallableBotToolFactory.createToolCallbacks(
                kbAgentConfig.getCallableBotIds(), kbAgentConfig.getWorkspaceId(),
                kbAgentConfig.getBotId(), allowAgentCallables);
        tools.addAll(callableBotTools);

//        // 根据工具方法id，获取工具列表信息
//        String[] toolNames = new String[0];
//        if (StringUtils.isNotEmpty(kbAgentConfig.getToolMethodIds())) {
//            Set<String> methodIdSet = StringUtils.str2Set(kbAgentConfig.getToolMethodIds(), ",");
//            List<KbToolMethodDO> kbToolMethodList = kbToolMethodService.listByIds(methodIdSet);
//            toolNames = kbToolMethodList.stream().map(KbToolMethodDO::getCode).toArray(String[]::new);
//        }

        // 构建 hooks 列表
        List<Hook> hooks = Lists.newArrayList();
        hooks.add(ModelCallLimitHook.builder().runLimit(10).build());

        if (StringUtils.isNotEmpty(kbAgentConfig.getSkillIds())) {
            Set<String> skillIdSet = StringUtils.str2Set(kbAgentConfig.getSkillIds(), ",");
            List<KbSkillsDO> skillsList = kbSkillsService.listByIds(skillIdSet).stream()
                    .filter(skill -> (skill.getWorkspaceId() == null
                            || skill.getWorkspaceId().equals(kbAgentConfig.getWorkspaceId()))
                            && !Integer.valueOf(1).equals(skill.getStatus()))
                    .toList();
            if (!skillsList.isEmpty()) {
                // 从 name 中提取目录名
                Set<String> selectedDirNames = skillsList.stream()
                        .map(KbSkillsDO::getName)
                        .filter(StringUtils::isNotEmpty)
                        .collect(Collectors.toSet());
                String skillsDir = storagePath + "skills/";
                File skillsDirectory = new File(skillsDir);
                if (skillsDirectory.exists() && skillsDirectory.isDirectory()) {
                    List<String> targetSkillPaths = selectedDirNames.stream()
                            .map(dirName -> skillsDir + dirName)
                            .collect(Collectors.toList());
                    SkillRegistry targetedRegistry = new TargetedSkillRegistry(targetSkillPaths, null);
                    SkillsAgentHook hook = SkillsAgentHook.builder()
                            .skillRegistry(targetedRegistry)
                            .autoReload(true)
                            .build();
                    hooks.add(hook);
                    // 注册读取 skill references 文件的工具
                    tools.add(ReadSkillReferenceTool.buildToolCallback(skillsDir));
                    log.info("Skills 集成完成，加载目录: {}, 关联技能目录: {}", skillsDir, selectedDirNames);
                } else {
                    log.warn("Skills 目录不存在: {}", skillsDir);
                }
            }
        }

        // 按配置携带最近若干轮历史消息，让 Agent 能理解多轮对话上下文。
        List<Message> messages = buildHistoryMessages(kbAgentConfig);

        // 获取预设提示语并且替换变量
        String systemPrompt = NodeUtils.replacePlaceholder(kbAgentConfig.getPrePrompt(), kbAgentConfig.getInput());
        systemPrompt = enhanceSystemPrompt(systemPrompt, thinkingOptions);
        systemPrompt = enhanceKnowledgeSystemPrompt(systemPrompt, hasKnowledgeTools);
        systemPrompt = enhanceCallableBotSystemPrompt(systemPrompt, callableBotTools);

        messages.add(new UserMessage(kbAgentConfig.getQuestion()));
        ToolCallbackProvider mcp = mcpConfigService.getMcpAsyncCallbackProvider(
                kbAgentConfig.getMcpConfigIds(), kbAgentConfig.getWorkspaceId());

        ToolCallbackProvider toolCallbackProvider = kbToolService.getToolCallbackProvider(
                kbAgentConfig.getToolMethodIds(), kbAgentConfig.getWorkspaceId());
        // 配置agent
        ReactAgent agent = ReactAgent.builder()
                .name("my_agent")
                .model(chatModel)
                .hooks(hooks)
                .systemPrompt(systemPrompt)
//                .toolNames(toolNames)
                .toolCallbackProviders(mcp)
                .toolCallbackProviders(toolCallbackProvider)
                .toolCallbackProviders(new StaticToolCallbackProvider(tools))
                .resolver(resolver)
                .build();

        Flux<NodeOutput> stream = agent.stream(messages);
        return stream.map(output -> {
            KbChatMessageSendRespVO sendRespVO = new KbChatMessageSendRespVO();
            try {
                // 检查是否为 StreamingOutput 类型
                if (output instanceof StreamingOutput streamingOutput) {
                    OutputType type = streamingOutput.getOutputType();

                    // 处理模型推理的流式输出
                    if (type == OutputType.AGENT_MODEL_STREAMING) {
                        // 流式增量内容，逐步显示
                        String text = sanitizeModelText(streamingOutput.message().getText());
                        // 机器人回复消息
                        KbChatMessageSendRespVO.Message message = new KbChatMessageSendRespVO.Message();
                        message.setType(MessageTypeEnums.ROBOT.code);
                        message.setContent(text);
                        if ("full".equals(thinkingOptions.displayMode())) {
                            message.setReasoningContent(sanitizeModelText(
                                    extractReasoningContent(streamingOutput)));
                        }
                        message.setCreateTime(DateUtils.getNowDate());
                        sendRespVO.setReceive(message); // 接收消息
                        // 用户发送消息
                        KbChatMessageSendRespVO.Message messageUser = new KbChatMessageSendRespVO.Message();
                        messageUser.setType(MessageTypeEnums.USER.code);
                        messageUser.setContent(kbAgentConfig.getQuestion());
                        messageUser.setCreateTime(kbAgentConfig.getCreateTime());
                        sendRespVO.setSend(messageUser); // 发送消息
                        return sendRespVO;
                    } else if (type == OutputType.AGENT_MODEL_FINISHED) {
                        // 模型推理完成，可获取完整响应
                        String text = sanitizeModelText(streamingOutput.message().getText());
                        String reasoning = "full".equals(thinkingOptions.displayMode())
                                ? sanitizeModelText(extractReasoningContent(streamingOutput)) : null;
                        // 某些适配器只在完成事件附带 reasoning，补发一次，避免前端丢失。
                        if (StringUtils.isNotEmpty(reasoning)) {
                            KbChatMessageSendRespVO.Message message = new KbChatMessageSendRespVO.Message();
                            message.setType(MessageTypeEnums.ROBOT.code);
                            message.setContent(text);
                            message.setReasoningContent(reasoning);
                            message.setCreateTime(DateUtils.getNowDate());
                            sendRespVO.setReceive(message);
                            return sendRespVO;
                        }
                    }

                    // 处理工具调用完成 - 跳过详细处理避免 JSON 转换错误
                    if (type == OutputType.AGENT_TOOL_FINISHED) {
                        // 只记录工具调用完成，不访问可能导致错误的 response 数据
                        //return "\n[工具已调用]";
                    }

                    // 对于 Hook 节点，通常只关注完成事件（如果 Hook 没有有效输出可以忽略）
                    if (type == OutputType.AGENT_HOOK_FINISHED) {
                        //return "Hook 执行完成：" + output.node();
                    }
                }
            } catch (Exception e) {
                String detail = StringUtils.isNotBlank(e.getMessage())
                        ? e.getMessage() : "模型流输出解析失败";
                throw new ServiceException("Agent 执行失败：" + detail);
            }
            return sendRespVO;
        }).filter(response -> response.getReceive() != null
                && (StringUtils.isNotBlank(response.getReceive().getContent())
                || StringUtils.isNotBlank(response.getReceive().getReasoningContent())))
                .switchIfEmpty(Flux.error(new ServiceException(
                        "Agent 执行完成，但模型未返回内容，请检查模型配置、提示词或工具调用")));
    }

    @Override
    public String executeSubAgent(Long botId, String question, Long workspaceId) {
        if (botId == null || StringUtils.isEmpty(question)) {
            throw new ServiceException("子 Agent 和问题不能为空");
        }
        KbAgentConfigDO subAgentConfig = getKbAgentConfigByBotId(botId);
        if (subAgentConfig == null
                || !Objects.equals(workspaceId, subAgentConfig.getWorkspaceId())
                || Boolean.FALSE.equals(subAgentConfig.getValidFlag())) {
            throw new ServiceException("子 Agent 配置不存在、已停用或不属于当前工作空间");
        }

        KbAgentConfigReqVO request = BeanUtils.toBean(subAgentConfig, KbAgentConfigReqVO.class);
        request.setQuestion(question);
        JSONObject input = new JSONObject();
        input.put("input", question);
        request.setInput(input.toJSONString());
        request.setCreateTime(DateUtils.getNowDate());
        request.setHistoryMessages(Collections.emptyList());

        CompletableFuture<List<KbChatMessageSendRespVO>> responseFuture;
        try {
            responseFuture = chatMessage(request, false)
                    .timeout(Duration.ofSeconds(115))
                    .collectList()
                    .toFuture();
        } catch (GraphRunnerException e) {
            throw new ServiceException("子 Agent 执行失败：" + e.getMessage());
        }
        try {
            return collectSubAgentResult(responseFuture.get(120, TimeUnit.SECONDS));
        } catch (TimeoutException e) {
            responseFuture.cancel(true);
            throw new ServiceException("子 Agent 执行超时（120秒）");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ServiceException("子 Agent 执行被中断");
        } catch (ExecutionException e) {
            Throwable cause = e.getCause() == null ? e : e.getCause();
            log.error("子 Agent 执行失败: botId={}", botId, e);
            throw new ServiceException("子 Agent 执行失败：" + cause.getMessage());
        }
    }

    private String collectSubAgentResult(List<KbChatMessageSendRespVO> responses) {
        String result = "";
        if (responses != null) {
            for (KbChatMessageSendRespVO response : responses) {
                String content = response == null || response.getReceive() == null
                        ? null : response.getReceive().getContent();
                result = mergeStreamingText(result, content);
            }
        }
        String answer = removeThinkingContent(result);
        return StringUtils.isEmpty(answer) ? "子 Agent 已执行，但没有返回内容" : answer;
    }

    private String mergeStreamingText(String current, String incoming) {
        if (StringUtils.isEmpty(incoming)) {
            return current;
        }
        if (StringUtils.isEmpty(current) || incoming.startsWith(current)) {
            return incoming;
        }
        if (current.endsWith(incoming)) {
            return current;
        }
        return current + incoming;
    }

    private String removeThinkingContent(String content) {
        String result = content.replaceAll("(?s)<think(?:\\s[^>]*)?>.*?</think>", "");
        return result.replaceAll("(?s)<think(?:\\s[^>]*)?>.*$", "").trim();
    }

    private List<Message> buildHistoryMessages(KbAgentConfigReqVO kbAgentConfig) {
        List<Message> messages = Lists.newArrayList();
        List<KbAgentConfigReqVO.HistoryMessage> historyMessages = kbAgentConfig.getHistoryMessages();
        int historyRounds = getHistoryRounds(kbAgentConfig.getParameters());
        if (historyRounds == 0 || historyMessages == null || historyMessages.isEmpty()) {
            return messages;
        }

        int firstMessageIndex = Math.max(0, historyMessages.size() - historyRounds * 2);
        for (int i = firstMessageIndex; i < historyMessages.size(); i++) {
            KbAgentConfigReqVO.HistoryMessage historyMessage = historyMessages.get(i);
            if (historyMessage == null || StringUtils.isEmpty(historyMessage.getContent())) {
                continue;
            }
            if ("assistant".equals(historyMessage.getRole())) {
                messages.add(new AssistantMessage(historyMessage.getContent()));
            } else if ("user".equals(historyMessage.getRole())) {
                messages.add(new UserMessage(historyMessage.getContent()));
            }
        }
        return messages;
    }

    private int getHistoryRounds(String parameters) {
        if (StringUtils.isEmpty(parameters)) {
            return DEFAULT_HISTORY_ROUNDS;
        }
        try {
            Integer configuredRounds = JSONObject.parseObject(parameters).getInteger("historyMessageCount");
            if (configuredRounds == null) {
                return DEFAULT_HISTORY_ROUNDS;
            }
            return Math.max(0, Math.min(configuredRounds, MAX_HISTORY_ROUNDS));
        } catch (Exception e) {
            log.warn("Agent 参数配置格式错误，使用默认历史聊天轮数: {}", parameters);
            return DEFAULT_HISTORY_ROUNDS;
        }
    }

    private ThinkingOptions getThinkingOptions(String parameters) {
        if (StringUtils.isEmpty(parameters)) {
            return new ThinkingOptions(false, "medium", "hidden");
        }
        try {
            JSONObject config = JSONObject.parseObject(parameters);
            Boolean enabled = config.containsKey("thinkingEnabled")
                    ? config.getBoolean("thinkingEnabled") : null;
            String effort = normalizeOption(config.getString("reasoningEffort"),
                    Set.of("low", "medium", "high"), "medium");
            String displayMode = normalizeOption(config.getString("thinkingDisplayMode"),
                    Set.of("hidden", "summary", "full"), "hidden");
            return new ThinkingOptions(enabled, effort,
                    Boolean.TRUE.equals(enabled) ? displayMode : "hidden");
        } catch (Exception e) {
            log.warn("Agent 参数配置中的深度思考设置格式错误，使用默认设置: {}", parameters);
            return new ThinkingOptions(false, "medium", "hidden");
        }
    }

    private String normalizeOption(String value, Set<String> supportedValues, String defaultValue) {
        return supportedValues.contains(value) ? value : defaultValue;
    }

    private String enhanceSystemPrompt(String systemPrompt, ThinkingOptions thinkingOptions) {
        if (!Boolean.TRUE.equals(thinkingOptions.enabled())
                || !"summary".equals(thinkingOptions.displayMode())) {
            return systemPrompt;
        }
        String summaryInstruction = "请在最终回答中先给出简短的‘思考摘要’，只说明采用的信息、工具和结论依据，"
                + "不要披露逐步的内部推理或隐藏思维链，然后再给出正式答案。";
        return StringUtils.isEmpty(systemPrompt) ? summaryInstruction : systemPrompt + "\n\n" + summaryInstruction;
    }

    private String enhanceCallableBotSystemPrompt(String systemPrompt, List<ToolCallback> callableBotTools) {
        if (callableBotTools.isEmpty()) {
            return systemPrompt;
        }
        String callableBotMapping = callableBotTools.stream()
                .map(tool -> "- " + tool.getToolDefinition().description())
                .collect(Collectors.joining("\n"));
        String callableBotInstruction = "你可以调用已导入的工作流或专业 Agent 完成用户任务。已导入能力如下：\n"
                + callableBotMapping
                + "\n先识别用户意图，业务名称是主要识别依据，用途描述和输入参数是辅助依据；只有当需求与用途匹配时才调用最合适的能力；"
                + "必须按工具参数说明提取输入，缺少必填信息时先向用户询问，不得猜测；调用结果返回后，结合结果直接回答用户，"
                + "不得虚构未执行的调用结果。内部工具调用标识仅用于系统执行，不得在思考摘要或面向用户的回答中输出；"
                + "提及工作流或 Agent 时必须使用上方给出的业务名称。子 Agent 只负责返回任务结果，由你负责组织最终回答。";
        callableBotInstruction += " 输出 SQL 或其他代码时必须使用完整、标准的 Markdown 代码块；不要输出未闭合反引号、额外星号或把语言标识插入 SQL 内容。";
        return StringUtils.isEmpty(systemPrompt)
                ? callableBotInstruction : systemPrompt + "\n\n" + callableBotInstruction;
    }

    private String enhanceKnowledgeSystemPrompt(String systemPrompt, boolean hasKnowledgeTools) {
        if (!hasKnowledgeTools) {
            return systemPrompt;
        }
        String knowledgeInstruction = "回答知识类问题时，先调用最相关的知识库工具检索，并以检索结果为依据组织答案；"
                + "检索结果不足时明确说明知识库中未找到足够信息，不得编造。";
        return StringUtils.isEmpty(systemPrompt) ? knowledgeInstruction : systemPrompt + "\n\n" + knowledgeInstruction;
    }

    /** Remove protocol markers that must never be shown in the user-facing stream. */
    private String sanitizeModelText(String text) {
        if (StringUtils.isEmpty(text)) {
            return text;
        }
        return text
                .replaceAll("(?i)(?:workflow|agent)_\\d+", "已调用能力")
                .replaceAll("(?i)<\\|(tool_call|function_call)\\|>", "")
                .replaceAll("(?i)\\[\\s*(tool_call|function_call)\\s*\\]", "");
    }

    private String extractReasoningContent(StreamingOutput<?> streamingOutput) {
        Message message = streamingOutput.message();
        if (message != null) {
            String metadataReasoning = getFirstText(message.getMetadata(),
                    "reasoningContent", "reasoning_content", "thinking");
            if (StringUtils.isNotEmpty(metadataReasoning)) {
                return metadataReasoning;
            }
            // DeepSeek 将 reasoningContent 放在 AssistantMessage 子类属性中。
            try {
                Method getter = message.getClass().getMethod("getReasoningContent");
                Object value = getter.invoke(message);
                if (value instanceof String text && StringUtils.isNotEmpty(text)) {
                    return text;
                }
            } catch (ReflectiveOperationException ignored) {
                // 当前消息类型没有独立推理属性。
            }
        }

        // Ollama/DashScope 等适配器通常将 reasoning 放在 Generation metadata 中。
        Object originData = streamingOutput.getOriginData();
        if (originData instanceof ChatResponse chatResponse && chatResponse.getResult() != null) {
            String reasoning = getFirstTextFromGeneration(chatResponse,
                    "reasoningContent", "reasoning_content", "thinking");
            if (StringUtils.isNotEmpty(reasoning)) {
                return reasoning;
            }
            if (chatResponse.getResult().getOutput() != null) {
                return getFirstText(chatResponse.getResult().getOutput().getMetadata(),
                        "reasoningContent", "reasoning_content", "thinking");
            }
            return null;
        }
        if (originData instanceof Generation generation && generation.getOutput() != null) {
            return getFirstText(generation.getMetadata(),
                    "reasoningContent", "reasoning_content", "thinking");
        }
        if (originData instanceof Message originMessage) {
            return getFirstText(originMessage.getMetadata(),
                    "reasoningContent", "reasoning_content", "thinking");
        }
        return null;
    }

    private String getFirstText(Map<String, Object> metadata, String... keys) {
        if (metadata == null) {
            return null;
        }
        for (String key : keys) {
            Object value = metadata.get(key);
            if (value instanceof String text && StringUtils.isNotEmpty(text)) {
                return text;
            }
        }
        return null;
    }

    private String getFirstText(ChatGenerationMetadata metadata, String... keys) {
        if (metadata == null) {
            return null;
        }
        for (String key : keys) {
            Object value = metadata.get(key);
            if (value instanceof String text && StringUtils.isNotEmpty(text)) {
                return text;
            }
        }
        return null;
    }

    private String getFirstTextFromGeneration(ChatResponse chatResponse, String... keys) {
        for (String key : keys) {
            Object value = chatResponse.getResult().getMetadata().get(key);
            if (value instanceof String text && StringUtils.isNotEmpty(text)) {
                return text;
            }
        }
        return null;
    }

    private record ThinkingOptions(Boolean enabled, String effort, String displayMode) {
    }
}
