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

package tech.qiantong.qknow.module.kb.service.flow.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.qiantong.qknow.common.exception.ServiceException;
import tech.qiantong.qknow.common.core.domain.CommonResult;
import tech.qiantong.qknow.common.core.utils.object.BeanUtils;
import tech.qiantong.qknow.module.kb.api.flow.util.BotUtil;
import tech.qiantong.qknow.module.kb.controller.admin.flow.vo.KbFlowVO;
import tech.qiantong.qknow.module.kb.controller.admin.runtime.vo.KbRuntimeRespVO;
import tech.qiantong.qknow.module.kb.convert.flow.KbFlowConvert;
import tech.qiantong.qknow.module.kb.dal.dataobject.flow.KbFlowEdgeDO;
import tech.qiantong.qknow.module.kb.dal.dataobject.flow.KbFlowNodeDO;
import tech.qiantong.qknow.module.kb.dal.enums.BotExecuteModeEnum;
import tech.qiantong.qknow.module.kb.dal.enums.BotTypeEnums;
import tech.qiantong.qknow.module.kb.dal.enums.FlowNodeTypeEnums;
import tech.qiantong.qknow.module.kb.dal.enums.RuntimeStatusEnums;
import tech.qiantong.qknow.module.kb.service.flow.IKbFlowEdgeService;
import tech.qiantong.qknow.module.kb.service.flow.IKbFlowNodeService;
import tech.qiantong.qknow.module.kb.service.flow.IKbFlowService;
import tech.qiantong.qknow.module.kb.service.flow.bo.BaseNodeBO;
import tech.qiantong.qknow.module.kb.service.flow.bo.KbFlowBO;
import tech.qiantong.qknow.module.kb.service.flow.bo.NodeRunResultBO;
import tech.qiantong.qknow.module.kb.service.flow.bo.RuntimeContextBO;
import tech.qiantong.qknow.module.kb.service.flow.factory.NodeFactory;
import tech.qiantong.qknow.module.kb.service.runtime.IKbRuntimeService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * bot流程节点Service业务层处理
 *
 * @author qknow
 * @date 2026-03-18
 */
@Slf4j
@Service
public class KbFlowServiceImpl implements IKbFlowService {

    @Resource
    private IKbFlowNodeService flowNodeService;
    @Resource
    private IKbFlowEdgeService flowEdgeService;
    @Resource
    private IKbRuntimeService runtimeService;
    @Resource
    private NodeFactory nodeFactory;

    /**
     * 根据 BotId 查询流程
     *
     * @param botId botId
     * @return 流程对象
     */
    @Override
    public KbFlowVO queryFlow(Long botId) {
        KbFlowVO result = new KbFlowVO();
        result.setBotId(botId);
        result.setNodes(flowNodeService.flowVOByBotId(botId));
        result.setEdges(flowEdgeService.flowVOByBotId(botId));
        return result;
    }

    /**
     * 根据 BotId 删除流程
     *
     * @param botId botId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByBotId(Long botId) {
        flowNodeService.removeByBotId(botId);
        flowEdgeService.removeByBotId(botId);
    }

    /**
     * 创建流程
     *
     * @param flowVO 流程对象
     * @return 操作是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean submitFlow(KbFlowVO flowVO) {
        boolean nodeResult = flowNodeService.submitBatch(flowVO);
        boolean edgeResult = flowEdgeService.submitBatch(flowVO);
        return nodeResult & edgeResult;
    }

    /**
     * 测试执行流程
     *
     * @param flowVO 流程对象
     * @param input  输入参数
     * @return 执行结果
     */
    @Override
    public Flux<CommonResult<String>> testExecuteFlow(KbFlowVO flowVO, JSONObject input) {
        KbFlowBO kbFlowBO = KbFlowConvert.toFlowBO(flowVO);
        RuntimeContextBO runtimeContext = runtimeService.createRuntimeContext(kbFlowBO, input);
        runtimeContext.setBotType(BotTypeEnums.WORK_FLOW);
        runtimeContext.setExecuteMode(BotExecuteModeEnum.STREAM);
        textExecuteFlow(kbFlowBO, runtimeContext);
//        Flux<CommonResult<String>> resultFlux = runtimeContext.getResultFlux();
//        // 添加一条总的数据
//        Mono<CommonResult<String>> map = resultFlux
//                .map(CommonResult::getData)
//                .collectList()
//                .map(this::margeOutPut)
//                .map(CommonResult::success);
//        runtimeContext.setResultFlux(Flux.concat(resultFlux, map));
        return runtimeContext.getResultFlux().onErrorResume(throwable ->
                Flux.just(CommonResult.error(new ServiceException("大模型调用异常", 500))));
    }

