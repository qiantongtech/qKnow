<!-- RelationMapping.vue -->
<template>
  <div>
    <!-- <div class="clearfix header-text">
      <div class="header-left">
        <div class="blue-bar"></div>
        关系映射
      </div>
    </div> -->
    <div class="h2-titles">关系映射</div>
    <div class="justify-between mb15">
      <el-row :gutter="15">
        <el-col :span="1.5" class="header-actions">
          <el-button type="primary" @click="addRelationItem" plain>
            新增关系映射
          </el-button>
        </el-col>
      </el-row>
    </div>

    <el-form
      ref="formRef"
      :model="formData"
      :rules="rules"
      :label-width="formLabelWidth"
      @submit.prevent
      v-loading="loading"
    >
      <div
        v-for="(data, index) in formData.dataList"
        :key="index"
        class="relation-item"
        :class="{
          'duplicate-error': duplicateStates[index]?.isDuplicate,
        }"
      >
        <!-- 关系类型 -->
        <el-row :gutter="20">
          <el-col :span="23">
            <el-form-item
              label="关系"
              :prop="`dataList[${index}].type`"
              :rules="rules.type"
            >
              <el-radio-group
                v-model="data.type"
                @change="handleRelationTypeChange(index, data.type)"
              >
                <el-radio
                  v-for="items in relationTypeOptions"
                  :key="parseInt(items.value)"
                  :label="parseInt(items.value)"
                >
                  {{ items.label }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="1">
            <el-button
              link
              icon="Delete"
              type="danger"
              @click="deleteItem(index, data)"
              plain
            >
            </el-button>
          </el-col>
        </el-row>

        <!-- 源表信息 -->
        <el-row :gutter="20">
          <el-col :span="10">
            <el-form-item
              label="源表"
              :prop="`dataList[${index}].source.tableName`"
              :rules="rules['source.tableName']"
            >
              <el-select
                @change="
                  handleTableChange(index, 'source', data.source.tableName)
                "
                v-model="data.source.tableName"
                placeholder="请选择源表"
                clearable
              >
                <el-option
                  v-for="item in tableData"
                  :key="item.tableName"
                  :label="item.tableName + '(' + item.tableComment + ')'"
                  :value="item.tableName"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item
              :prop="`dataList[${index}].source.fieldName`"
              :rules="rules['source.fieldName']"
            >
              <el-select
                v-model="data.source.fieldName"
                placeholder="请选择源表关联字段"
                :disabled="!data.source.tableName"
                clearable
              >
                <el-option
                  v-for="item in data.source.fields"
                  :key="item.field"
                  :label="item.field"
                  :value="item.field"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 关系描述 -->
        <el-row :gutter="20">
          <el-col :span="10">
            <el-form-item
              label="关系"
              :prop="`dataList[${index}].relation`"
              :rules="rules.relation"
            >
              <el-select
                v-model="data.relation"
                placeholder="请选择关系"
                :disabled="!data.source.tableName"
                clearable
              >
                <el-option
                  v-for="item in data.source.relations"
                  :key="item.id"
                  :label="item.relation"
                  :value="item.relation"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 中间表区域 -->
        <div v-if="data.type === 2">
          <!-- 添加中间表按钮 -->
          <el-row :gutter="20">
            <el-col :span="7">
              <el-form-item
                label="中间表"
                required
                :prop="`dataList[${index}].relationMappingMiddle`"
                :rules="[
                  {
                    validator: (rule, value, callback) => {
                      if (!value || value.length === 0) {
                        callback(new Error('请添加至少一个中间表'));
                      } else {
                        callback();
                      }
                    },
                    trigger: 'blur',
                  },
                ]"
              >
                <el-button
                  size="small"
                  type="primary"
                  @click="addIntermediateTable(index)"
                  plain
                >
                  添加中间表
                </el-button>
              </el-form-item>
            </el-col>
          </el-row>
          <!-- 中间表列表 -->
          <div
            v-for="(intermediate, iIndex) in data.relationMappingMiddle"
            :key="iIndex"
            class="intermediate-table-item"
          >
            <el-row :gutter="20">
              <el-col :span="8">
                <el-form-item
                  label=" "
                  class="no-required-star"
                  :prop="`dataList[${index}].relationMappingMiddle[${iIndex}].tableName`"
                  :rules="rules['intermediate.tableName']"
                >
                  <el-select
                    @change="
                      handleIntermediateTableChange(
                        index,
                        iIndex,
                        intermediate.tableName
                      )
                    "
                    v-model="intermediate.tableName"
                    placeholder="请选择中间表"
                    clearable
                  >
                    <el-option
                      v-for="item in dbTableList"
                      :key="item.tableName"
                      :label="item.tableName + '(' + item.tableComment + ')'"
                      :value="item.tableName"
                    >
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item
                  :prop="`dataList[${index}].relationMappingMiddle[${iIndex}].tableField`"
                  :rules="rules['intermediate.tableField']"
                >
                  <el-select
                    v-model="intermediate.tableField"
                    placeholder="连接源表"
                    :disabled="!intermediate.tableName"
                    clearable
                  >
                    <el-option
                      v-for="item in intermediate.fields"
                      :key="item.field"
                      :label="item.field"
                      :value="item.field"
                    >
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item
                  :prop="`dataList[${index}].relationMappingMiddle[${iIndex}].relationField`"
                  :rules="rules['intermediate.relationField']"
                >
                  <el-select
                    v-model="intermediate.relationField"
                    placeholder="连接目标表"
                    :disabled="!intermediate.tableName"
                    clearable
                  >
                    <el-option
                      v-for="item in intermediate.fields"
                      :key="item.field"
                      :label="item.field"
                      :value="item.field"
                    >
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="3">
                <el-button
                  link
                  icon="Delete"
                  type="danger"
                  @click="removeIntermediateTable(index, iIndex)"
                  plain
                  :disabled="data.relationMappingMiddle.length <= 1"
                >
                </el-button>
              </el-col>
            </el-row>
          </div>
        </div>

        <!-- 目标表信息 -->
        <el-row :gutter="20">
          <el-col :span="10">
            <el-form-item
              label="目标表"
              :prop="`dataList[${index}].target.tableName`"
              :rules="rules['target.tableName']"
            >
              <el-select
                @change="
                  handleTableChange(index, 'target', data.target.tableName)
                "
                v-model="data.target.tableName"
                placeholder="请选择目标表"
                :disabled="!data.source.tableName"
                clearable
              >
                <el-option
                  v-for="item in tableData"
                  :key="item.tableName"
                  :label="item.tableName + '(' + item.tableComment + ')'"
                  :value="item.tableName"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item
              :prop="`dataList[${index}].target.fieldName`"
              :rules="rules['target.fieldName']"
            >
              <el-select
                v-model="data.target.fieldName"
                placeholder="请选择目标表关联字段"
                :disabled="!data.target.tableName"
                clearable
              >
                <el-option
                  v-for="item in data.target.fields"
                  :key="item.field"
                  :label="item.field"
                  :value="item.field"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </div>
    </el-form>
  </div>
