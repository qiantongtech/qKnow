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

import Layout from '@/layout/index.vue';

export default [
    {
        path: 'ext/extSchemaDetail',
        hidden: true,
        children: [
            {
                path: 'schemaDetail',
                component: () => import('@/views/ext/extSchema/detail/index.vue'),
                name: 'tree',
                meta: { title: '概念配置属性', activeMenu: '/kg/ext/schema' }
            }
        ]
    },
    {
        path: 'ext/extractResults',
        redirect: 'extractResults',
        hidden: true,
        children: [
            {
                path: '',
                component: () => import('@/views/app/graphExploration/index.vue'),
                name: 'extractResultsIndex',
                meta: { title: '非结构化抽取结果', activeMenu: '/kg/ext/unstructTask' }
            }
        ]
    },
    {
        path: 'ext/structuredResult',
        redirect: 'structuredResult',
        hidden: true,
        children: [
            {
                path: '',
                component: () => import('@/views/app/graphExploration/index.vue'),
                name: 'structuredResultIndex',
                meta: { title: '结构化抽取结果', activeMenu: '/kg/ext/extStructTask' }
            }
        ]
    },
    {
        path: 'ext/addStructTask',
        redirect: 'addStructTask',
        hidden: true,
        children: [
            {
                path: '',
                component: () => import('@/views/ext/extStructTask/add/index.vue'),
                name: 'addStructTaskIndex',
                meta: { title: '添加结构化抽取', activeMenu: '/kg/ext/extStructTask' }
            }
        ]
    },
    {
        path: 'ext/editStructTask',
        redirect: 'editStructTask',
        hidden: true,
        children: [
            {
                path: '',
                component: () => import('@/views/ext/extStructTask/add/index.vue'),
                name: 'editStructTaskIndex',
                meta: { title: '修改结构化抽取', activeMenu: '/kg/ext/extStructTask' }
            }
        ]
    }
];
