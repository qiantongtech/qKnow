import request from '@/utils/request'

// 查询概念配置列表
export function listSchema(query) {
    return request({
        url: '/ext/schema/list',
        method: 'get',
        params: query
    })
}

// 查询概念配置详细
export function getSchema(id) {
    return request({
        url: '/ext/schema/' + id,
        method: 'get'
    })
}

// 新增概念配置
export function addSchema(data) {
    return request({
        url: '/ext/schema',
        method: 'post',
        data: data
    })
}

// 修改概念配置
export function updateSchema(data) {
    return request({
        url: '/ext/schema',
        method: 'put',
        data: data
    })
}

// 删除概念配置
export function delSchema(id) {
    return request({
        url: '/ext/schema/' + id,
        method: 'delete'
    })
}

// 查询全部数据
export function getExtSchemaAllList(query) {
    return request({
        url: '/ext/schema/allList',
        method: 'get',
        params: query
    })
}
