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

import request from '@/utils/request.js';

// 查询公告列表
export function listNotice(query) {
    return request({
        url: '/system/notice/list',
        method: 'get',
        params: query
    });
}

// 查询公告详细
export function getNotice(noticeId) {
    return request({
        url: '/system/notice/' + noticeId,
        method: 'get'
    });
}

// 新增公告
export function addNotice(data) {
    return request({
        url: '/system/notice',
        method: 'post',
        data: data
    });
}

// 修改公告
export function updateNotice(data) {
    return request({
        url: '/system/notice',
        method: 'put',
        data: data
    });
}

// 删除公告
export function delNotice(noticeId) {
    return request({
        url: '/system/notice/' + noticeId,
        method: 'delete'
    });
}
