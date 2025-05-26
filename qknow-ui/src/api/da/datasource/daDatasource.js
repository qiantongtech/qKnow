import request from '@/utils/request'

// 查询数据源列表
export function listDaDatasource(query) {
  return request({
    url: '/dm/dmDatasource/list',
    method: 'get',
    params: query
  })
}

// 查询数据源列表
export function getDaDatasourceList(query) {
  return request({
    url: '/dm/dmDatasource/getDataSourceByAsset',
    method: 'get',
    params: query
  })
}

// 查询数据源详细
export function getDaDatasource(id) {
  return request({
    url: '/dm/dmDatasource/' + id,
    method: 'get'
  })
}


// 测试连接
export function clientsTest(id) {
  return request({
    url: '/dm/dmDatasource/clientsTest/' + id,
    method: 'get'
  })
}

// 新增数据源
export function addDaDatasource(data) {
  return request({
    url: '/dm/dmDatasource',
    method: 'post',
    data: data
  })
}

// 修改数据源
export function updateDaDatasource(data) {
  return request({
    url: '/dm/dmDatasource',
    method: 'put',
    data: data
  })
}

// 删除数据源
export function delDaDatasource(id) {
  return request({
    url: '/dm/dmDatasource/' + id,
    method: 'delete'
  })
}

// 根据id获取表信息
export function getTablesByDataSourceId(query) {
  return request({
    url: '/da/daAsset/getTablesByDataSourceId',
    method: 'get',
    params: query
  })
}


// 根据id获取表信息
export function getColumnByAssetId(query) {
  return request({
    url: '/da/daAssetColumn/getColumnByAssetId',
    method: 'get',
    params: query
  })
}

// 获取数据源里面的数据表
export function getDaDatasourceTableList(id) {
  return request({
    url: '/dm/dmDatasource/tableList/' + id,
    method: 'get'
  })
}

// 获取数据源里面的数据表的数据字段
export function getColumnsList(data) {
  return request({
    url: '/dm/dmDatasource/columnsList',
    method: 'post',
    data: data
  })
}

