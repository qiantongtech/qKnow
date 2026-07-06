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

package tech.qiantong.qknow.thirdparty.domain.dify.studio;

import lombok.Data;

import java.util.Map;

/**
 * 知识问答
 * @program: qknow
 * @author wang
 * @date 2025/02/19 16:11
 **/
@Data
public class ChatMessage {

    /** 对话id */
    private String conversationId;

    /** 问题 */
    private String query;

    /** 用户 */
    private String user;

    /** url */
    private String url;

    /** apikey */
    private String apiKey;

    /** 其他参数 */
    private Map<String, Object> map;

}
