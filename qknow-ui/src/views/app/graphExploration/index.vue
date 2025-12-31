<!--
  Copyright © 2026 Qiantong Technology Co., Ltd.
  qKnow Knowledge Platform
   *
  License:
  Released under the Apache License, Version 2.0.
  You may use, modify, and distribute this software for commercial purposes
  under the terms of the License.
   *
  Special Notice:
  All derivative versions are strictly prohibited from modifying or removing
  the default system logo and copyright information.
  For brand customization, please apply for brand customization authorization via official channels.
   *
  More information: https://qknow.qiantong.tech/business.html
   *
  ============================================================================
   *
  版权所有 © 2026 江苏千桐科技有限公司
  qKnow 知识平台（开源版）
   *
  许可协议：
  本项目基于 Apache License 2.0 开源协议发布，
  允许在遵守协议的前提下进行商用、修改和分发。
   *
  特别说明：
  所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
  如需定制品牌，请通过官方渠道申请品牌定制授权。
   *
  更多信息请访问：https://qknow.qiantong.tech/business.html
-->

<template>
  <div
    class="app-container"
    ref="app-container"
    v-loading="appLoading"
    style="height: calc(100vh - 30px)"
  >
    <div class="head-title">
      <div class="name">{{ taskInfo.name }}</div>
      <div class="btns">
        <el-button v-if="taskInfo.pageType != 0" icon="Back" @click="handleBack"
          >返回</el-button
        >
        <!-- 结构化/非结构化 -->
        <el-button
          :disabled="graphData.nodes.length == 0"
          v-if="taskInfo.pageType != 0 && !releaseStatus"
          icon="Upload"
          type="primary"
          @click="handleRelease"
        >
          发布
        </el-button>
        <el-button
          :disabled="graphData.nodes.length == 0"
          v-if="taskInfo.pageType != 0 && releaseStatus"
          icon="Upload"
          type="primary"
          @click="handleCancelRelease"
        >
          取消发布
        </el-button>
        <!-- 故障树 -->
        <el-button
          v-if="taskInfo.pageType == 3"
          type="primary"
          @click="handleAddEntity"
        >
          <i class="iconfont-mini icon-xinzeng mr5"></i>编辑实体
        </el-button>
        <el-button
          v-if="taskInfo.pageType == 3"
          type="primary"
          @click="handleAddRelationship"
        >
          <i class="iconfont-mini icon-xinzeng mr5"></i>编辑三元组
        </el-button>
      </div>
    </div>
    <el-container class="wrap-container">
      <div :class="['gragh-wrap', { isfull: isfull }]">
        <div class="control-tree">
          <el-tree
            node-key="id"
            :props="props"
            show-checkbox
            highlight-current
            @check="handleCheck"
            @node-click="handleNodeClick"
            ref="treeRef"
            :data="treeData"
            :default-checked-keys="checkedKeys"
            default-expand-all
            :filter-node-method="filterNode"
          >
            <template #default="{ node }">
              <span class="ellipsis" :title="`${node.label}`">{{
                node.label
              }}</span>
            </template>
          </el-tree>
        </div>
        <div class="toolbar" ref="toolbarRef">
          <template v-for="item in toolbar" :key="item.id">
            <el-tooltip
              class="box-item"
              effect="light"
              :content="item.tip"
              placement="bottom"
            >
              <div class="toolbar-item" @click="toolbarClick(item)">
                <img :src="getAssetsFile(item.icon)" alt="" />
              </div>
            </el-tooltip>
          </template>
          <div class="search" v-if="searchShow">
            <el-autocomplete
              v-model="searchVal"
              style="width: 240px"
              :fetch-suggestions="querySearchAsync"
              placeholder="请输入实体名称"
              @select="searchSelect"
              clearable
            >
              <template #suffix>
                <el-icon class="el-input__icon"><Search /></el-icon>
              </template>
            </el-autocomplete>
          </div>
        </div>
        <!--  ----TODO 图例 --------------  -->
        <div class="tool" @click="toolShow = !toolShow">
          <div class="tool-mask" v-if="toolShow">
            <div class="mask-item" v-for="item in toolData" :key="item.label">
              <div
                class="mask-icon"
                :style="{
                  background: item.icon,
                  border: '2px solid ' + 'rgba(0,0,0, 0.3)',
                }"
              ></div>
              <div class="mask-label ellipsis" :title="item.label">
                {{ item.label }}
              </div>
            </div>
          </div>
        </div>
        <div
          :class="['gragh-container', { detailShow: detailShow }]"
          id="gragh-container"
        ></div>
        <transition name="el-zoom-in-right">
          <div class="details-dialog" v-if="detailShow">
            <div class="details-title">
              <div class="title-label">
                <el-icon class="icon" @click="detailClose">
                  <Close />
                </el-icon>
                <span class="label ellipsis" :title="currentNodeData.name">{{
                  currentNodeData.name
                }}</span>
              </div>
              <div class="title-slot">
                <el-button
                  :disabled="releaseStatus"
                  size="small"
                  type="danger"
                  icon="Delete"
                  @click="handleDel"
                  >删除
                </el-button>
              </div>
            </div>
            <div class="details-con">
              <el-collapse v-model="collapseAct" @change="collapseChange">
                <el-collapse-item title="属性信息" name="1">
                  <template #title>
                    <div class="collapse-title">属性信息</div>
                  </template>
                  <div class="collapse-con">
                    <el-table
                      stripe
                      height="150px"
                      v-loading="attrLoading"
                      :data="attrData"
                    >
                      <el-table-column
                        label="属性名称"
                        align="center"
                        prop="name"
                        show-overflow-tooltip
                      />
                      <el-table-column
                        label="数据类型"
                        prop="dataType"
                        align="center"
                        show-overflow-tooltip
                      >
                        <template #default="scope">
                          <dict-tag
                            :options="ext_data_type"
                            :value="scope.row.dataType"
                          />
                        </template>
                      </el-table-column>
                      <el-table-column
                        label="属性值"
                        prop="dataValue"
                        align="center"
                        show-overflow-tooltip
                      >
                        <template #default="scope">
                          {{ scope.row.dataValue ? scope.row.dataValue : "-" }}
                        </template>
                      </el-table-column>
                      <el-table-column
                        label="操作"
                        class-name="small-padding fixed-width"
                        fixed="right"
                        align="center"
                        width="120"
                      >
                        <template #default="scope">
                          <el-button
                            :disabled="releaseStatus"
                            link
                            type="primary"
                            icon="Edit"
                            @click="attrUpdate(scope.row)"
                            >修改
                          </el-button>
                          <el-button
                            :disabled="releaseStatus"
                            link
                            type="danger"
                            icon="Delete"
                            @click="attrDelete(scope.row)"
                            >删除
                          </el-button>
                        </template>
                      </el-table-column>
                    </el-table>
                  </div>
                </el-collapse-item>
                <el-collapse-item title="关联三元组" name="2">
                  <template #title>
                    <div class="collapse-title">关联三元组</div>
                  </template>
                  <div class="collapse-con">
                    <el-table
                      stripe
                      height="200px"
                      v-loading="tripletLoading"
                      :data="tripletData"
                    >
                      <el-table-column
                        label="起点"
                        key="startName"
                        prop="startName"
                        show-overflow-tooltip
                      />
                      <el-table-column
                        label="关系"
                        key="name"
                        prop="name"
                        show-overflow-tooltip
                      />
                      <el-table-column
                        label="终点"
                        key="endName"
                        prop="endName"
                        show-overflow-tooltip
                      />
                      <el-table-column
                        label="操作"
                        class-name="small-padding fixed-width"
                        fixed="right"
                      >
                        <template #default="scope">
                          <el-button
                            :disabled="releaseStatus"
                            link
                            type="danger"
                            icon="Delete"
                            @click="tripletDelete(scope.row)"
                            >删除</el-button
                          >
                        </template>
                      </el-table-column>
                    </el-table>
                  </div>
                </el-collapse-item>
                <template v-if="currentNodeData.entityType == 1">
                  <el-collapse-item title="关联数据源" name="3">
                    <template #title>
                      <div class="collapse-title">关联数据源</div>
                    </template>
                    <div class="collapse-con1">
                      <div
                        style="
                          display: grid;
                          grid-template-columns: 1fr 1fr;
                          margin-bottom: 8px;
                        "
                      >
                        <div style="text-align: left">
                          数据库名称:
                          {{ dataSource.database.type }}
                        </div>
                        <div style="text-align: left">
                          数据库地址: {{ dataSource.database.host }}
                        </div>
                      </div>
                      <div
                        style="
                          display: grid;
                          grid-template-columns: 1fr 1fr;
                          margin-bottom: 8px;
                        "
                      >
                        <div style="text-align: left">
                          数据库名称:
                          {{ dataSource.database.databaseName }}
                        </div>
                        <div style="text-align: left">
                          表名称: {{ dataSource.database.tableName }}
                        </div>
                      </div>
                      <el-table
                        height="200px"
                        v-loading="dataSourceLoading"
                        :data="dataSource.tableData"
                      >
                        <el-table-column
                          label="表字段"
                          align="center"
                          prop="field"
                          show-overflow-tooltip
                        />
                        <el-table-column
                          label="数据"
                          align="center"
                          prop="value"
                        >
                          <template #default="scope">
                            {{ scope.row.value ? scope.row.value : "-" }}
                          </template>
                        </el-table-column>
                      </el-table>
                    </div>
                  </el-collapse-item>
                </template>
                <template v-if="currentNodeData.entityType == 2">
                  <el-collapse-item title="关联分段" name="3">
                    <template #title>
                      <div class="collapse-title">关联分段</div>
                    </template>
                    <div class="collapse-con1">
                      <div v-for="(item, index) in textList" :key="index">
                        <!--                        <span v-if="currentNodeData.textIds && currentNodeData.textIds.split(',').includes(item.id.toString())">{{ item.text }}</span>-->
                        <span
                          v-if="
                            currentNodeData.paragraphIndex &&
                            currentNodeData.paragraphIndex ===
                              item.paragraphIndex
                          "
                          >{{ item.text }}</span
                        >
                      </div>
                    </div>
                  </el-collapse-item>
                  <el-collapse-item title="关联文件" name="4">
                    <template #title>
                      <div class="collapse-title">关联文件</div>
                    </template>
                    <div class="collapse-con1">
                      <div v-for="(doc, index) in docList" :key="index">
                        <div
                          v-if="currentNodeData.docId == doc.id"
                          class="docItem"
                        >
                          <td
                            class="docTd"
                            :title="doc.name"
                            @click="previewRefactoring(doc)"
                          >
                            {{ doc.name }}
                          </td>
                          <td style="text-align: right">
                            {{ parseTime(doc.createTime, "{y}-{m}-{d}") }}
                          </td>
                        </div>
                      </div>
                    </div>
                  </el-collapse-item>
                </template>
              </el-collapse>
            </div>
          </div>
        </transition>
        <!-- 属性信息修改 -->
        <el-dialog
          class="attr-dialog"
          title="属性信息"
          v-model="attrVisible"
          width="500px"
          :append-to="$refs['app-container']"
          draggable
          destroy-on-close
        >
          <el-form ref="attrRef" :model="attrForm" label-width="80px">
            <el-row :gutter="20">
              <el-col :span="24">
                <el-form-item
                  label="属性值"
                  prop="name"
                  :rules="{ required: true }"
                >
                  <el-input
                    v-model="attrForm.dataValue"
                    placeholder="请输入属性值"
                  />
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
          <template #footer>
            <div class="dialog-footer">
              <el-button @click="attrFormCancel">取 消</el-button>
              <el-button type="primary" @click="attrFormSubmit"
                >确 定</el-button
              >
            </div>
          </template>
        </el-dialog>
        <!--  弹框  -->
        <AddEntity
          ref="nodeRef"
          :title="addTitle"
          :graphId="taskInfo"
          @handle-refresh="handleRefresh"
        ></AddEntity>
        <AddRelationship
          ref="edgeRef"
          :title="addTitle"
          :graphId="taskInfo"
          @handle-refresh="handleRefresh"
        ></AddRelationship>
      </div>
    </el-container>
    <!-- 预览文件弹窗 -->
    <el-dialog class="fileDialog" v-model="dialogVisible" :title="'预览'">
      <div class="filecont" style="height: 100%">
        <vue-office-pdf
          v-if="fileType === 'pdf'"
          :src="fileUrl"
          :style="{ height: '100%' }"
        />
        <vue-office-excel
          v-if="fileType === 'xls'"
          :src="fileUrl"
          :style="{ height: '100%' }"
        />
        <vue-office-docx
          v-if="fileType === 'docx'"
          :src="fileUrl"
          :style="{ height: '100%' }"
        />
      </div>
    </el-dialog>
  </div>
