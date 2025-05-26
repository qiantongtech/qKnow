import request from '@/utils/request'

// 查询自定义映射列表
export function listMapping(query) {
  return request({
    url: '/ext/extCustom/list',
    method: 'get',
    params: query
  })
}

// 查询自定义映射详细
export function getMapping(id) {
  return request({
    url: '/ext/extCustom/' + id,
    method: 'get'
  })
}

// 新增自定义映射
export function addMapping(data) {
  return request({
    url: '/ext/extCustom',
    method: 'post',
    data: data
  })
}

// 修改自定义映射
export function updateMapping(data) {
  return request({
    url: '/ext/extCustom',
    method: 'put',
    data: data
  })
}

// 删除自定义映射
export function delMapping(id) {
  return request({
    url: '/ext/extCustom/' + id,
    method: 'delete'
  })
}
