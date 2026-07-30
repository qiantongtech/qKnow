<!--
Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.

This file is part of qKnow Intelligent Agent Building Platform (Open Source Edition).

qKnow is licensed under Apache License 2.0 with additional qKnow terms.
You may use qKnow for commercial purposes, but you may not remove, hide,
modify, or replace the qKnow logo, copyright notices, license notices,
or attribution information without a separate commercial license.

White-label use, OEM distribution, rebranding, or presenting qKnow as
another product requires separate commercial authorization from
Jiangsu Qiantong Technology Co., Ltd.

Business License: https://community.qknow.ai/business/policy.html
See the LICENSE file in the project root for full license information.
-->

<template>
  <el-table stripe v-loading="loading" :data="mcpConfigList" @selection-change="handleSelectionChange"
            :default-sort="defaultSort" @sort-change="handleSortChange">
    <el-table-column label="编号" align="center" prop="id" width="60"/>
    <el-table-column label="名称" align="left" prop="name" width="300">
      <template #default="scope">
        {{ scope.row.name || '-' }}
      </template>
    </el-table-column>
    <el-table-column label="描述" align="left" prop="description">
      <template #default="scope">
        {{ scope.row.description || '-' }}
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
      @pagination="queryMcpToolPage"
  />
</template>

<script setup name="McpToolList">
import {mcpToolPage} from "@/api/kb/mcp/mcpTool";

const {proxy} = getCurrentInstance();

const mcpConfigList = ref([]);
const loading = ref(true);
const total = ref(0);
const defaultSort = ref({prop: "createTime", order: "desc"});

const data = reactive({
  mcpConfigDetail: {},
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10
  },
});

const {queryParams, form} = toRefs(data);

const props = defineProps({
  mcpId: {
    type: Number,
    required: true
  },
})

// 监听父组件传递的Bot列表变化
watch(
    () => props.mcpId,
    (newVal) => {
      if (newVal) {
        queryMcpToolPage();
      }
    },
    {immediate: true}
);

/** 查询mcp 配置列表 */
function queryMcpToolPage() {
  loading.value = true;
  queryParams.value.mcpId = props.mcpId;
  mcpToolPage(queryParams.value).then(response => {
    mcpConfigList.value = response.data.rows;
    total.value = response.data.total;
    loading.value = false;
  });
}

/** 排序触发事件 */
function handleSortChange(column, prop, order) {
  queryParams.value.orderByColumn = column.prop;
  queryParams.value.isAsc = column.order;
  queryMcpToolPage();
}

queryMcpToolPage();

</script>
