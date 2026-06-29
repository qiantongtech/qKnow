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

// 查询物联网测点列表
export function listPoint(query) {
    return request({
        url: '/dm/point/list',
        method: 'get',
        params: query
    });
}

// 查询物联网测点详细
export function getPoint(id) {
    return request({
        url: '/dm/point/' + id,
        method: 'get'
    });
}

// 新增物联网测点
export function addPoint(data) {
    return request({
        url: '/dm/point',
        method: 'post',
        data: data
    });
}

// 修改物联网测点
export function updatePoint(data) {
    return request({
        url: '/dm/point',
        method: 'put',
        data: data
    });
}

// 删除物联网测点
export function delPoint(id) {
    return request({
        url: '/dm/point/' + id,
        method: 'delete'
    });
}
