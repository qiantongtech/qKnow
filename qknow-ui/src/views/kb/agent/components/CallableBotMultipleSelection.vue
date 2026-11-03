<template>
  <el-dialog
    v-model="visible"
    :title="single ? '选择 Agent / 工作流' : 'Agent / 工作流-多选'"
    width="1200px"
    draggable
    destroy-on-close
    @close="cancel"
  >
    <el-form
      ref="queryRef"
      class="btn-style"
      :model="queryParams"
      :inline="true"
      @submit.prevent
    >
      <el-form-item label="名称" prop="name">
        <el-input
          v-model="queryParams.name"
          clearable
          placeholder="请输入名称"
          style="width: 240px"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button
          plain
          type="primary"
          @click="handleQuery"
          @mousedown="(event) => event.preventDefault()"
        >
          <i class="iconfont-mini icon-a-zu22377 mr5"></i>查询
        </el-button>
        <el-button
          @click="resetQuery"
          @mousedown="(event) => event.preventDefault()"
        >
          <i class="iconfont-mini icon-a-zu22378 mr5"></i>重置
        </el-button>
      </el-form-item>
    </el-form>

    <el-table
      ref="multipleTableRef"
      v-loading="loading"
      :data="callableBotList"
      :default-sort="defaultSort"
      reserve-selection
      row-key="id"
      stripe
      @selection-change="handleSelectionChange"
      @row-click="handleRowClick"
      @sort-change="handleSortChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column
        label="编码"
        prop="id"
        width="80"
        align="center"
        sortable="custom"
        :sort-orders="['descending', 'ascending']"
      />
      <el-table-column label="名称" prop="name" min-width="180">
        <template #default="{ row }">
          {{ row.name || "-" }}
        </template>
      </el-table-column>
      <el-table-column
        label="描述"
        prop="description"
        min-width="280"
        :show-overflow-tooltip="{ effect: 'light' }"
      >
        <template #default="{ row }">
          {{ row.description || "-" }}
        </template>
      </el-table-column>
      <el-table-column label="类型" prop="type" width="100" align="center">
        <template #default="scope">
          <div>
            <dict-tag :options="kg_bot_type" :value="scope.row.type" />
          </div>
        </template>
      </el-table-column>
      <el-table-column
        label="创建人"
        prop="createBy"
        width="120"
        align="center"
      >
        <template #default="{ row }">
          {{ row.createBy || "-" }}
        </template>
      </el-table-column>
      <el-table-column
        label="创建时间"
        prop="createTime"
        width="180"
        align="center"
        sortable="custom"
        :sort-orders="['descending', 'ascending']"
      >
        <template #default="{ row }">
          <span>{{ parseTime(row.createTime, "{y}-{m}-{d} {h}:{i}") }}</span>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      :total="total"
      @pagination="loadCallableBots"
    />

    <template #footer>
      <div class="dialog-footer">
        <el-button size="mini" @click="cancel">取 消</el-button>
        <el-button type="primary" size="mini" @click="confirm">确 定</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup name="CallableBotMultipleSelection">
import { listBot } from "@/api/kb/bot/bot";

const props = defineProps({
  single: {
    type: Boolean,
    default: false,
  },
});

const { proxy } = getCurrentInstance();
const emit = defineEmits(["open", "confirm", "cancel"]);
const { kg_bot_type, sys_is_or_not } = proxy.useDict(
  "kg_bot_type",
  "sys_is_or_not"
);
const visible = ref(false);
const loading = ref(false);
const callableBotList = ref([]);
const selectedCallableBots = ref([]);
const total = ref(0);
const multipleTableRef = ref(null);
const syncingSelection = ref(false);
const ownerBotId = ref(null);
const defaultSort = ref({ prop: "createTime", order: "descending" });
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  name: null,
  types: [0, 2],
  excludeId: null,
  validFlag: true,
  orderByColumn: "createTime",
  isAsc: "desc",
});

function open(current = [], currentOwnerBotId = null) {
  selectedCallableBots.value = Array.isArray(current)
    ? [...current]
    : [current];
  ownerBotId.value = currentOwnerBotId;
  queryParams.excludeId = currentOwnerBotId;
  visible.value = true;
  loadCallableBots();
}

async function loadCallableBots() {
  loading.value = true;
  syncingSelection.value = true;
  try {
    const response = await listBot(queryParams);
    callableBotList.value = response?.data?.rows || [];
    total.value = response?.data?.total || 0;
    await syncTableSelection();
  } finally {
    syncingSelection.value = false;
    loading.value = false;
  }
}

async function syncTableSelection() {
  await nextTick();
  multipleTableRef.value?.clearSelection();
  callableBotList.value.forEach((callableBot) => {
    if (
      selectedCallableBots.value.some(
        (selected) => selected.id === callableBot.id
      )
    ) {
      multipleTableRef.value?.toggleRowSelection(callableBot, true);
    }
  });
}

function handleSelectionChange(selection) {
  if (syncingSelection.value) return;

  if (props.single) {
    const selected = selection[selection.length - 1];
    selectedCallableBots.value = selected ? [selected] : [];

    // Element Plus uses a checkbox column for the shared selector. Keep its
    // visual state in sync with the node's single-target contract instead of
    // merely dropping extra values when the dialog is confirmed.
    if (selection.length > 1) {
      syncingSelection.value = true;
      nextTick(() => {
        multipleTableRef.value?.clearSelection();
        if (selected) {
          multipleTableRef.value?.toggleRowSelection(selected, true);
        }
        syncingSelection.value = false;
      });
    }
    return;
  }

  const currentPageIds = new Set(callableBotList.value.map((item) => item.id));
  selectedCallableBots.value = [
    ...selectedCallableBots.value.filter(
      (item) => !currentPageIds.has(item.id)
    ),
    ...selection,
  ];
}

function handleRowClick(row, column) {
  if (column?.type === "selection") return;
  const selected = selectedCallableBots.value.some(
    (item) => item.id === row.id
  );
  multipleTableRef.value?.toggleRowSelection(row, !selected);
}

function handleQuery() {
  queryParams.pageNum = 1;
  loadCallableBots();
}

function resetQuery() {
  proxy.resetForm("queryRef");
  queryParams.pageNum = 1;
  queryParams.types = [0, 2];
  queryParams.validFlag = true;
  queryParams.excludeId = ownerBotId.value;
  loadCallableBots();
}

function handleSortChange(column) {
  queryParams.orderByColumn = column.prop;
  queryParams.isAsc = column.order === "ascending" ? "asc" : "desc";
  loadCallableBots();
}

function cancel() {
  visible.value = false;
  emit("cancel");
}

function confirm() {
  if (selectedCallableBots.value.length === 0) {
    proxy.$modal.msgWarning("未选择数据！");
    return;
  }
  const result = props.single
    ? [selectedCallableBots.value[selectedCallableBots.value.length - 1]]
    : [...selectedCallableBots.value];
  emit("confirm", result);
  visible.value = false;
}

defineExpose({ open });
</script>
