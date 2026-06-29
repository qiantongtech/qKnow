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

// 查询任务文件关联列表
export function listUnstructTaskDocRel(query) {
    return request({
        url: '/ext/unstructTaskDocRel/list',
        method: 'get',
        params: query
    });
}

// 查询任务文件关联详细
export function getUnstructTaskDocRel(id) {
    return request({
        url: '/ext/unstructTaskDocRel/' + id,
        method: 'get'
    });
}

// 新增任务文件关联
export function addUnstructTaskDocRel(data) {
    return request({
        url: '/ext/unstructTaskDocRel',
        method: 'post',
        data: data
    });
}

// 修改任务文件关联
export function updateUnstructTaskDocRel(data) {
    return request({
        url: '/ext/unstructTaskDocRel',
        method: 'put',
        data: data
    });
}

// 删除任务文件关联
export function delUnstructTaskDocRel(id) {
    return request({
        url: '/ext/unstructTaskDocRel/' + id,
        method: 'delete'
    });
}
