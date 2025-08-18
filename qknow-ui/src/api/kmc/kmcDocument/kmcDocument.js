import request from '@/utils/request'

// 查询知识文件列表
export function listDocument(query) {
    return request({
        url: '/kmcDocument/document/list',
        method: 'get',
        params: query
    })
}

// 查询知识文件详细
export function getDocument(id) {
    return request({
        url: '/kmcDocument/document/' + id,
        method: 'get'
    })
}

// 新增知识文件
export function addDocument(data) {
    return request({
        url: '/kmcDocument/document',
        method: 'post',
        data: data
    })
}

// 修改知识文件
export function updateDocument(data) {
    return request({
        url: '/kmcDocument/document',
        method: 'put',
        data: data
    })
}

// 删除知识文件
export function delDocument(id) {
    return request({
        url: '/kmcDocument/document/' + id,
        method: 'delete'
    })
}

// 获取文件的base64编码
export function getPdfPreview(data) {
    return request({
        url: '/kmcDocument/document/getPdfPreview',
        method: 'post',
        data: data
    })
}

// 修改下载次数并返回修改后的该条数据
export function updateDownloadCount(id) {
    return request({
        url: '/kmcDocument/document/downloadCount/' + id,
        method: 'get'
    })
}

// 修改预览次数并返回修改后的该条数据
export function updatePreviewCount(id) {
    return request({
        url: '/kmcDocument/document/previewCount/' + id,
        method: 'get'
    })
}

// 根据条件查询知识文件列表
export function selectList(query) {
    return request({
        url: '/kmcDocument/document/selectList',
        method: 'get',
        params: query
    })
}

// 获取多少种类型及每种类型下面的文件数量
export function getFileTypes() {
    return request({
        url: '/kmcDocument/document/getFileTypes',
        method: 'get',
    })
}

