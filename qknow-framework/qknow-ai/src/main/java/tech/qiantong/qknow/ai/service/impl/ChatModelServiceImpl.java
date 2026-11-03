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

package tech.qiantong.qknow.ai.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.ai.deepseek.DeepSeekChatOptions;
import org.springframework.ai.deepseek.api.DeepSeekApi;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaChatOptions;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.client.reactive.JdkClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import tech.qiantong.qknow.ai.enums.model.AiPlatformEnum;
import tech.qiantong.qknow.ai.service.IChatModelService;
import tech.qiantong.qknow.ai.utils.AiModelCapabilityUtils;
import tech.qiantong.qknow.common.exception.ServiceException;

import java.net.http.HttpClient;
import java.util.Map;

/**
 * springAi chatModel 服务
 *
 * @author fabian
 */
@Service
public class ChatModelServiceImpl implements IChatModelService {

    @Resource
    ObjectProvider<WebClient.Builder> webClientBuilderProvider;
    /**
     * 获取 chatModel
     *
     * @param platForm  平台名称
     * @param baseUrl   baseUrl
     * @param apiKey    apiKey
     * @param modelName 模型名称
     * @return chatModel
     */
    @Override
    public ChatModel getChatModel(String platForm, String baseUrl, String apiKey, String modelName) {
        ChatModel chatModel;
        switch (AiPlatformEnum.validatePlatform(platForm)) {
            case OPENAI -> chatModel = this.getOpenAiChatModel(baseUrl, apiKey, modelName, null, null);
            case TONG_YI -> chatModel = this.getDashScopeChatModel(baseUrl, apiKey, modelName, null, null);
            case OLLAMA -> chatModel = this.getOllamaChatModel(baseUrl, modelName, null, null);
            case DEEP_SEEK -> chatModel = this.getDeepSeekChatModel(apiKey, modelName);
            default -> throw new ServiceException("暂时不支持该平台");
        }
        return chatModel;
    }

    @Override
    public ChatModel getChatModel(String platForm, String baseUrl, String apiKey, String modelName,
                                  Boolean thinkingEnabled, String reasoningEffort) {
        ChatModel chatModel;
        switch (AiPlatformEnum.validatePlatform(platForm)) {
            case OPENAI -> chatModel = this.getOpenAiChatModel(baseUrl, apiKey, modelName,
                    thinkingEnabled, reasoningEffort);
            case TONG_YI -> chatModel = this.getDashScopeChatModel(baseUrl, apiKey, modelName,
                    thinkingEnabled, reasoningEffort);
            case OLLAMA -> chatModel = this.getOllamaChatModel(baseUrl, modelName,
                    thinkingEnabled, reasoningEffort);
            case DEEP_SEEK -> chatModel = this.getDeepSeekChatModel(apiKey, modelName);
            default -> throw new ServiceException("暂时不支持该平台");
        }
        return chatModel;
    }

    /**
     * 获取 OpenAi 聊天模型
     *
     * @param baseUrl   baseUrl（必需）
     * @param apiKey    apiKey（必需）
     * @param modelName modelName（必需）
     * @return OpenAiChatModel
     */
    private OpenAiChatModel getOpenAiChatModel(String baseUrl, String apiKey, String modelName,
                                               Boolean thinkingEnabled, String reasoningEffort) {
        if (StrUtil.hasBlank(baseUrl, apiKey, modelName)) {
            throw new ServiceException("必要字段不能为空");
        }
        WebClient.Builder webClientBuilder = webClientBuilderProvider.getIfAvailable(WebClient::builder);
        webClientBuilder.clientConnector( // 重新设置ClientHttpConnector，并设置HTTP1.1版本的HttpClient
                new JdkClientHttpConnector(
                        HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build()
                )
        );
        OpenAiChatOptions.Builder optionsBuilder = OpenAiChatOptions.builder().model(modelName);
        if (thinkingEnabled != null && AiModelCapabilityUtils.isGlmThinkingModel(modelName)) {
            optionsBuilder.extraBody(Map.of(
                    "thinking", Map.of("type", thinkingEnabled ? "enabled" : "disabled")
            ));
        } else if (Boolean.TRUE.equals(thinkingEnabled)) {
            optionsBuilder.reasoningEffort(normalizeReasoningEffort(reasoningEffort));
        } else if (Boolean.FALSE.equals(thinkingEnabled)) {
            optionsBuilder.reasoningEffort("none");
        }
        return OpenAiChatModel.builder()
                .openAiApi(OpenAiApi.builder().baseUrl(baseUrl).apiKey(apiKey).webClientBuilder(webClientBuilder).build())
                .defaultOptions(optionsBuilder.build())
                .build();
    }

