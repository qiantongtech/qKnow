<!--
 Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
 
 This file is part of qKnow Intelligent Agent Building Platform (Open Source Edition).
 
 qKnow is licensed under Apache License 2.0 with additional qKnow terms.
 You may use qKnow for commercial purposes, but you may not remove, hide,
 modify, or replace the qKnow logo, copyright notices, license notices,
 or attribution information without a separate commercial license.
 
 White-label use, OEM distribution, rebranding, or presenting qKnow as
 another product requires separate commercial authorization from
 Jiangsu Qiantong Technology Co., Ltd.
 
 Business License: https://community.qknow.ai/business/policy.html
 See the LICENSE file in the project root for full license information.
-->

<template>
  <el-form
    ref="formRef"
    :model="form"
    :rules="rules"
    :validate-on-rule-change="false"
    label-width="170px"
    class="storage-document-form"
    @submit.prevent
  >
    <el-row :gutter="20">
      <el-col :span="12">
        <el-form-item label="所属知识库" prop="knowledgeBaseId">
          <el-select
            v-model="form.knowledgeBaseId"
            :validate-event="false"
            disabled
            placeholder="请选择知识库"
          >
            <el-option
              v-for="item in knowledgeBaseList"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
      </el-col>

      <el-col :span="12">
        <el-form-item label="所属分类" prop="categoryId">
          <el-tree-select
            v-model="form.categoryId"
            :data="categoryOptions"
            :props="{ value: 'id', label: 'label', children: 'children' }"
            value-key="id"
            :validate-event="false"
            placeholder="请选择所属分类"
            check-strictly
            @change="handleCategoryChange"
          />
        </el-form-item>
      </el-col>

      <el-col v-if="hasSemiStructured" :span="12">
        <el-form-item label="数据风格" prop="jsonStyle">
          <div class="json-style-field">
            <el-select
              v-model="form.jsonStyle"
              :validate-event="false"
              filterable
              clearable
              placeholder="请选择数据风格"
            >
              <el-option
                v-for="item in jsonStyleOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
            <div class="field-tip">
              <el-icon><InfoFilled /></el-icon>
              <span
                >所选数据风格应与 JSON/JSONL
                文件中的数据风格保持一致，否则文件会解析失败。</span
              >
            </div>
          </div>
        </el-form-item>
      </el-col>

      <el-col :span="24">
        <el-form-item label="文件描述" prop="description">
          <el-input
            v-model="form.description"
            :validate-event="false"
            type="textarea"
            :rows="3"
            maxlength="256个字符"
            placeholder="请输入文件描述"
            show-word-limit
            resize="vertical"
          />
        </el-form-item>
      </el-col>
    </el-row>
  </el-form>
</template>

<script setup name="StorageDocumentStep">
import { computed, onMounted, reactive, ref, watch } from "vue";
import { InfoFilled } from "@element-plus/icons-vue";
import { kmcCategoryTree } from "@/api/kmc/kmcCategory/kmcCategory.js";
import { getKmcKnowledgeBaseList } from "@/api/kmc/knowledgeBase/knowledgeBase.js";

const props = defineProps({
  knowledgeBaseId: {
    type: [Number, String],
    required: true,
  },
  hasSemiStructured: {
    type: Boolean,
    default: false,
  },
});

const formRef = ref();
const knowledgeBaseList = ref([]);
const categoryOptions = ref([]);
const form = reactive({
  knowledgeBaseId: Number(props.knowledgeBaseId),
  knowledgeBaseName: "",
  categoryId: null,
  categoryName: "",
  description: "",
  jsonStyle: "",
});

const jsonStyleOptions = [
  { value: "Alpaca", label: "Alpaca" },
  { value: "ShareGPT", label: "ShareGPT" },
  { value: "Multilingual Thinking", label: "Multilingual Thinking" },
];

const rules = computed(() => ({
  knowledgeBaseId: [
    { required: true, message: "所属知识库不能为空", trigger: "change" },
  ],
  categoryId: [
    { required: true, message: "所属分类不能为空", trigger: "change" },
  ],
  jsonStyle: props.hasSemiStructured
    ? [{ required: true, message: "请选择数据风格", trigger: "change" }]
    : [],
}));

watch(
  () => props.hasSemiStructured,
  (value) => {
    if (!value) form.jsonStyle = "";
    formRef.value?.clearValidate("jsonStyle");
  }
);

function findCategory(nodes, id) {
  for (const node of nodes || []) {
    if (String(node.id) === String(id)) return node;
    const child = findCategory(node.children, id);
    if (child) return child;
  }
  return null;
}

function handleCategoryChange(value) {
  form.categoryName = findCategory(categoryOptions.value, value)?.label || "";
}

async function loadOptions() {
  const [knowledgeResponse, categoryResponse] = await Promise.all([
    getKmcKnowledgeBaseList(),
    kmcCategoryTree({ knowledgeBaseId: Number(props.knowledgeBaseId) }),
  ]);
  knowledgeBaseList.value = knowledgeResponse?.data || [];
  categoryOptions.value = categoryResponse?.data || [];
  const knowledgeBase = knowledgeBaseList.value.find(
    (item) => String(item.id) === String(props.knowledgeBaseId)
  );
  form.knowledgeBaseName = knowledgeBase?.name || "";
}

function validate() {
  return formRef.value?.validate();
}

function getFormData() {
  return { ...form };
}

onMounted(loadOptions);

defineExpose({ validate, getFormData });
</script>

<style scoped lang="scss">
.storage-document-form {
  width: 100%;
  padding: 0 90px 0 15px;
  box-sizing: border-box;

  :deep(.el-form-item) {
    margin-bottom: 16px;
  }

  :deep(.el-select),
  :deep(.el-tree-select),
  :deep(.el-input) {
    width: 100%;
  }
}

.json-style-field {
  width: 100%;
}

.field-tip {
  display: flex;
  align-items: center;
  margin-top: 6px;
  color: #909399;
  font-size: 12px;
  line-height: 18px;

  .el-icon {
    margin-right: 4px;
  }
}
</style>
