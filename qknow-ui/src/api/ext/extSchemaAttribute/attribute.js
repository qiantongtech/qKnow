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

// 查询概念属性列表
export function listAttribute(query) {
    return request({
        url: '/ext/attribute/list',
        method: 'get',
        params: query
    });
}

// 查询概念属性详细
export function getAttribute(id) {
    return request({
        url: '/ext/attribute/' + id,
        method: 'get'
    });
}

// 新增概念属性
export function addAttribute(data) {
    return request({
        url: '/ext/attribute',
        method: 'post',
        data: data
    });
}

// 修改概念属性
export function updateAttribute(data) {
    return request({
        url: '/ext/attribute',
        method: 'put',
        data: data
    });
}

// 删除概念属性
export function delAttribute(id) {
    return request({
        url: '/ext/attribute/' + id,
        method: 'delete'
    });
}

// 查询概念属性列表
export function getAllExtSchemaAttributeList(query) {
    return request({
        url: '/ext/attribute/getAllExtSchemaAttributeList',
        method: 'get',
        params: query
    });
}
