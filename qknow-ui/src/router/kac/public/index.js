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

/* Layout */
import Layout from '@/layout/index.vue';

// 应用中心模块动公共路由
export default [
    {
        path: '/kac',
        component: Layout,
        redirect: '/kac/horizontal',
        hidden: true,
        children: [
            {
                path: 'horizontal/horizontalDetail',
                component: () => import('@/views/kac/horizontal/detail/index.vue'),
                name: 'horizontalDetail',
                meta: { title: '横向通用应用详情', activeMenu: '/kac/horizontal' }
            },
            {
                path: 'myApp/myAppDetail',
                component: () => import('@/views/kac/horizontal/detail/index.vue'),
                name: 'myAppDetail',
                meta: { title: '我的应用详情', activeMenu: '/kac/myApp' }
            },
            {
                path: 'vertical/verticalDetail',
                component: () => import('@/views/kac/horizontal/detail/index.vue'),
                name: 'verticalDetail',
                meta: { title: '纵向行业应用详情', activeMenu: '/kac/vertical' }
            }
        ]
    }
];
