import request from '@/utils/request'

// 查询概念映射列表
export function listSchema(query) {
  return request({
    url: '/ext/extSchema/list',
    method: 'get',
    params: query
  })
}

// 查询概念映射详细
export function getSchema(id) {
  return request({
    url: '/ext/extSchema/' + id,
    method: 'get'
  })
}

// 新增概念映射
export function addSchema(data) {
  return request({
    url: '/ext/extSchema',
    method: 'post',
    data: data
  })
}

// 修改概念映射
export function updateSchema(data) {
  return request({
    url: '/ext/extSchema',
    method: 'put',
    data: data
  })
}

// 删除概念映射
export function delSchema(id) {
  return request({
    url: '/ext/extSchema/' + id,
    method: 'delete'
  })
}
