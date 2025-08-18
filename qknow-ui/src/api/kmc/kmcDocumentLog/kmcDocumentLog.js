import request from '@/utils/request'

// 查询文件操作日志列表
export function listKmcDocumentLog(query) {
    return request({
        url: '/kmc/kmcDocumentLog/list',
        method: 'get',
        params: query
    })
}

// 查询文件操作日志详细
export function getKmcDocumentLog(id) {
    return request({
        url: '/kmc/kmcDocumentLog/' + id,
        method: 'get'
    })
}

// 新增文件操作日志
export function addKmcDocumentLog(data) {
    return request({
        url: '/kmc/kmcDocumentLog',
        method: 'post',
        data: data
    })
}

// 修改文件操作日志
export function updateKmcDocumentLog(data) {
    return request({
        url: '/kmc/kmcDocumentLog',
        method: 'put',
        data: data
    })
}

// 删除文件操作日志
export function delKmcDocumentLog(id) {
    return request({
        url: '/kmc/kmcDocumentLog/' + id,
        method: 'delete'
    })
}

export function getUserAttention(query) {
    return request({
        url: '/kmc/kmcDocumentLog/getUserAttention',
        method: 'get',
        params: query
    })
}
