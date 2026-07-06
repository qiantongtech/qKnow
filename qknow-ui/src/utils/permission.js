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

import useUserStore from '@/store/system/user';

/**
 * 字符权限校验
 * @param {Array} value 校验值
 * @returns {Boolean}
 */
export function checkPermi(value) {
    if (value && value instanceof Array && value.length > 0) {
        const permissions = useUserStore().permissions;
        const permissionDatas = value;
        const all_permission = '*:*:*';

        const hasPermission = permissions.some((permission) => {
            return all_permission === permission || permissionDatas.includes(permission);
        });

        if (!hasPermission) {
            return false;
        }
        return true;
    } else {
        console.error(`need roles! Like checkPermi="['system:user:add','system:user:edit']"`);
        return false;
    }
}

/**
 * 角色权限校验
 * @param {Array} value 校验值
 * @returns {Boolean}
 */
export function checkRole(value) {
    if (value && value instanceof Array && value.length > 0) {
        const roles = useUserStore().roles;
        const permissionRoles = value;
        const super_admin = 'admin';

        const hasRole = roles.some((role) => {
            return super_admin === role || permissionRoles.includes(role);
        });

        if (!hasRole) {
            return false;
        }
        return true;
    } else {
        console.error(`need roles! Like checkRole="['admin','editor']"`);
        return false;
    }
}