    /**
     * 测试运行 chatFlow
     *
     * @param flowVO 流程对象
     * @param input  输入参数
     * @return 执行结果
     */
    @Override
    public Flux<CommonResult<String>> testExecuteChatFlow(KbFlowVO flowVO, JSONObject input, JSONArray messageArray) {
        KbFlowBO kbFlowBO = KbFlowConvert.toFlowBO(flowVO);
        RuntimeContextBO runtimeContext = runtimeService.createRuntimeContext(kbFlowBO, input);
        runtimeContext.setExecuteMode(BotExecuteModeEnum.STREAM);
        runtimeContext.setBotType(BotTypeEnums.CHAT_FLOW);
        if (Objects.nonNull(messageArray) && messageArray.size() > 0) {
            List<Message> messageList = new ArrayList<>(messageArray.size());
            for (int i = 0; i < messageArray.size(); i++) {
                JSONObject messageJson = messageArray.getJSONObject(i);
                String role = messageJson.getString("role");
                String context = messageJson.getString("context");
                if (Objects.equals(role, "assistant")) {
                    messageList.add(new AssistantMessage(context));
                } else {
                    messageList.add(new UserMessage(context));
                }
            }
            runtimeContext.setMessageList(messageList);
        }
        textExecuteFlow(kbFlowBO, runtimeContext);
        return runtimeContext.getResultFlux().onErrorResume(throwable ->
                Flux.just(CommonResult.error(new ServiceException("大模型调用异常", 500)))
        );
    }

    /**
     * 执行流程
     *
     * @param flowVO 流程对象
     * @param input  输入参数
     * @return 执行结果
     */
    @Override
    public KbRuntimeRespVO executeFlow(KbFlowVO flowVO, JSONObject input) {
        KbFlowBO kbFlowBO = KbFlowConvert.toFlowBO(flowVO);
        kbFlowBO.setNodeList(flowNodeService.listByBotId(flowVO.getBotId()));
        kbFlowBO.setEdgeList(flowEdgeService.listByBotId(flowVO.getBotId()));
        RuntimeContextBO runtimeContext = runtimeService.createSaveRuntimeContext(kbFlowBO, input);
        runtimeContext.setExecuteMode(BotExecuteModeEnum.BLOCK);
        runtimeContext.setBotType(BotTypeEnums.WORK_FLOW);
        return executeFlow(kbFlowBO, runtimeContext);
    }

    /**
     * 正式执行流程(流式输出)
     *
     * @param flowVO 流程对象
     * @param input  输入参数
     * @return 执行结果
     */
    @Override
    public Flux<CommonResult<String>> executeFlowStream(KbFlowVO flowVO, JSONObject input) {
        KbFlowBO kbFlowBO = KbFlowConvert.toFlowBO(flowVO);
        kbFlowBO.setNodeList(flowNodeService.listByBotId(flowVO.getBotId()));
        kbFlowBO.setEdgeList(flowEdgeService.listByBotId(flowVO.getBotId()));
        RuntimeContextBO runtimeContext = runtimeService.createSaveRuntimeContext(kbFlowBO, input);
        runtimeContext.setExecuteMode(BotExecuteModeEnum.STREAM);
        runtimeContext.setBotType(BotTypeEnums.WORK_FLOW);
        executeFlow(kbFlowBO, runtimeContext);

        Mono<CommonResult<String>> last = runtimeContext.getResultFlux().last();
        last.subscribe(result -> {
            runtimeContext.getRuntimeDO().setOutput(result.getData());
            runtimeService.saveRunSuccess(runtimeContext.getRuntimeDO());
        });
        return runtimeContext.getResultFlux().onErrorResume(throwable ->
                Flux.just(CommonResult.error(new ServiceException("大模型调用异常", 500)))
        );
    }

