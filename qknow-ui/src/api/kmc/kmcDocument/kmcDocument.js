/*
 * Copyright © 2026 Qiantong Technology Co., Ltd.
 * qKnow Knowledge Platform
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please apply for brand customization authorization via official channels.
 *  *
 * More information: https://qknow.qiantong.tech/business.html
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2026 江苏千桐科技有限公司
 * qKnow 知识平台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qknow.qiantong.tech/business.html
 */

import request from '@/utils/request'

// 查询知识文件列表
export function listDocument(query) {
    return request({
        url: '/kmcDocument/document/list',
        method: 'get',
        params: query
    })
}

// 查询知识文件详细
export function getDocument(id) {
    return request({
        url: '/kmcDocument/document/' + id,
        method: 'get'
    })
}

// 新增知识文件
export function addDocument(data) {
    return request({
        url: '/kmcDocument/document',
        method: 'post',
        data: data
    })
}

// 修改知识文件
export function updateDocument(data) {
    return request({
        url: '/kmcDocument/document',
        method: 'put',
        data: data
    })
}

// 删除知识文件
export function delDocument(id) {
    return request({
        url: '/kmcDocument/document/' + id,
        method: 'delete'
    })
}

// 获取文件的base64编码
export function getPdfPreview(data) {
    return request({
        url: '/kmcDocument/document/getPdfPreview',
        method: 'post',
        data: data
    })
}

// 修改下载次数并返回修改后的该条数据
export function updateDownloadCount(id) {
    return request({
        url: '/kmcDocument/document/downloadCount/' + id,
        method: 'get'
    })
}

// 修改预览次数并返回修改后的该条数据
export function updatePreviewCount(id) {
    return request({
        url: '/kmcDocument/document/previewCount/' + id,
        method: 'get'
    })
}

// 根据条件查询知识文件列表
export function selectList(query) {
    return request({
        url: '/kmcDocument/document/selectList',
        method: 'get',
        params: query
    })
}

// 获取多少种类型及每种类型下面的文件数量
export function getFileTypes() {
    return request({
        url: '/kmcDocument/document/getFileTypes',
        method: 'get',
    })
}

