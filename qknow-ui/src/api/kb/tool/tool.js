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

// 查询工具管理列表
export function listTool(query) {
    return request({
        url: '/kb/tool/list',
        method: 'get',
        params: query
    });
}

// 查询工具管理详细
export function getTool(id) {
    return request({
        url: '/kb/tool/' + id,
        method: 'get'
    });
}

// 新增工具管理
export function addTool(data) {
    return request({
        url: '/kb/tool',
        method: 'post',
        data: data
    });
}

// 修改工具管理
export function updateTool(data) {
    return request({
        url: '/kb/tool',
        method: 'put',
        data: data
    });
}

// 删除工具管理
export function delTool(id) {
    return request({
        url: '/kb/tool/' + id,
        method: 'delete'
    });
}
