import request from '@/utils/request'

// 查询专家经验列表
export function listAdvice(query) {
    return request({
        url: '/dm/advice/list',
        method: 'get',
        params: query
    })
}

// 查询专家经验详细
export function getAdvice(id) {
    return request({
        url: '/dm/advice/' + id,
        method: 'get'
    })
}

// 新增专家经验
export function addAdvice(data) {
    return request({
        url: '/dm/advice',
        method: 'post',
        data: data
    })
}

// 修改专家经验
export function updateAdvice(data) {
    return request({
        url: '/dm/advice',
        method: 'put',
        data: data
    })
}

// 删除专家经验
export function delAdvice(id) {
    return request({
        url: '/dm/advice/' + id,
        method: 'delete'
    })
}
