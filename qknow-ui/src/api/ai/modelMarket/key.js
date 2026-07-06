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

// 查询API秘钥列表
export function listKey(query) {
    return request({
        url: '/ai/key/list',
        method: 'get',
        params: query
    });
}

// 查询API秘钥列表
export function listByPlatform(platform) {
    return request({
        url: '/ai/key/listByPlatform?platform=' + platform,
        method: 'get'
    });
}

// 查询API秘钥列表
export function myModelPage(query) {
    return request({
        url: '/ai/key/myModelPage',
        method: 'get',
        params: query
    });
}

// 查询API秘钥详细
export function getKey(id) {
    return request({
        url: '/ai/key/' + id,
        method: 'get'
    });
}

// 查询API秘钥详细
export function getByPlatform(platform) {
    return request({
        url: '/ai/key/getByPlatform?platform=' + platform,
        method: 'get'
    });
}

// 新增API秘钥
export function addKey(data) {
    return request({
        url: '/ai/key',
        method: 'post',
        data: data
    });
}

// 修改API秘钥
export function updateKey(data) {
    return request({
        url: '/ai/key',
        method: 'put',
        data: data
    });
}

// 移除API秘钥
export function removeKey(id) {
    return request({
        url: '/ai/key/' + id,
        method: 'delete'
    });
}

// 移除API秘钥
export function submitBatch(data) {
    return request({
        url: '/ai/key/submitBatch',
        method: 'post',
        data: data
    });
}