</template>
  
  <script setup>
import { ref, computed, watch } from "vue";

const props = defineProps({
  formData: {
    type: Object,
    required: true,
  },
  rules: {
    type: Object,
    required: true,
  },
  tableData: {
    type: Array,
    default: () => [],
  },
  dbTableList: {
    type: Array,
    default: () => [],
  },
  relationTypeOptions: {
    type: Array,
    default: () => [],
  },
  loading: {
    type: Boolean,
    default: false,
  },
  shouldShowFormLabels: {
    type: Boolean,
    default: false,
  },
});

const emit = defineEmits([
  "add-relation",
  "delete-relation",
  "relation-type-change",
  "table-change",
  "add-intermediate",
  "remove-intermediate",
  "intermediate-table-change",
  "update:formData",
]);

const formRef = ref();
const duplicateStates = ref({});

// 计算label宽度
const formLabelWidth = computed(() => {
  return props.shouldShowFormLabels ? "80px" : "0px";
});

// 监听formData变化，同步到父组件
watch(
  () => props.formData,
  (newVal) => {
    emit("update:formData", newVal);
    checkDuplicates();
  },
  { deep: true }
);

// 新增关系映射项
const addRelationItem = () => {
  emit("add-relation");
};

// 删除关系项
const deleteItem = (index, data) => {
  emit("delete-relation", index, data);
};

// 处理关系类型变化
const handleRelationTypeChange = (index, type) => {
  emit("relation-type-change", index, type);
};

// 处理表选择变化
const handleTableChange = (index, type, tableName) => {
  emit("table-change", index, type, tableName);
};

// 添加中间表
const addIntermediateTable = (index) => {
  emit("add-intermediate", index);
};

