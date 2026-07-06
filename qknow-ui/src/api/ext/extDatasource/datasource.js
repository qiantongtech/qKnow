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
export function listDatasource(query) {
    return request({
        url: '/ext/datasource/list',
        method: 'get',
        params: query
    });
}

export function getTestConnection(id) {
    return request({
        url: '/ext/datasource/testConnection?id=' + id,
        method: 'get'
    });
}

export function getTableList(query) {
    return request({
        url: '/ext/datasource/getTableList',
        method: 'get',
        params: query
    });
}

export function getTableData(query) {
    return request({
        url: '/ext/datasource/getTableData',
        method: 'get',
        params: query
    });
}

//根据数据源id, 数据id和表名获取行数据
export function getTableDataByDataId(query) {
    return request({
        url: '/ext/datasource/getTableDataByDataId',
        method: 'get',
        params: query
    });
}

// 查询数据源详细
export function getDatasource(id) {
    return request({
        url: '/ext/datasource/' + id,
        method: 'get'
    });
}

// 新增数据源
export function addDatasource(data) {
    return request({
        url: '/ext/datasource',
        method: 'post',
        data: data
    });
}

// 修改数据源
export function updateDatasource(data) {
    return request({
        url: '/ext/datasource',
        method: 'put',
        data: data
    });
}

// 删除数据源
export function delDatasource(id) {
    return request({
        url: '/ext/datasource/' + id,
        method: 'delete'
    });
}
