<template>
  <div class="condition-editor">
    <div class="condition-editor-header">
      <div>
        <div class="condition-editor-title node-config-section-title">分支条件</div>
        <div class="condition-editor-tip">
          每个分支右侧都有独立连接点，最后一个分支固定为 ELSE。
        </div>
      </div>
      <el-button link type="primary" @click="$emit('addCase')">新增分支</el-button>
    </div>

    <div
      v-for="(caseItem, index) in cases"
      :key="caseItem.id"
      class="condition-case-card"
    >
      <div class="condition-case-header">
        <div class="condition-case-title">
          <span>{{ getCaseLabel(index) }}</span>
          <span class="condition-case-tag">{{
            getBranchLabel(index, cases.length)
          }}</span>
        </div>
        <el-button
          v-if="!caseItem.isElse"
          link
          type="danger"
          @click="$emit('removeCase', caseItem.id)"
        >
          删除
        </el-button>
      </div>

      <el-input
        v-if="!caseItem.isElse"
        :model-value="caseItem.expression"
        placeholder="例如：变量 > 10"
        @update:model-value="
          $emit('updateCase', { id: caseItem.id, expression: $event })
        "
      />
      <div v-else class="condition-case-placeholder">
        未命中以上条件时，进入该兜底分支。
      </div>
    </div>
  </div>
</template>

<script setup>
defineProps({
  cases: {
    type: Array,
    default: () => [],
  },
});

defineEmits(["addCase", "removeCase", "updateCase"]);

function getCaseLabel(index) {
  return `CASE ${index + 1}`;
}

function getBranchLabel(index, total) {
  if (index === 0) return "IF";
  if (index === total - 1) return "ELSE";
  return "ELIF";
}
</script>

<style scoped lang="scss">
.condition-editor {
  display: flex;
  flex-direction: column;
  gap: 12px;
}
.condition-editor-header {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 12px;
  margin-bottom: 4px;
}
.condition-editor-title {
  font-size: 14px;
  font-weight: 400;
  color: #111827;
}
.condition-editor-tip {
  margin-top: 4px;
  font-size: 12px;
  line-height: 1.5;
  color: #6b7280;
}
.condition-case-card {
  padding: 12px;
  border: 1px solid #e5e7eb;
  border-radius: 12px;
  background-color: #f9fafb;
}
.condition-case-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  margin-bottom: 10px;
}
.condition-case-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 13px;
  font-weight: 600;
  color: #111827;
}
.condition-case-tag {
  padding: 2px 8px;
  border-radius: 999px;
  background-color: #eff6ff;
  color: #2563eb;
  font-size: 11px;
  font-weight: 700;
}
.condition-case-placeholder {
  padding: 10px 12px;
  border-radius: 8px;
  background-color: #fff;
  color: #6b7280;
  font-size: 12px;
  line-height: 1.6;
}
</style>
