import request from '@/utils/request'

// 查询概念属性列表
export function listAttribute(query) {
    return request({
        url: '/ext/attribute/list',
        method: 'get',
        params: query
    })
}

// 查询概念属性详细
export function getAttribute(id) {
    return request({
        url: '/ext/attribute/' + id,
        method: 'get'
    })
}

// 新增概念属性
export function addAttribute(data) {
    return request({
        url: '/ext/attribute',
        method: 'post',
        data: data
    })
}

// 修改概念属性
export function updateAttribute(data) {
    return request({
        url: '/ext/attribute',
        method: 'put',
        data: data
    })
}

// 删除概念属性
export function delAttribute(id) {
    return request({
        url: '/ext/attribute/' + id,
        method: 'delete'
    })
}

// 查询概念属性列表
export function getAllExtSchemaAttributeList(query) {
    return request({
        url: '/ext/attribute/getAllExtSchemaAttributeList',
        method: 'get',
        params: query
    })
}
