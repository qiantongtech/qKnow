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
        path: '/kmc',
        component: Layout,
        redirect: 'knowledgeBaseEdit',
        children: [
            {
                path: 'knowledgeBase/add',
                component: () => import('@/views/kmc/knowledgeBase/components/settings.vue'),
                name: 'settingsAdd',
                meta: { title: '添加知识库', tagsView: false, sidebar: false },
                hidden: true
            }
        ]
    },
    {
        path: '/kmc/:kbId',
        component: Layout,
        children: [
            {
                path: 'recallLog',
                component: () => import('@/views/kmc/knowledgeBase/components/recallLog.vue'),
                name: 'recallLog',
                meta: { title: '召回测试记录', activeMenu: '/kmc/:kbId/recall' },
                hidden: true
            },
            {
                path: 'knowledgeSegment/index',
                component: () => import('@/views/kmc/knowledgeSegment/index.vue'),
                name: 'segmentIndex',
                meta: { title: '分段列表', activeMenu: '/kmc/:kbId/kmcDocument' },
                hidden: true
            },
            {
                path: 'knowledgeSegment/detail',
                component: () => import('@/views/kmc/knowledgeSegment/detail/index.vue'),
                name: 'segmentEdit',
                meta: { title: '分段详情', activeMenu: '/kmc/:kbId/knowledgeSegment/detail' },
                hidden: true
            },
            {
                path: 'kmcDocument/add',
                component: () => import('@/views/kmc/kmcDocument/selection/add.vue'),
                name: 'kmcDocumentAdd',
                meta: { title: '新增知识文件', activeMenu: '/kmc/:kbId/kmcDocument' },
                hidden: true
            },
            {
                path: 'kmcDocument/edit',
                component: () => import('@/views/kmc/kmcDocument/selection/add.vue'),
                name: 'kmcDocumentEdit',
                meta: { title: '修改知识文件', activeMenu: '/kmc/:kbId/kmcDocument' },
                hidden: true
            }
        ]
    }
];