</template>

<script setup name="KEresult">
//引入VueOfficeDocx组件
import VueOfficeDocx from "@vue-office/docx";
//引入相关样式
import "@vue-office/docx/lib/index.css";
//引入VueOfficeExcel组件
import VueOfficeExcel from "@vue-office/excel";
//引入相关样式
import "@vue-office/excel/lib/index.css";
//引入VueOfficePdf组件
import VueOfficePdf from "@vue-office/pdf";
import AddEntity from "./addEntity.vue";
import AddRelationship from "./addRelationship.vue";
// 初始化画布
import Vis from "vis-network/dist/vis-network.min.js";
import "vis-network/dist/dist/vis-network.min.css";
// 知识平台
import { useFullscreen } from "@vueuse/core";
// 非结构化接口
import {
  getTaskTextList,
  updateUnStructTaskPublishStatus,
} from "@/api/ext/extUnstructTask/unstructTask";
// 结构化接口
import {
  getAttributeInformation,
  updateNodeAttribute,
  updateStructTaskPublishStatus,
} from "@/api/ext/extStructTask/extStruct";
import { getTableDataByDataId } from "@/api/ext/extDatasource/datasource";
// 综合接口
import {
  getGraph,
  updateReleaseStatus,
  deleteNodeAttributeById,
  deleteRelationshipById,
  deleteNode,
} from "@/api/app/graph";
// 概念列表
import { listSchema } from "@/api/ext/extSchema/schema";

