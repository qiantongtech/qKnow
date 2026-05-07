/*
 * Copyright (c) 2026 Jiangsu Qiantong Technology Co., Ltd.
 *  *
 * Software Name: qKnow Knowledge Platform (Business Edition)
 * Software Copyright Registration No. 15980140
 *  *
 * [RIGHTS AND LICENSE STATEMENT]
 * This file contains non-public commercial source code of which Jiangsu Qiantong
 * Technology Co., Ltd. lawfully possesses complete intellectual property rights.
 *  *
 * Access and use are limited to entities or individuals who have signed a valid
 * commercial license agreement, within the scope stipulated in the agreement.
 * The "accessibility" of this source code is premised on lawful authorization
 * and does not constitute any form of transfer of intellectual property rights
 * or implied licensing.
 *  *
 * [PROHIBITIONS]
 * Unless explicitly agreed in the license agreement, the following acts in any
 * form are strictly prohibited:
 * 1. Copying, disseminating, disclosing, selling, renting, or redistributing
 * this source code;
 * 2. Providing the software's functionality to third parties via SaaS, PaaS,
 * cloud hosting, or other means;
 * 3. Using this software or its derivative versions to develop products that
 * compete with the Right Holder;
 * 4. Providing or displaying this source code or related technical information
 * to unauthorized third parties;
 * 5. Tampering with, circumventing, or destroying copyright notices, license
 * verifications, or other technical protection measures.
 *  *
 * [LEGAL LIABILITY]
 * Any unauthorized use constitutes an infringement of trade secrets and
 * intellectual property rights.
 *  *
 * The Right Holder will strictly pursue liability for breach of contract and
 * infringement in accordance with the commercial agreement and laws such as
 * the "Copyright Law of the People's Republic of China" and the "Anti-Unfair
 * Competition Law".
 *  *
 * ============================================================================
 *  *
 * Copyright (c) 2026 江苏千桐科技有限公司
 *  *
 * 软件名称：qKnow 知识平台（商业版） | 软著登字第15980140号
 *  *
 * 【权利与授权声明】
 * 本文件属于江苏千桐科技有限公司依法享有完全知识产权的非公开商业源代码。
 * 仅限已签署有效商业授权合同的单位或个人在约定范围内查阅和使用。
 * 源代码的“可访问性”均以合法授权为前提，不构成任何形式的知识产权转让或默示授权。
 *  *
 * 【禁止事项】
 * 除授权合同明确约定外，严禁任何形式的：
 * 1. 复制、传播、披露、出售、出租或再分发本源代码；
 * 2. 通过 SaaS、PaaS、云托管等方式向第三方提供本软件功能；
 * 3. 将本软件或其衍生版本用于开发与权利人构成竞争的产品；
 * 4. 向未授权第三方提供或展示本源代码或相关技术信息；
 * 5. 篡改、规避或破坏版权标识、授权校验及其他技术保护措施。
 *  *
 * 【法律责任】
 * 任何未经授权的利用行为，均构成对商业秘密及知识产权的侵害。
 * 权利人将依据商业合同及《中华人民共和国著作权法》《反不正当竞争法》
 * 等法律法规，严厉追究违约与侵权责任。
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
        return fetchEventSource(`${import.meta.env.VITE_APP_BASE_API}/app/writing/outline-optimize-stream`, {
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
        });
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
        return fetchEventSource(`${import.meta.env.VITE_APP_BASE_API}/app/writing/writing-optimize-stream`, {
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
        });
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
                summary: summary,
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
