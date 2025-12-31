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

// 查询数据源列表
export function listDatasource(query) {
  return request({
    url: '/ext/datasource/list',
    method: 'get',
    params: query
  })
}

export function getTestConnection(id) {
  return request({
    url: '/ext/datasource/testConnection?id=' + id,
    method: 'get'
  })
}

export function getTableList(query) {
  return request({
    url: '/ext/datasource/getTableList',
    method: 'get',
    params: query
  })
}

export function getTableData(query) {
  return request({
    url: '/ext/datasource/getTableData',
    method: 'get',
    params: query
  })
}

//根据数据源id, 数据id和表名获取行数据
export function getTableDataByDataId(query) {
  return request({
    url: '/ext/datasource/getTableDataByDataId',
    method: 'get',
    params: query
  })
}

// 查询数据源详细
export function getDatasource(id) {
  return request({
    url: '/ext/datasource/' + id,
    method: 'get'
  })
}

// 新增数据源
export function addDatasource(data) {
  return request({
    url: '/ext/datasource',
    method: 'post',
    data: data
  })
}

// 修改数据源
export function updateDatasource(data) {
  return request({
    url: '/ext/datasource',
    method: 'put',
    data: data
  })
}

// 删除数据源
export function delDatasource(id) {
  return request({
    url: '/ext/datasource/' + id,
    method: 'delete'
  })
}
