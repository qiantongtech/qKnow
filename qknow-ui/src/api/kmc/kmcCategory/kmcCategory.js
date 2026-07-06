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
export function listKmcCategory(query) {
    return request({
        url: '/kmc/kmcCategory/list',
        method: 'get',
        params: query
    });
}

// 查询知识分类详细
export function getKmcCategory(id) {
    return request({
        url: '/kmc/kmcCategory/' + id,
        method: 'get'
    });
}

// 新增知识分类
export function addKmcCategory(data) {
    return request({
        url: '/kmc/kmcCategory',
        method: 'post',
        data: data
    });
}

// 修改知识分类
export function updateKmcCategory(data) {
    return request({
        url: '/kmc/kmcCategory',
        method: 'put',
        data: data
    });
}

// 删除知识分类
export function delKmcCategory(id) {
    return request({
        url: '/kmc/kmcCategory/' + id,
        method: 'delete'
    });
}

// 查询全部知识分类数据
export function getAllList(query) {
    return request({
        url: '/kmc/kmcCategory/allList',
        method: 'get',
        params: query
    });
}

// 查询部门下拉树结构
export function kmcCategoryTree(query) {
    return request({
        url: '/kmc/kmcCategory/kmcCategoryTree',
        method: 'get',
        params: query
    });
}
