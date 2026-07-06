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

// 查询bot 管理列表
export function listBot(query) {
    return request({
        url: '/kb/bot/list',
        method: 'get',
        params: query
    });
}

// 查询bot 管理详细
export function getBot(id) {
    return request({
        url: '/kb/bot/' + id,
        method: 'get'
    });
}

// 新增bot 管理
export function addBot(data) {
    return request({
        url: '/kb/bot',
        method: 'post',
        data: data
    });
}

// 修改bot 管理
export function updateBot(data) {
    return request({
        url: '/kb/bot',
        method: 'put',
        data: data
    });
}

// 删除bot 管理
export function delBot(id) {
    return request({
        url: '/kb/bot/' + id,
        method: 'delete'
    });
}

// 复制bot 管理
export function copyBot(data) {
    return request({
        url: '/kb/bot/copyBot',
        method: 'post',
        data: data
    });
}
