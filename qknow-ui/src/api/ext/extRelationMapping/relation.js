import request from '@/utils/request'

// 查询关系映射列表
export function listRelation(query) {
  return request({
    url: '/ext/extRelation/list',
    method: 'get',
    params: query
  })
}

// 查询关系映射详细
export function getRelation(id) {
  return request({
    url: '/ext/extRelation/' + id,
    method: 'get'
  })
}

// 新增关系映射
export function addRelation(data) {
  return request({
    url: '/ext/extRelation',
    method: 'post',
    data: data
  })
}

// 修改关系映射
export function updateRelation(data) {
  return request({
    url: '/ext/extRelation',
    method: 'put',
    data: data
  })
}

// 删除关系映射
export function delRelation(id) {
  return request({
    url: '/ext/extRelation/' + id,
    method: 'delete'
  })
}
