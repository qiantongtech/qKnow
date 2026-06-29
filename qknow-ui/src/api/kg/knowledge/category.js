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

// 查询知识分类列表
export function listCategory(query) {
    return request({
        url: '/kg/category/list',
        method: 'get',
        params: query
    });
}

export function getAllList(query) {
    return request({
        url: '/kg/category/allList',
        method: 'get',
        params: query
    });
}

// 查询知识分类详细
export function getCategory(id) {
    return request({
        url: '/kg/category/' + id,
        method: 'get'
    });
}

// 新增知识分类
export function addCategory(data) {
    return request({
        url: '/kg/category',
        method: 'post',
        data: data
    });
}

// 修改知识分类
export function updateCategory(data) {
    return request({
        url: '/kg/category',
        method: 'put',
        data: data
    });
}

// 删除知识分类
export function delCategory(id) {
    return request({
        url: '/kg/category/' + id,
        method: 'delete'
    });
}