    /**
     * 正式运行 chatFlow
     *
     * @param flowVO      流程数据
     * @param input       输入参数
     * @param messageList 消息列表
     * @return 执行结果
     */
    @Override
    public Flux<CommonResult<String>> executeChatFlow(KbFlowVO flowVO, JSONObject input, List<Message> messageList) {
        KbFlowBO kbFlowBO = KbFlowConvert.toFlowBO(flowVO);
        kbFlowBO.setNodeList(flowNodeService.listByBotId(flowVO.getBotId()));
        kbFlowBO.setEdgeList(flowEdgeService.listByBotId(flowVO.getBotId()));
        RuntimeContextBO runtimeContext = runtimeService.createSaveRuntimeContext(kbFlowBO, input);
        runtimeContext.setExecuteMode(BotExecuteModeEnum.STREAM);
        runtimeContext.setBotType(BotTypeEnums.CHAT_FLOW);
        runtimeContext.setMessageList(messageList);
        executeFlow(kbFlowBO, runtimeContext);
        StringBuilder sb = new StringBuilder();
        return runtimeContext.getResultFlux()
                .doOnNext(result -> {
                    String chatResponseContent = BotUtil.getChatResponseContent(result);
                    sb.append(chatResponseContent);
                })
                .doOnComplete(() -> {
                    runtimeContext.getRuntimeDO().setOutput(sb.toString());
                    runtimeService.saveRunSuccess(runtimeContext.getRuntimeDO());
                })
                .onErrorResume(throwable ->
                        Flux.just(CommonResult.error(new ServiceException("大模型调用异常", 500)))
                );
    }

