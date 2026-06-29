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

// 查询应用关联知识图谱列表
export function listKacGraph(query) {
    return request({
        url: '/kac/graph/list',
        method: 'get',
        params: query
    });
}

// 查询应用关联知识图谱详细
export function getKacGraph(id) {
    return request({
        url: '/kac/graph/' + id,
        method: 'get'
    });
}

// 新增应用关联知识图谱
export function addKacGraph(data) {
    return request({
        url: '/kac/graph',
        method: 'post',
        data: data
    });
}

// 修改应用关联知识图谱
export function updateKacGraph(data) {
    return request({
        url: '/kac/graph',
        method: 'put',
        data: data
    });
}

// 删除应用关联知识图谱
export function delKacGraph(id) {
    return request({
        url: '/kac/graph/' + id,
        method: 'delete'
    });
}
