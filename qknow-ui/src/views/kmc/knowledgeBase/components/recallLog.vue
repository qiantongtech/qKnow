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
  <div class="app-container" ref="app-container">
    <div class="pagecont-top" v-show="showSearch">
      <el-form
        class="btn-style"
        :model="queryParams"
        ref="queryRef"
        :inline="true"
        v-show="showSearch"
        @submit.prevent
      >
        <el-form-item label="问题" prop="query">
          <el-input
            class="el-form-input-width"
            v-model="queryParams.query"
            placeholder="请输入问题"
            clearable
            @keyup.enter="handleQuery"
          />
        </el-form-item>

        <el-form-item>
          <el-button
            plain
            type="primary"
            @click="handleQuery"
            @mousedown="(e) => e.preventDefault()"
          >
            <i class="iconfont-mini icon-a-zu22377 mr5"></i>查询
          </el-button>
          <el-button @click="resetQuery" @mousedown="(e) => e.preventDefault()">
            <i class="iconfont-mini icon-a-zu22378 mr5"></i>重置
          </el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="pagecont-bottom">
      <div class="justify-between mb15">
        <div class="justify-end top-right-btn">
          <right-toolbar
            v-model:showSearch="showSearch"
            @queryTable="getList"
            :columns="columns"
          ></right-toolbar>
        </div>
      </div>
      <el-table
        stripe
        v-loading="loading"
        :data="logList"
        @selection-change="handleSelectionChange"
        :default-sort="defaultSort"
        @sort-change="handleSortChange"
      >
        <el-table-column
          v-if="getColumnVisibility(1)"
          label="编号"
          align="center"
          prop="id"
          width="80"
          sortable="custom"
        />
        <el-table-column
          v-if="getColumnVisibility(2)"
          label="问题"
          align="left"
          prop="query"
          width="300"
          :show-overflow-tooltip="{ effect: 'light' }"
        >
          <template #default="scope">
            {{ scope.row.query || "-" }}
          </template>
        </el-table-column>
        <!-- <el-table-column
          v-if="getColumnVisibility(3)"
          label="备注"
          align="left"
          prop="remark"
        >
          <template #default="scope">
            {{ scope.row.remark || "-" }}
          </template>
        </el-table-column> -->
        <el-table-column
          v-if="getColumnVisibility(4)"
          label="创建人"
          align="center"
          prop="createBy"
        >
          <template #default="scope">
            {{ scope.row.createBy || "-" }}
          </template>
        </el-table-column>
        <el-table-column
          v-if="getColumnVisibility(5)"
          label="创建时间"
          align="center"
          prop="createTime"
          width="180"
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
          v-if="getColumnVisibility(6)"
          label="更新人"
          align="center"
          prop="upadteBy"
        >
          <template #default="scope">
            {{ scope.row.createBy || "-" }}
          </template>
        </el-table-column>
        <el-table-column
          v-if="getColumnVisibility(7)"
          label="更新时间"
          align="center"
          prop="updateTime"
          width="180"
          sortable="custom"
          :sort-orders="['descending', 'ascending']"
        >
          <template #default="scope">
            <span>{{
              parseTime(scope.row.createTime, "{y}-{m}-{d} {h}:{i}")
            }}</span>
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
    </div>
  </div>
</template>

<script setup name="Log">
import { listLog, delLog } from "@/api/kmc/knowledgeBase/log";
import { ref } from "vue";

const { proxy } = getCurrentInstance();
const route = useRoute();

const logList = ref([]);

// 列显隐信息
const columns = ref([
  { key: 1, label: "编号", visible: true },
  { key: 2, label: "问题", visible: true },
  // { key: 3, label: "备注", visible: true },
  { key: 4, label: "创建人", visible: true },
  { key: 5, label: "创建时间", visible: true },
  { key: 6, label: "更新人", visible: true },
  { key: 7, label: "更新时间", visible: true },
]);

const getColumnVisibility = (key) => {
  const column = columns.value.find((col) => col.key === key);
  // 如果没有找到对应列配置，默认显示
  if (!column) return true;
  // 如果找到对应列配置，根据visible属性来控制显示
  return column.visible;
};

const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const defaultSort = ref({ prop: "createTime", order: "descending" });

const data = reactive({
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    knowledgeId: null,
    query: null,
    createTime: null,
    orderByColumn: "createTime",
    isAsc: "desc",
  },
});

const { queryParams } = toRefs(data);

watch(
  () => route.params.kbId,
  (newKbId) => {
    if (newKbId) {
      queryParams.value.knowledgeId = Number(newKbId);
      getList();
    }
  },
  { deep: true, immediate: true }
);

/** 查询召回记录列表 */
function getList() {
  loading.value = true;
  listLog(queryParams.value).then((response) => {
    logList.value = response.data.rows;
    total.value = response.data.total;
    loading.value = false;
  });
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef");
  handleQuery();
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

/** 删除按钮操作 */
function handleDelete(row) {
  const _ids = row.id || ids.value;
  proxy.$modal
    .confirm('是否确认删除召回记录编号为"' + _ids + '"的数据项？')
    .then(function () {
      return delLog(_ids);
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess("删除成功");
    })
    .catch(() => {});
}
</script>
