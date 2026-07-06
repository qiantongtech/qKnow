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

import vue from '@vitejs/plugin-vue';

import createAutoImport from './auto-import';
import createSvgIcon from './svg-icon';
import createCompression from './compression';
import createSetupExtend from './setup-extend';
// import { visualizer } from "rollup-plugin-visualizer";

export default function createVitePlugins(viteEnv, isBuild = false) {
    const vitePlugins = [vue()];
    vitePlugins.push(createAutoImport());
    vitePlugins.push(createSetupExtend());
    vitePlugins.push(createSvgIcon(isBuild));
    // vitePlugins.push(
    //   visualizer({
    //     gzipSize: true,
    //     brotliSize: true,
    //     filename: "err.html", // 默认在项目根目录下生成stats.html文件，可自定义
    //     open: true, //生成后自动打开浏览器查看
    //   }),
    // );
    isBuild && vitePlugins.push(...createCompression(viteEnv));
    return vitePlugins;
}
