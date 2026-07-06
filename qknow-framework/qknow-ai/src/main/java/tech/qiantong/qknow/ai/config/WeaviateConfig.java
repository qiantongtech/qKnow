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

package tech.qiantong.qknow.ai.config;

import io.weaviate.client.Config;
import io.weaviate.client.WeaviateClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * weaviate 向量数据库配置
 *
 * @author fabian
 */
@Configuration
public class WeaviateConfig {

    @Value("${weaviate.scheme:http}")
    private String scheme;
    @Value("${weaviate.apiKey}")
    private String apiKey;
    @Value("${weaviate.host}")
    private String host;

    /**
     * 向容器中添加 数据库客户端
     *
     * @return WeaviateClient
     */
    @Bean
    public WeaviateClient weaviateClient() {
        Map<String, String> header = new HashMap<>();
        if (!Objects.isNull(apiKey) && !Objects.equals(apiKey.trim(), "")) {
            header.put("Authorization", "Bearer " + apiKey.trim());
        }
        return new WeaviateClient(new Config(scheme, host, header));
    }
}
