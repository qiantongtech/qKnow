import request from '@/utils/request'

// 查询AI 模型列表
export function listModel(query) {
  return request({
    url: '/ai/model/list',
    method: 'get',
    params: query
  })
}

// 查询AI 模型详细
export function getModel(id) {
  return request({
    url: '/ai/model/' + id,
    method: 'get'
  })
}

// 新增AI 模型
export function addModel(data) {
  return request({
    url: '/ai/model',
    method: 'post',
    data: data
  })
}

// 修改AI 模型
export function updateModel(data) {
  return request({
    url: '/ai/model',
    method: 'put',
    data: data
  })
}

// 删除AI 模型
export function delModel(id) {
  return request({
    url: '/ai/model/' + id,
    method: 'delete'
  })
}
