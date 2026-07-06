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

// 查询知识文件列表
export function listDocument(query) {
    return request({
        url: '/kg/document/list',
        method: 'get',
        params: query
    });
}

export function getFileTypes() {
    return request({
        url: '/kg/document/getFileTypes',
        method: 'get'
    });
}

// 查询知识文件详细
export function getDocument(id) {
    return request({
        url: '/kg/document/' + id,
        method: 'get'
    });
}

// 新增知识文件
export function addDocument(data) {
    return request({
        url: '/kg/document',
        method: 'post',
        data: data
    });
}

// 修改知识文件
export function updateDocument(data) {
    return request({
        url: '/kg/document',
        method: 'put',
        data: data
    });
}

// 删除知识文件
export function delDocument(id) {
    return request({
        url: '/kg/document/' + id,
        method: 'delete'
    });
}

export function kgTrackCount(data) {
    return request({
        url: '/kg/document/trackCount',
        method: 'post',
        data: data
    });
}
