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

// 查询自定义映射列表
export function listMapping(query) {
    return request({
        url: '/ext/extCustom/list',
        method: 'get',
        params: query
    });
}

// 查询自定义映射详细
export function getMapping(id) {
    return request({
        url: '/ext/extCustom/' + id,
        method: 'get'
    });
}

// 新增自定义映射
export function addMapping(data) {
    return request({
        url: '/ext/extCustom',
        method: 'post',
        data: data
    });
}

// 修改自定义映射
export function updateMapping(data) {
    return request({
        url: '/ext/extCustom',
        method: 'put',
        data: data
    });
}

// 删除自定义映射
export function delMapping(id) {
    return request({
        url: '/ext/extCustom/' + id,
        method: 'delete'
    });
}
