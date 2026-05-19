<!--
  Copyright (c) 2026 Jiangsu Qiantong Technology Co., Ltd.
   *
  Software Name: qKnow Knowledge Platform (Business Edition)
  Software Copyright Registration No. 15980140
   *
  [RIGHTS AND LICENSE STATEMENT]
  This file contains non-public commercial source code of which Jiangsu Qiantong
  Technology Co., Ltd. lawfully possesses complete intellectual property rights.
   *
  Access and use are limited to entities or individuals who have signed a valid
  commercial license agreement, within the scope stipulated in the agreement.
  The "accessibility" of this source code is premised on lawful authorization
  and does not constitute any form of transfer of intellectual property rights
  or implied licensing.
   *
  [PROHIBITIONS]
  Unless explicitly agreed in the license agreement, the following acts in any
  form are strictly prohibited:
  1. Copying, disseminating, disclosing, selling, renting, or redistributing
  this source code;
  2. Providing the software's functionality to third parties via SaaS, PaaS,
  cloud hosting, or other means;
  3. Using this software or its derivative versions to develop products that
  compete with the Right Holder;
  4. Providing or displaying this source code or related technical information
  to unauthorized third parties;
  5. Tampering with, circumventing, or destroying copyright notices, license
  verifications, or other technical protection measures.
   *
  [LEGAL LIABILITY]
  Any unauthorized use constitutes an infringement of trade secrets and
  intellectual property rights.
   *
  The Right Holder will strictly pursue liability for breach of contract and
  infringement in accordance with the commercial agreement and laws such as
  the "Copyright Law of the People's Republic of China" and the "Anti-Unfair
  Competition Law".
   *
  ============================================================================
   *
  Copyright (c) 2026 江苏千桐科技有限公司
   *
  软件名称：qKnow 知识平台（商业版） | 软著登字第15980140号
   *
  【权利与授权声明】
  本文件属于江苏千桐科技有限公司依法享有完全知识产权的非公开商业源代码。
  仅限已签署有效商业授权合同的单位或个人在约定范围内查阅和使用。
  源代码的“可访问性”均以合法授权为前提，不构成任何形式的知识产权转让或默示授权。
   *
  【禁止事项】
  除授权合同明确约定外，严禁任何形式的：
  1. 复制、传播、披露、出售、出租或再分发本源代码；
  2. 通过 SaaS、PaaS、云托管等方式向第三方提供本软件功能；
  3. 将本软件或其衍生版本用于开发与权利人构成竞争的产品；
  4. 向未授权第三方提供或展示本源代码或相关技术信息；
  5. 篡改、规避或破坏版权标识、授权校验及其他技术保护措施。
   *
  【法律责任】
  任何未经授权的利用行为，均构成对商业秘密及知识产权的侵害。
  权利人将依据商业合同及《中华人民共和国著作权法》《反不正当竞争法》
  等法律法规，严厉追究违约与侵权责任。
-->

