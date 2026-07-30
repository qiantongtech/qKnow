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

// 知识应用模块动公共路由
export default [
    {
        path: '/kb',
        component: Layout,
        redirect: 'kbTool',
        hidden: true,
        children: [
            {
                path: 'tool/toolDetail',
                component: () => import('@/views/kb/tool/detail/index.vue'),
                name: 'Method',
                meta: { title: '工具管理详情', activeMenu: '/kb/tool' }
            }
        ]
    },
    {
        path: '/kb',
        component: Layout,
        redirect: 'kbAgent',
        hidden: true,
        children: [
            {
                name: 'agent',
                path: 'bot/agent/build',
                component: () => import('@/views/kb/agent/index.vue'),
                meta: { title: 'Agent编排', activeMenu: '/kb/bot/agent' }
            }
        ]
    },
    {
        path: '/kb',
        component: Layout,
        children: [
            {
                path: 'bot/processflow',
                component: () => import('@/views/kb/bot/build/index.vue'),
                name: 'ProcessFlow',
                meta: { title: 'Bot构建', activeMenu: '/kb/bot' }
            },
            {
                path: 'bot/codeNative',
                component: () => import('@/views/kb/codeNative/index.vue'),
                name: 'CodeNative',
                meta: { title: '白盒化开发', activeMenu: '/system/bot' }
            },
            {
                path: 'bot/workflow/detail',
                component: () => import('@/views/kb/bot/detail/index.vue'),
                name: 'workflowDetail',
                meta: { title: '工作流详情', activeMenu: '/kb/bot/workflow' }
            },
            {
                path: 'bot/chatflow/detail',
                component: () => import('@/views/kb/bot/detail/index.vue'),
                name: 'chatflowDetail',
                meta: { title: 'Chatflow详情', activeMenu: '/kb/bot/chatflow' }
            },
            {
                path: 'bot/agent/detail',
                component: () => import('@/views/kb/bot/detail/index.vue'),
                name: 'agentDetail',
                meta: { title: 'agent详情', activeMenu: '/kb/bot/agent' }
            },
            {
                path: 'mcp/mcpConfigDetail',
                component: () => import('@/views/kb/mcp/detail/index.vue'),
                name: 'mcpDetail',
                meta: { title: 'MCP 详情', activeMenu: '/kb/mcp' }
            }
        ]
    }
];
