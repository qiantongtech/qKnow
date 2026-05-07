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