<template>
  <el-table
    stripe
    height="58vh"
    v-loading="loading"
    :data="graphList"
    @selection-change="handleSelectionChange"
    :default-sort="defaultSort"
    @sort-change="handleSortChange"
  >
    <el-table-column
      v-if="getColumnVisibility(1)"
      label="编号"
      align="center"
      width="55"
    >
      <template #default="scope">
        <span>{{ scope.$index + 1 }}</span>
      </template>
    </el-table-column>
    <el-table-column
      v-if="getColumnVisibility(2)"
      label="名称"
      align="center"
      prop="graphId"
      width="250"
    >
      <template #default="scope">
        {{ getGraphInfo(scope.row.graphId, "name") }}
      </template>
    </el-table-column>
    <el-table-column
      v-if="getColumnVisibility(3)"
      label="挂载说明"
      align="left"
      prop="description"
      width="350"
    >
      <template #default="scope">
        {{ scope.row.description || "-" }}
      </template>
    </el-table-column>
    <el-table-column
      v-if="getColumnVisibility(4)"
      label="标签"
      align="center"
      prop="graphId"
    >
      <template #default="scope">
        <div class="multiline-ellipsis">
          <el-tag
            v-for="(tag, tagIndex) in getTags(
              getGraphInfo(scope.row.graphId, 'tags')
            )"
            :key="tagIndex"
            class="card-tag"
          >
            {{ tag.name }}
          </el-tag>
          <span
            v-if="getTags(getGraphInfo(scope.row.graphId, 'tags')).length <= 0"
            >-</span
          >
        </div>
      </template>
    </el-table-column>
    <el-table-column
      v-if="getColumnVisibility(7)"
      label="创建人"
      align="center"
      prop="createBy"
    />
    <el-table-column
      v-if="getColumnVisibility(8)"
      label="创建时间"
      align="center"
      prop="createTime"
      sortable="custom"
      :sort-orders="['descending', 'ascending']"
    >
      <template #default="scope">
        <span>{{
          parseTime(scope.row.createTime, "{y}-{m}-{d} {h}:{i}")
        }}</span>
      </template>
    </el-table-column>
    <el-table-column
      v-if="getColumnVisibility(8)"
      label="最后更新时间"
      align="center"
      prop="updateTime"
      sortable="custom"
      :sort-orders="['descending', 'ascending']"
    >
      <template #default="scope">
        <span>{{
          parseTime(scope.row.updateTime, "{y}-{m}-{d} {h}:{i}")
        }}</span>
      </template>
    </el-table-column>
    <el-table-column
      label="操作"
      align="center"
      class-name="small-padding fixed-width"
      v-if="props.source === 'myApp'"
    >
      <template #default="scope">
        <el-button
          link
          type="primary"
          icon="Switch"
          @click="handleUpdate(scope.row)"
          >替换</el-button
        >
      </template>
    </el-table-column>

    <template #empty>
      <div class="emptyBg">
        <img src="@/assets/system/images/no_data/noData.png" alt="" />
        <p>暂无记录</p>
      </div>
    </template>
  </el-table>

  <pagination
    v-show="total > 0"
    :total="total"
    v-model:page="queryParams.pageNum"
    v-model:limit="queryParams.pageSize"
    @pagination="getList"
  />

  <!-- 选择知识图谱对话框 -->
  <el-dialog
    :title="graphDialogTitle"
    v-model="graphDialogOpen"
    width="1000px"
    :append-to="$refs['app-container']"
    draggable
  >
    <template #header="{ close, titleId, titleClass }">
      <span role="heading" aria-level="2" class="el-dialog__title">
        {{ graphDialogTitle }}
      </span>
    </template>
    <!--知识图谱数据-->
    <el-form
      class="btn-style"
      :model="queryParamsGraph"
      ref="queryGraphRef"
      :inline="true"
    >
      <el-form-item label="知识图谱名称" prop="name">
        <el-input
          class="el-form-input-width"
          v-model="queryParamsGraph.name"
          placeholder="请输入知识图谱名称"
          clearable
          @keyup.enter="handleQueryGraph"
        />
      </el-form-item>
      <el-form-item>
        <el-button
          plain
          type="primary"
          @click="handleQueryGraph"
          @mousedown="(e) => e.preventDefault()"
        >
          <i class="iconfont-mini icon-a-zu22377 mr5"></i>查询
        </el-button>
        <el-button
          @click="resetQueryGraph"
          @mousedown="(e) => e.preventDefault()"
        >
          <i class="iconfont-mini icon-a-zu22378 mr5"></i>重置
        </el-button>
      </el-form-item>
    </el-form>
    <el-table
      stripe
      ref="graphTableRef"
      v-loading="graphLoading"
      :data="graphSelectList"
      row-key="id"
      :row-class-name="getTableRowClassName"
      highlight-current-row
      @row-click="handleGraphRowClick"
    >
      <el-table-column
        label="知识图谱名称"
        prop="name"
        align="center"
        :show-overflow-tooltip="true"
        width="200"
      />
      <el-table-column
        label="描述"
        prop="description"
        align="center"
        :show-overflow-tooltip="true"
        width="280"
      />
      <el-table-column
        label="标签"
        prop="tags"
        align="center"
        :show-overflow-tooltip="true"
        width="180"
      >
        <template #default="scope">
          <el-tag
            v-for="(tag, index) in getTags(scope.row.tags)"
            :key="index"
            class="card-tag"
          >
            {{ tag.name }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        label="创建时间"
        align="center"
        prop="createTime"
        width="180"
      >
        <template #default="scope">
          <span>{{
            scope.row.createTime
              ? parseTime(scope.row.createTime, "{y}-{m}-{d} {h}:{i}")
              : "-"
          }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="最后更新时间"
        align="center"
        prop="updateTime"
        width="180"
      >
        <template #default="scope">
          <span>{{
            scope.row.updateTime
              ? parseTime(scope.row.updateTime, "{y}-{m}-{d} {h}:{i}")
              : "-"
          }}</span>
        </template>
      </el-table-column>
    </el-table>
    <pagination
      v-show="graphTotal > 0"
      :total="graphTotal"
      v-model:page="queryParamsGraph.pageNum"
      v-model:limit="queryParamsGraph.pageSize"
      @pagination="getGraphSelectList"
    />
    <template #footer>
      <div class="dialog-footer">
        <el-button size="mini" @click="graphDialogOpen = false"
          >取 消</el-button
        >
        <el-button type="primary" size="mini" @click="submitGraphForm"
          >确 定</el-button
        >
      </div>
    </template>
  </el-dialog>
</template>

<script setup name="Method">
import {
  listKacGraph,
  getKacGraph,
  updateKacGraph,
} from "@/api/kac/applyGraph/applyGraph.js";
// import { listSimple } from "@/api/kg/graph/graph.js";

const { proxy } = getCurrentInstance();

const graphList = ref([]);
const graphAllList = ref([]);
const graphName = ref("");

const graphDialogTitle = ref("");
const graphDialogOpen = ref(false);
const graphLoading = ref(true);
const graphSelectList = ref([]);
const graphTotal = ref(0);
const selectedGraph = ref(null);
// 【修复点 1】：此处不应初始化为数组，DOM ref 应初始化为 null
const graphTableRef = ref(null);
const currentEditRow = ref(null);

const props = defineProps({
  applyId: {
    type: Number,
    required: true,
  },
  graphList: {
    type: Array,
    default: () => [],
  },
  source: {
    type: String,
    default: "horizontal",
    validator: (value) => ["horizontal", "vertical", "myApp"].includes(value),
  },
});

// 列显隐信息
const columns = ref([
  { key: 1, label: "编号", visible: true },
  { key: 2, label: "名称", visible: true },
  { key: 3, label: "挂载说明", visible: true },
  { key: 4, label: "标签", visible: true },
  { key: 5, label: "创建人", visible: true },
  { key: 6, label: "创建时间", visible: true },
  { key: 8, label: "最后更新时间", visible: true },
]);

const getColumnVisibility = (key) => {
  const column = columns.value.find((col) => col.key === key);
  if (!column) return true;
  return column.visible;
};

const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const defaultSort = ref({ prop: "createTime", order: "desc" });
const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    workspaceId: null,
    applyId: null,
    graphId: null,
    trenchKey: null,
    createTime: null,
  },
  queryParamsGraph: {
    pageNum: 1,
    pageSize: 10,
    name: null,
  },
  rules: {},
});

