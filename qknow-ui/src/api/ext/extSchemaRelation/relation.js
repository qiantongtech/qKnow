import request from '@/utils/request'

// 查询关系配置列表
export function listRelation(query) {
    return request({
        url: '/ext/relation/list',
        method: 'get',
        params: query
    })
}

// 查询关系配置详细
export function getRelation(id) {
    return request({
        url: '/ext/relation/' + id,
        method: 'get'
    })
}

// 新增关系配置
export function addRelation(data) {
    return request({
        url: '/ext/relation',
        method: 'post',
        data: data
    })
}

// 修改关系配置
export function updateRelation(data) {
    return request({
        url: '/ext/relation',
        method: 'put',
        data: data
    })
}

// 删除关系配置
export function delRelation(id) {
    return request({
        url: '/ext/relation/' + id,
        method: 'delete'
    })
}

export function listByIds(ids) {
    return request({
        url: '/ext/relation/listByIds/' + ids,
        method: 'get'
    })
}

