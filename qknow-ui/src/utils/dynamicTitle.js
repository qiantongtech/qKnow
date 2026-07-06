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

import store from '@/store';
import defaultSettings from '@/settings';
import useSettingsStore from '@/store/system/settings';

/**
 * 动态修改标题
 */
export function useDynamicTitle() {
    const settingsStore = useSettingsStore();
    if (settingsStore.dynamicTitle) {
        document.title = settingsStore.title + ' - ' + defaultSettings.title;
    } else {
        document.title = defaultSettings.title;
    }
}
