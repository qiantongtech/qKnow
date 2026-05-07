package tech.qiantong.qknow.module.kb.service.flow.bo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import tech.qiantong.qknow.module.kb.dal.dataobject.flow.KbFlowEdgeDO;
import tech.qiantong.qknow.module.kb.dal.dataobject.flow.KbFlowNodeDO;
import tech.qiantong.qknow.module.kb.dal.enums.RuntimeStatusEnums;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 节点抽象基类
 * 所有节点类型都需要继承此类
 */
@Slf4j
@Data
public abstract class BaseNodeBO {
    /**
     * 节点定义
     */
    private KbFlowNodeDO nodeDefinition;

    /**
     * 所有边列表（用于查找下一个节点）
     */
    private List<KbFlowEdgeDO> edgeList;

    /**
     * 构造函数
     *
     * @param nodeDefinition 节点定义
     * @param edgeList       所有边列表
     */
    public BaseNodeBO(KbFlowNodeDO nodeDefinition, List<KbFlowEdgeDO> edgeList) {
        this.nodeDefinition = nodeDefinition;
        this.edgeList = edgeList;
    }

    /**
     * 准备输入数据（根据 inputMapping 从上下文中获取）
     */
    protected Map<String, Object> prepareInput(RuntimeContextBO context) {
        JSONObject contextVariables = context.getVariables();
        JSONArray inputJsonDefArray = JSONArray.parseArray(nodeDefinition.getInput());
        Map<String, Object> inputData = new java.util.HashMap<>(inputJsonDefArray.size());
        for (int i = 0; i < inputJsonDefArray.size(); i++) {
            JSONObject inputJsonDef = inputJsonDefArray.getJSONObject(i);
            String inputName = inputJsonDef.getString("name");
            if (StrUtil.isBlank(inputName)){
                continue;
            }
            inputData.put(inputName, contextVariables.get(inputName));
        }

        return inputData;
    }

    /**
     * 处理输出映射（将结果存入上下文）
     */
    protected void processOutput(Map<String, Object> resultData, RuntimeContextBO context) {
        if (CollUtil.isEmpty(resultData)) {
            log.info("节点 {} 没有输出定义，将结果存入上下文", nodeDefinition.getName());
            return;
        }
        JSONObject contextVariables = context.getVariables();
        contextVariables.putAll(resultData);
    }

    /**
     * 查找下一个要执行的节点
     */
    protected List<String> findNextNodes(NodeRunResultBO result, RuntimeContextBO context) {
        // 如果结果中已经指定了下一个节点 ID（如 IF 节点），直接使用
        if (result.getNextNodeIds() != null && !result.getNextNodeIds().isEmpty()) {
            return result.getNextNodeIds();
        }

        // 否则查找所有指向的下一个节点
        return edgeList.stream()
                .filter(edge -> Objects.equals(edge.getSourceNodeUuid(), nodeDefinition.getUuid()))
                .map(KbFlowEdgeDO::getTargetNodeUuid)
                .collect(Collectors.toList());
    }

    /**
     * 执行节点的模板方法
     * 子类需要实现具体的 executeLogic 方法
     */
    public NodeRunResultBO execute(RuntimeContextBO context) {
        long startTime = System.currentTimeMillis();

        try {
            log.info("开始执行节点：{} ({})", nodeDefinition.getName(), nodeDefinition.getType());

            // 1. 准备输入数据
            Map<String, Object> inputData = prepareInput(context);

            // 2. 执行具体的节点逻辑
            NodeRunResultBO result = executeLogic(inputData, context);

            // 3. 处理输出映射
            if (Objects.equals(RuntimeStatusEnums.SUCCESS.getCode(), result.getStatus())
                    && CollUtil.isNotEmpty(result.getOutput())) {
                processOutput(result.getOutput(), context);
            }

            // 4. 确定下一个要执行的节点
            List<String> nextNodeIds = findNextNodes(result, context);
            result.setNextNodeIds(nextNodeIds);

            // 5. 设置执行耗时
            long duration = System.currentTimeMillis() - startTime;
            result.setDuration(duration);

            log.info("节点执行完成：{}, 耗时：{}ms", nodeDefinition.getName(), duration);
            result.setInput(inputData);
            return result;

        } catch (Exception e) {
            log.error("节点执行失败：{}", nodeDefinition.getName(), e);
//            long duration = System.currentTimeMillis() - startTime;
            return NodeRunResultBO.failure(nodeDefinition.getUuid(), nodeDefinition.getName(), e.getMessage());
        }
    }

    /**
     * 将含有参数的字符串进行格式化
     *
     * @param str 待格式化的字符串,参数格式：{{#xxx#}}
     * @return 格式化后的字符串
     */
    public String format(String str, RuntimeContextBO context) {
        JSONObject contextVariables = context.getVariables();
        Pattern pattern = Pattern.compile("\\{\\{\\s*(.+?)\\s*}}");
        Matcher matcher = pattern.matcher(str);
        if (!matcher.find()) {
            return str;
        }
        matcher.reset();
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            String key = matcher.group(1);
            String replacement = contextVariables.getString(key);
            if (StrUtil.isEmpty(replacement)) {
                continue;
            }
            matcher.appendReplacement(sb, replacement);
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 执行具体的节点逻辑（由子类实现）
     *
     * @param inputData 输入数据
     * @param context   工作流上下文
     * @return 执行结果
     */
    protected abstract NodeRunResultBO executeLogic(Map<String, Object> inputData, RuntimeContextBO context);
}
