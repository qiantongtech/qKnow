<template>
  <div class="app-container" ref="app-container" v-loading="appLoading">
    <el-container>
      <!-- 左侧可调整的部分 -->
      <!-- 左侧可调整的部分 -->
      <el-aside :style="{ width: `${leftWidth}px` }" class="left-pane">
        <div class="left-tree">
          <div class="head-container">
            <el-input
              :suffix-icon="Search"
              v-model="filterText"
              clearable
              style="width: 100%; margin-bottom: 20px"
              placeholder="输入查找名称"
            />
          </div>
          <div class="head-container control-tree">
            <el-tree
              node-key="id"
              :props="props"
              show-checkbox
              highlight-current
              @check="handleCheck"
              @node-click="handleNodeClick"
              ref="treeRef"
              class="filter-tree"
              :data="treeData"
              :default-checked-keys="checkedKeys"
              default-expand-all
              :filter-node-method="filterNode"
            />
          </div>
        </div>
      </el-aside>
      <!-- 拖拽条 -->
      <div class="resize-bar" @mousedown="startResize">
        <div class="resize-handle-sx">
          <span class="zjsx"></span>
        </div>
      </div>
      <!-- 右侧部分 -->
      <el-main>
        <div class="head-title">
          <div class="name">{{ taskInfo.name }}</div>
          <div class="btns">
            <el-button icon="Back" @click="handleBack">返回</el-button>
            <el-button
              v-if="!releaseStatus"
              icon="Upload"
              type="primary"
              @click="handleRelease"
              >发布
            </el-button>
            <el-button
              v-if="releaseStatus"
              icon="Upload"
              type="primary"
              @click="handleCancelRelease"
              >取消发布
            </el-button>
          </div>
        </div>
        <div class="gragh-wrap">
          <div class="gragh-container" id="gragh-container">
            <div class="toolbar" ref="toolbarRef">
              <template v-for="item in toolbar" :key="item.id">
                <el-tooltip
                  class="box-item"
                  effect="light"
                  :content="item.tip"
                  placement="right"
                >
                  <div
                    class="toolbar-item"
                    @click="toolbarClick(item)"
                    :title="item.tip"
                  >
                    <el-icon>
                      <component :is="item.icon"></component>
                    </el-icon>
                  </div>
                </el-tooltip>
              </template>
            </div>
          </div>
          <!--  ----TODO 图例 --------------  -->
          <!-- <div class="tool" @click="toolShow = !toolShow">
                                <div class="tool-mask" v-if="toolShow">
                                  <div class="mask-item" v-for="item in toolData" :key="item.label">
                                    <div class="mask-icon" :style="{ background: 'rgba(' + item.icon + ', 1)', border: '2px solid ' + 'rgba(0,0,0, 0.3)' }"></div>
                                    <div class="mask-label">{{ item.label }}</div>
                                  </div>
                                </div>
                              </div> -->
          <transition name="el-zoom-in-right">
            <div class="details-dialog" v-if="detailShow">
              <div class="details-title">
                <div class="title-label">
                  <el-icon class="icon" @click="detailClose">
                    <Close />
                  </el-icon>
                  <span class="label">{{ currentNodeData.name }}</span>
                </div>
                <div class="title-slot">
                  <el-button
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
                            {{
                              scope.row.dataValue ? scope.row.dataValue : "-"
                            }}
                          </template>
                        </el-table-column>
                        <el-table-column
                          label="操作"
                          class-name="small-padding fixed-width"
                          fixed="right"
                          align="center"
                          width="140"
                        >
                          <template #default="scope">
                            <el-button
                              link
                              type="primary"
                              icon="Edit"
                              @click="attrUpdate(scope.row)"
                              >修改
                            </el-button>
                            <el-divider direction="vertical" />
                            <el-button
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
                          align="center"
                          width="140"
                        >
                          <template #default="scope">
                            <el-button
                              link
                              type="primary"
                              icon="Edit"
                              @click="tripletUpdate(scope.row)"
                              >修改
                            </el-button>
                            <el-divider direction="vertical" />
                            <el-button
                              link
                              type="danger"
                              icon="Delete"
                              @click="tripletDelete(scope.row)"
                              >删除
                            </el-button>
                          </template>
                        </el-table-column>
                      </el-table>
                    </div>
                  </el-collapse-item>
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
                </el-collapse>
              </div>
            </div>
          </transition>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script setup name="KEresult">
