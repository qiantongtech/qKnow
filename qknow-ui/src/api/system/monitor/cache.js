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

import request from '@/utils/request.js';

// 查询缓存详细
export function getCache() {
    return request({
        url: '/monitor/cache',
        method: 'get'
    });
}

// 查询缓存名称列表
export function listCacheName() {
    return request({
        url: '/monitor/cache/getNames',
        method: 'get'
    });
}

// 查询缓存键名列表
export function listCacheKey(cacheName) {
    return request({
        url: '/monitor/cache/getKeys/' + cacheName,
        method: 'get'
    });
}

// 查询缓存内容
export function getCacheValue(cacheName, cacheKey) {
    return request({
        url: '/monitor/cache/getValue/' + cacheName + '/' + cacheKey,
        method: 'get'
    });
}

// 清理指定名称缓存
export function clearCacheName(cacheName) {
    return request({
        url: '/monitor/cache/clearCacheName/' + cacheName,
        method: 'delete'
    });
}

// 清理指定键名缓存
export function clearCacheKey(cacheKey) {
    return request({
        url: '/monitor/cache/clearCacheKey/' + cacheKey,
        method: 'delete'
    });
}

// 清理全部缓存
export function clearCacheAll() {
    return request({
        url: '/monitor/cache/clearCacheAll',
        method: 'delete'
    });
}
