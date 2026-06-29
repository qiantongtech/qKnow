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

import compression from 'vite-plugin-compression';

export default function createCompression(env) {
    const { VITE_BUILD_COMPRESS } = env;
    const plugin = [];
    if (VITE_BUILD_COMPRESS) {
        const compressList = VITE_BUILD_COMPRESS.split(',');
        if (compressList.includes('gzip')) {
            plugin.push(
                compression({
                    ext: '.gz',
                    deleteOriginFile: false
                })
            );
        }
        if (compressList.includes('brotli')) {
            plugin.push(
                compression({
                    ext: '.br',
                    algorithm: 'brotliCompress',
                    deleteOriginFile: false
                })
            );
        }
    }
    return plugin;
}
