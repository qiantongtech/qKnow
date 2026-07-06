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

// 查询召回记录列表
export function listLog(query) {
    return request({
        url: '/kmc/log/list',
        method: 'get',
        params: query
    });
}

// 查询召回记录详细
export function getLog(id) {
    return request({
        url: '/kmc/log/' + id,
        method: 'get'
    });
}

// 新增召回记录
export function addLog(data) {
    return request({
        url: '/kmc/log',
        method: 'post',
        data: data
    });
}

// 修改召回记录
export function updateLog(data) {
    return request({
        url: '/kmc/log',
        method: 'put',
        data: data
    });
}

// 删除召回记录
export function delLog(id) {
    return request({
        url: '/kmc/log/' + id,
        method: 'delete'
    });
}

export function getKnowledgeBaseRecallLogList(query) {
    return request({
        url: '/kmc/log/getKnowledgeBaseRecallLogList',
        method: 'get',
        params: query
    });
}
