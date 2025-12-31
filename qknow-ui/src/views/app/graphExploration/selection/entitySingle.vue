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
  <el-dialog title="实体-单选" v-model="visible" width="1200px" :append-to="$refs['app-container']" draggable destroy-on-close @close="cancel">
    <el-form class="btn-style" :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="实体名称" prop="name">
        <el-input style="width: 240px" v-model="queryParams.name" placeholder="请输入参数名称" clearable @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button plain type="primary" @click="handleQuery" @mousedown="(e) => e.preventDefault()"> <i class="iconfont-mini icon-a-zu22377 mr5"></i>查询 </el-button>
        <el-button @click="resetQuery" @mousedown="(e) => e.preventDefault()"> <i class="iconfont-mini icon-a-zu22378 mr5"></i>重置 </el-button>
      </el-form-item>
    </el-form>

    <el-table ref="tableRef" stripe height="400px" v-loading="loading" :data="dataList" highlight-current-row row-key="id" @current-change="handleCurrentChange">
      <el-table-column label="名称" align="center" prop="name">
        <template #default="scope">
          {{ scope.row.name || "-" }}
        </template>
      </el-table-column>
      <el-table-column label="描述" align="center" prop="description">
        <template #default="scope">
          {{ scope.row.description || "-" }}
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, "{y}-{m}-{d}") }}</span>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />

    <template #footer>
      <div class="dialog-footer">
        <el-button size="mini" @click="cancel">取 消</el-button>
        <el-button type="primary" size="mini" @click="confirm"> 确 定 </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup name="ParameterSingle">
import { ref } from "vue";
import { getGraphPage } from "@/api/app/graph";
const { proxy } = getCurrentInstance();

const dataList = ref([]);
const loading = ref(true);
const showSearch = ref(true);
const total = ref(0);
const props = defineProps({
  graphId: {
    default: {},
  },
  entityType: {
    default: null,
  },
});
const id = computed(() => props.graphId.id);
const entityType = computed(() => props.graphId.pageType);
const data = reactive({
  form: {},
  queryParams: {
    graphId: id.value,
    entityType: entityType.value,
    pageNum: 1,
    pageSize: 10,
    name: null,
  },
});
const { queryParams } = toRefs(data);

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
  getGraphPage(queryParams.value).then(async (response) => {
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