const getAssetsFile = (url) => {
  return new URL(`/src/assets/ke/images/${url}`, import.meta.url).href;
};

// 界面路由入参
const { proxy } = getCurrentInstance();
const { ext_data_type } = proxy.useDict("ext_data_type");
const router = useRouter();
// 1结构化 2非结构化 3故障 0探索,
const taskInfo = ref({ id: "", name: "图谱探索", pageType: "0" });
let selectedIds = ref([]);
watch(
  () => router.currentRoute.value.query.pageType,
  (val) => {
    if (val) {
      taskInfo.value = router.currentRoute.value.query;
      taskInfo.value.id = taskInfo.value.id - 0;
    }
  },
  { immediate: true }
);
// #region 点击事件
// 返回
const handleBack = () => {
  let path;
  switch (taskInfo.value.pageType) {
    case "1":
      path = "/ext/extStructTask";
      break;
    case "2":
      path = "/ext/unstructTask";
      break;
    case "3":
      path = "/flt/graph";
      break;
  }
  router.push({
    path: path,
    query: {
      pageNum: taskInfo.value.pageNum,
      pageSize: taskInfo.value.pageSize,
    },
  });
};
// 发布
const releaseStatus = ref(false);
const handleRelease = () => {
  let params = {
    id: taskInfo.value.id,
    entityId: taskInfo.value.id,
    entityType: taskInfo.value.pageType,
    releaseStatus: 1,
    publishStatus: 1,
  };
  proxy.$modal.confirm(`是否确认发布？`).then(() => {
    updateReleaseStatus(params).then((res) => {
      if (res && res.code == 200) {
        releaseStatus.value = true;
        proxy.$modal.msgSuccess("发布成功");
      }
    });
    if (taskInfo.value.pageType == "1") {
      updateStructTaskPublishStatus(params).then(() => {});
    }
    if (taskInfo.value.pageType == "2") {
      updateUnStructTaskPublishStatus(params).then(() => {});
    }
  });
};
//取消发布
const handleCancelRelease = () => {
  let params = {
    id: taskInfo.value.id,
    entityId: taskInfo.value.id,
    entityType: taskInfo.value.pageType,
    releaseStatus: 0,
    publishStatus: 0,
  };
  proxy.$modal.confirm(`是否确认取消发布？`).then(() => {
    updateReleaseStatus(params).then((res) => {
      if (res && res.code == 200) {
        releaseStatus.value = false;
        proxy.$modal.msgSuccess("取消发布成功");
      }
    });
    if (taskInfo.value.pageType == "1") {
      updateStructTaskPublishStatus(params).then(() => {});
    }
    if (taskInfo.value.pageType == "2") {
      updateUnStructTaskPublishStatus(params).then(() => {});
    }
  });
};
// 编辑实体
const nodeRef = ref();
const edgeRef = ref();
const addTitle = ref("");
const handleAddEntity = () => {
  nodeRef.value.openDialog(nodes.value);
  addTitle.value = "编辑实体";
};
// 编辑三元组
const handleAddRelationship = () => {
  edgeRef.value.openDialog(edges.value);
  addTitle.value = "编辑三元组";
};
const handleRefresh = () => {
  toolbarClick({
    id: "reset",
  });
};
// #endregion

