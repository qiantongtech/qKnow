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

// 查询专家经验列表
export function listAdvice(query) {
    return request({
        url: '/dm/advice/list',
        method: 'get',
        params: query
    });
}

// 查询专家经验详细
export function getAdvice(id) {
    return request({
        url: '/dm/advice/' + id,
        method: 'get'
    });
}

// 新增专家经验
export function addAdvice(data) {
    return request({
        url: '/dm/advice',
        method: 'post',
        data: data
    });
}

// 修改专家经验
export function updateAdvice(data) {
    return request({
        url: '/dm/advice',
        method: 'put',
        data: data
    });
}

// 删除专家经验
export function delAdvice(id) {
    return request({
        url: '/dm/advice/' + id,
        method: 'delete'
    });
}