// 初始化画布
import { Graph } from "@antv/g6";
import { Search } from "@element-plus/icons-vue";
import { deleteNode } from "@/api/ext/extUnstructTask/unstructTask";
import {
  getExtStructByTaskId,
  getAttributeInformation,
  strutReleaseByTaskId,
  strutCancelReleaseByTaskId,
  deleteNodeAttribute,
  updateNodeAttribute,
  updateRelationship,
  deleteRelationship,
} from "@/api/ext/extStructTask/extStruct";
import { getTableDataByDataId } from "@/api/ext/extDatasource/datasource";

const { proxy } = getCurrentInstance();
const { ext_data_source_type, ext_data_type } = proxy.useDict(
  "ext_data_source_type",
  "ext_data_type"
);
const router = useRouter();
const taskInfo = ref({ name: "" });
const releaseStatus = ref(null); //true已发布 false未发布
watch(
  () => router.currentRoute.value.query.id,
  (val) => {
    if (val) {
      taskInfo.value = router.currentRoute.value.query;
    }
  },
  { immediate: true }
);

let graph = null;
let graphData = ref({
  nodes: [],
  edges: [],
});
const appLoading = ref(false);
onMounted(() => {
  //查询抽取结果
  let id = taskInfo.value.id;
  getExtExtractionData({ taskId: id });
});

//查询抽取结果
function getExtExtractionData(val) {
  appLoading.value = true;
  getExtStructByTaskId(val.taskId).then((response) => {
    appLoading.value = false;
    let data = response.data;
    console.log("-----data----", data);
    releaseStatus.value = response.data.releaseStatus == 1 ? true : false;

    let entityList = data.entities.map((item) => {
      return {
        ...item,
        id: item.id + "",
        size: 40,
        halo: true,
        style: {
          fill: "#7dbffa",
        },
      };
    });
    let tripletList = data.relationships.map((item) => {
      return {
        ...item,
        id: "edge" + item.id,
        name: item.relationType, //关系
        source: item.startId + "", //头部实体
        target: item.endId + "", //尾部实体
      };
    });
    console.log("-----数据1----", entityList);
    console.log("-----数据2----", tripletList);
    graphData.value.nodes = entityList;
    graphData.value.edges = tripletList;
    console.log("-----数据----", graphData.value);
    //设置画布
    setGraph(graphData.value);
    if (data.length == 0) return;
    // 初始化树结构
    initTree();
  });
}

