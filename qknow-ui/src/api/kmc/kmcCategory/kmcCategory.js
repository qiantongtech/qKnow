import request from '@/utils/request'

// 查询知识分类列表
export function listKmcCategory(query) {
    return request({
        url: '/kmc/kmcCategory/list',
        method: 'get',
        params: query
    })
}

// 查询知识分类详细
export function getKmcCategory(id) {
    return request({
        url: '/kmc/kmcCategory/' + id,
        method: 'get'
    })
}

// 新增知识分类
export function addKmcCategory(data) {
    return request({
        url: '/kmc/kmcCategory',
        method: 'post',
        data: data
    })
}

// 修改知识分类
export function updateKmcCategory(data) {
    return request({
        url: '/kmc/kmcCategory',
        method: 'put',
        data: data
    })
}

// 删除知识分类
export function delKmcCategory(id) {
    return request({
        url: '/kmc/kmcCategory/' + id,
        method: 'delete'
    })
}

// 查询全部知识分类数据
export function getAllList(query) {
    return request({
        url: '/kmc/kmcCategory/allList',
        method: 'get',
        params: query
    })
}

// 查询部门下拉树结构
export function kmcCategoryTree() {
    return request({
        url: '/kmc/kmcCategory/kmcCategoryTree',
        method: 'get'
    })
}
