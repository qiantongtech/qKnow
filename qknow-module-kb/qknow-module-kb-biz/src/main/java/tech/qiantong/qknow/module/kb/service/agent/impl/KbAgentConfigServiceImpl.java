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
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
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
import tech.qiantong.qknow.common.core.page.PageResult;
import tech.qiantong.qknow.common.core.utils.object.BeanUtils;
import tech.qiantong.qknow.common.exception.ServiceException;
import tech.qiantong.qknow.common.utils.DateUtils;
import tech.qiantong.qknow.common.utils.StringUtils;
import tech.qiantong.qknow.module.ai.api.modelMarket.IAiModelApiService;
import tech.qiantong.qknow.module.kb.controller.admin.agent.vo.*;
import tech.qiantong.qknow.module.kb.dal.dataobject.agent.KbAgentConfigDO;
import tech.qiantong.qknow.module.kb.dal.dataobject.skills.KbSkillsDO;
import tech.qiantong.qknow.module.kb.dal.dataobject.tool.KbToolMethodDO;
import tech.qiantong.qknow.module.kb.dal.mapper.agent.KbAgentConfigMapper;
import tech.qiantong.qknow.module.kb.service.agent.IKbAgentConfigService;
import tech.qiantong.qknow.module.kb.service.agent.TargetedSkillRegistry;
import tech.qiantong.qknow.module.kb.service.agent.tool.ReadSkillReferenceTool;
import tech.qiantong.qknow.module.kb.service.skills.IKbSkillsService;
import tech.qiantong.qknow.module.kb.service.tool.IKbToolMethodService;
import tech.qiantong.qknow.module.kb.service.tool.IKbToolService;
import tech.qiantong.qknow.module.kb.tool.function.SearchKnowledgeTool;
import tech.qiantong.qknow.module.kb.tool.function.query.knowledgeQuery;
import tech.qiantong.qknow.module.kb.utils.NodeUtils;
import tech.qiantong.qknow.module.kmc.api.knowledgeBase.dto.KmcKnowledgeBaseRespDTO;
import tech.qiantong.qknow.module.kmc.api.service.IKmcApiService;
import tech.qiantong.qknow.mybatis.core.query.LambdaQueryWrapperX;

import java.io.File;
import java.util.*;
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
public class KbAgentConfigServiceImpl extends ServiceImpl<KbAgentConfigMapper, KbAgentConfigDO> implements IKbAgentConfigService {
    @Resource
    private KbAgentConfigMapper kbAgentConfigMapper;
    @Resource
    private IKbToolMethodService kbToolMethodService;
    @Resource
    private IAiModelApiService aiModelService;
    @Resource
    private IKmcApiService kmcApiService;
    @Resource
    private IKbToolService kbToolService;
    @Resource
    private IKbSkillsService kbSkillsService;
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
        KbAgentConfigDO dictType = BeanUtils.toBean(createReqVO, KbAgentConfigDO.class);
        kbAgentConfigMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateKbAgentConfig(KbAgentConfigSaveReqVO updateReqVO) {
        // 相关校验

        // 更新agent配置
        KbAgentConfigDO updateObj = BeanUtils.toBean(updateReqVO, KbAgentConfigDO.class);
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
     * @param operName        操作用户
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
                Long kbAgentConfigId = respVO.getId();
                if (isUpdateSupport) {
                    if (kbAgentConfigId != null) {
                        KbAgentConfigDO existingKbAgentConfig = kbAgentConfigMapper.selectById(kbAgentConfigId);
                        if (existingKbAgentConfig != null) {
                            kbAgentConfigMapper.updateById(kbAgentConfigDO);
                            successNum++;
                            successMessages.add("数据更新成功，ID为 " + kbAgentConfigId + " 的agent配置记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，ID为 " + kbAgentConfigId + " 的agent配置记录不存在。");
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
        // 获取模型配置
        String modelConfig = kbAgentConfig.getModelConfig();
        if (StringUtils.isNull(modelConfig)) {
            throw new GraphRunnerException("模型配置不能为空！");
        }
        JSONObject jsonObject = JSONObject.parseObject(modelConfig);
        if (StringUtils.isNull(jsonObject.getString("modelId")) || StringUtils.isNull(jsonObject.getString("modelName"))) {
            throw new GraphRunnerException("模型不能为空！");
        }
        ChatModel chatModel = aiModelService.getChatModel(
                Long.parseLong(jsonObject.getString("modelId")),
                jsonObject.getString("modelName")
        );

        // 获取知识库
        List<ToolCallback> tools = Lists.newArrayList();
        if (StringUtils.isNotEmpty(kbAgentConfig.getKnowledgeIds())) {
            Set<String> idSet = StringUtils.str2Set(kbAgentConfig.getKnowledgeIds(), ",");
            List<KmcKnowledgeBaseRespDTO> knowledgeBaseList = kmcApiService.getKnowledgeBaseByIds(idSet.stream().map(Long::parseLong).toList());
            knowledgeBaseList.forEach(knowledgeBase -> {
                FunctionToolCallback<knowledgeQuery, String> toolCallback = FunctionToolCallback.builder("knowledgeBase" + knowledgeBase.getId(),
                                new SearchKnowledgeTool(kmcApiService, knowledgeBase.getId()))
                        .inputType(knowledgeQuery.class)
                        .description("当需要查询" + knowledgeBase.getName() + "相关的信息时调用")
                        .build();
                tools.add(toolCallback);
            });
        }

        // 根据工具方法id，获取工具列表信息
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
            List<KbSkillsDO> skillsList = kbSkillsService.listByIds(skillIdSet);
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

        // TODO 获取历史聊天记录，构建历史对话的Prompt
        List<Message> messages = Lists.newArrayList();

        // 获取预设提示语并且替换变量
        String systemPrompt = NodeUtils.replacePlaceholder(kbAgentConfig.getPrePrompt(), kbAgentConfig.getInput());

        messages.add(new UserMessage(kbAgentConfig.getQuestion()));

        ToolCallbackProvider toolCallbackProvider = kbToolService.getToolCallbackProvider(kbAgentConfig.getToolMethodIds());
        // 配置agent
        ReactAgent agent = ReactAgent.builder()
                .name("my_agent")
                .model(chatModel)
                // 限制最多调用 5 次
                .hooks(hooks)
                .systemPrompt(systemPrompt)
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
                        String text = streamingOutput.message().getText();
                        // 机器人回复消息
                        KbChatMessageSendRespVO.Message message = new KbChatMessageSendRespVO.Message();
                        message.setType(MessageTypeEnums.ROBOT.code);
                        message.setContent(text);
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
                        String text = streamingOutput.message().getText();
                        //return "\n模型输出完成：" + (text != null ? text : "");
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
                // 捕获任何异常并返回友好的错误信息
            }
            return sendRespVO;
        });
    }
}
