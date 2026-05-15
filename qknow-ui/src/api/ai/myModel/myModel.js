import request from '@/utils/request'

// 更改模型是否启用
export function changeModelEnable(data) {
    return request({
        url: '/ai/model/changeModelEnable',
        method: 'put',
        data
    })
}

// 更改模型是否启用
export function getChatModelDict() {
    return request({
        url: '/ai/model/getChatModelDict',
        method: 'get'
    })
}

// 获取平台下模型列表
export function getModelPage(query) {
    return request({
        url: '/ai/model/getModelPage',
        method: 'get',
        params: query,
        timeout: 40 * 1000
    })
}

// 获取平台下模型列表
export function syncModel(keyId) {
    return request({
        url: '/ai/model/syncModel?keyId='+keyId,
        method: 'get'
    })
}