    /**
     * 执行流程
     *
     * @param flowBO 流程运行对象
     * @return 执行结果
     */
    public KbRuntimeRespVO executeFlow(KbFlowBO flowBO, RuntimeContextBO runtimeContext) {
        log.info("{}", runtimeContext);
        // 校验工作流
        validateWorkflow(flowBO);
        // 找到开始节点
        KbFlowNodeDO startNode = findStartNode(flowBO.getNodeList());
        // 将节点列表转为map，方便后期使用，key 是 uuid，value 是节点对象
        Map<String, KbFlowNodeDO> nodeMap = flowBO.getNodeList().stream()
                .collect(Collectors.toMap(KbFlowNodeDO::getUuid, node -> node, (existing, replacement) -> replacement));
        runtimeContext.setNodeMap(nodeMap);
        Queue<String> nodeQueue = new LinkedList<>();// 等待执行节点的Uuid
        Set<String> executedNodes = ConcurrentHashMap.newKeySet();// 已经执行过的节点
        nodeQueue.offer(startNode.getUuid());
        int step = 1;
        while (!nodeQueue.isEmpty()) {
            String nodeUuid = nodeQueue.poll();
            // 跳过已执行的节点（避免循环）
            if (executedNodes.contains(nodeUuid)) {
                log.debug("节点 {} 已执行过，跳过", nodeUuid);
                continue;
            }
            KbFlowNodeDO currentNodeDef = nodeMap.get(nodeUuid);
            // 创建节点实例
            BaseNodeBO node = nodeFactory.createNode(currentNodeDef, flowBO.getEdgeList());
            // 执行节点
            NodeRunResultBO nodeResult = node.execute(runtimeContext);
            // 标记为已执行
            executedNodes.add(nodeUuid);
            nodeResult.setStep(step++);

            runtimeService.saveRuntimeNode(nodeResult, runtimeContext);

            // 如果执行失败，终止工作流
            if (Objects.equals(RuntimeStatusEnums.ERROR.getCode(), nodeResult.getStatus())) {
                log.error("节点执行失败：{}, 错误：{}", currentNodeDef.getName(), nodeResult.getErrorMessage());
                // 更新运行状态
                runtimeService.saveRunError(runtimeContext.getRuntimeDO());
                return BeanUtils.toBean(runtimeContext.getRuntimeDO(), KbRuntimeRespVO.class);
            }

            // 将下一个节点加入队列
            List<String> nextNodeIdList = nodeResult.getNextNodeIds();
            if (CollUtil.isNotEmpty(nextNodeIdList)) {
                log.debug("添加下一个节点到队列：{}", nextNodeIdList);
                for (String nextNodeUuId : nextNodeIdList) {
                    // 检查节点是否存在
                    boolean exists = nodeMap.containsKey(nextNodeUuId);
                    if (exists) {
                        nodeQueue.offer(nextNodeUuId);
                    } else {
                        log.warn("下一个节点不存在：{}", nextNodeUuId);
                    }
                }
            }
        }

        // 修改 运行实例状态
        runtimeService.saveRunSuccess(runtimeContext.getRuntimeDO());
        log.info("========== 工作流执行完成：{} ==========", flowBO.getBotId());
        // 更新运行状态
        return BeanUtils.toBean(runtimeContext.getRuntimeDO(), KbRuntimeRespVO.class);
    }

    /**
     * 测试执行流程
     *
     * @param flowBO 流程运行对象
     * @return 执行结果
     */
    public KbRuntimeRespVO textExecuteFlow(KbFlowBO flowBO, RuntimeContextBO runtimeContext) {
        log.info("{}", runtimeContext);
        // 校验工作流
        validateWorkflow(flowBO);
        // 找到开始节点
        KbFlowNodeDO startNode = findStartNode(flowBO.getNodeList());
        // 将节点列表转为map，方便后期使用，key 是 uuid，value 是节点对象
        Map<String, KbFlowNodeDO> nodeMap = flowBO.getNodeList().stream()
                .collect(Collectors.toMap(KbFlowNodeDO::getUuid, node -> node, (existing, replacement) -> replacement));
        runtimeContext.setNodeMap(nodeMap);
        Queue<String> nodeQueue = new LinkedList<>();// 等待执行节点的Uuid
        Set<String> executedNodes = ConcurrentHashMap.newKeySet();// 已经执行过的节点
        nodeQueue.offer(startNode.getUuid());
        int step = 1;
        while (!nodeQueue.isEmpty()) {
            String nodeUuid = nodeQueue.poll();
            // 跳过已执行的节点（避免循环）
            if (executedNodes.contains(nodeUuid)) {
                log.debug("节点 {} 已执行过，跳过", nodeUuid);
                continue;
            }
            KbFlowNodeDO currentNodeDef = nodeMap.get(nodeUuid);
            // 创建节点实例
            BaseNodeBO node = nodeFactory.createNode(currentNodeDef, flowBO.getEdgeList());
            // 执行节点
            NodeRunResultBO nodeResult = node.execute(runtimeContext);
            // 标记为已执行
            executedNodes.add(nodeUuid);
            nodeResult.setStep(step++);

            // 如果执行失败，终止工作流
            if (Objects.equals(RuntimeStatusEnums.ERROR.getCode(), nodeResult.getStatus())) {
                log.error("节点执行失败：{}, 错误：{}", currentNodeDef.getName(), nodeResult.getErrorMessage());
                runtimeContext.getRuntimeDO().setOutput(nodeResult.getErrorMessage());
                return BeanUtils.toBean(runtimeContext.getRuntimeDO(), KbRuntimeRespVO.class);
            }

            // 将下一个节点加入队列
            List<String> nextNodeIdList = nodeResult.getNextNodeIds();
            if (CollUtil.isNotEmpty(nextNodeIdList)) {
                log.debug("添加下一个节点到队列：{}", nextNodeIdList);
                for (String nextNodeUuId : nextNodeIdList) {
                    // 检查节点是否存在
                    boolean exists = nodeMap.containsKey(nextNodeUuId);
                    if (exists) {
                        nodeQueue.offer(nextNodeUuId);
                    } else {
                        log.warn("下一个节点不存在：{}", nextNodeUuId);
                    }
                }
            }
        }
        log.info("========== 工作流执行完成：{} ==========", flowBO.getBotId());
        // 更新运行状态
        return BeanUtils.toBean(runtimeContext.getRuntimeDO(), KbRuntimeRespVO.class);
    }

