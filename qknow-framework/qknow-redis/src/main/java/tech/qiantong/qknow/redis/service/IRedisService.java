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

package tech.qiantong.qknow.redis.service;

import java.util.List;
import java.util.Map;

public interface IRedisService {

    /**
     * 设置
     *
     * @param key
     * @param value
     */
    void set(String key, String value);

    /**
     * 设置，带超时
     *
     * @param key
     * @param value
     * @param timeout
     */
    void set(String key, String value, long timeout);

    String get(String key);

    boolean delete(String key);

    void leftPush(String key, String value);

    void rightPush(String key, String value);

    void leftPushAll(String key, List<String> value);

    String rightPop(String key);

    String leftPop(String key);

    String rightRead(String key);

    List<String> range(String key, Integer start, Integer end);

    Long getListSize(String key);

    void hashPut(String key, String hashKey, String value);

    String hashGet(String key, String hashKey);

    Long hashIncrement(String key, String hashKey, long delta);

    Long hashDelete(String key, Object... hashKeys);

    Map<String, Object> hashGetAll(String key);

    List<Object> hashMultiGet(String key, List<String> hashKeys);
}
