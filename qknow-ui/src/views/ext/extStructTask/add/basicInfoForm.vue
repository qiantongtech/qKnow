<!-- BasicInfoForm.vue -->
<template>
  <el-form
    ref="formRef"
    :model="formData"
    :rules="rules"
    label-width="170px"
    style="padding-right: 90px"
    :disabled="disabled"
  >
    <div class="h2-titles">基础信息</div>
    <el-row :gutter="20">
      <el-col :span="11">
        <el-form-item label="任务名称" prop="name">
          <el-input v-model="formData.name" placeholder="请输入任务名称" />
        </el-form-item>
      </el-col>
      <el-col :span="9">
        <el-form-item label="数据源" prop="dataSourceId">
          <el-select
            @change="onDataSourceChange"
            v-model="formData.dataSourceId"
            placeholder="请选择"
          >
            <el-option
              v-for="item in dataSourceList"
              :key="item.id"
              :label="item.datasourceName"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="2">
        <div>
          <el-button type="primary" plain @click="onTestConnection">
            测试连接
          </el-button>
          <div v-if="connectionSuccess" class="success-icon">
            <el-icon style="color: white"><check /></el-icon>
          </div>
          <div v-if="connectionError" class="error-icon">
            <el-icon style="color: white"><close /></el-icon>
          </div>
        </div>
      </el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="11">
        <el-form-item label="更新类型" prop="updateType">
          <el-radio-group v-model="formData.updateType">
            <el-radio
              v-for="item in updateTypeOptions"
              :key="parseInt(item.value)"
              :label="parseInt(item.value)"
            >
              {{ item.label }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
      </el-col>
      <el-col :span="10">
        <el-form-item label="更新频率" prop="updateFrequency">
          <el-input
            v-model="formData.updateFrequency"
            placeholder="请输入cron执行表达式"
          >
            <template #append>
              <el-button
                type="primary"
                @click="onShowCron"
                style="background-color: #2666fb; color: #fff"
              >
                生成
                <i class="el-icon-time el-icon--right"></i>
              </el-button>
            </template>
          </el-input>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="24">
        <el-form-item label="备注" prop="remark">
          <el-input type="textarea" v-model="formData.remark" :rows="2" />
        </el-form-item>
      </el-col>
    </el-row>
  </el-form>
</template>
  
  <script setup>
import { ref, watch } from "vue";
import { Check, Close } from "@element-plus/icons-vue";

const props = defineProps({
  formData: {
    type: Object,
    required: true,
  },
  rules: {
    type: Object,
    default: () => ({}),
  },
  dataSourceList: {
    type: Array,
    default: () => [],
  },
  updateTypeOptions: {
    type: Array,
    default: () => [],
  },
  disabled: {
    type: Boolean,
    default: false,
  },
  connectionSuccess: Boolean,
  connectionError: Boolean,
});

const emit = defineEmits([
  "data-source-change",
  "test-connection",
  "show-cron-dialog",
  "update:formData",
]);

const formRef = ref();

// 监听formData变化，同步到父组件
watch(
  () => props.formData,
  (newVal) => {
    emit("update:formData", newVal);
  },
  { deep: true }
);

const onDataSourceChange = (value) => {
  emit("data-source-change", value);
};

const onTestConnection = () => {
  emit("test-connection");
};

const onShowCron = () => {
  emit("show-cron-dialog");
};

// 暴露方法给父组件
defineExpose({
  validate: () => formRef.value?.validate(),
  validateField: (props) => formRef.value?.validateField(props),
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

.success-icon,
.error-icon {
  margin: -28.5px 0px 0px 102px;
  transform: translateY(2px);
  width: 20px;
  height: 20px;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
}
.success-icon {
  background-color: #0b930b;
}
.error-icon {
  background-color: rgba(246, 2, 2, 0.97);
}
</style>