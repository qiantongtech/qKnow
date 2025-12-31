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

// 查询关系配置列表
export function listRelation(query) {
    return request({
        url: '/ext/relation/list',
        method: 'get',
        params: query
    })
}

// 查询关系配置详细
export function getRelation(id) {
    return request({
        url: '/ext/relation/' + id,
        method: 'get'
    })
}

// 新增关系配置
export function addRelation(data) {
    return request({
        url: '/ext/relation',
        method: 'post',
        data: data
    })
}

// 修改关系配置
export function updateRelation(data) {
    return request({
        url: '/ext/relation',
        method: 'put',
        data: data
    })
}

// 删除关系配置
export function delRelation(id) {
    return request({
        url: '/ext/relation/' + id,
        method: 'delete'
    })
}

export function listByIds(ids) {
    return request({
        url: '/ext/relation/listByIds/' + ids,
        method: 'get'
    })
}

