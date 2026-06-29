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

/**
 * v-hasRole 角色权限处理
 * Copyright (c) 2019 qiantong
 */

import useUserStore from '@/store/system/user';

export default {
    mounted(el, binding, vnode) {
        const { value } = binding;
        const super_admin = 'admin';
        const roles = useUserStore().roles;

        if (value && value instanceof Array && value.length > 0) {
            const roleFlag = value;

            const hasRole = roles.some((role) => {
                return super_admin === role || roleFlag.includes(role);
            });

            if (!hasRole) {
                el.parentNode && el.parentNode.removeChild(el);
            }
        } else {
            throw new Error(`请设置角色权限标签值`);
        }
    }
};
