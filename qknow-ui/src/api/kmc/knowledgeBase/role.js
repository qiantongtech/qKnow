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

// 查询知识库角色关联列表
export function listRole(query) {
    return request({
        url: '/kmc/role/list',
        method: 'get',
        params: query
    });
}

// 查询知识库角色关联详细
export function getRole(id) {
    return request({
        url: '/kmc/role/' + id,
        method: 'get'
    });
}

// 新增知识库角色关联
export function addRole(data) {
    return request({
        url: '/kmc/role',
        method: 'post',
        data: data
    });
}

// 修改知识库角色关联
export function updateRole(data) {
    return request({
        url: '/kmc/role',
        method: 'put',
        data: data
    });
}

// 删除知识库角色关联
export function delRole(id) {
    return request({
        url: '/kmc/role/' + id,
        method: 'delete'
    });
}
