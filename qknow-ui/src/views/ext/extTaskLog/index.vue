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
      <el-form class="btn-style" :model="queryParams" ref="queryRef" :inline="true" label-width="75px"
               v-show="showSearch" @submit.prevent>

        <el-form-item label="任务名称" prop="taskName">
          <el-input
              class="el-form-input-width"
              v-model="queryParams.taskName"
              placeholder="请输入任务名称"
              clearable
              @keyup.enter="handleQuery"
          />
        </el-form-item>

<!--        <el-form-item label="任务类型" prop="taskType">-->
<!--          <el-select v-model="queryParams.taskType" style="width: 200px;" placeholder="请选择任务类型" clearable>-->
<!--            <el-option-->
<!--                v-for="dict in ext_task_type"-->
<!--                :key="dict.value"-->
<!--                :label="dict.label"-->
<!--                :value="parseInt(dict.value)"-->
<!--            ></el-option>-->
<!--          </el-select>-->
<!--        </el-form-item>-->

        <el-form-item label="执行状态" prop="status">
          <el-select v-model="queryParams.status" style="width: 200px;" placeholder="请选择执行状态" clearable>
            <el-option
                v-for="dict in ext_log_status"
                :key="dict.value"
                :label="dict.label"
                :value="parseInt(dict.value)"
            ></el-option>
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button plain type="primary" @click="handleQuery" @mousedown="(e) => e.preventDefault()">
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
        <el-row :gutter="15" class="btn-style">
          <el-col :span="1.5">
            <el-button
              type="danger"
              plain
              :disabled="multiple"
              @click="handleDelete"
              icon="Delete"
              @mousedown="(e) => e.preventDefault()"
            >
              删除
            </el-button>
          </el-col>
        </el-row>
        <div class="justify-end top-right-btn">
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
        </div>
      </div>
      <el-table stripe v-loading="loading" :data="taskLogList"
                @selection-change="handleSelectionChange"
                :default-sort="defaultSort" @sort-change="handleSortChange"
                :tooltip-options="tooltipOptions"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column v-if="getColumnVisibility(1)" 
                    label="编号" 
                    align="center" 
                    prop="id" 
                    width="100"                     
                    sortable="custom"
                    :sort-orders="['descending', 'ascending']"
                    show-overflow-tooltip
                    />
        <el-table-column v-if="getColumnVisibility(2)" label="任务名称" align="left" prop="taskName" width="260">
          <template #default="scope">
            {{ scope.row.taskName || '-' }}
          </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(3)" label="执行类型" align="center" prop="taskType" width="150">
          <template #default="scope">
            <div>
              <dict-tag :options="ext_task_log_type" :value="scope.row.taskType"/>
            </div>
          </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(4)" label="执行状态" align="center" prop="status">
          <template #default="scope">
            <div>
              <dict-tag :options="ext_log_status" :value="scope.row.status"/>
            </div>
          </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(5)" label="开始时间" align="center" prop="startTime" width="160"
                         sortable="custom" :sort-orders="['descending', 'ascending']">
          <template #default="scope">
            <span>{{ parseTime(scope.row.startTime, '{y}-{m}-{d} {h}:{i}') }}</span>
          </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(6)" label="结束时间" align="center" prop="endTime" width="160"
                         sortable="custom" :sort-orders="['descending', 'ascending']">
          <template #default="scope">
            <span>{{ parseTime(scope.row.endTime, '{y}-{m}-{d} {h}:{i}') }}</span>
          </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(7)" label="创建人" align="center" prop="createBy" width="160">
          <template #default="scope">
            {{ scope.row.createBy || '-' }}
          </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(8)" label="创建时间" align="center" prop="createTime" width="160"
                  sortable="custom" :sort-orders="['descending', 'ascending']">
          <template #default="scope">
            <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}') }}</span>
          </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(9)" label="操作" align="center" class-name="small-padding fixed-width" fixed="right" width="200">
          <template #default="scope">
            <!-- <el-button link type="primary" icon="view"
                       @click="handleDetail(scope.row)"
                       v-if="scope.row.errorMsg"
            >查看错误日志
            </el-button> -->

            <el-button link type="primary"
                       icon="view"
                       @click="showLogDetail(scope.row)"
            >查看步骤
            </el-button>
          </template>
        </el-table-column>

        <template #empty>
          <div class="emptyBg">
            <img src="@/assets/system/images/no_data/noData.png" alt=""/>
            <p>暂无记录</p>
          </div>
        </template>
      </el-table>

      <pagination
          v-show="total>0"
          :total="total"
          v-model:page="queryParams.pageNum"
          v-model:limit="queryParams.pageSize"
          @pagination="getList"
      />
    </div>

    <!-- 查看具体步骤 -->
    <el-dialog v-model="open" width="800px" :append-to="$refs['app-container']" draggable>
      <template #header="{ close, titleId, titleClass }">
        <span role="heading" aria-level="2" class="el-dialog__title">
          抽取步骤
        </span>
      </template>

      <el-table stripe height="48vh"
                v-loading="detailPageLoading"
                :data="taskLogDetailList"
                @sort-change="handleLogDetailSortChange"
      >
        <el-table-column type="index" label="编号" align="center" prop="id" width="60">
          <template #default="scope">
            {{ scope.$index+1 || '-' }}
          </template>
        </el-table-column>
        <el-table-column label="执行步骤" align="left" prop="step">
          <template #default="scope">
            <span>{{ scope.row.step || '-' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="执行时间" align="center" prop="createTime"
                         sortable="custom" :sort-orders="['descending', 'ascending']">
          <template #default="scope">
            <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}') }}</span>
          </template>
        </el-table-column>

        <template #empty>
          <div class="emptyBg">
            <img src="@/assets/system/images/no_data/noData.png" alt=""/>
            <p>暂无记录</p>
          </div>
        </template>
      </el-table>
      <pagination
          v-show="detailPageTotal>0"
          :total="detailPageTotal"
          v-model:page="logDetailParams.pageNum"
          v-model:limit="logDetailParams.pageSize"
          @pagination="showLogDetail()"
      />
      <template #footer>
        <div class="dialog-footer">
          <el-button size="mini" @click="cancel">关 闭</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 查看错误消息 -->
    <el-dialog
        v-model="centerDialogVisible"
        width="800px"
        destroy-on-close
    >
      <template #header="{ close, titleId, titleClass }">
        <span role="heading" aria-level="2" class="el-dialog__title">
          错误日志
        </span>
      </template>
      <div class="wrap-line">
        {{ errorMsg }}
      </div>
      <template #footer>
        <div class="dialog-footer">
          <el-button size="mini" @click="centerDialogVisible=false">关 闭</el-button>
        </div>
      </template>
    </el-dialog>

  </div>
</template>

<script setup name="ExtTaskLog">
import {listTaskLog, getDetailPage, delTaskLog} from "@/api/ext/extTaskLog/extTaskLog";

const {proxy} = getCurrentInstance();

const taskLogList = ref([]);
const taskLogDetailList = ref([]);
const {ext_task_log_type, ext_log_status} = proxy.useDict('ext_task_log_type', 'ext_log_status');

// 列显隐信息
const columns = ref([
  {key: 1, label: "编号", visible: true},
  {key: 2, label: "任务名称", visible: true},
  {key: 3, label: "执行类型", visible: true},
  {key: 4, label: "执行状态", visible: true},
  {key: 5, label: "开始时间", visible: true},
  {key: 6, label: "结束时间", visible: true},
  {key: 7, label: "创建人", visible: true},
  {key: 8, label: "创建时间", visible: true},
  {key: 9, label: "操作", visible: true},
]);

const getColumnVisibility = (key) => {
  const column = columns.value.find(col => col.key === key);
  // 如果没有找到对应列配置，默认显示
  if (!column) return true;
  // 如果找到对应列配置，根据visible属性来控制显示
  return column.visible;
};

const open = ref(false);
const centerDialogVisible = ref(false);
const errorMsg = ref("");
const loading = ref(true);
const detailPageLoading = ref(true);
const showSearch = ref(true);
const total = ref(0);
const detailPageTotal = ref(0);
const logId = ref(0);
const defaultSort = ref({prop: "createTime", order: "desc"});
const logDetailParams = ref({
  pageNum: 1,
  pageSize: 10,
  logId: null,
  orderByColumn: "createTime",
  isAsc: "descending"
});
const tooltipOptions = ref({"effect": "light"});
const router = useRouter();

const ids = ref([]);
const single = ref(true);
const multiple = ref(true);

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    workspaceId: null,
    logId: null,
    step: null,
    createTime: null,
    taskName: "",
    orderByColumn: "startTime",
    isAsc: "descending"
  },

});

