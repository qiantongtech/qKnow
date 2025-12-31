/*
 * Copyright © 2026 Qiantong Technology Co., Ltd.
 * qKnow Knowledge Platform
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please apply for brand customization authorization via official channels.
 *  *
 * More information: https://qknow.qiantong.tech/business.html
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2026 江苏千桐科技有限公司
 * qKnow 知识平台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qknow.qiantong.tech/business.html
 */

/* Layout */
import Layout from '@/layout/index.vue'

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
    path: "/:pathMatch(.*)*",
    component: () => import('@/views/system/error/404.vue'),
    hidden: true
  },
  {
    path: '/401',
    component: () => import('@/views/system/error/401.vue'),
    hidden: true
  },
  {
    path: '',
    component: Layout,
    redirect: '/index',
    children: [
      {
        path: '/index',
        component: () => import('@/views/system/index.vue'),
        name: 'Index',
        meta: { title: '首页', icon: '首页', affix: true }
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
  }
]
