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
        url: '/kmcDocument/document/list',
        method: 'get',
        params: query
    });
}

// 查询知识文件详细
export function getDocument(id) {
    return request({
        url: '/kmcDocument/document/' + id,
        method: 'get'
    });
}

// 新增知识文件
export function addDocument(data) {
    return request({
        url: '/kmcDocument/document',
        method: 'post',
        data: data
    });
}

// 修改知识文件
export function updateDocument(data) {
    return request({
        url: '/kmcDocument/document',
        method: 'put',
        data: data
    });
}

// 删除知识文件
export function delDocument(id) {
    return request({
        url: '/kmcDocument/document/' + id,
        method: 'delete'
    });
}

// 获取文件的base64编码
export function getPdfPreview(data) {
    return request({
        url: '/kmcDocument/document/getPdfPreview',
        method: 'post',
        data: data
    });
}

// 修改下载次数并返回修改后的该条数据
export function updateDownloadCount(id) {
    return request({
        url: '/kmcDocument/document/downloadCount/' + id,
        method: 'get'
    });
}

// 修改预览次数并返回修改后的该条数据
export function updatePreviewCount(id) {
    return request({
        url: '/kmcDocument/document/previewCount/' + id,
        method: 'get'
    });
}

// 根据条件查询知识文件列表
export function selectList(query) {
    return request({
        url: '/kmcDocument/document/selectList',
        method: 'get',
        params: query
    });
}

// 获取多少种类型及每种类型下面的文件数量
export function getFileTypes(id) {
    return request({
        url: '/kmcDocument/document/getFileTypes/' + id,
        method: 'get'
    });
}

export function trackCount(data) {
    return request({
        url: '/kmcDocument/document/trackCount',
        method: 'post',
        data: data
    });
}

export function getOne(id) {
    return request({
        url: '/kmcDocument/document/getOne/' + id,
        method: 'get'
    });
}

export function checkFileName(data) {
    return request({
        url: '/kmcDocument/document/checkFileName',
        method: 'get',
        params: data
    });
}