// #region 初始化抽取
const appLoading = ref(false);
let graph = null;
let graphData = ref({
  nodes: [],
  edges: [],
});
const nodes = ref([]);
const edges = ref([]);
const schemaList = ref({ data: { rows: [] } });
//查询抽取结果
function getGraphData(params) {
  appLoading.value = true;
  getGraph(params).then((response) => {
    appLoading.value = false;
    let data = response.data;
    // step：普通：0，特殊颜色：1，选中：2，相邻节点：-1
    nodes.value = data.entities.map((item) => {
      let row = schemaList.value.data.rows.find((i) => item.schemaId == i.id);
      return {
        ...item,
        id: item.id + "",
        label:
          item.name.length > 6 ? item.name.substring(0, 6) + "..." : item.name,
        rowBackground: row ? row.color : "#315790",
        rowBorder: row ? `${row.color}4d` : "#7dbffa4d",
        color: {
          background: row ? row.color : "#315790",
          border: row ? `${row.color}4d` : "#7dbffa4d",
        },
        borderWidth: row ? 15 : 0,
        step: row ? 1 : 0,
      };
    });
    edges.value = [];
    data.relationships.map((item) => {
      if (
        data.entities.some((node) => node.id == item.startId) &&
        data.entities.some((node) => node.id == item.endId)
      ) {
        edges.value.push({
          ...item,
          id: item.id + "",
          name: item.relationType,
          label: item.relationType, //关系
          from: item.startId + "", //头部实体
          to: item.endId + "", //尾部实体
        });
      }
    });
    graphData.value.nodes = new Vis.DataSet(nodes.value);
    graphData.value.edges = new Vis.DataSet(edges.value);
    // 搜索值
    searchData.value = nodes.value.map((item) => {
      return { ...item, value: item.name, name: item.name };
    });
    console.log("-----数据----", graphData.value);
    // 设置发布状态：已发布不能修改和不能删除，除了图谱探索页
    if (taskInfo.value.pageType == "0") {
      releaseStatus.value = false;
    } else {
      releaseStatus.value =
        nodes.value.length && nodes.value[0].releaseStatus === 1 ? true : false;
    }
    //设置画布
    setGraph(graphData.value);
    if (data.length != 0) {
      // 初始化树结构
      initTree();
    }
  });
}

// 初始化画布
const utils = {
  // 重置颜色
  resetColor: () => {
    nodes.value.forEach((item) => {
      graphData.value.nodes.update([
        {
          id: item.id,
          step: 0,
          borderWidth: item.step == 0 ? 0 : 15,
          size: 10,
          color: {
            background: item.rowBackground,
            border: item.rowBorder,
          },
        },
      ]);
    });
  },
  // 定位
  zoomBy: (data) => {
    graph.moveTo({
      position: graph.getPosition(data.id),
      scale: 1.3,
      animation: {
        duration: 300,
        easingFunction: "linear",
      },
    });
  },
};
function setGraph(data) {
  const container = document.getElementById("gragh-container");
  const options = {
    autoResize: true,
    interaction: {
      hover: true,
    },
    clickToUse: false,
    layout: {
      improvedLayout: true,
    },
    nodes: {
      shape: "dot",
      size: 10,
      borderWidth: 0,
      color: {
        background: "#315790",
        border: "#314790",
        highlight: {
          background: "#89b8ff",
          border: "#89b8ff4d",
        },
        hover: {
          background: "#89b8ff",
          border: "#89b8ff4d",
        },
      },
      font: {
        size: 14,
        color: "#d7e7ff",
      },
      shapeProperties: {
        useBorderWithImage: true,
      },
      label: "",
    },
    edges: {
      length: 150,
      arrows: {
        to: {
          enabled: true,
          scaleFactor: 1,
        },
      },
      arrowStrikethrough: false,
      color: {
        color: "#89b8ff",
        highlight: "#89b8ff",
        hover: "#89b8ff",
      },
      hoverWidth: 2,
      smooth: {
        enabled: true,
        type: "dynamic",
        forceDirection: "none",
      },
      selectionWidth: 4,
      font: {
        size: 14,
        color: "#89b8ff",
        strokeWidth: 0,
        align: "top",
      },
    },
    physics: {
      enabled: true,
      stabilization: edges.value.length > 100 ? true : false,
      wind: { x: 0, y: 0 },
      barnesHut: {
        gravitationalConstant: -2000,
        springConstant: 0.1,
        damping: 0.08,
      },
    },
  };
  graph = new Vis.Network(container, data, options);
  graph.moveTo({ scale: 1.1 });
  console.log(graph, "===graph");
  graph.on("hoverNode", function (t) {
    let node = graphData.value.nodes.get(t.node);
    node.borderWidth = 15;
    node.size = 20;
    // 选中后的相邻节点hover颜色
    node.color.hover = {
      background: node.step != -1 ? node.rowBackground : "#89b8ff",
      border: node.step != -1 ? node.rowBorder : "#89b8ff4d",
    };
    graphData.value.nodes.update(node);
  });
  graph.on("blurNode", function (t) {
    let node = graphData.value.nodes.get(t.node);
    // 普通节点无border
    node.borderWidth = node.step == 0 ? 0 : 15;
    // 选中节点不变
    node.size = node.step < 2 ? 10 : 20;
    graphData.value.nodes.update(node);
  });
  graph.on("click", function (d) {
    console.log(d, data, graphData.value.nodes, "===dd");
    utils.resetColor();
    if (d.nodes.length == 0) {
      detailShow.value = false;
      currentNodeData.value = null;
      // 恢复视角
      setTimeout(() => {
        graph.moveTo({ scale: 1.1 });
      }, 300);
    } else {
      currentNodeData.value = nodes.value.find((e) => e.id == d.nodes[0]);
      let node = graphData.value.nodes.get(currentNodeData.value);
      graphData.value.nodes.add({
        id: "-node",
        color: "rgba(255, 255, 255, 0)",
      });
      graphData.value.edges.add({
        id: "-edge",
        from: currentNodeData.value.id,
        to: "-node",
        color: "rgba(255, 255, 255, 0)",
      });
      setTimeout(() => {
        graphData.value.nodes.remove({ id: "-node" });
        graphData.value.edges.remove({ id: "-edge" });
      }, 500);
      // 定位至中心
      setTimeout(() => {
        utils.zoomBy(currentNodeData.value);
      }, 300);
      /** 高亮临节点-暂时不要了
      // d.edges.forEach((e) => {
      //   let edge = edges.value.find((f) => f.id == e);
      //   console.log(edge, "===edge");
      //   if (edge) {
      //     let conNodes = nodes.value.filter((f) => f.id == edge.from || f.id == edge.to);
      //     if (conNodes.length > 0) {
      //       conNodes.forEach((item) => {
      //         graphData.value.nodes.update([
      //           {
      //             id: item.id,
      //             borderWidth: 15,
      //             step: -1,
      //           },
      //         ]);
      //       });
      //     }
      //   }
      // }); */
      graphData.value.nodes.update([
        {
          id: d.nodes[0],
          size: 20,
          borderWidth: 15,
          step: 2,
          color: {
            highlight: {
              background: currentNodeData.value.rowBackground,
              border: currentNodeData.value.rowBorder,
            },
          },
        },
      ]);
      console.log(
        "---------currentNodeData.value-------",
        currentNodeData.value
      );
      //属性信息
      getAttrData(currentNodeData.value);
      //关联三元组
      getTripletData(d.nodes[0]);
      detailShow.value = true;
      if (currentNodeData.value.entityType == 1) {
        if (currentNodeData.value.databaseId) {
          getDataSorceData({
            id: currentNodeData.value.databaseId,
            primaryKey: currentNodeData.value.primaryKey,
            primaryKeyData: currentNodeData.value.primaryKeyData,
            tableName: currentNodeData.value.tableName,
          });
        }
      }
    }
  });
}