function setGraph(data) {
  // 画布逻辑
  graph = new Graph({
    container: "gragh-container",
    animation: data.nodes.length < 100,
    data: data,
    autoFit: "view",
    autoResize: true,
    node: {
      type: "circle",
      style: {
        halo: (d) => d.halo || false,
        labelWordWrap: true,
        labelMaxWidth: "300",
        labelFill: "#DAE6FD",
        labelPlacement: "bottom",
        labelText: (d) => d.name,
        labelFontSize: 16,
        size: (d) => d.size,
        ports: [],
      },
      state: {
        // 高亮
        highlight: {
          fill: "#0076ff",
          halo: true,
          lineWidth: 0,
        },
        click: {
          halo: true,
          haloLineWidth: 20,
          stroke: "#0036ff",
          lineWidth: 4,
        },
        dim: {
          fill: "#99ADD1",
        },
      },
      palette: {
        type: "group",
        field: "cluster",
      },
    },
    edge: {
      state: {
        // 高亮
        highlight: {
          stroke: "#0076ff",
        },
      },
      style: {
        lineWidth: 5,
        labelText: (d) => d.name,
        labelFill: "#7DBFFA",
        labelFontSize: 12,
        labelOffsetY: 16,
        endArrow: true,
      },
      palette: {
        type: "group",
        field: "cluster",
      },
    },
    layout: {
      type: "d3-force",
      link: {
        distance: (d) => {
          return 100;
        },
        strength: (d) => {
          return 0.2;
        },
      },
      manyBody: {
        strength: (d) => {
          return -180;
        },
      },
    },
    plugins: [
      {
        type: "fullscreen",
        key: "fullscreen",
      },
    ],
    behaviors: [
      "zoom-canvas",
      "drag-canvas",
      "drag-element-force",
      {
        type: "hover-activate",
        enable: (event) => event.targetType === "node",
        degree: 1, // 👈🏻 Activate relations.
        state: "highlight",
        inactiveState: "dim",
        onHover: (event) => {
          event.view.setCursor("pointer");
        },
        onHoverEnd: (event) => {
          event.view.setCursor("default");
        },
      },
      {
        type: "click-select",
        key: "clickControl",
        state: "click",
        onClick: (d) => {
          console.log(d, "===dd");
          if (d.target.nodeName === "document") {
            detailShow.value = false;
          }
          if (d.target.type == "node") {
            currentNodeData.value = graphData.value.nodes.find(
              (e) => e.id == d.target.id
            );
            console.log(
              "---------currentNodeData.value-------",
              currentNodeData.value
            );

            //获取关联的属性信息
            getAttrData(currentNodeData.value);

            //获取关联的数据源
            getDataSorceData({
              id: currentNodeData.value.database_id,
              dataId: currentNodeData.value.data_id,
              tableName: currentNodeData.value.table_name,
            });

            //关联三元组
            tripletData.value = graphData.value.edges.filter(
              (e) => e.startId == d.target.id || e.endId == d.target.id
            );

            detailShow.value = true;
          }
        },
      },
    ],
  });
  graph.render();
}

//获取关联的属性信息
const getAttrData = (node) => {
  attrLoading.value = true;
  attrData.value = [];
  let attributeIdList = [];
  let attributeList = [];
  for (let key in node) {
    if (node.hasOwnProperty(key)) {
      console.log(key, node[key]);
      if (key.includes("attributeId_")) {
        attributeIdList.push(key);
        attributeList.push({ key: key, value: node[key] });
      }
    }
  }
  if (attributeIdList.length > 0) {
    let attributeIdString = attributeIdList.join(",");
    console.log("------attributeIdString------", attributeIdString);
    getAttributeInformation(attributeIdString).then((res) => {
      if (res && res.code == 200) {
        res.data.forEach((item) => {
          attributeList.forEach((att) => {
            if (att.key == "attributeId_" + item.id) {
              item.dataValue = att.value;
            }
          });
        });
        console.log("------res.data----", res.data);
        attrData.value = res.data;
        attrLoading.value = false;
      }
    });
  } else {
    attrLoading.value = false;
  }
};

//发布
const handleRelease = () => {
  proxy.$modal.confirm(`是否确认发布？`).then(() => {
    strutReleaseByTaskId({ taskId: taskInfo.value.id }).then((res) => {
      if (res && res.code == 200) {
        releaseStatus.value = true;
        proxy.$modal.msgSuccess("发布成功");
      }
    });
  });
};

//取消发布
const handleCancelRelease = () => {
  proxy.$modal.confirm(`是否确认取消发布？`).then(() => {
    strutCancelReleaseByTaskId({ taskId: taskInfo.value.id }).then((res) => {
      if (res && res.code == 200) {
        releaseStatus.value = false;
        proxy.$modal.msgSuccess("取消发布成功");
      }
    });
  });
};

