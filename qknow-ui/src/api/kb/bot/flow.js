import request from '@/utils/request.js';
import { fetchEventSource } from '@microsoft/fetch-event-source';
import { getToken } from '@/utils/auth';
const token = getToken();
// 获取流程
export function getFlow(botId) {
    return request({
        url: '/kb/flow?botId=' + botId,
        method: 'get'
    });
}

// 新增或修改流程
export function submitFlow(data) {
    return request({
        url: '/kb/flow',
        method: 'post',
        data: data
    });
}

// 运行流程
/*数据格式
{
    "flow": {
        "botId": 1,
        "nodes": [],
        "edges": []
    },
    "input": {
        "query": "你好"
    }
}
 */
export function executeFlow(data) {
    return request({
        url: '/kb/flow/executeFlow',
        method: 'post',
        data: data
    });
}

// 流式运行，输入与 executeFlow 相同
export function executeFlowStream(data) {
    return request({
        url: '/kb/flow/executeFlowStream',
        method: 'post',
        data: data
    });
}

export const ProcessFlow = {
    executeFlowStream: async (flow, input, ctrl, onMessage, onError, onClose) => {
        return fetchEventSource(`${import.meta.env.VITE_APP_BASE_API}/kb/flow/testExecuteFlow`, {
            method: 'post',
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${token}`
            },
            openWhenHidden: true,
            body: JSON.stringify({
                flow: flow,
                input: input
            }),
            onmessage: onMessage,
            onerror: onError,
            onclose: onClose,
            signal: ctrl.signal
        });
    }
};
export const ProcessChstFlow = {
    executeChatflowDebugStream: async (
        flow,
        input,
        messageList,
        ctrl,
        onMessage,
        onError,
        onClose
    ) => {
        return fetchEventSource(
            `${import.meta.env.VITE_APP_BASE_API}/kb/flow/testExecuteChatFlow`,
            {
                method: 'post',
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${token}`
                },
                openWhenHidden: true,
                body: JSON.stringify({
                    flow: flow,
                    input: input,
                    messageList: messageList
                }),
                onmessage: onMessage,
                onerror: onError,
                onclose: onClose,
                signal: ctrl.signal
            }
        );
    }
};
