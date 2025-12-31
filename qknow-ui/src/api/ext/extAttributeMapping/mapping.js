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

// 查询属性映射列表
export function listMapping(query) {
  return request({
    url: '/ext/extAttribute/list',
    method: 'get',
    params: query
  })
}

// 查询属性映射详细
export function getMapping(id) {
  return request({
    url: '/ext/extAttribute/' + id,
    method: 'get'
  })
}

// 新增属性映射
export function addMapping(data) {
  return request({
    url: '/ext/extAttribute',
    method: 'post',
    data: data
  })
}

// 修改属性映射
export function updateMapping(data) {
  return request({
    url: '/ext/extAttribute',
    method: 'put',
    data: data
  })
}

// 删除属性映射
export function delMapping(id) {
  return request({
    url: '/ext/extAttribute/' + id,
    method: 'delete'
  })
}