// tool图例
const toolShow = ref(false);
const toolData = ref([
  {
    label: "其他",
    icon: "#7DBFFA",
  },
]);
// #region 右侧弹框数据
// 折叠面板
const currentNodeData = ref();
const detailShow = ref(false);
const detailClose = () => {
  detailShow.value = false;
  currentNodeData.value = null;
  utils.resetColor();
  graph.unselectAll();
  // 恢复视角
  setTimeout(() => {
    graph.moveTo({ scale: 1.1 });
  }, 300);
};
// #endregion

onMounted(async () => {
  // 获取schema&tool图例
  schemaList.value = await listSchema({ pageNum: 1, pageSize: 10000 });
  schemaList.value.data.rows.map((item) => {
    toolData.value.push({
      label: item.name,
      icon: item.color,
    });
  });
  //查询抽取结果
  let params = {
    entityType: taskInfo.value.pageType,
    entityId: taskInfo.value.id,
  };
  getGraphData(params);
  //根据taskId获取段落数据和文档
  if (taskInfo.value.pageType == "2" || taskInfo.value.pageType == "0") {
    getTextListAndDocList(params);
  }
});

onBeforeUnmount(() => {
  console.log("组件将卸载，页面可能不再活动");
  console.log(selectedIds.value);
});

const searchShow = ref(false);
const searchVal = ref("");
const searchData = ref([]);
const querySearchAsync = (queryString, cb) => {
  const results = queryString
    ? searchData.value.filter(createFilter(queryString))
    : searchData.value;
  cb(results);
};
const createFilter = (queryString) => {
  return (restaurant) => {
    return (
      restaurant.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0
    );
  };
};
const searchSelect = (item) => {
  filterEmit(item);
};
const isfull = ref(false);
const toolbarRef = ref();
const toolbar = ref([
  {
    id: "zoom-out",
    icon: "toolbar (1).png",
    tip: "缩小",
  },
  {
    id: "zoom-in",
    icon: "toolbar (9).png",
    tip: "放大",
  },
  {
    id: "full-screen",
    icon: "toolbar (8).png",
    isFull: false,
    tip: "全屏",
  },
  {
    id: "offset-left",
    icon: "toolbar (7).png",
    tip: "左移",
  },
  {
    id: "offset-right",
    icon: "toolbar (6).png",
    tip: "右移",
  },
  //   {
  //     id: "undo",
  //     icon: "RefreshLeft",
  //     tip: "撤销",
  //   },
  //   {
  //     id: "redo",
  //     icon: "RefreshRight",
  //     tip: "重做",
  //   },
  {
    id: "auto-fit",
    icon: "toolbar (2).png",
    tip: "恢复视角",
  },
  {
    id: "export",
    icon: "toolbar (3).png",
    tip: "导出",
  },
  {
    id: "reset",
    icon: "toolbar (4).png",
    tip: "重置",
  },
  {
    id: "search",
    icon: "toolbar (5).png",
    tip: "搜索",
  },
]);
const { toggle } = useFullscreen();
const toolbarClick = async (item) => {
  const animation = {
    duration: 500,
    easingFunction: "linear",
  };
  switch (item.id) {
    case "full-screen": {
      toggle();
      isfull.value = !isfull.value;
      break;
    }
    case "zoom-in": {
      let zoom = graph.getScale();
      graph.moveTo({
        scale: zoom + 0.2,
        animation,
      });
      break;
    }
    case "zoom-out": {
      let zoom = graph.getScale();
      graph.moveTo({
        scale: zoom - 0.2,
        animation,
      });
      break;
    }
    case "offset-left": {
      let position = graph.getViewPosition();
      graph.moveTo({
        position: { x: position.x - 300, y: position.y },
        animation,
      });
      break;
    }
    case "offset-right": {
      let position = graph.getViewPosition();
      graph.moveTo({
        position: { x: position.x + 300, y: position.y },
        animation,
      });
      break;
    }
    case "redo":
      if (history.canRedo()) history.redo();
      break;
    case "undo":
      if (history.canUndo()) history.undo();
      break;
    case "auto-fit":
      graph.stabilize();
      break;
    case "export": {
      // graph.stabilize();
      // setTimeout(() => {
      let dom = document.querySelector("#gragh-container .vis-network canvas");
      let img = dom.toDataURL("image/png");
      let link = document.createElement("a");
      // let url = URL.createObjectURL(blob); // 创建 Blob URL
      link.href = img;
      link.setAttribute("download", taskInfo.value.name + "抽取结果"); // 设置文件名
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
      URL.revokeObjectURL(url); // 释放 Blob URL
      // }, 1000);
      break;
    }
    case "reset": {
      if (graph !== null) {
        graph.destroy();
        graph = null;
      }
      // 关闭弹框
      detailShow.value = false;
      currentNodeData.value = null;
      let params = {
        entityType: taskInfo.value.pageType,
        entityId: taskInfo.value.id,
      };
      getGraphData(params);
      //根据taskId获取段落数据和文档
      if (taskInfo.value.pageType == "2" || taskInfo.value.pageType == "0") {
        getTextListAndDocList(params);
      }
      break;
    }
    case "search": {
      searchShow.value = !searchShow.value;
      break;
    }
  }
};

