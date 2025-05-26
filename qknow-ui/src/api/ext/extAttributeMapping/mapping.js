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
