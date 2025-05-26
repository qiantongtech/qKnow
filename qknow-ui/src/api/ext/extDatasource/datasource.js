import request from '@/utils/request'

// 查询数据源列表
export function listDatasource(query) {
  return request({
    url: '/ext/datasource/list',
    method: 'get',
    params: query
  })
}

export function getTestConnection(id) {
  return request({
    url: '/ext/datasource/testConnection?id=' + id,
    method: 'get'
  })
}

export function getTableList(query) {
  return request({
    url: '/ext/datasource/getTableList',
    method: 'get',
    params: query
  })
}

export function getTableData(query) {
  return request({
    url: '/ext/datasource/getTableData',
    method: 'get',
    params: query
  })
}

//根据数据源id, 数据id和表名获取行数据
export function getTableDataByDataId(query) {
  return request({
    url: '/ext/datasource/getTableDataByDataId',
    method: 'get',
    params: query
  })
}

// 查询数据源详细
export function getDatasource(id) {
  return request({
    url: '/ext/datasource/' + id,
    method: 'get'
  })
}

// 新增数据源
export function addDatasource(data) {
  return request({
    url: '/ext/datasource',
    method: 'post',
    data: data
  })
}

// 修改数据源
export function updateDatasource(data) {
  return request({
    url: '/ext/datasource',
    method: 'put',
    data: data
  })
}

// 删除数据源
export function delDatasource(id) {
  return request({
    url: '/ext/datasource/' + id,
    method: 'delete'
  })
}
