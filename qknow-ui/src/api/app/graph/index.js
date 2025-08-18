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
