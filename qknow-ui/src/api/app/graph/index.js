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

// 查询知识推荐标签列表
export function getGraph(query) {
    return request({
        url: '/app/graph/getGraph',
        method: 'get',
        params: query
    });
}
// 获取实体分页
export function getGraphPage(query) {
    return request({
        url: '/app/graph/getGraphPage',
        method: 'get',
        params: query
    });
}
// 发布 / 取消发布
export function updateReleaseStatus(data) {
    return request({
        url: '/app/graph/updateReleaseStatus',
        method: 'post',
        data: data
    });
}
// 根据节点id和属性的key删除属性
export function deleteNodeAttributeById(data) {
    return request({
        url: '/app/graph/deleteNodeAttributeById',
        method: 'delete',
        data: data
    });
}
// 新增实体
export function addNode(data) {
    return request({
        url: '/app/graph/addNode',
        method: 'post',
        data: data
    });
}
// 根据节点id删除对应的节点
export function deleteNode(id) {
    return request({
        url: `/app/graph/deleteNode/${id}`,
        method: 'delete'
    });
}
// 根据节点ids删除对应的节点
export function deleteNodeByIds(ids) {
    return request({
        url: `/app/graph/deleteNodeByIds/${ids}`,
        method: 'delete'
    });
}

// 新增关系
export function addTripletRel(data) {
    return request({
        url: '/app/graph/addTripletRel',
        method: 'post',
        data: data
    });
}
// 根据关系ids删除关系
export function deleteRelationshipsByIds(ids) {
    return request({
        url: `/app/graph/deleteRelationshipsByIds/${ids}`,
        method: 'delete'
    });
}
// 根据关系ids删除关系
export function deleteRelationshipById(id) {
    return request({
        url: `/app/graph/deleteRelationshipById/${id}`,
        method: 'delete'
    });
}

// 统计 (实体数量,关系类型数量,三元组数量)
export function getGraphDataStatistics() {
    return request({
        url: '/app/graph/getGraphDataStatistics',
        method: 'get'
    });
}
