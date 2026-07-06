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

import request from '@/utils/request';
import { fetchEventSource } from '@microsoft/fetch-event-source';
import { getToken } from '@/utils/auth';

// 查询工作流运行记录列表
export function listLog(query) {
    return request({
        url: '/app/log/list',
        method: 'get',
        params: query
    });
}

// 查询工作流运行记录详细
export function getLog(id) {
    return request({
        url: '/app/log/' + id,
        method: 'get'
    });
}

// 新增工作流运行记录
export function addLog(data) {
    return request({
        url: '/app/log',
        method: 'post',
        data: data
    });
}

// 修改工作流运行记录
export function updateLog(data) {
    return request({
        url: '/app/log',
        method: 'put',
        data: data
    });
}

// 删除工作流运行记录
export function delLog(id) {
    return request({
        url: '/app/log/' + id,
        method: 'delete'
    });
}

export function getParameters(query) {
    return request({
        url: '/app/log/getParameters',
        method: 'get',
        params: query
    });
}

export function uploadFile(data) {
    return request({
        url: '/app/log/uploadFile',
        method: 'post',
        data: data
    });
}

export function runWorkflow(chatTypeId, parameters, ctrl, onMessage, onError, onClose) {
    const token = getToken();
    return fetchEventSource(`${import.meta.env.VITE_APP_BASE_API}/app/log/runWorkflow`, {
        method: 'post',
        headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${token}`
        },
        body: JSON.stringify({
            chatTypeId: chatTypeId,
            parameters: parameters
        }),
        openWhenHidden: true,
        onmessage: onMessage,
        onerror: onError,
        onclose: onClose,
        signal: ctrl.signal
    });
}
