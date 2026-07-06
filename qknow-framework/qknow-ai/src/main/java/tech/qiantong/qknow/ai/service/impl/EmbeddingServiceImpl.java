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
import com.alibaba.cloud.ai.dashscope.embedding.DashScopeEmbeddingModel;
import com.alibaba.cloud.ai.dashscope.embedding.DashScopeEmbeddingOptions;
import jakarta.annotation.Resource;
import org.springframework.ai.document.MetadataMode;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.ollama.OllamaEmbeddingModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaEmbeddingOptions;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.openai.OpenAiEmbeddingOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.client.reactive.JdkClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import tech.qiantong.qknow.ai.service.IEmbeddingService;
import tech.qiantong.qknow.common.exception.ServiceException;

import java.net.http.HttpClient;

/**
 * 向量化模型服务
 *
 * @author fabian
 */
@Service
public class EmbeddingServiceImpl implements IEmbeddingService {

    @Resource
    ObjectProvider<WebClient.Builder> webClientBuilderProvider;

    /**
     * 获取 向量化模型
     *
     * @param platForm  平台名称
     * @param baseUrl   baseUrl
     * @param apiKey    apiKey
     * @param modelName 模型名称
     * @return embeddingModel
     */
    @Override
    public EmbeddingModel getEmbeddingModel(String platForm, String baseUrl, String apiKey, String modelName) {
        EmbeddingModel embeddingModel;
        switch (platForm) {
            case "OpenAI" -> embeddingModel = this.getOpenAiModel(baseUrl, apiKey, modelName);
            case "TongYi" -> embeddingModel = this.getDashScopeModel(apiKey, modelName);
            case "Ollama" -> embeddingModel = this.getOllamaModel(baseUrl, modelName);
            default -> throw new ServiceException("暂时不支持该平台");

        }
        return embeddingModel;
    }

    /**
     * 获取 OpenAi 向量化模型
     *
     * @param baseUrl   baseUrl（必需）
     * @param apiKey    apiKey（必需）
     * @param modelName modelName（必需）
     * @return OpenAiEmbeddingModel
     */
    private OpenAiEmbeddingModel getOpenAiModel(String baseUrl, String apiKey, String modelName) {
        if (StrUtil.hasBlank(baseUrl, apiKey, modelName)) {
            throw new ServiceException("必要字段不能为空");
        }
        WebClient.Builder webClientBuilder = webClientBuilderProvider.getIfAvailable(WebClient::builder);
        webClientBuilder.clientConnector( // 重新设置ClientHttpConnector，并设置HTTP1.1版本的HttpClient
                new JdkClientHttpConnector(
                        HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build()
                )
        );
        return new OpenAiEmbeddingModel(
                OpenAiApi.builder().baseUrl(baseUrl).apiKey(apiKey).webClientBuilder(webClientBuilder).build(),
                MetadataMode.EMBED,
                OpenAiEmbeddingOptions.builder().model(modelName).build()
        );
    }

    /**
     * 获取 阿里百炼 向量化模型
     *
     * @param apiKey    apiKey（必需）
     * @param modelName modelName（必需）
     * @return DashScopeEmbeddingModel
     */
    private DashScopeEmbeddingModel getDashScopeModel(String apiKey, String modelName) {
        if (StrUtil.hasBlank(apiKey, modelName)) {
            throw new ServiceException("必要字段不能为空");
        }
        return new DashScopeEmbeddingModel(
                DashScopeApi.builder().apiKey(apiKey).build(),
                MetadataMode.EMBED,
                DashScopeEmbeddingOptions.builder().model(modelName).build()
        );
    }

    /**
     * 获取 ollama 向量化模型
     *
     * @param baseUrl   baseUrl（必需）
     * @param modelName modelName（必需）
     * @return OllamaEmbeddingModel
     */
    private OllamaEmbeddingModel getOllamaModel(String baseUrl, String modelName) {
        if (StrUtil.hasBlank(baseUrl, modelName)) {
            throw new ServiceException("必要字段不能为空");
        }
        return OllamaEmbeddingModel.builder()
                .ollamaApi(OllamaApi.builder().baseUrl(baseUrl).build())
                .defaultOptions(OllamaEmbeddingOptions.builder().model(modelName).build())
                .build();
    }
}