    /**
     * 验证工作流配置
     */
    private void validateWorkflow(KbFlowBO flowBO) {
        if (CollUtil.isEmpty(flowBO.getNodeList())) {
            throw new ServiceException("工作流必须包含至少一个节点");
        }

        // 检查是否有且仅有一个开始节点
        long startNodeCount = flowBO.getNodeList().stream()
                .filter(n -> Objects.equals(FlowNodeTypeEnums.START.getCode(), n.getType()))
                .count();

        if (startNodeCount != 1) {
            throw new ServiceException("工作流必须有且只有一个开始节点");
        }

        // 检查节点 ID 是否唯一
        Set<String> nodeIdSet = new HashSet<>();
        for (KbFlowNodeDO node : flowBO.getNodeList()) {
            if (!nodeIdSet.add(node.getUuid())) {
                throw new ServiceException("节点 ID 重复：" + node.getId());
            }
        }

        // 检查边的引用是否有效
        if (CollUtil.isEmpty(flowBO.getEdgeList())) {
            return;
        }
        for (KbFlowEdgeDO edge : flowBO.getEdgeList()) {
            String sourceId = edge.getSourceNodeUuid();
            String targetId = edge.getTargetNodeUuid();

            if (!nodeIdSet.contains(sourceId)) {
                throw new IllegalArgumentException("边的源节点不存在：" + sourceId);
            }
            if (!nodeIdSet.contains(targetId)) {
                throw new IllegalArgumentException("边的目标节点不存在：" + targetId);
            }
        }
    }

    /**
     * 查找开始节点
     *
     * @param nodes 节点集合
     * @return 开始节点
     */
    private KbFlowNodeDO findStartNode(List<KbFlowNodeDO> nodes) {
        return nodes.stream()
                .filter(n -> Objects.equals(FlowNodeTypeEnums.START.getCode(), n.getType()))
                .findFirst()
                .orElse(null);
    }

    /**
     * 参数校验
     * JSONObject input 输入参数
     *
     * @return true 表示校验通过
     */
    private boolean validateParam(JSONObject input) {
        Set<String> keySet = input.keySet();
        for (String key : keySet) {
            String value = input.getString(key);

            if (value == null) {
                log.error("参数 {} 为空", key);
                return false;
            }
        }
        return true;
    }

    private String margeOutPut(List<String> list) {
        Map<String, StringBuilder> sbMap = new HashMap<>();
        for (String str : list) {
            JSONObject jsonObject = JSONObject.parseObject(str);
            String key = jsonObject.getString("name");
            String text = jsonObject.getString("text");
            if (StrUtil.hasBlank(key, text)) {
                continue;
            }
            StringBuilder sb = sbMap.get(key);
            if (Objects.isNull(sb)) {
                sb = new StringBuilder();
            }
            sb.append(text);
            sbMap.put(key, sb);
        }
        return JSONObject.toJSONString(sbMap);
    }
}
