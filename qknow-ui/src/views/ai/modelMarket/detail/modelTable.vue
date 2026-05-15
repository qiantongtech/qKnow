<template>
  <el-table stripe
            v-loading="loading"
            :data="modelList"
            @sort-change="handleSortChange"
            :default-sort="defaultSort">
    <el-table-column label="编号" align="center" prop="id" width="85" sortable="custom"
                     :sort-orders="['descending', 'ascending']">
      <template #default="scope">
        {{ scope.row.id || '-' }}
      </template>
    </el-table-column>
    <el-table-column label="模型名称" align="center" prop="name"sortable="custom"
                     :sort-orders="['descending', 'ascending']">
      <template #default="scope">
        {{ scope.row.name || '-' }}
      </template>
    </el-table-column>
    <el-table-column label="模型类型" align="center" prop="type">
      <template #default="scope">
        <dict-tag :options="ai_model_tag" :value="scope.row.type"/>
      </template>
    </el-table-column>
    <el-table-column label="启用/禁用" align="center" prop="isEnable">
      <template #default="scope">
        <el-switch v-model="scope.row.status"
                   :active-value=1
                   :inactive-value=0
                   @change="changeModel(scope.row.status,scope.row)"/>
      </template>
    </el-table-column>
    <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right" width="240">
      <template #default="scope">
        <el-button link type="primary"
                   icon="Edit"
                   :disabled=true>编辑
        </el-button>

        <el-button link type="danger"
                   icon="Delete"
                   :disabled=true
        >删除
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
      v-show="total > 0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="showModelList"
  />
</template>

<script setup>
import {changeModelEnable, getModelPage} from "@/api/ai/myModel/myModel";

const loading = ref(true);
const data = reactive({
  modelList: [],
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    workspaceId: null,
    code: null,
    name: null,
    description: null,
    createTime: null,
    orderByColumn: "createTime",
    isAsc: "desc",
  },
  rules: {
    code: [{required: true, message: "标识不能为空", trigger: "blur"}],
    name: [{required: true, message: "名称不能为空", trigger: "blur"}],
    // description: [{ required: true, message: "描述不能为空", trigger: "blur" }],
  },
});
const {proxy} = getCurrentInstance();
const {
  ai_model_tag
} = proxy.useDict('ai_model_tag');

const {modelList} = toRefs(data);
const total = ref(0);
const {queryParams, form, rules} = toRefs(data);
const keyId = ref(0);
const defaultSort = ref({prop: "createTime", order: "desc"});

const props = defineProps({
  keyId: {
    type: Number,
    default: null
  }
});

/** 模型管理操作 */
function showModelList(id) {
  loading.value = true;
  queryParams.value.keyId = keyId.value;
  getModelPage(queryParams.value).then(response => {
    loading.value = false;
    modelList.value = response.data.list;
    total.value = response.data.total;
  }).catch(() => {
    loading.value = false;
  });
}

/**
 * 大模型管理
 * @param isEnable 是否启用
 * @param row 行数据
 */
function changeModel(isEnable, row) {
  let data = {
    "keyId": props.keyId,
    "name": row.name,
    "status": isEnable
  }

  const text = isEnable === 0 ? "关闭" : "启用";
  proxy.$modal.confirm('确认"' + text + '""' + row.name + '"模型吗?').then(function () {
    return changeModelEnable(data);
  }).then(() => {
    proxy.$modal.msgSuccess(text + "成功");
  }).catch(function () {
    row.status = row.status === 0 ? 1 : 0;
  });
}

/** 排序触发事件 */
function handleSortChange(column, prop, order) {
  queryParams.value.orderByColumn = column.prop;
  queryParams.value.isAsc = column.order;
  showModelList();
}

watch(
    () => props.keyId,
    (val) => {
      if (val) {
        keyId.value = val;
        showModelList();
      }
    },
    {immediate: true}
);
</script>

<style scoped lang="scss">

</style>
