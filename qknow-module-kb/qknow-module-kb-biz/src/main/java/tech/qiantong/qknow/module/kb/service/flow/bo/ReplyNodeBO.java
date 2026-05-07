package tech.qiantong.qknow.module.kb.service.flow.bo;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.qiantong.qknow.common.core.domain.CommonResult;
import tech.qiantong.qknow.module.kb.dal.dataobject.flow.KbFlowEdgeDO;
import tech.qiantong.qknow.module.kb.dal.dataobject.flow.KbFlowNodeDO;
import tech.qiantong.qknow.module.kb.dal.enums.BotExecuteModeEnum;
import tech.qiantong.qknow.module.kb.dal.enums.BotTypeEnums;
import tech.qiantong.qknow.module.kb.dal.enums.FlowNodeTypeEnums;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReplyNodeBO extends BaseNodeBO {
    public ReplyNodeBO(KbFlowNodeDO nodeDefinition, List<KbFlowEdgeDO> edgeList) {
        super(nodeDefinition, edgeList);
    }

    /**
     * 执行具体的节点逻辑（由子类实现）
     *
     * @param inputData 输入数据
     * @param context   工作流上下文
     * @return 执行结果
     */
    @Override
    protected NodeRunResultBO executeLogic(Map<String, Object> inputData, RuntimeContextBO context) {
        KbFlowNodeDO nodeDefinition = super.getNodeDefinition();
        JSONObject contextVariables = context.getVariables();
        JSONArray outputDefArray = JSONArray.parseArray(nodeDefinition.getOutput());

        Map<String, Object> outputData = new java.util.HashMap<>(outputDefArray.size());

        if (Objects.equals(context.getBotType(), BotTypeEnums.CHAT_FLOW)) {
            this.setChatFlowFlux(nodeDefinition, context);// chatFlow 结果
        } else if (!Objects.equals(context.getExecuteMode(), BotExecuteModeEnum.BLOCK)) {
            this.setWorkFlowFlux(outputDefArray, context);// 工作流的流式结果
        } else {
            for (int i = 0; i < outputDefArray.size(); i++) {
                JSONObject outputDef = outputDefArray.getJSONObject(i);
                String sourceNodeId = outputDef.getString("sourceNodeId");
                String key = sourceNodeId + "." + outputDef.getString("variableKey");
                String value = contextVariables.getString(key);
                outputData.put(outputDef.getString("name"), value);
            }
            context.getRuntimeDO().setOutput(JSONObject.toJSONString(outputData));
        }
        // 从配置中获取提示词
        return NodeRunResultBO.success(nodeDefinition.getUuid(), nodeDefinition.getName(), outputData);
    }

    /**
     * 设置获取流式输出结果
     *
     * @param outputDefArray 工作流的输出定义
     * @param context        运行上下文
     */
    private void setWorkFlowFlux(JSONArray outputDefArray, RuntimeContextBO context) {
        JSONObject contextVariables = context.getVariables();
        Flux<CommonResult<String>> resultFlux = context.getResultFlux();
        if (Objects.isNull(resultFlux)) {
            resultFlux = Flux.just(CommonResult.success("{}"));
        }

        for (int i = 0; i < outputDefArray.size(); i++) {
            JSONObject outputDef = outputDefArray.getJSONObject(i);
            String sourceNodeId = outputDef.getString("sourceNodeId");
            String sourceNodeType = outputDef.getString("sourceNodeType");
            String key = sourceNodeId + "." + outputDef.getString("variableKey");
            String name = outputDef.getString("name");

            if (Objects.equals(sourceNodeType, FlowNodeTypeEnums.LLM.getName())) {
                Flux<String> flux = context.getFluxMap().get(sourceNodeId);
                if (!Objects.isNull(flux)) {
                    Flux<CommonResult<String>> map = flux.map(result -> {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("text", result);
                        jsonObject.put("name", name);
                        return CommonResult.success(jsonObject.toString());
                    });
                    resultFlux = Flux.concat(resultFlux,
                            map,
                            this.gapFlux(name));
                    continue;
                }
            }

            String value = contextVariables.getString(key);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("text", value);
            jsonObject.put("name", name);
            resultFlux = Flux.concat(resultFlux,
                    Flux.just(CommonResult.success(jsonObject.toString())),
                    this.gapFlux(name));
        }
        context.setResultFlux(resultFlux);
    }

    /**
     * 设置工作流的输出
     */
    private void setChatFlowFlux(KbFlowNodeDO nodeDefinition, RuntimeContextBO context) {
        JSONObject contextVariables = context.getVariables();
        JSONObject configJson = JSONObject.parseObject(nodeDefinition.getConfig());
        String resultFormat = configJson.getString("content");
        Map<String, KbFlowNodeDO> nodeMap = context.getNodeMap();
        List<String> resultList = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\{\\{\\s*(.+?)\\s*}}|[^\\s{}]+");
        Matcher matcher = pattern.matcher(resultFormat);
        while (matcher.find()) {
            resultList.add(matcher.group());
        }
        Flux<CommonResult<String>> resultFlux = Flux.just(CommonResult.success("{}"));
        for (String result : resultList) {
            if (result.startsWith("{{") && result.endsWith("}}")) {
                String key = result.substring(2, result.length() - 2).trim();
                String sourceNodeId = key.split("\\.")[0];
                KbFlowNodeDO sourceNode = nodeMap.get(sourceNodeId);
                if (sourceNode.getType().equals(FlowNodeTypeEnums.LLM.getCode())) {
                    Flux<String> flux = context.getFluxMap().get(sourceNodeId);
                    if (!Objects.isNull(flux)) {
                        Flux<CommonResult<String>> map = flux.map(segment -> {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("text", segment);
                            return CommonResult.success(jsonObject.toString());
                        });
                        resultFlux = resultFlux.concatWith(map);
                    } else {
                        String string = contextVariables.getString(key);
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("text", string);
                        resultFlux = resultFlux.concatWith(Flux.just(CommonResult.success(jsonObject.toString())));
                    }
                } else {
                    String string = contextVariables.getString(key);
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("text", string);
                    resultFlux = resultFlux.concatWith(Flux.just(CommonResult.success(jsonObject.toString())));
                }
            } else {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("text", result);
                resultFlux = resultFlux.concatWith(Flux.just(CommonResult.success(jsonObject.toString())));
            }
        }

        // 添加一条总的数据
//        Mono<CommonResult<String>> map = resultFlux
//                .map(CommonResult::getData)
//                .collectList()
//                .map(this::margeOutPutChatFlow)
//                .map(CommonResult::success);

//        context.setResultFlux(resultFlux.concatWith(map));
        context.setResultFlux(resultFlux);
    }

    /**
     * 添加间隔符
     *
     * @param name 名称
     * @return Flux
     */
    private Flux<CommonResult<String>> gapFlux(String name) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("text", "\n");
        jsonObject.put("name", name);
        return Flux.just(CommonResult.success(jsonObject.toString()));
    }

    /**
     * 合并输出结果
     *
     * @param list 输出数据列表
     * @return 合并后的结果
     */
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

    private String margeOutPutChatFlow(List<String> list) {
        StringBuilder sb = new StringBuilder();
        for (String str : list) {
            JSONObject jsonObject = JSONObject.parseObject(str);
            String text = jsonObject.getString("text");
            if (StrUtil.hasBlank(text)) {
                continue;
            }
            sb.append(text);
        }
        return sb.toString();
    }
}
