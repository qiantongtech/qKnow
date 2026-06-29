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

// 系统模块动态路由，基于用户权限动态去加载
export default [
    {
        path: '/kmc',
        component: Layout,
        permissions: ['kmc:knowledgeBase:knowledgebase:list'],
        children: [
            {
                path: 'knowledgeBase',
                component: () => import('@/views/kmc/knowledgeBase/index.vue'),
                meta: {
                    title: '知识库',
                    activeMenu: '/kmc/knowledgeBase',
                    tagsView: false,
                    sidebar: false
                } // 标记此页面不使用标签视图
            }
        ]
    }
];
