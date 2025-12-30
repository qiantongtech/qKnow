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
