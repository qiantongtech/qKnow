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

// 查询文件操作日志列表
export function listKmcDocumentLog(query) {
    return request({
        url: '/kmc/kmcDocumentLog/list',
        method: 'get',
        params: query
    });
}

// 查询文件操作日志详细
export function getKmcDocumentLog(id) {
    return request({
        url: '/kmc/kmcDocumentLog/' + id,
        method: 'get'
    });
}

// 新增文件操作日志
export function addKmcDocumentLog(data) {
    return request({
        url: '/kmc/kmcDocumentLog',
        method: 'post',
        data: data
    });
}

// 修改文件操作日志
export function updateKmcDocumentLog(data) {
    return request({
        url: '/kmc/kmcDocumentLog',
        method: 'put',
        data: data
    });
}

// 删除文件操作日志
export function delKmcDocumentLog(id) {
    return request({
        url: '/kmc/kmcDocumentLog/' + id,
        method: 'delete'
    });
}

export function getUserAttention(query) {
    return request({
        url: '/kmc/kmcDocumentLog/getUserAttention',
        method: 'get',
        params: query
    });
}
