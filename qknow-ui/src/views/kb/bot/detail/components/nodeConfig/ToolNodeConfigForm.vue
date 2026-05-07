<template>
  <div class="tool-config-panel">
    <div class="tool-config-section">
      <div class="tool-config-section-title node-config-section-title">
        工具信息
      </div>
      <div class="tool-config-summary-card">
        <div class="tool-config-summary-row">
          <span class="tool-config-summary-label">工具名称</span>
          <span class="tool-config-summary-value">{{ toolName || "-" }}</span>
        </div>
        <div class="tool-config-summary-row" v-if="toolSource">
          <span class="tool-config-summary-label">来源</span>
          <span class="tool-config-summary-value">{{ toolSource }}</span>
        </div>
        <div class="tool-config-summary-row" v-if="toolId">
          <span class="tool-config-summary-label">工具 ID</span>
          <span class="tool-config-summary-value">{{ toolId }}</span>
        </div>
        <div class="tool-config-summary-desc" v-if="toolDescription">
          {{ toolDescription }}
        </div>
      </div>
    </div>

    <div class="tool-config-section">
      <div class="tool-config-section-title node-config-section-title">
        节点描述
      </div>
      <el-input
        :model-value="description"
        type="textarea"
        :autosize="{ minRows: 3, maxRows: 5 }"
        placeholder="请输入节点描述"
        @update:model-value="emitField('description', $event)"
      />
    </div>

    <div class="tool-config-section">
      <div class="tool-config-section-header">
        <div>
          <div class="tool-config-section-title node-config-section-title">
            输出变量
          </div>
          <div class="tool-config-section-tip">
            配置工具执行后可供下游节点引用的输出变量。
          </div>
        </div>
        <el-button type="primary" plain @click="$emit('addOutput')">
          <i class="iconfont-mini icon-xinzeng mr5"></i>新增
        </el-button>
      </div>

      <el-table
        :data="outputs"
        row-key="id"
        class="tool-output-table"
        table-layout="fixed"
        stripe
        empty-text="暂无输出变量"
      >
        <el-table-column label="变量名" min-width="120" align="center">
          <template #default="{ row }">
            <el-input
              :model-value="row.variableKey"
              class="tool-output-field"
              placeholder="变量名"
              @update:model-value="
                updateOutput(row.id, {
                  variableKey: $event,
                  path: $event,
                })
              "
            />
          </template>
        </el-table-column>

        <el-table-column label="显示名称" min-width="140" align="center">
          <template #default="{ row }">
            <el-input
              :model-value="row.variableLabel"
              class="tool-output-field"
              placeholder="显示名称"
              @update:model-value="
                updateOutput(row.id, { variableLabel: $event })
              "
            />
          </template>
        </el-table-column>

        <el-table-column label="类型" min-width="120" align="center">
          <template #default="{ row }">
            <el-select
              :model-value="row.valueType || 'string'"
              class="tool-output-select"
              popper-class="dydome-square-popper"
              @update:model-value="updateOutput(row.id, { valueType: $event })"
            >
              <el-option
                v-for="option in valueTypeOptions"
                :key="option.value"
                :label="option.label"
                :value="option.value"
              />
            </el-select>
          </template>
        </el-table-column>

        <el-table-column label="操作" width="88" align="center">
          <template #default="{ row }">
            <el-button
              link
              type="danger"
              @click.stop="handleRemoveOutput(row.id)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup>
const props = defineProps({
  toolId: {
    type: [String, Number],
    default: "",
  },
  toolName: {
    type: String,
    default: "",
  },
  toolSource: {
    type: String,
    default: "",
  },
  toolDescription: {
    type: String,
    default: "",
  },
  description: {
    type: String,
    default: "",
  },
  outputs: {
    type: Array,
    default: () => [],
  },
});

const emit = defineEmits([
  "updateField",
  "addOutput",
  "updateOutput",
  "removeOutput",
]);

const valueTypeOptions = [
  { label: "字符串", value: "string" },
  { label: "数字", value: "number" },
  { label: "布尔", value: "boolean" },
  { label: "对象", value: "object" },
  { label: "数组", value: "array" },
];

function emitField(field, value) {
  emit("updateField", { field, value });
}

function updateOutput(id, patch = {}) {
  emit("updateOutput", { id, patch });
}

function handleRemoveOutput(outputId) {
  ElMessageBox.confirm("确认删除该输出变量吗？", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  })
    .then(() => {
      emit("removeOutput", outputId);
      ElMessage({
        type: "success",
        message: "删除成功",
      });
    })
    .catch(() => {});
}
</script>

<style scoped lang="scss">
.tool-config-panel {
  display: flex;
  flex-direction: column;
  gap: 18px;
}

.tool-config-section {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.tool-config-section-header {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 12px;
}

.tool-config-section-title {
  font-size: 16px;
  font-weight: 400;
  color: #111827;
}

.tool-config-section-tip {
  margin-top: 4px;
  font-size: 12px;
  color: #6b7280;
  line-height: 1.6;
}

.tool-config-summary-card {
  padding: 14px 16px;
  border: 1px solid #e5e7eb;
  border-radius: 12px;
  background: linear-gradient(180deg, #ffffff 0%, #f8fafc 100%);
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.tool-config-summary-row {
  display: flex;
  align-items: center;
  gap: 12px;
}

.tool-config-summary-label {
  flex-shrink: 0;
  width: 60px;
  font-size: 12px;
  font-weight: 600;
  color: #6b7280;
}

.tool-config-summary-value {
  min-width: 0;
  font-size: 13px;
  color: #111827;
  word-break: break-word;
}

.tool-config-summary-desc {
  font-size: 12px;
  line-height: 1.7;
  color: #6b7280;
}

.tool-output-table {
  width: 100%;

  :deep(.el-table__body-wrapper) {
    overflow-x: hidden;
  }

  :deep(.el-table__inner-wrapper::before) {
    display: none;
  }

  :deep(th.el-table__cell) {
    background-color: #f5f7fa;
    color: #5f6b7a;
    font-size: 12px;
    font-weight: 600;
  }

  :deep(td.el-table__cell),
  :deep(th.el-table__cell) {
    padding: 12px 0;
    text-align: center;
    border-bottom: 1px solid #ebeef5;
  }

  :deep(.cell) {
    width: 100%;
  }

  :deep(.el-table__empty-text) {
    color: #6b7280;
    font-size: 13px;
  }
}

.tool-output-field,
.tool-output-select {
  width: 100%;
}
</style>
