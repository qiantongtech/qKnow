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

// 查询检索的资源列表
export function listResources(query) {
    return request({
        url: '/app/resources/list',
        method: 'get',
        params: query
    });
}

export function listByMessage(query) {
    return request({
        url: '/app/resources/listByMessage',
        method: 'get',
        params: query
    });
}

// 查询检索的资源详细
export function getResources(id) {
    return request({
        url: '/app/resources/' + id,
        method: 'get'
    });
}

// 新增检索的资源
export function addResources(data) {
    return request({
        url: '/app/resources',
        method: 'post',
        data: data
    });
}

// 修改检索的资源
export function updateResources(data) {
    return request({
        url: '/app/resources',
        method: 'put',
        data: data
    });
}

// 删除检索的资源
export function delResources(id) {
    return request({
        url: '/app/resources/' + id,
        method: 'delete'
    });
}
