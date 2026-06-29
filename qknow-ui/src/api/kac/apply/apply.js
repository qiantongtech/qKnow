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

// 查询应用管理列表
export function listApply(query) {
    return request({
        url: '/kac/apply/list',
        method: 'get',
        params: query
    });
}

// 查询应用管理详细
export function getApply(id) {
    return request({
        url: '/kac/apply/' + id,
        method: 'get'
    });
}

// 新增应用管理
export function addApply(data) {
    return request({
        url: '/kac/apply',
        method: 'post',
        data: data
    });
}

// 修改应用管理
export function updateApply(data) {
    return request({
        url: '/kac/apply',
        method: 'put',
        data: data
    });
}

// 删除应用管理
export function delApply(id) {
    return request({
        url: '/kac/apply/' + id,
        method: 'delete'
    });
}

// 查询应用管理详细
export function getByApplyIdId(applyId) {
    return request({
        url: '/kac/apply/apply/' + applyId,
        method: 'get'
    });
}

// 复制应用管理
export function copy(data) {
    return request({
        url: '/kac/apply/copy',
        method: 'post',
        data: data
    });
}