const {queryParams, form} = toRefs(data);

/** 查询抽取任务执行日志详情列表 */
function getList() {
  loading.value = true;
  listTaskLog(queryParams.value).then(response => {
    taskLogList.value = response.data.rows;
    total.value = response.data.total;
    loading.value = false;
  });
}

/**
 * 打开日志详情页面弹窗
 * @param row
 */
function showLogDetail(row) {
  if (row) {
    logId.value = row.id;
  }
  detailPageLoading.value = true;
  logDetailParams.value.logId = logId.value
  getDetailPage(logDetailParams.value).then(response => {
    taskLogDetailList.value = response.data.rows;
    detailPageTotal.value = response.data.total;
    detailPageLoading.value = false;
    open.value = true;
  });
}

// 取消按钮
function cancel() {
  open.value = false;
  reset();
}

// 表单重置
function reset() {
  form.value = {
    id: null,
    workspaceId: null,
    logId: null,
    step: null,
    validFlag: null,
    delFlag: null,
    createBy: null,
    creatorId: null,
    createTime: null,
    updateBy: null,
    updaterId: null,
    updateTime: null,
    remark: null
  };
  proxy.resetForm("taskLogDetailRef");
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

/** 排序触发事件 */
function handleSortChange(column, prop, order) {
  queryParams.value.orderByColumn = column.prop;
  queryParams.value.isAsc = column.order;
  getList();
}

/** 日志详情排序触发事件 */
function handleLogDetailSortChange(column, prop, order) {
  logDetailParams.value.orderByColumn = column.prop;
  logDetailParams.value.isAsc = column.order;
  showLogDetail();
}

/** 详情按钮操作 */
function handleDetail(row) {
  reset();
  console.log(row.errorMsg)
  errorMsg.value = row.errorMsg;
  centerDialogVisible.value = true;
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map((item) => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 删除按钮操作 */
function handleDelete(row) {
  const _ids = row.id || ids.value;
  proxy.$modal
    .confirm('是否确认删除抽取日志编号为"' + _ids + '"的数据项？')
    .then(function () {
      return delTaskLog(_ids);
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess("删除成功");
    })
    .catch(() => {});
}

getList();
</script>

<style scoped>
.wrap-line {
  white-space: pre-wrap;
}
</style>