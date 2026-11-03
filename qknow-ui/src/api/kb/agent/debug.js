import { fetchEventSource } from '@microsoft/fetch-event-source';
import { getToken } from '@/utils/auth';

async function validateStreamResponse(response) {
    if (response.ok) {
        return;
    }
    let message = 'Agent 调试请求失败';
    try {
        const text = await response.text();
        const body = text ? JSON.parse(text) : null;
        message = body?.msg || body?.message || body?.data?.msg || text || message;
    } catch {
        // 非 JSON 错误响应使用默认提示。
    }
    throw new Error(`${message}（HTTP ${response.status}）`);
}

// 调试 Agent（流式输出）
export function debugAgent(data, onMessage, onError, onClose, options = {}) {
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
        onopen: validateStreamResponse,
        onmessage: onMessage,
        onerror: onError,
        onclose: onClose,
        signal: options.signal || ctrl.signal
    });
}
