/*
Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.

This file is part of qKnow Intelligent Agent Building Platform (Open Source Edition).

qKnow is licensed under Apache License 2.0 with additional qKnow terms.
You may use qKnow for commercial purposes, but you may not remove, hide,
modify, or replace the qKnow logo, copyright notices, license notices,
or attribution information without a separate commercial license.

White-label use, OEM distribution, rebranding, or presenting qKnow as
another product requires separate commercial authorization from
Jiangsu Qiantong Technology Co., Ltd.

Business License: https://community.qknow.ai/business/policy.html
See the LICENSE file in the project root for full license information.
 */


import request from '@/utils/request'

// 查询mcp 配置列表
export function listMcpConfig(query) {
    return request({
        url: '/kb/mcpConfig/list',
        method: 'get',
        params: query
    })
}

// 查询mcp 配置详细
export function getMcpConfig(id) {
    return request({
        url: '/kb/mcpConfig/' + id,
        method: 'get'
    })
}

// 新增mcp 配置
export function addMcpConfig(data) {
    return request({
        url: '/kb/mcpConfig',
        method: 'post',
        data: data
    })
}

// 修改mcp 配置
export function updateMcpConfig(data) {
    return request({
        url: '/kb/mcpConfig',
        method: 'put',
        data: data
    })
}

// 删除mcp 配置
export function delMcpConfig(id) {
    return request({
        url: '/kb/mcpConfig/' + id,
        method: 'delete'
    })
}

// 修改 mcp 状态
export function updateMcpStatus(mcpId, newStatus) {
    return request({
        url: '/kb/mcpConfig/updateMcpStatus',
        method: 'post',
        data: {
            "id": mcpId,
            "status": newStatus
        }
    })
}

// 同步 mcp 工具列表
export function syncMcpTool(mcpId) {
    return request({
        url: '/kb/mcpConfig/syncMcpTool',
        method: 'post',
        data: {"id": mcpId}
    })
}