    /**
     * 获取 阿里百炼 聊天模型
     *
     * @param baseUrl   baseUrl（可选，为空时使用默认 https://dashscope.aliyuncs.com）
     * @param apiKey    apiKey（必需）
     * @param modelName modelName（必需）
     * @return DashScopeChatModel
     */
    private DashScopeChatModel getDashScopeChatModel(String baseUrl, String apiKey, String modelName,
                                                     Boolean thinkingEnabled, String reasoningEffort) {
        if (StrUtil.hasBlank(apiKey, modelName)) {
            throw new ServiceException("模型 API 密钥为空，请检查后重新配置");
        }

        // DashScopeApi 默认 baseUrl 是 https://dashscope.aliyuncs.com，
        // 只有调用业务空间专属域名（{wsId}.{region}.maas.aliyuncs.com）时才需要显式覆盖。
        DashScopeApi.Builder apiBuilder = DashScopeApi.builder()
                .apiKey(apiKey);
        if (StrUtil.isNotBlank(baseUrl)) {
            apiBuilder.baseUrl(baseUrl);
        }

        // 多模态模型需要走 multimodal-generation endpoint，否则阿里云返回 url error。
        if (AiModelCapabilityUtils.usesDashScopeMultimodalEndpoint(modelName)) {
            apiBuilder.completionsPath("/api/v1/services/aigc/multimodal-generation/generation");
        }

        DashScopeChatOptions.DashScopeChatOptionsBuilder optionsBuilder = DashScopeChatOptions.builder()
                .model(modelName);
        if (thinkingEnabled != null) {
            optionsBuilder.enableThinking(thinkingEnabled);
        }
        if (Boolean.TRUE.equals(thinkingEnabled)) {
            optionsBuilder.thinkingBudget(getThinkingBudget(reasoningEffort));
        }
        return DashScopeChatModel.builder()
                .dashScopeApi(apiBuilder.build())
                .defaultOptions(optionsBuilder.build())
                .build();
    }

    /**
     * 获取 ollama 聊天模型
     *
     * @param baseUrl   baseUrl（必需）
     * @param modelName modelName（必需）
     * @return OllamaChatModel
     */
    private OllamaChatModel getOllamaChatModel(String baseUrl, String modelName,
                                               Boolean thinkingEnabled, String reasoningEffort) {
        if (StrUtil.hasBlank(baseUrl, modelName)) {
            throw new ServiceException("必要字段不能为空");
        }
        OllamaChatOptions.Builder optionsBuilder = OllamaChatOptions.builder().model(modelName);
        if (Boolean.FALSE.equals(thinkingEnabled)) {
            optionsBuilder.disableThinking();
        } else if (Boolean.TRUE.equals(thinkingEnabled)) {
            switch (normalizeReasoningEffort(reasoningEffort)) {
                case "low" -> optionsBuilder.thinkLow();
                case "high" -> optionsBuilder.thinkHigh();
                default -> optionsBuilder.thinkMedium();
            }
        }
        return OllamaChatModel.builder()
                .ollamaApi(OllamaApi.builder().baseUrl(baseUrl).build())
                .defaultOptions(optionsBuilder.build())
                .build();
    }

    /**
     * 获取 deepseek 聊天模型
     *
     * @param apiKey    apiKey（必需）
     * @param modelName modelName（必需）
     * @return DeepSeekChatModel
     */
    private DeepSeekChatModel getDeepSeekChatModel(String apiKey, String modelName) {
        if (StrUtil.hasBlank(apiKey, modelName)) {
            throw new ServiceException("必要字段不能为空");
        }
        return DeepSeekChatModel.builder()
                .deepSeekApi(DeepSeekApi.builder().apiKey(apiKey).build())
                .defaultOptions(DeepSeekChatOptions.builder().model(modelName).build())
                .build();
    }

    private String normalizeReasoningEffort(String reasoningEffort) {
        if ("low".equals(reasoningEffort) || "high".equals(reasoningEffort)) {
            return reasoningEffort;
        }
        return "medium";
    }

    private int getThinkingBudget(String reasoningEffort) {
        return switch (normalizeReasoningEffort(reasoningEffort)) {
            case "low" -> 1024;
            case "high" -> 8192;
            default -> 4096;
        };
    }
}
