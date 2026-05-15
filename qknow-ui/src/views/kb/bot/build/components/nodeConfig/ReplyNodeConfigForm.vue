<template>
  <div class="reply-config-panel">
    <div class="reply-config-section">
      <div class="reply-config-section-header">
        <div class="reply-config-section-title node-config-section-title">
          <span class="blue-bar"></span> 输出
        </div>
        <el-button type="primary" plain @click="emit('addOutput')">
          <i class="iconfont-mini icon-xinzeng mr5"></i>新增
        </el-button>
      </div>

      <el-table
        :data="outputs"
        row-key="id"
        class="reply-output-table"
        table-layout="fixed"
        stripe
        empty-text="暂无输出变量"
      >
        <el-table-column label="变量名" min-width="100" align="center">
          <template #default="{ row }">
            <el-input
              :model-value="row.name"
              class="reply-output-field"
              placeholder="变量名"
              @update:model-value="updateOutput(row.id, { name: $event })"
            />
          </template>
        </el-table-column>

        <el-table-column label="变量值" min-width="160" align="center">
          <template #default="{ row }">
            <el-select
              :model-value="getBindingKey(row)"
              class="reply-output-select"
              popper-class="dydome-square-popper"
              filterable
              clearable
              placeholder="变量值"
              @update:model-value="handleBindingChange(row.id, $event)"
            >
              <template #label="{ value }">
                <el-tag
                  v-if="getContextOptionByBindingKey(value)"
                  disable-transitions
                  effect="light"
                  size="small"
                  class="reply-output-select-tag"
                  :title="
                    getContextOptionTagLabel(
                      getContextOptionByBindingKey(value)
                    )
                  "
                >
                  {{
                    getContextOptionTagLabel(
                      getContextOptionByBindingKey(value)
                    )
                  }}
                </el-tag>
              </template>
              <el-option-group
                v-for="group in contextGroups"
                :key="group.nodeId"
                :label="group.nodeLabel || '上游节点'"
              >
                <el-option
                  v-for="option in group.variables"
                  :key="getBindingKey(option)"
                  :label="getContextOptionDisplay(option)"
                  :value="getBindingKey(option)"
                >
                  <div class="reply-output-option">
                    <el-tag
                      disable-transitions
                      effect="light"
                      size="small"
                      class="reply-output-option-tag"
                      :title="getContextOptionTagLabel(option)"
                    >
                      {{ getContextOptionTagLabel(option) }}
                    </el-tag>
                  </div>
                </el-option>
              </el-option-group>
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
import { computed } from "vue";
import { Delete } from "@element-plus/icons-vue";

const props = defineProps({
  outputs: {
    type: Array,
    default: () => [],
  },
  availableContextVariableGroups: {
    type: Array,
    default: () => [],
  },
  hasUpstreamContextSources: {
    type: Boolean,
    default: false,
  },
});

const emit = defineEmits(["addOutput", "updateOutput", "removeOutput"]);

const contextGroups = computed(() =>
  Array.isArray(props.availableContextVariableGroups)
    ? props.availableContextVariableGroups.filter(
        (group) => Array.isArray(group?.variables) && group.variables.length
      )
    : []
);

const contextOptionMap = computed(() => {
  const optionMap = new Map();

  contextGroups.value.forEach((group) => {
    group.variables.forEach((option) => {
      const optionKey = getBindingKey(option);

      if (optionKey) {
        optionMap.set(optionKey, option);
      }
    });
  });

  return optionMap;
});

function getBindingKey(binding = {}) {
  const sourceNodeId = `${binding.sourceNodeId || ""}`.trim();
  const path = `${binding.path || binding.variableKey || ""}`.trim();

  return sourceNodeId && path ? `${sourceNodeId}::${path}` : "";
}

function getContextOptionByBindingKey(bindingKey = "") {
  return contextOptionMap.value.get(bindingKey) || null;
}

function getContextOptionTagLabel(option = {}) {
  return `${option.variableLabel || option.variableKey || "-"}`.trim() || "-";
}

function getContextOptionDisplay(option = {}) {
  const variableKey = `${
    option.variableLabel || option.variableKey || ""
  }`.trim();

  if (!variableKey) {
    return "";
  }

  return variableKey;
}

function updateOutput(id, patch = {}) {
  emit("updateOutput", { id, patch });
}

function handleBindingChange(id, bindingKey = "") {
  const option = contextOptionMap.value.get(bindingKey);

  if (!option) {
    updateOutput(id, {
      sourceNodeId: "",
      sourceNodeLabel: "",
      sourceNodeType: "",
      variableKey: "",
      variableLabel: "",
      valueType: "",
      path: "",
    });
    return;
  }

  updateOutput(id, {
    sourceNodeId: `${option.sourceNodeId || ""}`.trim(),
    sourceNodeLabel: `${option.sourceNodeLabel || ""}`.trim(),
    sourceNodeType: `${option.sourceNodeType || ""}`.trim(),
    variableKey: `${option.variableKey || ""}`.trim(),
    variableLabel: `${option.variableLabel || option.variableKey || ""}`.trim(),
    valueType: `${option.valueType || ""}`.trim(),
    path: `${option.path || option.variableKey || ""}`.trim(),
  });
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
.reply-config-panel {
  display: flex;
  flex-direction: column;
  gap: 18px;
}

.reply-config-section {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.reply-config-section-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
}

.reply-config-section-title {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  font-size: 16px;
  font-weight: 400;
  color: #111827;
  .blue-bar {
    background-color: #2666fb;
    width: 6px;
    height: 16px;
    // margin-right: 10px;
    border-radius: 10px;
  }
}

.reply-config-add-btn {
  width: 30px;
  height: 30px;
}

.reply-output-field,
.reply-output-select {
  width: 100%;
}

.reply-output-option {
  display: flex;
  align-items: center;
  justify-content: flex-start;
  gap: 12px;
}

:deep(.reply-output-option-tag),
:deep(.reply-output-select-tag) {
  --el-tag-bg-color: #eff4ff;
  --el-tag-border-color: transparent;
  --el-tag-text-color: #2563eb;
  max-width: 100%;
  height: 24px;
  line-height: 22px;
  padding: 0 10px;
  border-radius: 4px;
  border-color: transparent;
  font-size: 12px;
  font-weight: 500;
}

:deep(.reply-output-option-tag .el-tag__content),
:deep(.reply-output-select-tag .el-tag__content) {
  display: block;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.reply-output-select {
  :deep(.el-select__selected-item) {
    display: flex;
    align-items: center;
    min-width: 0;
    max-width: 100%;
  }

  :deep(.el-select__placeholder) {
    min-width: 0;
    max-width: 100%;
  }
}

.reply-output-table {
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
    padding: 12px 10px;
    text-align: center;
    border-bottom: 1px solid #ebeef5;
  }

  :deep(.el-table__body tr > td.el-table__cell) {
    background-color: #fff;
  }

  :deep(.el-table__body tr.el-table__row--striped > td.el-table__cell) {
    background-color: #fafafa;
  }

  :deep(.el-table__body tr:hover > td.el-table__cell) {
    background-color: #f2f6fc;
  }

  :deep(.cell) {
    width: 100%;
  }

  :deep(.el-input__wrapper),
  :deep(.el-select__wrapper) {
    box-shadow: 0 0 0 1px #e5e7eb inset;
  }

  :deep(.el-table__empty-block) {
    min-height: 72px;
  }

  :deep(.el-table__empty-text) {
    color: #6b7280;
    font-size: 13px;
  }
}
</style>
