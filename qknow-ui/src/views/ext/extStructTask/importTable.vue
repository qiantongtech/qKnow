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
  <!-- 导入表 -->
  <el-dialog
    title="导入表"
    v-model="visible"
    width="1000px"
    top="5vh"
    :append-to="$refs['app-container']"
    draggable
    destroy-on-close
    @close="handleClose"
  >
    <el-form :model="queryParams" ref="queryRef" :inline="true">
      <el-form-item label="表名称" prop="tableName">
        <el-input
          v-model="queryParams.tableName"
          placeholder="请输入表名称"
          clearable
          class="el-form-input-width"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <!-- <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
                  <el-button icon="Refresh" @click="resetQuery">重置</el-button> -->
        <el-button type="primary" @click="handleQuery">
          <i class="iconfont-mini icon-a-zu22377 mr5"></i>查询
        </el-button>
        <el-button @click="resetQuery">
          <i class="iconfont-mini icon-a-zu22378 mr5"></i>重置
        </el-button>
      </el-form-item>
    </el-form>
    <el-table
      @row-click="clickRow"
      v-loading="loading"
      ref="table"
      :data="dbTableList"
      @selection-change="handleSelectionChange"
      height="480px"
    >
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column
        prop="tableName"
        label="表名称"
        :show-overflow-tooltip="true"
      ></el-table-column>
      <el-table-column
        prop="tableComment"
        label="表描述"
        :show-overflow-tooltip="true"
      ></el-table-column>
      <!--            <el-table-column prop="createTime" label="创建时间"></el-table-column>-->
      <!--            <el-table-column prop="updateTime" label="更新时间"></el-table-column>-->
    </el-table>
    <pagination
      v-show="total > 0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="changePage"
    />
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="visible = false">取 消</el-button>
        <el-button type="primary" @click="handleImportTable">确 定</el-button>
      </div>
    </template>
  </el-dialog>
</template>
  
  <script setup>
import { getDaDatasourceTableList } from "@/api/da/datasource/daDatasource";
const total = ref(0);
const visible = ref(false);
const tables = ref([]);
const dbTableList = ref([]);
const { proxy } = getCurrentInstance();
const loading = ref(true);

const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  tableName: undefined,
  tableComment: undefined,
});

const emit = defineEmits(["ok", "importDataTable", "close-dialog"]);

const handleClose = () => {
  emit("close-dialog");
};

const dataSourceId = ref(null);
/** 查询参数列表 */
function show(val) {
  console.log("---导入表----val----", val);
  dataSourceId.value = val.dataSourceId;
  getList();
  visible.value = true;
}

/** 单击选择行 */
function clickRow(row) {
  proxy.$refs.table.toggleRowSelection(row);
}

/** 多选框选中数据 */
function handleSelectionChange(selection) {
  tables.value = selection.map((item) => item.tableName);
}

const dataList = ref([]);
/** 查询表数据 */
function getList(val) {
  loading.value = true;
  getDaDatasourceTableList(dataSourceId.value).then((res) => {
    dataList.value = res.data;
    total.value = res.data.length;
    changePage();
    loading.value = false;
  });
}

//切换分页
const changePage = () => {
  let startIdx = (queryParams.pageNum - 1) * queryParams.pageSize;
  let endIdx = startIdx + queryParams.pageSize;
  dbTableList.value = dataList.value.slice(startIdx, endIdx);
};

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef");
  handleQuery();
}

/** 导入按钮操作 */
function handleImportTable() {
  console.log("---导入表----tables----", tables);
  const tableNames = tables.value.join(",");
  if (tableNames == "") {
    proxy.$modal.msgError("请选择要导入的表");
    return;
  }
  let dataTableList = [];
  dbTableList.value.forEach((e) => {
    e.tableName;
  });
  dataTableList = dbTableList.value.filter((e) =>
    tables.value.includes(e.tableName)
  );
  console.log("-------dataTableList----", dataTableList);
  emit("importDataTable", dataTableList);
  visible.value = false;
  emit("ok");
}

defineExpose({
  show,
});
</script>
  