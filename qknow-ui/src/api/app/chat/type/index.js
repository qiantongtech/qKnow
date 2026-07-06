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

// 查询ai应用类型列表
export function listType(query) {
    return request({
        url: '/app/type/list',
        method: 'get',
        params: query
    });
}

// 查询ai应用类型详细
export function getType(id) {
    return request({
        url: '/app/type/' + id,
        method: 'get'
    });
}

// 新增ai应用类型
export function addType(data) {
    return request({
        url: '/app/type',
        method: 'post',
        data: data
    });
}

// 修改ai应用类型
export function updateType(data) {
    return request({
        url: '/app/type',
        method: 'put',
        data: data
    });
}

// 删除ai应用类型
export function delType(id) {
    return request({
        url: '/app/type/' + id,
        method: 'delete'
    });
}

// 修改ai应用类型状态
export function updateTypeStatus(data) {
    return request({
        url: '/app/type/editStatus',
        method: 'put',
        data: data
    });
}

export function getChatFlowEnableList() {
    return request({
        url: '/app/type/getChatFlowEnableList',
        method: 'get'
    });
}

export function getWorkFlowEnableList() {
    return request({
        url: '/app/type/getWorkFlowEnableList',
        method: 'get'
    });
}
