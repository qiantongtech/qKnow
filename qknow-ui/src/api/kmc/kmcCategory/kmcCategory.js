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

// 查询知识分类列表
export function listKmcCategory(query) {
    return request({
        url: '/kmc/kmcCategory/list',
        method: 'get',
        params: query
    })
}

// 查询知识分类详细
export function getKmcCategory(id) {
    return request({
        url: '/kmc/kmcCategory/' + id,
        method: 'get'
    })
}

// 新增知识分类
export function addKmcCategory(data) {
    return request({
        url: '/kmc/kmcCategory',
        method: 'post',
        data: data
    })
}

// 修改知识分类
export function updateKmcCategory(data) {
    return request({
        url: '/kmc/kmcCategory',
        method: 'put',
        data: data
    })
}

// 删除知识分类
export function delKmcCategory(id) {
    return request({
        url: '/kmc/kmcCategory/' + id,
        method: 'delete'
    })
}

// 查询全部知识分类数据
export function getAllList(query) {
    return request({
        url: '/kmc/kmcCategory/allList',
        method: 'get',
        params: query
    })
}

// 查询部门下拉树结构
export function kmcCategoryTree() {
    return request({
        url: '/kmc/kmcCategory/kmcCategoryTree',
        method: 'get'
    })
}
