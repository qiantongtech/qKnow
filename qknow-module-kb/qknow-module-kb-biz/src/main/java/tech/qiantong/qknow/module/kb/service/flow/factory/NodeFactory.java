package tech.qiantong.qknow.module.kb.service.flow.factory;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tech.qiantong.qknow.common.exception.ServiceException;
import tech.qiantong.qknow.module.ai.api.modelMarket.IAiModelApiService;
import tech.qiantong.qknow.module.kb.dal.dataobject.flow.KbFlowEdgeDO;
import tech.qiantong.qknow.module.kb.dal.dataobject.flow.KbFlowNodeDO;
import tech.qiantong.qknow.module.kb.dal.enums.FlowNodeTypeEnums;
import tech.qiantong.qknow.module.kb.service.flow.bo.BaseNodeBO;
import tech.qiantong.qknow.module.kb.service.flow.bo.LLMNodeBO;
import tech.qiantong.qknow.module.kb.service.flow.bo.ReplyNodeBO;
import tech.qiantong.qknow.module.kb.service.flow.bo.StartNodeBO;

import java.util.List;
import java.util.Objects;

/**
 * 节点创建工厂
 */
@Slf4j
@Component
public class NodeFactory {

    @Resource
    private IAiModelApiService aiModelService;

    /**
     * 根据节点定义创建节点实例
     *
     * @param nodeDefinition 节点定义
     * @param edgeList       所有边列表
     * @return 节点实例
     */
    public BaseNodeBO createNode(KbFlowNodeDO nodeDefinition, List<KbFlowEdgeDO> edgeList) {
        FlowNodeTypeEnums nodeType = FlowNodeTypeEnums.getByCode(nodeDefinition.getType());

        if (Objects.isNull(nodeType)) {
            throw new ServiceException("节点类型不能为空");
        }

        log.info("创建节点实例：{}", nodeDefinition.getName());

        return switch (nodeType) {
            case START -> new StartNodeBO(nodeDefinition, edgeList);
            case LLM -> new LLMNodeBO(nodeDefinition, edgeList, aiModelService);
            case REPLY -> new ReplyNodeBO(nodeDefinition, edgeList);
//
//            case IF:
//                return new IFNode(nodeDefinition, edges);
//
//            case OUTPUT:
//                return new OutputNode(nodeDefinition, edges);
//
//            // 预留扩展
//            case HTTP:
//                // return new HTTPNode(nodeDefinition, edges);
//                throw new UnsupportedOperationException("HTTP 节点暂未实现");
//
//            case CODE:
//                // return new CodeNode(nodeDefinition, edges);
//                throw new UnsupportedOperationException("代码执行节点暂未实现");
//
//            case KNOWLEDGE:
//                // return new KnowledgeNode(nodeDefinition, edges);
//                throw new UnsupportedOperationException("知识库检索节点暂未实现");

            default -> throw new ServiceException("不支持的节点类型：" + nodeType);
        };
    }
}