// 过滤树
const filterText = ref("");
const treeRef = ref();
watch(filterText, (val) => {
  treeRef.value && treeRef.value.filter(val);
});

// 树结构
const props = {
  label: "label",
  children: "children",
};
let treeData = ref([]);
let checkedKeys = ref([]);
const initTree = () => {
  treeData.value = [
    {
      id: "parent1",
      label: "实体",
      children: nodes.value.map((item) => {
        return {
          id: item.id,
          label: item.name,
        };
      }),
    },
  ];
  const getAllIds = (treeData) => {
    let ids = [];
    for (const node of treeData) {
      ids.push(node.id); // 将当前节点的id加入数组
      if (node.children && node.children.length > 0) {
        ids = ids.concat(getAllIds(node.children)); // 递归处理子节点
      }
    }
    return ids;
  };
  checkedKeys.value = getAllIds(treeData.value);
  // console.log(checkedKeys.value);
};

const handleCheck = (data, checked) => {
  const id = data.id;
  // console.log(data, checked.checkedKeys);
  if (id !== "parent1") {
    if (checked.checkedKeys.includes(id)) {
      graphData.value.nodes.update([{ id: id, hidden: false }]);
    } else {
      graphData.value.nodes.update([{ id: id, hidden: true }]);
    }
  } else {
    if (checked.checkedKeys.length !== 0) {
      nodes.value.forEach((e) =>
        graphData.value.nodes.update([{ id: e.id, hidden: false }])
      );
    } else {
      nodes.value.forEach((e) =>
        graphData.value.nodes.update([{ id: e.id, hidden: true }])
      );
    }
  }
};
const handleNodeClick = (data) => {
  console.log(data.id);
  filterEmit(data);
};
const oldId = ref(null);
const filterEmit = (data) => {
  if (/^\d+$/.test(data.id)) {
    selectedIds.value.push(Number(data.id));
  }
  if (oldId.value) {
    utils.resetColor();
  }
  oldId.value = data.id;
  if (data.id == "parent1" || data.id == "parent2") return;
  // 修改颜色及定位
  graphData.value.nodes.update([
    {
      id: data.id,
      color: {
        background: "#0006ff",
        border: "rgba(0, 6, 255, 0.3)",
      },
    },
  ]);
  utils.zoomBy(data);
  // 弹框已展示，调整内容
  if (detailShow.value) {
    currentNodeData.value = nodes.value.find((e) => e.id == data.id);
    //获取关联的属性信息
    getAttrData(currentNodeData.value);
    //关联三元组
    getTripletData(data.id);
    if (currentNodeData.value.entityType == 1) {
      getDataSorceData({
        id: currentNodeData.value.databaseId,
        dataId: currentNodeData.value.dataId,
        tableName: currentNodeData.value.tableName,
      });
    }
  }
};
const filterNode = (value, data) => {
  if (!value) return true;
  return data.label.includes(value);
};

const handleDel = () => {
  const id = currentNodeData.value.id;
  proxy.$modal
    .confirm(`是否确认删除实体为【${currentNodeData.value.name}】的数据项？`)
    .then(() => {
      // 隐藏弹框，过滤树，暂隐node节点
      detailClose();
      treeData.value[0].children = treeData.value[0].children.filter(
        (e) => e.id !== id
      );
      graphData.value.nodes.update([{ id: id, hidden: true }]);
      // 过滤数据
      nodes.value = nodes.value.filter((e) => e.id !== id);
      edges.value = edges.value.filter((e) => e.from !== id && e.to !== id);
      // 接口
      deleteNode(id).then((response) => {
        console.log("-------删除成功------", response);
        proxy.$modal.msgSuccess("删除成功");
      });
    })
    .catch((err) => {
      console.log(err);
    });
};
const collapseAct = ref(["1", "2", "3", "4"]);
const collapseChange = (val) => {
  console.log(val);
};
// 属性信息
const getAttrData = (node) => {
  attrLoading.value = true;
  attrData.value = [];
  let attributeIdList = [];
  let attributeList = [];
  for (let key in node) {
    if (key.includes("attributeId")) {
      attributeIdList.push(key);
      attributeList.push({ key: key, value: node[key] });
    }
  }
  if (attributeIdList.length > 0) {
    let attributeIdString = attributeIdList.join(",");
    getAttributeInformation(attributeIdString).then((res) => {
      if (res && res.code == 200) {
        res.data.forEach((item) => {
          attributeList.forEach((att) => {
            if (att.key == "attributeId" + item.id) {
              item.dataValue = att.value;
            }
          });
        });
        attrData.value = res.data;
        attrLoading.value = false;
      }
    });
  } else {
    attrLoading.value = false;
  }
};
const attrLoading = ref(false);
const attrData = ref([]);
const attrVisible = ref(false);
const attrForm = ref({});
const attrUpdate = (row) => {
  attrVisible.value = true;
  attrForm.value = row;
};
const attrDelete = (row) => {
  proxy.$modal
    .confirm(`是否确认删除属性名称为【${row.name}】的数据项？`)
    .then(() => {
      let node = currentNodeData.value;
      deleteNodeAttributeById({
        nodeId: node.id,
        attributeKey: "attribute_id_" + row.id,
      }).then((res) => {
        if (res && res.code == 200) {
          attrData.value = attrData.value.filter((e) => e.id != row.id);
          proxy.$modal.msgSuccess("删除成功");
        }
      });
    });
};
const attrFormCancel = () => {
  attrForm.value = {};
  attrVisible.value = false;
  proxy.resetForm("attrRef");
};
const attrFormSubmit = () => {
  let node = currentNodeData.value;
  updateNodeAttribute({
    taskId: node.taskId,
    tableName: node.tableName,
    dataId: node.dataId,
    extractType: node.extractType,
    attributeKey: "attribute_id_" + attrForm.value.id,
    attributeValue: attrForm.value.dataValue,
  }).then((res) => {
    if (res && res.code == 200) {
      proxy.$modal.msgSuccess("修改成功");
    }
  });
  attrForm.value = {};
  attrVisible.value = false;
  proxy.resetForm("attrRef");
};
// 关联三元组
const getTripletData = (id) => {
  tripletData.value = edges.value.filter(
    (e) => e.startId == id || e.endId == id
  );
};
const tripletLoading = ref(false);
const tripletData = ref([]);
// const tripletUpdate = () => {};
const tripletDelete = (row) => {
  const id = row.id.replace(/edge/g, "");
  proxy.$modal
    .confirm(`是否确认删除当前三元组？`)
    .then(() => {
      // 暂隐edge节点
      graphData.value.edges.remove({ id: row.id });
      // 过滤数据
      edges.value = edges.value.filter((e) => e.id !== row.id);
      getTripletData(currentNodeData.value.id);
      // 接口
      deleteRelationshipById(id).then((response) => {
        console.log("-------删除成功------", response);
        proxy.$modal.msgSuccess("删除成功");
      });
    })
    .catch(() => {});
};

