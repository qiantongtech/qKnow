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

import request from '@/utils/request.js';

// AI 聊天对话 API
export const ChatConversationApi = {
    // 获得【我的】聊天对话
    getChatConversationMy: (id) => {
        return request({
            url: `/app/conversation/` + id,
            method: 'get'
        });
    },

    // 新增【我的】聊天对话
    createChatConversationMy: (data) => {
        return request({
            url: `/app/conversation`,
            method: 'post',
            data: data
        });
    },

    // 更新【我的】聊天对话
    updateChatConversationMy: (data) => {
        return request({
            url: `/app/conversation`,
            method: 'put',
            data: data
        });
    },

    // 删除【我的】聊天对话
    deleteChatConversationMy: (id) => {
        return request({
            url: `/app/conversation/` + id,
            method: 'delete'
        });
    },

    // 获得【我的】聊天对话列表
    getChatConversationMyList: () => {
        return request({
            url: `/app/conversation/myList`,
            method: 'get'
        });
    }
};
