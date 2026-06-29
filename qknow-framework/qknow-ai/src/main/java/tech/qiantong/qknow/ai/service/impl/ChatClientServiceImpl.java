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

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;
import tech.qiantong.qknow.ai.service.IChatClientService;
import tech.qiantong.qknow.ai.service.IChatModelService;

/**
 * @author fabian
 */
@Service
public class ChatClientServiceImpl implements IChatClientService {

    @Resource
    IChatModelService chatModelService;

    /**
     * 获取 chatClient 对象
     *
     * @param platForm  平台名称
     * @param baseUrl   baseUrl
     * @param apiKey    apiKey
     * @param modelName 模型名称
     * @return chatClient
     */
    @Override
    public ChatClient getChatClient(String platForm, String baseUrl, String apiKey, String modelName) {
        ChatModel chatModel = chatModelService.getChatModel(platForm, baseUrl, apiKey, modelName);
        return ChatClient.builder(chatModel).build();
    }
}
