import request from '@/utils/request'

// 查询结构化抽取任务列表
export function listExtStruct(query) {
    return request({
        url: '/ext/extStruct/list',
        method: 'get',
        params: query
    })
}

// 查询结构化抽取任务详细
export function getExtStruct(id) {
    return request({
        url: '/ext/extStruct/' + id,
        method: 'get'
    })
}

//获取结构化任务抽取结果
export function getExtStructByTaskId(id) {
    return request({
        url: '/ext/extStruct/getExtStructByTaskId?taskId=' + id,
        method: 'get'
    })
}

export function getAttributeInformation(attributeIds) {
    return request({
        url: '/ext/extStruct/getAttributeInformation?attributeIds=' + attributeIds,
        method: 'get',
    })
}

// 新增结构化抽取任务
export function addExtStruct(data) {
    return request({
        url: '/ext/extStruct',
        method: 'post',
        data: data
    })
}

//执行抽取
export function extExecuteExtraction(data) {
    return request({
        url: '/ext/extStruct/executeExtraction',
        method: 'post',
        data: data
    })
}

//新建结构化抽取任务
export function addExtStructDataMapping(data) {
    return request({
        url: '/ext/extStruct/addDataMapping',
        method: 'post',
        data: data
    })
}

//修改结构化抽取任务
export function updateExtStructDataMapping(data) {
    return request({
        url: '/ext/extStruct/updateDataMapping',
        method: 'post',
        data: data
    })
}

//发布
export function strutReleaseByTaskId(data) {
    return request({
        url: '/ext/extStruct/releaseByTaskId',
        method: 'post',
        data: data
    })
}

//取消发布
export function strutCancelReleaseByTaskId(data) {
    return request({
        url: '/ext/extStruct/cancelReleaseByTaskId',
        method: 'post',
        data: data
    })
}

//修改关系
export function updateRelationship(data) {
    return request({
        url: '/ext/extStruct/updateRelationship',
        method: 'post',
        data: data
    })
}

//删除关系
export function deleteRelationship(data) {
    return request({
        url: '/ext/extStruct/deleteRelationship',
        method: 'post',
        data: data
    })
}

//删除节点的某个属性
export function deleteNodeAttribute(data) {
    return request({
        url: '/ext/extStruct/deleteNodeAttribute',
        method: 'post',
        data: data
    })
}

//修改节点的某个属性
export function updateNodeAttribute(data) {
    return request({
        url: '/ext/extStruct/updateNodeAttribute',
        method: 'post',
        data: data
    })
}

// 修改结构化抽取任务
export function updateExtStruct(data) {
    return request({
        url: '/ext/extStruct',
        method: 'put',
        data: data
    })
}

// 删除结构化抽取任务
export function delExtStruct(id) {
    return request({
        url: '/ext/extStruct/' + id,
        method: 'delete'
    })
}

// 修改结构化发布状态
export function updateStructTaskPublishStatus(data) {
    return request({
        url: '/ext/extStruct/updateStructTaskPublishStatus',
        method: 'post',
        data: data
    })
}


// 定时任务立即执行一次
export function runStructTask(taskId, updateType) {
    const data = {
      taskId,
      updateType
    }
    return request({
      url: '/ext/extStruct/runStructTask',
      method: 'put',
      data: data
    })
  }