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

import request from '@/utils/request'

// 查询skills列表
export function listSkills(query) {
    return request({
        url: '/kb/skills/list',
        method: 'get',
        params: query
    })
}

// 查询skills详细
export function getSkills(id) {
    return request({
        url: '/kb/skills/' + id,
        method: 'get'
    })
}

// 新增skills
export function addSkills(data) {
    return request({
        url: '/kb/skills',
        method: 'post',
        data: data
    })
}

// 修改skills
export function updateSkills(data) {
    return request({
        url: '/kb/skills',
        method: 'put',
        data: data
    })
}

// 删除skills
export function delSkills(id) {
    return request({
        url: '/kb/skills/' + id,
        method: 'delete'
    })
}

// 批量导入skills（从ZIP）
export function batchImportSkills(data) {
    return request({
        url: '/kb/skills/batchImport',
        method: 'post',
        data: data
    })
}

// 预览 skills 的 SKILL.md
export function previewSkillMd(id) {
    return request({
        url: '/kb/skills/preview/' + id,
        method: 'get'
    })
}

// 下载 skill 为 ZIP
export function downloadSkill(id) {
    return request({
        url: '/kb/skills/download/' + id,
        method: 'get',
        responseType: 'blob'
    })
}
