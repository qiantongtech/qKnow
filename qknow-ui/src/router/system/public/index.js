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
/* AI模块公共路由 */
import AIPublicRouter from '../../ai/public/index.js';
// 系统模块公共路由
export default [
    {
        path: '/redirect',
        component: Layout,
        hidden: true,
        children: [
            {
                path: '/redirect/:path(.*)',
                component: () => import('@/views/system/redirect/index.vue')
            }
        ]
    },
    {
        path: '/sso',
        component: () => import('@/views/system/sso'),
        hidden: true
    },
    {
        path: '/login',
        component: () => import('@/views/system/login.vue'),
        hidden: true
    },
    {
        path: '/sso/login',
        component: () => import('../../../../ssoLogin/index.vue'),
        hidden: true
    },
    {
        path: '/register',
        component: () => import('@/views/system/register.vue'),
        hidden: true
    },
    {
        path: '/:pathMatch(.*)*',
        component: () => import('@/views/system/error/404.vue'),
        hidden: true
    },
    {
        path: '/401',
        component: () => import('@/views/system/error/401.vue'),
        hidden: true
    },
    // {
    //   path: '',
    //   component: Layout,
    //   redirect: '/index',
    //   children: [
    //     {
    //       path: '/index',
    //       component: () => import('@/views/system/index.vue'),
    //       name: 'Index',
    //       meta: { title: '首页', icon: '首页', affix: true }
    //     }
    //   ]
    // },
    {
        path: '/index',
        redirect: '/kd/integrated',
        hidden: true
    },
    {
        path: '',
        component: 'Layout',
        redirect: '/kd/integrated',
        meta: { title: '看板', icon: '组 24885' },
        children: [
            {
                path: 'kd/integrated',
                component: () => import('@/views/system/index.vue'),
                name: 'Integrated',
                meta: { title: '综合看板', icon: 'book-open-fill' }
            }
        ]
    },
    {
        path: '/bases',
        component: Layout,
        redirect: 'message',
        children: [
            {
                path: 'message',
                component: () => import('@/views/system/system/message/index.vue'),
                name: 'Message',
                meta: { title: '我的消息', icon: 'message' },
                hidden: true
            }
        ]
    },
    {
        path: '/user',
        component: Layout,
        hidden: true,
        redirect: 'noredirect',
        children: [
            {
                path: 'profile',
                component: () => import('@/views/system/system/user/profile/index.vue'),
                name: 'Profile',
                meta: { title: '个人中心', icon: 'user' }
            }
        ]
    },
    {
        path: '/system',
        component: Layout,
        redirect: 'ai',
        // hidden: true,
        children: [...AIPublicRouter]
    }
];
