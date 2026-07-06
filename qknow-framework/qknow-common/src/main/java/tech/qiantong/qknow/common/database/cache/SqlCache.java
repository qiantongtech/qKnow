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

package tech.qiantong.qknow.common.database.cache;


import tech.qiantong.qknow.common.database.utils.MD5Util;

import java.util.Arrays;

/**
 * SQL缓存接口
 */
public interface SqlCache {

    /**
     * 计算key
     */
    default String buildSqlCacheKey(String sql, Object[] args) {
        return MD5Util.encrypt(sql + ":" + Arrays.toString(args));
    }

    /**
     * 存入缓存
     * @param key   key
     * @param value 值
     */
    void put(String key, Object value, long ttl);

    /**
     * 获取缓存
     * @param key   key
     * @return
     */
    <T> T get(String key);

    /**
     * 删除缓存
     * @param key  key
     */
    void delete(String key);
}