//关联数据源
const dataSource = ref({
  database: {},
  dataTable: [],
});
const dataSourceLoading = ref(false);
const getDataSorceData = (val) => {
  dataSourceLoading.value = true;
  dataSource.value = {
    database: {},
    dataTable: [],
  };
  getTableDataByDataId(val).then((res) => {
    if (res && res.code == 200) {
      dataSource.value = res.data;
      dataSourceLoading.value = false;
    }
  });
};

//关联分段和关联文件
const textList = ref(null);
const docList = ref(null);
function getTextListAndDocList(val) {
  textList.value = null;
  docList.value = null;
  getTaskTextList({
    taskId: val.entityId,
  }).then((response) => {
    if (response && response.code === 200) {
      //文本段落数据
      textList.value = response.data.textListByTaskId;
      //文档数据
      docList.value = response.data.docListByTaskId;
    }
  });
}
//关联文件-预览
const env = import.meta.env.VITE_APP_ENV;
const dialogVisible = ref(false);
const fileType = ref("");
const fileUrl = ref("");
function previewRefactoring(row) {
  console.log(row);

  const fileName = row.name;
  let fileExt = fileName.split(".").pop().toLowerCase();
  fileExt = fileExt.trim();
  const allowedExtensions = ["pdf", "docx", "xls", "xlsx", "csv"];
  let path = "";
  if (env === "production") {
    path = "http://10.32.80.211:8090/prod-api/profile" + row.path;
  } else {
    path = row.path;
    // path = 'http://localhost:9035/profile' + row.path
  }
  if (!allowedExtensions.includes(fileExt)) {
    const supportedFormats = allowedExtensions.join("、");
    proxy.$modal.msgWarning(`当前仅支持以下格式文件预览：${supportedFormats}`);
    return;
  }
  if (["pdf"].includes(fileExt)) {
    fileType.value = "pdf";
  } else if (["docx"].includes(fileExt)) {
    fileType.value = "docx";
  } else if (["xls", "xlsx", "csv"].includes(fileExt)) {
    fileType.value = "xls";
  }
  fileUrl.value = path;
  dialogVisible.value = true;
}
// #endregion
</script>

<style lang="scss">
.fileDialog {
  height: 90%;
  width: 85%;
  .el-dialog__body {
    height: calc(100% - 42px) !important;
  }
}
</style>
<style scoped lang="scss">
.el-zoom-in-right-enter-active,
.el-zoom-in-right-leave-active {
  opacity: 1;
  transform: translateX(0);
  transform-origin: center right;
  transition: var(--el-transition-md-fade);
}

.el-zoom-in-right-enter-from,
.el-zoom-in-right-leave-active {
  opacity: 0;
  transform: translateX(100%);
}

