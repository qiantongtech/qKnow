package tech.qiantong.qknow.module.kb.tool.function;


import com.alibaba.fastjson2.JSONObject;
import org.springframework.ai.chat.model.ToolContext;
import tech.qiantong.qknow.module.kb.tool.function.query.knowledgeQuery;
import tech.qiantong.qknow.module.kmc.api.service.IKmcApiService;
import tech.qiantong.qknow.thirdparty.domain.dify.knowledge.RetrieveResult;

import java.util.List;
import java.util.function.BiFunction;

/**
 * @Description: 查找用户工具
 * @Author: snail
 * @CreateDate: 2026/3/16 18:33
 */
public class SearchKnowledgeTool implements BiFunction<knowledgeQuery, ToolContext, String> {

    private IKmcApiService kmcApiService;

    private Long knowledgeId;

    public SearchKnowledgeTool(IKmcApiService kmcApiService, Long knowledgeId) {
        this.kmcApiService = kmcApiService;
        this.knowledgeId = knowledgeId;
    }

    @Override
    public String apply(knowledgeQuery knowledgeQuery, ToolContext toolContext) {
        List<RetrieveResult> retrieveResults = kmcApiService.recallTest(knowledgeId, knowledgeQuery.getQuery());
        if(!retrieveResults.isEmpty()){
            return JSONObject.toJSONString(retrieveResults);
        }else{
            return "知识库中没有找到" + knowledgeQuery.getQuery() + "的相关信息!";
        }
    }
}
