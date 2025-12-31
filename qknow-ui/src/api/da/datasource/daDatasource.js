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
export function listDaDatasource(query) {
  return request({
    url: '/dm/dmDatasource/list',
    method: 'get',
    params: query
  })
}

// 查询数据源列表
export function getDaDatasourceList(query) {
  return request({
    url: '/dm/dmDatasource/getDataSourceByAsset',
    method: 'get',
    params: query
  })
}

// 查询数据源详细
export function getDaDatasource(id) {
  return request({
    url: '/dm/dmDatasource/' + id,
    method: 'get'
  })
}


// 测试连接
export function clientsTest(id) {
  return request({
    url: '/dm/dmDatasource/clientsTest/' + id,
    method: 'get'
  })
}

// 新增数据源
export function addDaDatasource(data) {
  return request({
    url: '/dm/dmDatasource',
    method: 'post',
    data: data
  })
}

// 修改数据源
export function updateDaDatasource(data) {
  return request({
    url: '/dm/dmDatasource',
    method: 'put',
    data: data
  })
}

// 删除数据源
export function delDaDatasource(id) {
  return request({
    url: '/dm/dmDatasource/' + id,
    method: 'delete'
  })
}

// 根据id获取表信息
export function getTablesByDataSourceId(query) {
  return request({
    url: '/da/daAsset/getTablesByDataSourceId',
    method: 'get',
    params: query
  })
}


// 根据id获取表信息
export function getColumnByAssetId(query) {
  return request({
    url: '/da/daAssetColumn/getColumnByAssetId',
    method: 'get',
    params: query
  })
}

// 获取数据源里面的数据表
export function getDaDatasourceTableList(id) {
  return request({
    url: '/dm/dmDatasource/tableList/' + id,
    method: 'get'
  })
}

// 获取数据源里面的数据表的数据字段
export function getColumnsList(data) {
  return request({
    url: '/dm/dmDatasource/columnsList',
    method: 'post',
    data: data
  })
}

