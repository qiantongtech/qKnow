<template>
  <div class="start-config-panel">
    <div class="start-config-section">
      <div class="start-config-section-header">
        <div>
          <div class="start-config-section-title node-config-section-title">
            <span class="blue-bar"></span> 输入变量
          </div>
          <div class="start-config-section-tip">
            配置进入工作流前可用的输入变量。
          </div>
        </div>
        <el-button type="primary" plain @click="$emit('addField')">
          <i class="iconfont-mini icon-xinzeng mr5"></i>新增
        </el-button>
      </div>

      <el-table
        :data="fields"
        row-key="id"
        class="start-field-table"
        table-layout="fixed"
        stripe
        empty-text="当前没有输入变量，请添加。"
      >
        <el-table-column prop="name" label="变量名称" align="center" />

        <el-table-column prop="label" label="显示名称" align="center" />
        <el-table-column prop="typeLabel" label="字段类型" align="center" />
        <el-table-column label="操作" width="110" align="center">
          <template #default="{ row }">
            <div v-if="row.readonly" class="start-field-table-readonly">
              默认变量
            </div>
            <div v-else class="start-field-table-actions">
              <el-button
                link
                type="primary"
                @click.stop="$emit('editField', row)"
              >
                修改
              </el-button>
              <el-button
                link
                type="danger"
                @click.stop="handleRemoveField(row.id)"
              >
                删除
              </el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup>
defineProps({
  fields: {
    type: Array,
    default: () => [],
  },
});

const emit = defineEmits(["addField", "editField", "removeField"]);

function handleRemoveField(fieldId) {
  ElMessageBox.confirm("确认删除该输入字段吗？", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  })
    .then(() => {
      emit("removeField", fieldId);
      ElMessage({
        type: "success",
        message: "删除成功",
      });
    })
    .catch(() => {});
}
</script>

<style scoped lang="scss">
.start-config-panel {
  display: flex;
  flex-direction: column;
  gap: 18px;
}

.start-config-section {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.start-config-section-header {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 12px;
}

.start-config-section-title {
  font-size: 16px;
  font-weight: 400;
  color: #111827;
}

.start-config-section-tip {
  margin-top: 4px;
  font-size: 12px;
  color: #6b7280;
  line-height: 1.6;
}
.start-field-table {
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
    display: flex;
    align-items: center;
    justify-content: center;
    width: 100%;
    white-space: normal;
    word-break: break-word;
    line-height: 1.5;
  }

  :deep(.el-table__empty-block) {
    min-height: 72px;
  }

  :deep(.el-table__empty-text) {
    color: #6b7280;
    font-size: 13px;
  }
}

.start-field-table-readonly {
  font-size: 12px;
  color: #9ca3af;
}
.blue-bar {
  background-color: #2666fb;
  width: 6px;
  height: 16px;
  // margin-right: 10px;
  border-radius: 10px;
}
</style>
