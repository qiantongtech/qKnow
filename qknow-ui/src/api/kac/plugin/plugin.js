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

// 查询插件管理列表
export function listPlugin(query) {
    return request({
        url: '/kac/plugin/list',
        method: 'get',
        params: query
    });
}

// 查询插件管理详细
export function getPlugin(id) {
    return request({
        url: '/kac/plugin/' + id,
        method: 'get'
    });
}

// 新增插件管理
export function addPlugin(data) {
    return request({
        url: '/kac/plugin',
        method: 'post',
        data: data
    });
}

// 修改插件管理
export function updatePlugin(data) {
    return request({
        url: '/kac/plugin',
        method: 'put',
        data: data
    });
}

// 删除插件管理
export function delPlugin(id) {
    return request({
        url: '/kac/plugin/' + id,
        method: 'delete'
    });
}
