<!-- TableMapping.vue -->
<template>
  <div>
    <div class="h2-titles">表映射</div>
    <div class="justify-between mb15">
      <el-row :gutter="15">
        <el-col :span="1.5" class="header-actions">
          <el-button type="primary" plain @click="onImportTable">
            导入表
          </el-button>
          <el-icon class="tip-icon" size="20">
            <InfoFilled />
          </el-icon>
          <span class="tip-text">注：导入表之前需确保数据库连接信息正确</span>
        </el-col>
      </el-row>
    </div>
    <el-table :data="tableData" max-height="410">
      <el-table-column prop="tableName" label="表名"></el-table-column>
      <el-table-column prop="tableComment" label="表显示名称">
        <template #default="scope">
          {{ scope.row.tableComment || "-" }}
        </template>
      </el-table-column>
      <el-table-column prop="operate" label="对应概念">
        <template #default="scope">
          {{ scope.row.operate || "-" }}
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" align="center" width="180">
        <template #default="scope">
          <dict-tag :options="ext_mapping_status" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column prop="operate" align="center" label="操作" width="180">
        <template #default="scope">
          <el-button
            link
            type="primary"
            icon="Edit"
            @click="onMappingClick(scope.row)"
          >
            映射
          </el-button>
          <el-button
            link
            type="danger"
            icon="Delete"
            @click="onDeleteClick(scope.row)"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>
  
  <script setup>
import { InfoFilled } from "@element-plus/icons-vue";
const { proxy } = getCurrentInstance();
const { ext_mapping_status, ext_update_type } = proxy.useDict(
  "ext_mapping_status",
  "ext_update_type"
);

const props = defineProps({
  tableData: {
    type: Array,
    default: () => [],
  },
  mappingStatusOptions: {
    type: Array,
    default: () => [],
  },
});

const emit = defineEmits(["import-table", "mapping-click", "delete-click"]);

const onImportTable = () => {
  emit("import-table");
};

const onMappingClick = (row) => {
  emit("mapping-click", row);
};

const onDeleteClick = (row) => {
  emit("delete-click", row);
};
</script>
  
  <style scoped  lang="scss">
.h2-titles {
  font-size: 16px;
  color: rgba(0, 0, 0, 0.85);
  display: flex;
  align-items: center;
  font-weight: 500;
  margin: 8px 0;
}
.h2-titles::before {
  display: inline-block;
  content: "";
  width: 6px;
  height: 16px;
  border-radius: 3px;
  background: var(--el-color-primary);
  margin-right: 8px;
}

.header-actions {
  display: flex;
  align-items: center;
}
.tip-icon {
  color: #909399;
  font-size: 16px;
  margin-left: 12px;
  margin-right: 2px;
}
.tip-text {
  color: #909399;
  font-size: 12px;
}
</style>