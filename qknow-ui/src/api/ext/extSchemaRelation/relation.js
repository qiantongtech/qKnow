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

// 查询关系配置列表
export function listRelation(query) {
    return request({
        url: '/ext/relation/list',
        method: 'get',
        params: query
    });
}

// 查询关系配置详细
export function getRelation(id) {
    return request({
        url: '/ext/relation/' + id,
        method: 'get'
    });
}

// 新增关系配置
export function addRelation(data) {
    return request({
        url: '/ext/relation',
        method: 'post',
        data: data
    });
}

// 修改关系配置
export function updateRelation(data) {
    return request({
        url: '/ext/relation',
        method: 'put',
        data: data
    });
}

// 删除关系配置
export function delRelation(id) {
    return request({
        url: '/ext/relation/' + id,
        method: 'delete'
    });
}

export function listByIds(ids) {
    return request({
        url: '/ext/relation/listByIds/' + ids,
        method: 'get'
    });
}
