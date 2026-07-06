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

package tech.qiantong.qknow.thirdparty.constant;

/**
 * 连接池常量
 *
 * @author qknow
 */
public class HttpClientConstants {

    /**
     * 默认连接超时
     */
    public static final long DEFAULT_CONNECT_TIMEOUT = 10000L;

    /**
     * 默认读取超时
     */
    public static final long DEFAULT_READ_TIMEOUT = 120000L;

    /**
     * 默认写入超时
     */
    public static final long DEFAULT_WRITE_TIMEOUT = 120000L;

    /**
     * 默认总时长
     */
    public static final long DEFAULT_TIMEOUT = DEFAULT_CONNECT_TIMEOUT + DEFAULT_READ_TIMEOUT + DEFAULT_WRITE_TIMEOUT;
}