// 删除中间表
const removeIntermediateTable = (index, iIndex) => {
  emit("remove-intermediate", index, iIndex);
};

// 处理中间表选择变化
const handleIntermediateTableChange = (index, iIndex, tableName) => {
  emit("intermediate-table-change", index, iIndex, tableName);
};

// 检查重复项
const checkDuplicates = () => {
  clearDuplicateErrors();

  const duplicateIndices = new Set();
  const formData = props.formData.dataList;

  for (let i = 0; i < formData.length; i++) {
    for (let j = i + 1; j < formData.length; j++) {
      if (isDuplicateRelation(formData[i], formData[j])) {
        duplicateIndices.add(i);
        duplicateIndices.add(j);
      }
    }
  }

  // 标记重复项
  duplicateIndices.forEach((index) => {
    duplicateStates.value[index] = {
      isDuplicate: true,
      errorMessage: "存在重复的关系映射配置",
    };
  });
};

// 检查是否重复
const isDuplicateRelation = (item1, item2) => {
  if (!isCompleteRelation(item1) || !isCompleteRelation(item2)) {
    return false;
  }

  const basicFieldsMatch =
    item1.source.tableName === item2.source.tableName &&
    // item1.source.fieldName === item2.source.fieldName &&
    item1.relation === item2.relation &&
    item1.target.tableName === item2.target.tableName &&
    // item1.target.fieldName === item2.target.fieldName &&
    item1.type === item2.type;

  if (!basicFieldsMatch) return false;

  if (item1.type === 2 && item2.type === 2) {
    if (
      item1.relationMappingMiddle.length !== item2.relationMappingMiddle.length
    ) {
      return false;
    }

    const middleTablesMatch = item1.relationMappingMiddle.every(
      (middleTable, idx) => {
        const otherMiddleTable = item2.relationMappingMiddle[idx];
        return (
          middleTable.tableName === otherMiddleTable.tableName
          //    &&
          //   middleTable.tableField === otherMiddleTable.tableField &&
          //   middleTable.relationField === otherMiddleTable.relationField
        );
      }
    );

    return middleTablesMatch;
  }

  return true;
};

// 检查关系是否完整
const isCompleteRelation = (item) => {
  const basicComplete =
    item.source?.tableName &&
    // item.source?.fieldName &&
    item.relation &&
    item.target?.tableName;
  //  &&
  // item.target?.fieldName
  if (!basicComplete) return false;

  if (item.type === 2) {
    if (
      !item.relationMappingMiddle ||
      item.relationMappingMiddle.length === 0
    ) {
      return false;
    }

    const middleComplete = item.relationMappingMiddle.every(
      (middle) => middle.tableName
      //    && middle.tableField && middle.relationField
    );

    return middleComplete;
  }

  return true;
};

// 清除重复错误标记
const clearDuplicateErrors = () => {
  duplicateStates.value = {};
};

// 验证表单
const validate = () => {
  return formRef.value?.validate();
};

// 暴露方法给父组件
defineExpose({
  validate,
});
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
.clearfix {
  width: 100%;
  height: 36px;
  /* background-color: #f8f8f9; */
  display: flex;
  align-items: center;
  /* padding-left: 10px; */
}
.blue-bar {
  background-color: #2666fb;
  width: 5px;
  height: 20px;
  margin-right: 10px;
  border-radius: 2px;
}
.header-text {
  margin: 8px 0;
}
.header-left {
  font-size: 16px;
  color: rgba(0, 0, 0, 0.85);
  display: flex;
  align-items: center;
  font-weight: 500;
}
.header-actions {
  display: flex;
  align-items: center;
}

.relation-item {
  padding: 10px;
  background-color: #f0f2f5;
  margin-bottom: 10px;
}

.relation-item.duplicate-error {
  border: 2px solid #f56c6c;
  border-radius: 4px;
  padding: 15px;
  margin-bottom: 15px;
  position: relative;
}
.relation-item.duplicate-error::before {
  content: "⚠️ 存在重复配置";
  position: absolute;
  top: -10px;
  left: 10px;
  background: #f56c6c;
  color: white;
  padding: 2px 8px;
  border-radius: 3px;
  font-size: 12px;
  z-index: 1;
}

::v-deep .el-form-item__content {
  margin-left: 0px !important;
}
.no-required-star :deep(.el-form-item__label::before) {
  color: transparent !important;
  visibility: hidden;
}
</style>