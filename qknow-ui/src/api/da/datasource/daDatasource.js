/*
 * Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
 *
 * This file is part of qKnow Intelligent Agent Building Platform (Open Source Edition).
 *
 * qKnow is licensed under Apache License 2.0 with additional qKnow terms.
 * You may use qKnow for commercial purposes, but you may not remove, hide,
 * modify, or replace the qKnow logo, copyright notices, license notices,
 * or attribution information without a separate commercial license.
 *
 * White-label use, OEM distribution, rebranding, or presenting qKnow as
 * another product requires separate commercial authorization from
 * Jiangsu Qiantong Technology Co., Ltd.
 *
 * Business License: https://community.qknow.ai/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

import request from '@/utils/request';

// 查询数据源列表
export function listDaDatasource(query) {
    return request({
        url: '/dm/dmDatasource/list',
        method: 'get',
        params: query
    });
}

// 查询数据源列表
export function getDaDatasourceList(query) {
    return request({
        url: '/dm/dmDatasource/getDataSourceByAsset',
        method: 'get',
        params: query
    });
}

// 查询数据源详细
export function getDaDatasource(id) {
    return request({
        url: '/dm/dmDatasource/' + id,
        method: 'get'
    });
}

// 测试连接
export function clientsTest(id) {
    return request({
        url: '/dm/dmDatasource/clientsTest/' + id,
        method: 'get'
    });
}

// 测试连接(表单数据)
export function clientsTestWithForm(data) {
    return request({
        url: '/dm/dmDatasource/clientsTestWithForm',
        method: 'post',
        data: data
    });
}

// 新增数据源
export function addDaDatasource(data) {
    return request({
        url: '/dm/dmDatasource',
        method: 'post',
        data: data
    });
}

// 修改数据源
export function updateDaDatasource(data) {
    return request({
        url: '/dm/dmDatasource',
        method: 'put',
        data: data
    });
}

// 删除数据源
export function delDaDatasource(id) {
    return request({
        url: '/dm/dmDatasource/' + id,
        method: 'delete'
    });
}

// 根据id获取表信息
export function getTablesByDataSourceId(query) {
    return request({
        url: '/da/daAsset/getTablesByDataSourceId',
        method: 'get',
        params: query
    });
}

// 根据id获取表信息
export function getColumnByAssetId(query) {
    return request({
        url: '/da/daAssetColumn/getColumnByAssetId',
        method: 'get',
        params: query
    });
}

// 获取数据源里面的数据表
export function getDaDatasourceTableList(id) {
    return request({
        url: '/dm/dmDatasource/tableList/' + id,
        method: 'get'
    });
}

// 获取数据源里面的数据表的数据字段
export function getColumnsList(data) {
    return request({
        url: '/dm/dmDatasource/columnsList',
        method: 'post',
        data: data
    });
}
