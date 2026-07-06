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

// 查询agent配置列表
export function listConfig(query) {
    return request({
        url: '/kb/agent/list',
        method: 'get',
        params: query
    });
}

// 查询 agent 配置详细
export function getConfig(id) {
    return request({
        url: '/kb/agent/' + id,
        method: 'get'
    });
}

// 根据 botId 查询 agent 配置详细
export function getConfigByBotId(botId) {
    return request({
        url: '/kb/agent/byBot/' + botId,
        method: 'get'
    });
}

// 新增agent配置
export function addConfig(data) {
    return request({
        url: '/kb/agent',
        method: 'post',
        data: data
    });
}

// 修改agent配置
export function updateConfig(data) {
    return request({
        url: '/kb/agent',
        method: 'put',
        data: data
    });
}

// 删除agent配置
export function delConfig(id) {
    return request({
        url: '/kb/agent/' + id,
        method: 'delete'
    });
}
