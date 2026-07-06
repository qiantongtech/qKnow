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

export function getTaskTextList(query) {
    return request({
        url: '/ext/unstructTask/getTaskTextList',
        method: 'get',
        params: query
    });
}

export function getExtExtractionData(query) {
    return request({
        url: '/ext/unstructTask/getExtExtractionData',
        method: 'get',
        params: query
    });
}

export function getExtExtraction(query) {
    return request({
        url: '/ext/unstructTask/getExtExtraction',
        method: 'get',
        params: query
    });
}

// 图谱探索暂用
export function getExtractionAllData(query) {
    return request({
        url: '/ext/unstructTask/getExtractionAllData',
        method: 'get',
        params: query
    });
}

export function deleteNode(id) {
    return request({
        url: '/ext/unstructTask/deleteNode?id=' + id,
        method: 'post'
    });
}

export function executeExtraction(data) {
    return request({
        url: '/ext/unstructTask/executeExtraction',
        method: 'post',
        data: data
    });
}

//发布
export function strutReleaseByTaskId(data) {
    return request({
        url: '/ext/unstructTask/releaseByTaskId',
        method: 'post',
        data: data
    });
}

//取消发布
export function strutCancelReleaseByTaskId(data) {
    return request({
        url: '/ext/unstructTask/cancelReleaseByTaskId',
        method: 'post',
        data: data
    });
}

// 查询非结构化抽取任务列表
export function listUnstructTask(query) {
    return request({
        url: '/ext/unstructTask/list',
        method: 'get',
        params: query
    });
}

// 查询非结构化抽取任务详细
export function getUnstructTask(id) {
    return request({
        url: '/ext/unstructTask/' + id,
        method: 'get'
    });
}

// 新增非结构化抽取任务
export function addUnstructTask(data) {
    return request({
        url: '/ext/unstructTask',
        method: 'post',
        data: data
    });
}

// 修改非结构化抽取任务
export function updateUnstructTask(data) {
    return request({
        url: '/ext/unstructTask',
        method: 'put',
        data: data
    });
}

// 删除非结构化抽取任务
export function delUnstructTask(id) {
    return request({
        url: '/ext/unstructTask/' + id,
        method: 'delete'
    });
}
// 修改非结构化发布状态
export function updateUnStructTaskPublishStatus(data) {
    return request({
        url: '/ext/unstructTask/updateUnStructTaskPublishStatus',
        method: 'post',
        data: data
    });
}

export function getTextList(ids) {
    return request({
        url: '/ext/unstructTask/getTextList/' + ids,
        method: 'get'
    });
}
