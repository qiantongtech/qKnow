package tech.qiantong.qknow.module.kb.tool.function.query;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.Data;

@Data
public class knowledgeQuery {

        /**
         * 城市名称
         */
        @JsonProperty(required = true, value = "query")
        @JsonPropertyDescription("需要查询的信息")
        private String query;

    }
