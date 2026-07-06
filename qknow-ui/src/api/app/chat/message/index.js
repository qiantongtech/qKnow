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
import { fetchEventSource } from '@microsoft/fetch-event-source';
import { getToken } from '@/utils/auth';
// import { data } from 'vis-network';

// AI chat 聊天
export const ChatMessageApi = {
    // 消息列表
    getChatMessageListByConversationId: (conversationId) => {
        return request({
            url: `/app/message/list-by-conversation-id?conversationId=${conversationId}`
        });
    },
    getSuggested: (messageId) => {
        return request({
            url: `/app/message/getSuggested/${messageId}`,
            method: 'get',
            timeout: 30 * 1000
        });
    },

    // 发送 Stream 消息
    // 为什么不用 axios 呢？因为它不支持 SSE 调用
    sendChatMessageStream: async (
        conversationId,
        content,
        ctrl,
        enableContext,
        knowledgeBaseIds,
        graphIds,
        onMessage,
        onError,
        onClose
    ) => {
        const token = getToken();
        return fetchEventSource(`${import.meta.env.VITE_APP_BASE_API}/app/message/send-stream`, {
            method: 'post',
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${token}`
            },
            openWhenHidden: true,
            body: JSON.stringify({
                conversationId,
                content,
                useContext: enableContext,
                knowledgeBaseIds: knowledgeBaseIds,
                graphIds: graphIds
            }),
            onmessage: onMessage,
            onerror: onError,
            onclose: onClose,
            signal: ctrl.signal
        });
    },
    // 合规性检查
    ruleWriting: async (
        writingId,
        writingTitle,
        writingArticle,
        ruleIds,
        ruleNames,
        customRule,
        ctrl,
        onMessage,
        onError,
        onClose
    ) => {
        const token = getToken();
        return fetchEventSource(
            `${import.meta.env.VITE_APP_BASE_API}/app/complianceCheck/checkStream`,
            {
                method: 'post',
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${token}`
                },
                openWhenHidden: true,
                body: JSON.stringify({
                    writingId: writingId,
                    writingTitle: writingTitle,
                    writingArticle: writingArticle,
                    ruleIds: ruleIds,
                    ruleNames: ruleNames,
                    customRule: customRule
                }),
                onmessage: onMessage,
                onerror: onError,
                onclose: onClose,
                signal: ctrl.signal
            }
        );
    },
    // 智能写作助手大纲
    sendChatIntelligentWriting: async (
        title,
        summary,
        wordCount,
        knowledgeId,
        knowledgeName,
        type,
        id,
        ctrl,
        onMessage,
        onError,
        onClose
    ) => {
        const token = getToken();
        return fetchEventSource(`${import.meta.env.VITE_APP_BASE_API}/app/writing/outline-stream`, {
            method: 'post',
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${token}`
            },
            openWhenHidden: true,
            body: JSON.stringify({
                title: title,
                summary: summary,
                wordCount: wordCount,
                knowledgeId: knowledgeId,
                knowledgeName: knowledgeName,
                type: type,
                id: id
            }),
            onmessage: onMessage,
            onerror: onError,
            onclose: onClose,
            signal: ctrl.signal
        });
    },
    // 智能写作助手大纲内容优化
    sendChatIntelligentWritingOptimize: async (
        title,
        outline,
        outlineInfo,
        summary,
        wordCount,
        knowledgeId,
        knowledgeName,
        type,
        id,
        ctrl,
        onMessage,
        onError,
        onClose
    ) => {
        const token = getToken();
        return fetchEventSource(
            `${import.meta.env.VITE_APP_BASE_API}/app/writing/outline-optimize-stream`,
            {
                method: 'post',
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${token}`
                },
                openWhenHidden: true,
                body: JSON.stringify({
                    title: title,
                    outline: outline,
                    info: outlineInfo,
                    summary: summary,
                    wordCount: wordCount,
                    knowledgeId: knowledgeId,
                    knowledgeName: knowledgeName,
                    type: type,
                    id: id
                }),
                onmessage: onMessage,
                onerror: onError,
                onclose: onClose,
                signal: ctrl.signal
            }
        );
    },
    // 模板生成
    templateArticle: async (
        title,
        type,
        planId,
        promptWordId,
        info,
        ctrl,
        onMessage,
        onError,
        onClose
    ) => {
        const token = getToken();
        return fetchEventSource(
            `${import.meta.env.VITE_APP_BASE_API}/app/writing/template-stream`,
            {
                method: 'post',
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${token}`
                },
                openWhenHidden: true,
                body: JSON.stringify({
                    title: title,
                    type: type,
                    planId: planId,
                    promptWordId: promptWordId,
                    info: info
                }),
                onmessage: onMessage,
                onerror: onError,
                onclose: onClose,
                signal: ctrl.signal
            }
        );
    },
    // 智能写作助手文章
    sendChatIntelligentArticle: async (
        title,
        wordCount,
        knowledgeId,
        knowledgeName,
        info,
        id,
        outline,
        type,
        ctrl,
        onMessage,
        onError,
        onClose
    ) => {
        const token = getToken();
        return fetchEventSource(`${import.meta.env.VITE_APP_BASE_API}/app/writing/writing-stream`, {
            method: 'post',
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${token}`
            },
            openWhenHidden: true,
            body: JSON.stringify({
                title: title,
                wordCount: wordCount,
                knowledgeId: knowledgeId,
                knowledgeName: knowledgeName,
                info: info,
                id: id,
                outline: outline,
                type: type
            }),
            onmessage: onMessage,
            onerror: onError,
            onclose: onClose,
            signal: ctrl.signal
        });
    },
    // 智能写作助手文章内容优化
    sendChatIntelligentArticleOptimize: async (
        article,
        articleInfo,
        summary,
        outline,
        wordCount,
        knowledgeId,
        knowledgeName,
        id,
        type,
        ctrl,
        onMessage,
        onError,
        onClose
    ) => {
        const token = getToken();
        return fetchEventSource(
            `${import.meta.env.VITE_APP_BASE_API}/app/writing/writing-optimize-stream`,
            {
                method: 'post',
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${token}`
                },
                openWhenHidden: true,
                body: JSON.stringify({
                    article: article,
                    info: articleInfo,
                    summary: summary,
                    outline: outline,
                    wordCount: wordCount,
                    knowledgeId: knowledgeId,
                    knowledgeName: knowledgeName,
                    id: id,
                    type: type
                }),
                onmessage: onMessage,
                onerror: onError,
                onclose: onClose,
                signal: ctrl.signal
            }
        );
    },
    // 重新建议
    suggestAgain: async (
        id,
        rewriteSuggestionType,
        paragraph,
        ctrl,
        onMessage,
        onError,
        onClose
    ) => {
        const token = getToken();
        return fetchEventSource(
            `${import.meta.env.VITE_APP_BASE_API}/app/writing/rewrite-suggestion-stream`,
            {
                method: 'post',
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${token}`
                },
                openWhenHidden: true,
                body: JSON.stringify({
                    id: id,
                    rewriteSuggestionType: rewriteSuggestionType,
                    paragraph: paragraph
                }),
                onmessage: onMessage,
                onerror: onError,
                onclose: onClose,
                signal: ctrl.signal
            }
        );
    },

    // 智能写作日/月/周报告
    sendChatIntelligentReport: async (
        id,
        title,
        type,
        position,
        workContent,
        workPlan,
        problem,
        progress,
        summary,
        ctrl,
        onMessage,
        onError,
        onClose
    ) => {
        const token = getToken();
        return fetchEventSource(`${import.meta.env.VITE_APP_BASE_API}/app/report/report-stream`, {
            method: 'post',
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${token}`
            },
            openWhenHidden: true,
            body: JSON.stringify({
                id: id,
                title: title,
                type: type,
                position: position,
                workContent: workContent,
                workPlan: workPlan,
                problem: problem,
                progress: progress,
                summary: summary
            }),
            onmessage: onMessage,
            onerror: onError,
            onclose: onClose,
            signal: ctrl.signal
        });
    },

    // 删除消息
    deleteChatMessage: (id) => {
        return request({
            url: `/app/message/` + id,
            method: 'delete'
        });
    },
    // 删除指定对话的消息
    deleteByConversationId: (conversationId) => {
        return request({
            url: `/app/message/deleteByConversationId?conversationId=${conversationId}`,
            method: 'delete'
        });
    }
};

export function outlineStream(query) {
    return request({
        url: '/app/writing/outline-stream',
        method: 'post',
        params: query
    });
}

export function putwriting(query) {
    return request({
        url: '/app/writing',
        method: 'put',
        data: query
    });
}

export function lsMyList(query) {
    return request({
        url: '/app/writing/myList',
        method: 'get',
        params: query
    });
}

export function deleteWriting(ids) {
    return request({
        url: `/app/writing/${ids}`,
        method: 'delete'
    });
}