const { queryParams, queryParamsGraph, form, rules } = toRefs(data);

const getGraphInfo = (id, field) => {
  if (!id) return "-";
  const target = graphAllList.value.find((item) => item.id == id);
  if (!target) return "-";
  if (field === "tags") {
    return target[field];
  }
  return target[field] != null && target[field] !== "" ? target[field] : "-";
};

/** 查询应用关联知识图谱列表 */
function getList() {
  loading.value = true;
  queryParams.value.applyId = props.applyId;
  listKacGraph(queryParams.value).then((response) => {
    graphList.value = response.data.rows;
    total.value = response.data.total;
    loading.value = false;
  });
}

// 监听父组件传递的知识图谱列表变化
watch(
  () => props.graphList,
  (newList) => {
    if (newList && newList.length > 0) {
      graphAllList.value = newList;
      loading.value = false;
    }
  },
  { immediate: true, deep: true }
);

function getTags(data) {
  if (!data) {
    return [];
  }
  if (typeof data === "string") {
    try {
      return JSON.parse(data);
    } catch (e) {
      return [];
    }
  }
  if (Array.isArray(data)) {
    return data;
  }
  return [];
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map((item) => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 排序触发事件 */
function handleSortChange(column, prop, order) {
  queryParams.value.orderByColumn = column.prop;
  queryParams.value.isAsc = column.order;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef");
  handleQuery();
}

// 表单重置
function reset() {
  form.value = {
    id: null,
    workspaceId: null,
    appId: null,
    graphId: null,
    trenchKey: null,
    validFlag: null,
    delFlag: null,
    createBy: null,
    creatorId: null,
    createTime: null,
    updateBy: null,
    updaterId: null,
    updateTime: null,
    remark: null,
    description: null,
  };
  proxy.resetForm("graphRef");
}

/** 修改按钮操作 */
function handleUpdate(row) {
  currentEditRow.value = { ...row };
  reset();
  const _id = row.id || ids.value;
  getKacGraph(_id).then((response) => {
    form.value = response.data;
    graphName.value = getGraphInfo(form.value.graphId, "name");
    graphDialogTitle.value = "替换关联知识图谱";
    graphLoading.value = true;
    queryParamsGraph.value.pageNum = 1;
    queryParamsGraph.value.name = null;
    selectedGraph.value = form.value.graphId
      ? { id: form.value.graphId }
      : null;
    graphDialogOpen.value = true;
    getGraphSelectList();
  });
}

function getGraphSelectList() {
  // graphLoading.value = true;
  // listSimple(queryParamsGraph.value).then(response => {
  //   graphLoading.value = false;
  //   graphSelectList.value = response.data.rows;
  //   graphTotal.value = response.data.total;
  //   nextTick(() => {
  //     // 【修复点 2】：利用 nextTick 确保列表 DOM 渲染后，通知表格进行选中高亮
  //     if (selectedGraph.value && graphTableRef.value) {
  //       const data = graphSelectList.value.find(item => item.id == selectedGraph.value.id);
  //       if (data) {
  //         selectedGraph.value = data;
  //         graphTableRef.value.setCurrentRow(data); // 触发 Element 原生高亮
  //       } else {
  //         graphTableRef.value.setCurrentRow(); // 未找到则清除高亮
  //       }
  //     }
  //   });
  // });
}

function handleGraphRowClick(row) {
  if (selectedGraph.value && selectedGraph.value.id == row.id) {
    // 【修复点 3】：反选时需要清除 el-table 的内部高亮记录
    selectedGraph.value = null;
    if (graphTableRef.value) {
      graphTableRef.value.setCurrentRow();
    }
  } else {
    selectedGraph.value = row;
  }
}

function submitGraphForm() {
  if (!selectedGraph.value) {
    proxy.$modal.msgError("请选择一个知识图谱");
    return;
  }
  form.value.graphId = selectedGraph.value.id;
  graphName.value = selectedGraph.value.name;

  updateKacGraph(form.value)
    .then((response) => {
      proxy.$modal.msgSuccess("替换成功");
      graphDialogOpen.value = false;
      getList();
    })
    .catch((error) => {});
}

function handleQueryGraph() {
  queryParamsGraph.value.pageNum = 1;
  getGraphSelectList();
}

function resetQueryGraph() {
  queryParamsGraph.value.pageNum = 1;
  queryParamsGraph.value.pageSize = 10;
  queryParamsGraph.value.name = null;
  getGraphSelectList();
}

function getTableRowClassName({ row }) {
  if (selectedGraph.value && selectedGraph.value.id == row.id) {
    return "selected-row";
  }
  return "";
}

getList();
</script>
<style lang="scss" scoped>
.card-tag {
  margin: 2px;
}

:deep(.el-table .selected-row > td.el-table__cell) {
  background-color: #e6f7ff !important;
}
</style>
