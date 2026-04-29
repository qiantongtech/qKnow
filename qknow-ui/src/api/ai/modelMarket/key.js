import request from '@/utils/request'

// 查询API秘钥列表
export function listKey(query) {
  return request({
    url: '/ai/key/list',
    method: 'get',
    params: query
  })
}

// 查询API秘钥列表
export function listByPlatform(platform) {
  return request({
    url: '/ai/key/listByPlatform?platform='+platform,
    method: 'get',
  })
}

// 查询API秘钥列表
export function myModelPage(query) {
  return request({
    url: '/ai/key/myModelPage',
    method: 'get',
    params: query
  })
}

// 查询API秘钥详细
export function getKey(id) {
  return request({
    url: '/ai/key/' + id,
    method: 'get'
  })
}

// 查询API秘钥详细
export function getByPlatform(platform) {
  return request({
    url: '/ai/key/getByPlatform?platform=' + platform,
    method: 'get'
  })
}

// 新增API秘钥
export function addKey(data) {
  return request({
    url: '/ai/key',
    method: 'post',
    data: data
  })
}

// 修改API秘钥
export function updateKey(data) {
  return request({
    url: '/ai/key',
    method: 'put',
    data: data
  })
}

// 移除API秘钥
export function removeKey(id) {
  return request({
    url: '/ai/key/'+id,
    method: 'delete',
  })
}

// 移除API秘钥
export function submitBatch(data) {
  return request({
    url: '/ai/key/submitBatch',
    method: 'post',
    data: data
  })
}
