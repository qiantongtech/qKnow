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

// 查询文件分段列表
export function listKnowledgeSegment(query) {
    return request({
        url: '/kmc/knowledgeSegment/list',
        method: 'get',
        params: query
    });
}

// 查询文件分段列表树形结构
export function listKnowledgeSegmentTree(query) {
    return request({
        url: '/kmc/knowledgeSegment/listTree',
        method: 'get',
        params: query
    });
}

// 获取文件分段所有顶级节点
export function getAllLevelNodes(query) {
    return request({
        url: '/kmc/knowledgeSegment/getAllLevelNodes',
        method: 'get',
        params: query
    });
}

// 查询文件分段详细
export function getKnowledgeSegment(id) {
    return request({
        url: '/kmc/knowledgeSegment/' + id,
        method: 'get'
    });
}

// 新增文件分段
export function addKnowledgeSegment(data) {
    return request({
        url: '/kmc/knowledgeSegment',
        method: 'post',
        data: data
    });
}

// 修改文件分段
export function updateKnowledgeSegment(data) {
    return request({
        url: '/kmc/knowledgeSegment',
        method: 'put',
        data: data
    });
}

// 删除文件分段
export function delKnowledgeSegment(id) {
    return request({
        url: '/kmc/knowledgeSegment/' + id,
        method: 'delete'
    });
}
