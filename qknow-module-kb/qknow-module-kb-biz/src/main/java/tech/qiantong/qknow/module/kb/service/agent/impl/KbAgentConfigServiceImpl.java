/*
 * Copyright (c) 2026 Jiangsu Qiantong Technology Co., Ltd.
 *  *
 * Software Name: qKnow Knowledge Platform (Business Edition)
 * Software Copyright Registration No. 15980140
 *  *
 * [RIGHTS AND LICENSE STATEMENT]
 * This file contains non-public commercial source code of which Jiangsu Qiantong
 * Technology Co., Ltd. lawfully possesses complete intellectual property rights.
 *  *
 * Access and use are limited to entities or individuals who have signed a valid
 * commercial license agreement, within the scope stipulated in the agreement.
 * The "accessibility" of this source code is premised on lawful authorization
 * and does not constitute any form of transfer of intellectual property rights
 * or implied licensing.
 *  *
 * [PROHIBITIONS]
 * Unless explicitly agreed in the license agreement, the following acts in any
 * form are strictly prohibited:
 * 1. Copying, disseminating, disclosing, selling, renting, or redistributing
 * this source code;
 * 2. Providing the software's functionality to third parties via SaaS, PaaS,
 * cloud hosting, or other means;
 * 3. Using this software or its derivative versions to develop products that
 * compete with the Right Holder;
 * 4. Providing or displaying this source code or related technical information
 * to unauthorized third parties;
 * 5. Tampering with, circumventing, or destroying copyright notices, license
 * verifications, or other technical protection measures.
 *  *
 * [LEGAL LIABILITY]
 * Any unauthorized use constitutes an infringement of trade secrets and
 * intellectual property rights.
 *  *
 * The Right Holder will strictly pursue liability for breach of contract and
 * infringement in accordance with the commercial agreement and laws such as
 * the "Copyright Law of the People's Republic of China" and the "Anti-Unfair
 * Competition Law".
 *  *
 * ============================================================================
 *  *
 * Copyright (c) 2026 江苏千桐科技有限公司
 *  *
 * 软件名称：qKnow 知识平台（商业版） | 软著登字第15980140号
 *  *
 * 【权利与授权声明】
 * 本文件属于江苏千桐科技有限公司依法享有完全知识产权的非公开商业源代码。
 * 仅限已签署有效商业授权合同的单位或个人在约定范围内查阅和使用。
 * 源代码的“可访问性”均以合法授权为前提，不构成任何形式的知识产权转让或默示授权。
 *  *
 * 【禁止事项】
 * 除授权合同明确约定外，严禁任何形式的：
 * 1. 复制、传播、披露、出售、出租或再分发本源代码；
 * 2. 通过 SaaS、PaaS、云托管等方式向第三方提供本软件功能；
 * 3. 将本软件或其衍生版本用于开发与权利人构成竞争的产品；
 * 4. 向未授权第三方提供或展示本源代码或相关技术信息；
 * 5. 篡改、规避或破坏版权标识、授权校验及其他技术保护措施。
 *  *
 * 【法律责任】
 * 任何未经授权的利用行为，均构成对商业秘密及知识产权的侵害。
 * 权利人将依据商业合同及《中华人民共和国著作权法》《反不正当竞争法》
 * 等法律法规，严厉追究违约与侵权责任。
 */

package tech.qiantong.qknow.module.kb.service.agent.impl;

import com.alibaba.cloud.ai.graph.NodeOutput;
import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import com.alibaba.cloud.ai.graph.agent.hook.modelcalllimit.ModelCallLimitHook;
import com.alibaba.cloud.ai.graph.exception.GraphRunnerException;
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
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.function.FunctionToolCallback;
import org.springframework.ai.tool.resolution.ToolCallbackResolver;
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
import tech.qiantong.qknow.module.kb.dal.dataobject.tool.KbToolMethodDO;
import tech.qiantong.qknow.module.kb.dal.mapper.agent.KbAgentConfigMapper;
import tech.qiantong.qknow.module.kb.service.agent.IKbAgentConfigService;
import tech.qiantong.qknow.module.kb.service.tool.IKbToolMethodService;
import tech.qiantong.qknow.module.kb.tool.function.SearchKnowledgeTool;
import tech.qiantong.qknow.module.kb.tool.function.query.knowledgeQuery;
import tech.qiantong.qknow.module.kb.utils.NodeUtils;
import tech.qiantong.qknow.module.kmc.api.knowledgeBase.dto.KmcKnowledgeBaseRespDTO;
import tech.qiantong.qknow.module.kmc.api.service.IKmcApiService;
import tech.qiantong.qknow.mybatis.core.query.LambdaQueryWrapperX;

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
public class KbAgentConfigServiceImpl  extends ServiceImpl<KbAgentConfigMapper,KbAgentConfigDO> implements IKbAgentConfigService {
    @Resource
    private KbAgentConfigMapper kbAgentConfigMapper;
    @Resource
    private IKbToolMethodService kbToolMethodService;
    @Resource
    private IAiModelApiService aiModelService;
    @Resource
    private IKmcApiService kmcApiService;

    @Resource
    private ToolCallbackResolver resolver;

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
        String[] toolNames = new String[0];
        if (StringUtils.isNotEmpty(kbAgentConfig.getToolMethodIds())) {
            Set<String> methodIdSet = StringUtils.str2Set(kbAgentConfig.getToolMethodIds(), ",");
            List<KbToolMethodDO> kbToolMethodList = kbToolMethodService.listByIds(methodIdSet);
            toolNames = kbToolMethodList.stream().map(KbToolMethodDO::getCode).toArray(String[]::new);
        }

        // TODO 获取历史聊天记录，构建历史对话的Prompt
        List<Message> messages = Lists.newArrayList();

        // 获取预设提示语并且替换变量
        String systemPrompt = NodeUtils.replacePlaceholder(kbAgentConfig.getPrePrompt(), kbAgentConfig.getInput());

        messages.add(new UserMessage(kbAgentConfig.getQuestion()));

        // 配置agent
        ReactAgent agent = ReactAgent.builder()
                .name("my_agent")
                .model(chatModel)
                // 限制最多调用 5 次
                .hooks(ModelCallLimitHook.builder().runLimit(10).build())
                .systemPrompt(systemPrompt)
                .toolNames(toolNames)
                .tools(tools)
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
