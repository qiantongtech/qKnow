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

// 查询应用关联知识库列表
export function listKnowledge(query) {
    return request({
        url: '/kac/knowledge/list',
        method: 'get',
        params: query
    });
}

// 查询应用关联知识库详细
export function getKnowledge(id) {
    return request({
        url: '/kac/knowledge/' + id,
        method: 'get'
    });
}

// 新增应用关联知识库
export function addKnowledge(data) {
    return request({
        url: '/kac/knowledge',
        method: 'post',
        data: data
    });
}

// 修改应用关联知识库
export function updateKnowledge(data) {
    return request({
        url: '/kac/knowledge',
        method: 'put',
        data: data
    });
}

// 删除应用关联知识库
export function delKnowledge(id) {
    return request({
        url: '/kac/knowledge/' + id,
        method: 'delete'
    });
}