const dataSource = ref({
  database: {},
  dataTable: [],
});
const dataSourceLoading = ref(false);
//获取关联的数据源
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

const toolbarRef = ref();
const toolbar = ref([
  {
    id: "full-screen",
    icon: "FullScreen",
    isFull: false,
    tip: "全屏",
  },
  {
    id: "zoom-in",
    icon: "ZoomIn",
    tip: "放大",
  },
  {
    id: "zoom-out",
    icon: "ZoomOut",
    tip: "缩小",
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
    icon: "Aim",
    tip: "恢复视角",
  },
  {
    id: "export",
    icon: "Download",
    tip: "导出",
  },
  {
    id: "reset",
    icon: "Refresh",
    tip: "重置",
  },
]);
const toolbarClick = async (item) => {
  const animation = {
    duration: 500,
    easing: "linear",
  };
  const history = graph.getPluginInstance("history");
  switch (item.id) {
    case "full-screen": {
      const fullscreenPlugin = graph.getPluginInstance("fullscreen");
      if (!item.isFull) {
        fullscreenPlugin.request();
      } else {
        fullscreenPlugin.exit();
      }
      item.isFull = !item.isFull;
      break;
    }
    case "zoom-in":
      graph.zoomBy(1.2, animation);
      break;
    case "zoom-out":
      graph.zoomBy(0.8, animation);
      break;
    case "redo":
      if (history.canRedo()) history.redo();
      break;
    case "undo":
      if (history.canUndo()) history.undo();
      break;
    case "auto-fit":
      graph.fitView(
        {
          when: "always", // 始终进行适配
          direction: "both", // 在两个方向上适配
        },
        animation
      );
      break;
    case "export": {
      const dataURL = await graph.toDataURL();
      const [head, content] = dataURL.split(",");
      const contentType = head.match(/:(.*?);/)[1];

      const bstr = atob(content);
      let length = bstr.length;
      const u8arr = new Uint8Array(length);
      while (length--) {
        u8arr[length] = bstr.charCodeAt(length);
      }
      const blob = new Blob([u8arr], { type: contentType });
      let link = document.createElement("a");
      let url = URL.createObjectURL(blob); // 创建 Blob URL
      link.href = url;
      link.setAttribute("download", taskInfo.value.name + "抽取结果"); // 设置文件名
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
      URL.revokeObjectURL(url); // 释放 Blob URL
      break;
    }
    case "reset": {
      graph.destroy();
      graph = null;
      let id = taskInfo.value.id;
      getExtExtractionData({ taskId: id });
      //根据taskId获取段落数据和文档
      getTextListAndDocList({ taskId: id });
      break;
    }
  }
};

