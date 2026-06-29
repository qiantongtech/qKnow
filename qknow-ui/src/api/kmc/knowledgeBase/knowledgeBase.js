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

// 查询知识库列表
export function listKnowledgeBase(query) {
    return request({
        url: '/kmc/knowledgeBase/list',
        method: 'get',
        params: query
    });
}

// 查询知识库详细
export function getKnowledgeBase(id) {
    return request({
        url: '/kmc/knowledgeBase/' + id,
        method: 'get'
    });
}

// 新增知识库
export function addKnowledgeBase(data) {
    return request({
        url: '/kmc/knowledgeBase',
        method: 'post',
        data: data
    });
}

// 修改知识库
export function updateKnowledgeBase(data) {
    return request({
        url: '/kmc/knowledgeBase',
        method: 'put',
        data: data
    });
}

// 删除知识库
export function delKnowledgeBase(id) {
    return request({
        url: '/kmc/knowledgeBase/' + id,
        method: 'delete'
    });
}

// 获取文本嵌入模型
export function getTextEmbedding() {
    return request({
        url: '/kmc/knowledgeBase/getTextEmbedding',
        method: 'get'
    });
}

// 获取文本嵌入模型
export function getRerank() {
    return request({
        url: '/kmc/knowledgeBase/getRerank',
        method: 'get'
    });
}

export function getRole(id) {
    return request({
        url: '/kmc/knowledgeBase/role/' + id,
        method: 'get'
    });
}

// 修改知识库
export function updateKnowledgeBaseRole(data) {
    return request({
        url: '/kmc/knowledgeBase/role',
        method: 'put',
        data: data
    });
}

// 召回测试
export function recallTest(data) {
    return request({
        url: '/kmc/knowledgeBase/recallTest',
        method: 'post',
        data: data
    });
}

// 根据权限获取知识库列表
export function getKmcKnowledgeBaseList(isValid) {
    return request({
        url: '/kmc/knowledgeBase/getKmcKnowledgeBaseList',
        method: 'get',
        params: { isValid }
    });
}

export function changeKnowledgeValid(id, validFlag) {
    return request({
        url: '/kmc/knowledgeBase/changeKnowledgeValid',
        method: 'put',
        params: { id, validFlag }
    });
}
