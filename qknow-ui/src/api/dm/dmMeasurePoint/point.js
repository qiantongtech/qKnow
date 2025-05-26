import request from '@/utils/request'

// 查询物联网测点列表
export function listPoint(query) {
    return request({
        url: '/dm/point/list',
        method: 'get',
        params: query
    })
}

// 查询物联网测点详细
export function getPoint(id) {
    return request({
        url: '/dm/point/' + id,
        method: 'get'
    })
}

// 新增物联网测点
export function addPoint(data) {
    return request({
        url: '/dm/point',
        method: 'post',
        data: data
    })
}

// 修改物联网测点
export function updatePoint(data) {
    return request({
        url: '/dm/point',
        method: 'put',
        data: data
    })
}

// 删除物联网测点
export function delPoint(id) {
    return request({
        url: '/dm/point/' + id,
        method: 'delete'
    })
}
