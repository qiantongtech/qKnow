<template>
  <div class="loop-config-panel">
    <div class="loop-config-title node-config-section-title">循环设置</div>
    <el-form label-position="top" size="default">
      <div class="loop-settings-grid">
        <div class="loop-settings-item">
          <div class="loop-settings-label">循环变量</div>
          <el-input
            :model-value="itemAlias"
            placeholder="例如：item"
            @update:model-value="emitField('itemAlias', $event)"
          />
        </div>
        <div class="loop-settings-item">
          <div class="loop-settings-label">最大迭代次数</div>
          <el-input-number
            :model-value="maxIterations"
            :min="1"
            :max="999"
            controls-position="right"
            style="width: 100%"
            @update:model-value="emitField('maxIterations', $event)"
          />
        </div>
      </div>
    </el-form>
  </div>
</template>

<script setup>
defineProps({
  itemAlias: {
    type: String,
    default: "",
  },
  maxIterations: {
    type: Number,
    default: 1,
  },
});

const emit = defineEmits(["updateField"]);

function emitField(field, value) {
  emit("updateField", { field, value });
}
</script>

<style scoped lang="scss">
.loop-config-panel {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.loop-settings-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 12px;
}
.loop-settings-item {
  display: flex;
  flex-direction: column;
  gap: 8px;
}
.loop-settings-label {
  font-size: 12px;
  font-weight: 600;
  color: #374151;
}
</style>