// 侧边栏拖拽
const leftWidth = ref(278); // 初始左侧宽度
const isResizing = ref(false); // 判断是否正在拖拽
let startX = 0; // 鼠标按下时的初始位置
const startResize = (event) => {
  isResizing.value = true;
  startX = event.clientX;
  // 使用 requestAnimationFrame 减少重绘频率
  document.addEventListener("mousemove", updateResize);
  document.addEventListener("mouseup", stopResize);
};
const updateResize = (event) => {
  if (isResizing.value) {
    const delta = event.clientX - startX; // 计算鼠标移动距离
    leftWidth.value += delta; // 修改左侧宽度
    startX = event.clientX; // 更新起始位置
    // 使用 requestAnimationFrame 来减少页面重绘频率
    requestAnimationFrame(() => {});
  }
};
const stopResize = () => {
  isResizing.value = false;
  document.removeEventListener("mousemove", updateResize);
  document.removeEventListener("mouseup", stopResize);
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
      children: graphData.value.nodes.map((item) => {
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
    // 隐藏数据
    let hideData = graphData.value.edges
      .filter((e) => e.source === id || e.target === id)
      .map((e) => e.id);
    hideData.push(id);
    // 显示数据
    let showData = graphData.value.edges
      .filter((e) => e.source === id || e.target === id)
      .filter(
        (e) =>
          checked.checkedKeys.includes(e.source) &&
          checked.checkedKeys.includes(e.target)
      )
      .map((e) => e.id);
    showData.push(id);
    if (checked.checkedKeys.includes(id)) {
      showData.forEach((e) => graph.showElement(e));
    } else {
      hideData.forEach((e) => graph.hideElement(e));
    }
  } else {
    let data = [...graphData.value.nodes, ...graphData.value.edges];
    if (checked.checkedKeys.length !== 0) {
      data.map((e) => e.id).forEach((e) => graph.showElement(e));
    } else {
      data.map((e) => e.id).forEach((e) => graph.hideElement(e));
    }
  }
};
const oldId = ref(null);
const handleNodeClick = (data) => {
  console.log(data.id);
  if (oldId.value) {
    graph.setElementState(oldId.value, "");
  }
  oldId.value = data.id;
  if (data.id == "parent1" || data.id == "parent2") return;
  graph.setElementState(data.id, "click").then(() =>
    graph.focusElement(data.id).then(() => {
      graph.zoomTo(2);
    })
  );
  // 弹框已展示，调整内容
  if (detailShow.value) {
    currentNodeData.value = graphData.value.nodes.find((e) => e.id == data.id);

    //获取关联的属性信息
    getAttrData(currentNodeData.value);

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

    //关联三元组
    tripletData.value = graphData.value.edges.filter(
      (e) => e.startId == data.id || e.endId == data.id
    );
  }
};
const filterNode = (value, data) => {
  if (!value) return true;
  return data.label.includes(value);
};

// 点击事件
const handleBack = () => {
  router.push({
    path: "/ext/extStructTask",
    query: {
      pageNum: taskInfo.value.pageNum,
      pageSize: taskInfo.value.pageSize,
    },
  });
};

/** tool图例
     const toolShow = ref(false);
     const toolData = ref([
     {
    label: "泵站",
    icon: "125, 191, 249",
  },
     {
    label: "设备",
    icon: "169, 114, 41",
  },
     {
    label: "故障现象",
    icon: "236, 90, 172",
  },
     {
    label: "故障原因",
    icon: "180, 140, 244",
  },
     {
    label: "处理措施",
    icon: "117, 221, 202",
  },
     {
    label: "测点告警",
    icon: "147, 156, 248",
  },
     ]);
     */

// 右侧弹框
const currentNodeData = ref();
const detailShow = ref(false);
const detailClose = () => {
  detailShow.value = false;
};

//删除节点
const handleDel = () => {
  if (releaseStatus.value) {
    proxy.$modal.msgWarning("已发布状态禁止删除和修改！");
  } else {
    proxy.$modal
      .confirm(`是否确认删除实体为【${currentNodeData.value.name}】的数据项？`)
      .then(() => {
        // 接口
        const id = currentNodeData.value.id;
        deleteNode(id).then((response) => {
          console.log("-------删除成功------", response);
          // 隐藏弹框，过滤树，暂隐node节点
          detailClose();
          treeData.value[0].children = treeData.value[0].children.filter(
            (e) => e.id !== id
          );
          let hideData = graphData.value.edges
            .filter((e) => e.source === id || e.target === id)
            .map((e) => e.id);
          hideData.push(id);
          hideData.forEach((e) => graph.hideElement(e, false));
          // 过滤数据
          graphData.value.nodes = graphData.value.nodes.filter(
            (e) => e.id !== id
          );
          graphData.value.edges = graphData.value.edges.filter(
            (e) => e.source !== id && e.target !== id
          );

          proxy.$modal.msgSuccess("删除成功");
        });
      })
      .catch(() => {});
  }
};

// 右侧详情-折叠面板
const collapseAct = ref(["1", "2", "3", "4"]);
const collapseChange = (val) => {
  console.log(val);
};

// 属性信息表格
const attrLoading = ref(false);
const attrData = ref([]);
//修改属性
const attrUpdate = (val) => {
  if (releaseStatus.value) {
    proxy.$modal.msgWarning("已发布状态禁止删除和修改！");
  } else {
    proxy.$modal
      .prompt(`请输入属性名称为【${val.name}】的属性值`)
      .then((res) => {
        console.log("---修改属性----", res);
        let node = currentNodeData.value;
        updateNodeAttribute({
          taskId: node.task_id,
          extractType: node.extract_type,
          tableName: node.table_name,
          dataId: node.data_id,
          attributeKey: "attributeId_" + val.id,
          attributeValue: res.value,
        }).then((response) => {
          if (response && response.code == 200) {
            attrData.value.forEach((e) => {
              if (e.id == val.id) {
                e.dataValue = res.value;
              }
            });
          }
        });
      });
  }
};
//删除属性
const attrDelete = (val) => {
  if (releaseStatus.value) {
    proxy.$modal.msgWarning("已发布状态禁止删除和修改！");
  } else {
    proxy.$modal
      .confirm(`是否确认删除属性名称为【${val.name}】的数据项？`)
      .then(() => {
        let node = currentNodeData.value;
        deleteNodeAttribute({
          taskId: node.task_id,
          extractType: node.extract_type,
          tableName: node.table_name,
          dataId: node.data_id,
          attributeKey: "attributeId_" + val.id,
        }).then((res) => {
          if (res && res.code == 200) {
            console.log("------删除属性成功---", attrData, val);
            attrData.value = attrData.value.filter((e) => e.id != val.id);
          }
        });
      });
  }
};

// 关系三元组表格
const tripletLoading = ref(false);
const tripletData = ref([]);
// 修改关系
const tripletUpdate = (val) => {
  if (releaseStatus.value) {
    proxy.$modal.msgWarning("已发布状态禁止删除和修改！");
  } else {
    proxy.$modal
      .prompt(`请输入起点【${val.startName}】终点【${val.endName}】的关系`)
      .then((res) => {
        console.log("---修改关系----", res);
        if (!res.value) {
          proxy.$modal.msgWarning("关系不能为空");
        } else if (/^\d+$/.test(res.value)) {
          proxy.$modal.msgWarning("关系不能为纯数字");
        } else {
          let startNode = graphData.value.nodes.find(
            (e) => e.id == val.startId
          );
          let endNode = graphData.value.nodes.find((e) => e.id == val.endId);
          updateRelationship({
            relationshipId: Number(val.id.replace("edge", "")),
            relationship: res.value,
            startId: startNode.data_id,
            endId: endNode.data_id,
            startTableName: startNode.table_name,
            endTableName: endNode.table_name,
            taskId: Number(taskInfo.value.id),
            extractType: 1,
          }).then((response) => {
            if (response && response.code == 200) {
              graphData.value.edges.forEach((e) => {
                if (e.id == val.id) {
                  e.relationType = res.value;
                }
              });
              tripletData.value.forEach((e) => {
                if (e.id == val.id) {
                  e.name = res.value;
                }
              });
            }
          });
        }
      });
  }
};
// 删除关系
const tripletDelete = (val) => {
  if (releaseStatus.value) {
    proxy.$modal.msgWarning("已发布状态禁止删除和修改！");
  } else {
    proxy.$modal
      .confirm(
        `是否确认删除起点【${val.startName}】终点【${val.endName}】的数据项？`
      )
      .then(() => {
        deleteRelationship({
          relationshipId: Number(val.id.replace("edge", "")),
        }).then((res) => {
          if (res && res.code == 200) {
            graphData.value.edges = graphData.value.edges.filter(
              (e) => e.id != val.id
            );
            tripletData.value = tripletData.value.filter((e) => e.id != val.id);
          }
        });
      });
  }
};

// 表格3
// const sourceLoading = ref(false);
// const sourceData = ref([]);
</script>

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
  justify-content: space-between;

  .el-main {
    padding: 0px 0px;
  }

  .el-aside {
    padding: 0px;
    margin-bottom: 0px;
    background-color: #f0f2f5;
  }

  .left-tree {
    padding: 15px 15px 5px 15px;
  }

  .control-tree {
    height: calc(100% - 52px);
    background: #fff;
    overflow: hidden auto;

    :deep(.el-icon) {
      color: #7e7e7e;
    }

    :deep(
        .el-tree--highlight-current
          .el-tree-node.is-current
          > .el-tree-node__content
      ) {
      background-color: #e3f4fc !important;
      border-right: 2px solid var(--el-color-primary);
    }
  }

  .resize-bar {
    cursor: ew-resize;
    background-color: #f0f2f5;
    height: 86vh;
    display: flex;
    align-items: center;
    justify-content: center;

    .resize-handle-sx {
      width: 15px;
      text-align: center;

      .zjsx {
        display: inline-block;
        width: 5px;
        height: 50px;
        border-left: 1px solid #ccc;
        border-right: 1px solid #ccc;
      }
    }
  }

  .control {
    width: 100%;
    height: 100%;
  }

  .head-title {
    height: 50px;
    background: #fff;
    padding: 10px;
    margin-bottom: 10px;
    display: flex;
    justify-content: space-between;
    align-items: center;

    .name {
      font-family: PingFangSC;
      font-weight: 500;
      font-size: 16px;
    }

    .btns {
      float: right;

      .el-button {
        height: 28px;
      }
    }
  }

  .gragh-wrap {
    position: relative;
    height: calc(100% - 60px);
    overflow: hidden auto;

    .gragh-container {
      width: 100%;
      height: 100%;
      background: #fff url("@/assets/ke/images/bg.png") no-repeat;
      background-size: 100% 100%;

      :deep(.g6-toolbar) {
        .g6-toolbar-item {
          fill: #7dbffa;

          &:hover {
            background-color: rgba(255, 255, 255, 0.2);
          }
        }
      }
    }

    .toolbar {
      position: absolute;
      top: 8px;
      right: unset;
      bottom: unset;
      left: 8px;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      border-radius: 2px;
      box-shadow: 0px 0px 8px rgba(0, 0, 0, 0.4);
      // opacity: 0.65;
      z-index: 100;

      .toolbar-item {
        display: inline-block;
        width: 20px;
        height: 20px;
        padding: 4px;
        cursor: pointer;
        box-sizing: content-box;

        &:hover {
          background-color: rgba(255, 255, 255, 0.2);
        }

        i {
          color: #7dbffa;
          font-size: 20px;
          pointer-events: none;
        }
      }
    }

    .tool {
      cursor: pointer;
      position: absolute;
      left: 20px;
      bottom: 20px;
      width: 50px;
      height: 50px;
      background: url("@/assets/ke/images/tool.png") no-repeat;
      background-size: 100% 100%;

      .tool-mask {
        width: 151px;
        height: 214px;
        background: #1c376a;
        border-radius: 4px 4px 4px 4px;
        border: 1px solid #448fff;
        position: absolute;
        bottom: calc(100% + 8px);
        left: 0;
        box-sizing: border-box;
        padding: 20px;

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
            line-height: 1;
            font-family: PingFangSCs;
            font-size: 13px;
            color: #d7e3fa;
          }
        }
      }
    }

    .details-dialog {
      position: absolute;
      top: 0;
      right: 0;
      width: 500px;
      height: 100%;
      background: #fff;
      box-shadow: 0px 0px 4px 1px rgba(0, 0, 0, 0.2);

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
        }
      }
    }
  }
}
</style>
