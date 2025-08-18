import request from '@/utils/request'

// 查询告警配置列表
export function listConfig(query) {
    return request({
        url: '/dm/config/list',
        method: 'get',
        params: query
    })
}

// 查询告警配置详细
export function getConfig(id) {
    return request({
        url: '/dm/config/' + id,
        method: 'get'
    })
}

// 新增告警配置
export function addConfig(data) {
    return request({
        url: '/dm/config',
        method: 'post',
        data: data
    })
}

// 修改告警配置
export function updateConfig(data) {
    return request({
        url: '/dm/config',
        method: 'put',
        data: data
    })
}

// 删除告警配置
export function delConfig(id) {
    return request({
        url: '/dm/config/' + id,
        method: 'delete'
    })
}