.app-container {
  height: calc(100% - 30px);
  display: flex;
  flex-direction: column;

  .head-title {
    height: 46px;
    background: #fff;
    padding: 0px 15px;
    display: flex;
    justify-content: space-between;
    align-items: center;

    .name {
      font-family: PingFang SC;
      font-size: 16px;
      color: var(--el-color-primary);
      display: flex;
      align-items: center;
      &::before {
        content: "";
        display: inline-block;
        background: var(--el-color-primary);
        width: 6px;
        height: 16px;
        border-radius: 2px;
        margin-right: 10px;
      }
    }

    .btns {
      float: right;

      .el-button {
        height: 28px;
      }
    }
  }
  .wrap-container {
    height: calc(100% - 46px);
  }
  .gragh-wrap {
    position: relative;
    width: 100%;
    height: 100%;
    overflow: hidden auto;
    &.isfull {
      position: fixed;
      width: 100vw;
      height: 100vh;
      left: 0;
      top: 0;
      z-index: 3000;
    }

    .control-tree {
      position: absolute;
      top: 10px;
      bottom: 10px;
      left: 10px;
      width: 250px;
      height: calc(100% - 20px);
      background: radial-gradient(
        100% 0% at 50% 50%,
        #1c3668 0%,
        rgba(0, 15, 39, 0.27) 100%
      );
      border: 1px solid var(--el-color-primary);
      border-radius: 4px;
      overflow: hidden auto;
      padding: 10px;
      z-index: 1;
      &::-webkit-scrollbar {
        width: 0;
      }
      :deep(.el-tree) {
        --el-tree-text-color: #fff;
        background: transparent;
        .el-tree-node__content:hover {
          background-color: var(--el-color-primary);
          .el-checkbox__input {
            &.is-checked {
              .el-checkbox__inner {
                background: url("@/assets/ke/images/check-fff.png") no-repeat;
                &::after {
                  border-color: transparent;
                }
              }
            }
            .el-checkbox__inner {
              background: url("@/assets/ke/images/check-no-fff.png") no-repeat;
            }
          }
        }
        .el-tree-node.is-checked {
          .el-checkbox__inner {
            background: url("@/assets/ke/images/check.png") no-repeat;
            &::after {
              border-color: transparent;
            }
          }
        }
        .el-tree-node.is-current {
          .el-checkbox__input {
            &.is-checked {
              .el-checkbox__inner {
                background: url("@/assets/ke/images/check-fff.png") no-repeat;
                &::after {
                  border-color: transparent;
                }
              }
            }
            .el-checkbox__inner {
              background: url("@/assets/ke/images/check-no-fff.png") no-repeat;
            }
          }
        }
        .el-tree-node:focus > .el-tree-node__content {
          background-color: var(--el-color-primary);
        }
        .el-checkbox__inner {
          background: url("@/assets/ke/images/check-no.png") no-repeat;
          border: none;
        }
        .el-tree-node__content > label.el-checkbox {
          margin-right: 10px;
        }
      }

      :deep(
          .el-tree--highlight-current
            .el-tree-node.is-current
            > .el-tree-node__content
        ) {
        background-color: var(--el-color-primary) !important;
      }
    }
    .toolbar {
      position: absolute;
      top: 10px;
      right: unset;
      bottom: unset;
      left: calc(250px + 10px + 15px);
      display: flex;
      align-items: center;
      justify-content: center;
      border-radius: 2px;
      box-shadow: 0px 0px 8px rgba(0, 0, 0, 0.4);
      // opacity: 0.65;
      z-index: 1;
      .toolbar-item {
        display: inline-block;
        width: 34px;
        height: 32px;
        cursor: pointer;
        box-sizing: content-box;
        margin-right: 5px;
        &:hover {
          background-color: rgba(255, 255, 255, 0.2);
        }
        img {
          width: 100%;
          height: 100%;
        }
      }
      .search {
        :deep(.el-input__wrapper) {
          background: transparent;
          .el-input__inner {
            color: #fff;
          }
        }
      }
    }
    .tool {
      cursor: pointer;
      position: absolute;
      left: calc(250px + 10px + 15px);
      bottom: 10px;
      width: 50px;
      height: 50px;
      background: url("@/assets/ke/images/tool.png") no-repeat;
      background-size: 100% 100%;
      z-index: 1;

      .tool-mask {
        width: 151px;
        max-height: 214px;
        background: #1c376a;
        border-radius: 4px 4px 4px 4px;
        border: 1px solid #448fff;
        position: absolute;
        bottom: calc(100% + 8px);
        left: 0;
        box-sizing: border-box;
        padding: 20px;
        overflow: hidden auto;

        &::-webkit-scrollbar {
          width: 0px;
        }

        .mask-item {
          display: flex;
          align-items: center;
          margin-bottom: 12px;

          .mask-icon {
            width: 18px;
            height: 18px;
            border-radius: 50%;
            margin-right: 8px;
          }

          .mask-label {
            max-width: 100px;
            line-height: 1;
            font-family: PingFangSCs;
            font-size: 13px;
            color: #d7e3fa;
          }
        }
      }
    }

    .gragh-container {
      width: 100%;
      height: 100%;
      background: #fff url("@/assets/ke/images/bg1.png") no-repeat;
      background-size: 100% 100%;
      transition: width 0.3s;
      &.detailShow {
        width: calc(100% - 400px);
      }
      :deep(.g6-toolbar) {
        .g6-toolbar-item {
          fill: #7dbffa;
          &:hover {
            background-color: rgba(255, 255, 255, 0.2);
          }
        }
      }
    }

    .details-dialog {
      position: absolute;
      top: 0;
      right: 0;
      width: 400px;
      height: 100%;
      background: #fff;
      border: 1px solid rgba(0, 0, 0, 0.1);

      .details-title {
        height: 42px;
        padding: 0 12px;
        display: flex;
        justify-content: space-between;
        align-items: center;
        border-bottom: 1px solid #e8e8e8;

        .title-label {
          display: flex;
          align-items: center;

          .icon {
            cursor: pointer;
          }

          .label {
            width: 270px;
            margin-left: 12px;
            color: var(--el-color-primary);
          }
        }

        //.title-slot {}
      }

      .details-con {
        height: calc(100% - 42px);
        overflow: hidden auto;
        padding: 10px 10px;
        --el-border-color-lighter: #ddd;

        &::-webkit-scrollbar {
          width: 2px;
        }

        :deep(.el-collapse) {
          border: none;

          .el-collapse-item__wrap {
            border: none;
          }

          .el-collapse-item {
            margin-bottom: 10px;
          }
        }

        :deep(.el-collapse-item__header) {
          border: none;
          height: 30px;
        }

        :deep(.el-collapse-item__content) {
          padding-bottom: 0px;
        }

        .collapse-title {
          font-family: Pingfang Sc;
          font-size: 16px;
          font-weight: 600;
          // padding: 0 10px;
          display: flex;
          align-items: center;

          &::before {
            display: inline-block;
            content: "";
            width: 6px;
            height: 16px;
            border-radius: 2px 2px 2px 2px;
            background: var(--el-color-primary);
            margin-right: 10px;
          }
        }

        .collapse-con {
          padding: 5px 0;

          :deep(
              .el-descriptions__label.el-descriptions__cell.is-bordered-label
            ) {
            width: 100px;
          }
        }

        .collapse-con1 {
          padding: 5px 5px;

          :deep(
              .el-descriptions__label.el-descriptions__cell.is-bordered-label
            ) {
            width: 100px;
          }

          ::v-deep {
            border-color: #f5ecec !important;
            border: 0.0001rem solid;
          }
          .docItem {
            display: flex;
            justify-content: space-between;
            .docTd {
              text-align: left;
              width: 80%;
              white-space: nowrap;
              overflow: hidden;
              text-overflow: ellipsis;
              cursor: pointer;
            }
          }
        }
      }
    }
  }
  :deep(.attr-dialog) {
    &:not(.is-fullscreen) {
      margin-top: 30vh !important;
    }
    .el-dialog__body {
      height: 150px;
    }
  }
}
</style>
