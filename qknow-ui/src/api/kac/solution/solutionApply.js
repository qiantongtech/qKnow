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

// 查询解决方案关联应用列表
export function listSolutionApply(query) {
    return request({
        url: '/kac/solutionApply/list',
        method: 'get',
        params: query
    });
}

// 查询解决方案关联应用详细
export function getSolutionApply(id) {
    return request({
        url: '/kac/solutionApply/' + id,
        method: 'get'
    });
}

// 新增解决方案关联应用
export function addSolutionApply(data) {
    return request({
        url: '/kac/solutionApply',
        method: 'post',
        data: data
    });
}

// 修改解决方案关联应用
export function updateSolutionApply(data) {
    return request({
        url: '/kac/solutionApply',
        method: 'put',
        data: data
    });
}

// 删除解决方案关联应用
export function delSolutionApply(id) {
    return request({
        url: '/kac/solutionApply/' + id,
        method: 'delete'
    });
}
