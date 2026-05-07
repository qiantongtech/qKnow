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
  <el-dialog
    title="知识分类-单选"
    v-model="visible"
    width="1200px"
    :append-to="$refs['app-container']"
    draggable
    destroy-on-close
    @close="cancel"
  >
    <el-form
      class="btn-style"
      :model="queryParams"
      ref="queryRef"
      :inline="true"
      v-show="showSearch"
      label-width="68px"
    >
      <el-form-item label="工作区id" prop="workspaceId">
        <el-input
          style="width: 240px"
          v-model="queryParams.workspaceId"
          placeholder="请输入工作区id"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="父级id" prop="parentId">
        <el-input
          style="width: 240px"
          v-model="queryParams.parentId"
          placeholder="请输入父级id"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="分类名称" prop="name">
        <el-input
          style="width: 240px"
          v-model="queryParams.name"
          placeholder="请输入分类名称"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="显示顺序" prop="orderNum">
        <el-input
          style="width: 240px"
          v-model="queryParams.orderNum"
          placeholder="请输入显示顺序"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="祖级列表" prop="ancestors">
        <el-input
          style="width: 240px"
          v-model="queryParams.ancestors"
          placeholder="请输入祖级列表"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="创建时间" prop="createTime">
        <el-date-picker
          style="width: 240px"
          clearable
          v-model="queryParams.createTime"
          type="date"
          value-format="YYYY-MM-DD"
          placeholder="请选择创建时间"
        >
        </el-date-picker>
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

    <el-table
      ref="tableRef"
      stripe
      height="300px"
      v-loading="loading"
      :data="dataList"
      highlight-current-row
      row-key="id"
      @current-change="handleCurrentChange"
    >
      <el-table-column label="ID" align="center" prop="id" />
      <el-table-column label="工作区id" align="center" prop="workspaceId">
        <template #default="scope">
          {{ scope.row.workspaceId || "-" }}
        </template>
      </el-table-column>
      <el-table-column label="父级id" align="center" prop="parentId">
        <template #default="scope">
          {{ scope.row.parentId || "-" }}
        </template>
      </el-table-column>
      <el-table-column label="分类名称" align="center" prop="name">
        <template #default="scope">
          {{ scope.row.name || "-" }}
        </template>
      </el-table-column>
      <el-table-column label="显示顺序" align="center" prop="orderNum">
        <template #default="scope">
          {{ scope.row.orderNum || "-" }}
        </template>
      </el-table-column>
      <el-table-column label="祖级列表" align="center" prop="ancestors">
        <template #default="scope">
          {{ scope.row.ancestors || "-" }}
        </template>
      </el-table-column>
      <el-table-column label="创建人" align="center" prop="createBy">
        <template #default="scope">
          {{ scope.row.createBy || "-" }}
        </template>
      </el-table-column>
      <el-table-column
        label="创建时间"
        align="center"
        prop="createTime"
        width="180"
      >
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, "{y}-{m}-{d}") }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark">
        <template #default="scope">
          {{ scope.row.remark || "-" }}
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />

    <template #footer>
      <div class="dialog-footer">
        <el-button size="mini" @click="cancel">取 消</el-button>
        <el-button type="primary" size="mini" @click="confirm">
          确 定
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup name="CategorySingle">
import { listCategory } from "@/api/kg/knowledge/category";
import { ref } from "vue";
const { proxy } = getCurrentInstance();

const dataList = ref([]);
const loading = ref(true);
const showSearch = ref(true);
const total = ref(0);
const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    workspaceId: null,
    parentId: null,
    name: null,
    orderNum: null,
    ancestors: null,
    createTime: null,
  },
});
const { queryParams, form } = toRefs(data);

// -------------------------------------------
const visible = ref(false);
// 定义单选数据
const single = ref();
// 当前界面table
const tableRef = ref();

const emit = defineEmits(["open", "confirm", "cancel"]);

/** 单选选中事件 */
function handleCurrentChange(selection) {
  if (selection) {
    single.value = selection;
  }
}

/**
 * 设置当前行
 * @param {Object} row 行对象
 * @returns 更改选中对象
 */
function setCurrentRow(row) {
  if (row) {
    let data = dataList.value.filter((item) => item.id == row.id);
    tableRef.value?.setCurrentRow(data[0]);
  }
}

/**
 * 打开选择框
 * @param {Array} val 选中的对象数组
 */
function open(val) {
  visible.value = true;
  single.value = val;
  resetQuery();
  getList();
}

/**
 * 取消按钮
 * @description 取消按钮时，重置所有状态
 */
function cancel() {
  queryParams.value.pageNum = 1;
  proxy.resetForm("queryRef");
  visible.value = false;
}

/**
 * 确定按钮
 * @description 确定按钮时，emit confirm 事件，以便父组件接收到选中的数据
 */
function confirm() {
  if (!single.value) {
    proxy.$modal.msgWarning("请选择数据！");
    return;
  }
  emit("confirm", single.value);
  visible.value = false;
}

/** 查询字典类型列表 */
function getList() {
  loading.value = true;
  listCategory(
    proxy.addDateRange(queryParams.value, daterangeCreateTime.value)
  ).then(async (response) => {
    dataList.value = response.data.rows;
    total.value = response.data.total;
    loading.value = false;
    // 初始化及分页切换选中逻辑
    await nextTick();
    setCurrentRow(single.value);
  });
}

/** 搜索按钮操作 */
function handleQuery() {
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef");
  queryParams.value.pageNum = 1;
  handleQuery();
}

defineExpose({ open });
</script>
