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

import Layout from "@/layout/index.vue";

export default [
  {
    path: "/ext/extSchemaDetail",
    component: Layout,
    redirect: "extSchema",
    hidden: true,
    children: [
      {
        path: "schemaDetail",
        component: () => import("@/views/ext/extSchema/detail/index.vue"),
        name: "tree",
        meta: { title: "概念配置属性", activeMenu: "/ext/schema" },
      },
    ],
  },
  {
    path: "/ext/extractResults",
    component: Layout,
    redirect: "extractResults",
    hidden: true,
    children: [
      {
        path: "",
        component: () => import("@/views/app/graphExploration/index.vue"),
        name: "extractResultsIndex",
        meta: { title: "非结构化抽取结果", activeMenu: "/ext/unstructTask" },
      },
    ],
  },
  {
    path: "/ext/structuredResult",
    component: Layout,
    redirect: "structuredResult",
    hidden: true,
    children: [
      {
        path: "",
        component: () => import("@/views/app/graphExploration/index.vue"),
        name: "structuredResultIndex",
        meta: { title: "结构化抽取结果", activeMenu: "/ext/extStructTask" },
      },
    ],
  },
  {
    path: "/ext/addStructTask",
    component: Layout,
    redirect: "addStructTask",
    hidden: true,
    children: [
      {
        path: "",
        component: () => import("@/views/ext/extStructTask/add/index.vue"),
        name: "addStructTaskIndex",
        meta: { title: "添加结构化抽取", activeMenu: "/ext/extStructTask" },
      },
    ],
  },
  {
    path: "/ext/editStructTask",
    component: Layout,
    redirect: "editStructTask",
    hidden: true,
    children: [
      {
        path: "",
        component: () => import("@/views/ext/extStructTask/add/index.vue"),
        name: "editStructTaskIndex",
        meta: { title: "修改结构化抽取", activeMenu: "/ext/extStructTask" },
      },
    ],
  },
];
