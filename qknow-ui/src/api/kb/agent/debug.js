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
import { fetchEventSource } from '@microsoft/fetch-event-source';
import { getToken } from '@/utils/auth';

// 调试 Agent（流式输出）
export function debugAgent(data, onMessage, onError, onClose) {
    const token = getToken();
    const ctrl = new AbortController();

    return fetchEventSource(`${import.meta.env.VITE_APP_BASE_API}/kb/agent/testChatMessages`, {
        method: 'post',
        headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${token}`
        },
        openWhenHidden: true,
        body: JSON.stringify(data),
        onmessage: onMessage,
        onerror: onError,
        onclose: onClose,
        signal: ctrl.signal
    });
}
