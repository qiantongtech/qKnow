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

import request from "@/utils/request";

// 查询知识推荐标签列表
export function getGraph(query) {
  return request({
    url: "/app/graph/getGraph",
    method: "get",
    params: query,
  });
}
// 获取实体分页
export function getGraphPage(query) {
  return request({
    url: "/app/graph/getGraphPage",
    method: "get",
    params: query,
  });
}
// 发布 / 取消发布
export function updateReleaseStatus(data) {
  return request({
    url: "/app/graph/updateReleaseStatus",
    method: "post",
    data: data,
  });
}
// 根据节点id和属性的key删除属性
export function deleteNodeAttributeById(data) {
  return request({
    url: "/app/graph/deleteNodeAttributeById",
    method: "delete",
    data: data,
  });
}
// 新增实体
export function addNode(data) {
  return request({
    url: "/app/graph/addNode",
    method: "post",
    data: data,
  });
}
// 根据节点id删除对应的节点
export function deleteNode(id) {
  return request({
    url: `/app/graph/deleteNode/${id}`,
    method: "delete",
  });
}
// 根据节点ids删除对应的节点
export function deleteNodeByIds(ids) {
  return request({
    url: `/app/graph/deleteNodeByIds/${ids}`,
    method: "delete",
  });
}

// 新增关系
export function addTripletRel(data) {
  return request({
    url: "/app/graph/addTripletRel",
    method: "post",
    data: data,
  });
}
// 根据关系ids删除关系
export function deleteRelationshipsByIds(ids) {
  return request({
    url: `/app/graph/deleteRelationshipsByIds/${ids}`,
    method: "delete",
  });
}
// 根据关系ids删除关系
export function deleteRelationshipById(id) {
  return request({
    url: `/app/graph/deleteRelationshipById/${id}`,
    method: "delete",
  });
}

// 统计 (实体数量,关系类型数量,三元组数量)
export function getGraphDataStatistics() {
  return request({
    url: "/app/graph/getGraphDataStatistics",
    method: "get",
  });
}
