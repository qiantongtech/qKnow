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

// 查询知识推荐标签列表
export function listLabel(query) {
    return request({
        url: '/app/label/list',
        method: 'get',
        params: query
    });
}

// 查询知识推荐标签详细
export function getLabel(id) {
    return request({
        url: '/app/label/' + id,
        method: 'get'
    });
}

// 新增知识推荐标签
export function addLabel(data) {
    return request({
        url: '/app/label',
        method: 'post',
        data: data
    });
}

// 修改知识推荐标签
export function updateLabel(data) {
    return request({
        url: '/app/label',
        method: 'put',
        data: data
    });
}

// 删除知识推荐标签
export function delLabel(id) {
    return request({
        url: '/app/label/' + id,
        method: 'delete'
    });
}

// 修改搜索次数
export function updateSearchCount(data) {
    return request({
        url: '/app/label/updateSearchCount',
        method: 'put',
        data: data
    });
}

export function labelAllList() {
    return request({
        url: '/app/label/allList',
        method: 'get'
    });
}
