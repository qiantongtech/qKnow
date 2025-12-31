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

export function getTaskTextList(query) {
  return request({
    url: '/ext/unstructTask/getTaskTextList',
    method: 'get',
    params: query
  })
}

export function getExtExtractionData(query) {
  return request({
    url: '/ext/unstructTask/getExtExtractionData',
    method: 'get',
    params: query
  })
}

export function getExtExtraction(query) {
  return request({
    url: '/ext/unstructTask/getExtExtraction',
    method: 'get',
    params: query
  })
}

// 图谱探索暂用
export function getExtractionAllData(query) {
  return request({
    url: '/ext/unstructTask/getExtractionAllData',
    method: 'get',
    params: query
  })
}

export function deleteNode(id) {
  return request({
    url: '/ext/unstructTask/deleteNode?id=' + id,
    method: 'post'
  })
}

export function executeExtraction(data) {
  return request({
    url: '/ext/unstructTask/executeExtraction',
    method: 'post',
    data: data
  })
}

//发布
export function strutReleaseByTaskId(data) {
  return request({
    url: '/ext/unstructTask/releaseByTaskId',
    method: 'post',
    data: data
  })
}

//取消发布
export function strutCancelReleaseByTaskId(data) {
  return request({
    url: '/ext/unstructTask/cancelReleaseByTaskId',
    method: 'post',
    data: data
  })
}

// 查询非结构化抽取任务列表
export function listUnstructTask(query) {
  return request({
    url: '/ext/unstructTask/list',
    method: 'get',
    params: query
  })
}

// 查询非结构化抽取任务详细
export function getUnstructTask(id) {
  return request({
    url: '/ext/unstructTask/' + id,
    method: 'get'
  })
}

// 新增非结构化抽取任务
export function addUnstructTask(data) {
  return request({
    url: '/ext/unstructTask',
    method: 'post',
    data: data
  })
}

// 修改非结构化抽取任务
export function updateUnstructTask(data) {
  return request({
    url: '/ext/unstructTask',
    method: 'put',
    data: data
  })
}

// 删除非结构化抽取任务
export function delUnstructTask(id) {
  return request({
    url: '/ext/unstructTask/' + id,
    method: 'delete'
  })
}
// 修改非结构化发布状态
export function updateUnStructTaskPublishStatus(data) {
    return request({
        url: '/ext/unstructTask/updateUnStructTaskPublishStatus',
        method: 'post',
        data: data
    })
}

export function getTextList(ids) {
  return request({
    url: '/ext/unstructTask/getTextList/' + ids,
    method: 'get',
  })
}
