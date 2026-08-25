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
      <el-col :span="24">
        <el-form-item label="文件分类" prop="categoryId">
          <el-tree-select
            v-model="form.categoryId"
            :data="categoryOptions"
            :props="{ value: 'id', label: 'name', children: 'children' }"
            value-key="id"
            :validate-event="false"
            placeholder="请选择所属分类"
            check-strictly
            @change="handleCategoryChange"
          />
        </el-form-item>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="24">
        <el-form-item label="文件描述" prop="description">
          <el-input
            v-model="form.description"
            :validate-event="false"
            type="textarea"
            maxlength="1024个字符"
            placeholder="请输入文件描述"
            show-word-limit
          />
        </el-form-item>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="24">
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="form.remark"
            :validate-event="false"
            type="textarea"
            maxlength="512个字符"
            placeholder="请输入备注"
            show-word-limit
          />
        </el-form-item>
      </el-col>
    </el-row>
  </el-form>
</template>

<script setup name="KgStorageDocumentStep">
import { onMounted, reactive, ref } from "vue";
import { getFileTypes } from "@/api/kg/knowledge/document";

const formRef = ref();
const categoryOptions = ref([]);
const form = reactive({
  categoryId: null,
  categoryName: "",
  description: "",
  remark: "",
});

const rules = {
  categoryId: [
    { required: true, message: "所属分类不能为空", trigger: "change" },
  ],
};

function findCategory(nodes, id) {
  for (const node of nodes || []) {
    if (String(node.id) === String(id)) return node;
    const child = findCategory(node.children, id);
    if (child) return child;
  }
  return null;
}

function handleCategoryChange(value) {
  form.categoryName = findCategory(categoryOptions.value, value)?.name || "";
}

async function loadOptions() {
  const categoryResponse = await getFileTypes();
  categoryOptions.value = categoryResponse?.data || [];
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
</style>
