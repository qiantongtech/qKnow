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
  <el-dialog :title="title" v-model="openExport" width="800px" draggable>
    <el-form ref="exportFormRef" :model="form" :rules="rules" label-width="80px">
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="文件格式" prop="fileType">
            <el-select
              v-model="form.fileType"
              filterable
              placeholder="请选择文件格式"
              clearable
              @change="handleChange"
            >
              <el-option
                v-for="item in jsonDict"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="数据风格" prop="jsonStyle">
            <el-select
              v-model="form.jsonStyle"
              filterable
              placeholder="请选择数据风格"
              clearable
              @change="handleChange"
            >
              <el-option
                v-for="item in jsonStyleDict"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="数据示例" prop="exampleData">
            <el-input
              v-model="form.exampleData"
              type="textarea"
              show-word-limit
              autosize
              :readonly="true"
              :disabled="true"
              :rows="3"
            />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button size="mini" @click="cancel">关 闭</el-button>
        <el-button type="primary" size="mini" @click="handleDownload">开始导出</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup name="segmentExportDialog">
import { genJsonFile } from '@/api/kmc/knowledgeSegment/knowledgeSegment';

const { proxy } = getCurrentInstance();
const openExport = ref(false);
const title = ref('导出分段');
const form = ref({ fileType: '', jsonStyle: '', exampleData: '' });
const rules = ref({
  fileType: [{ required: true, message: '文件格式不能为空', trigger: 'change' }],
  jsonStyle: [{ required: true, message: '数据风格不能为空', trigger: 'change' }]
});

const props = defineProps({
  documentIdList: {
    type: Array,
    default: () => []
  },
  idType: {
    type: String,
    default: ''
  }
});

const jsonDict = ref([
  { value: 'json', label: 'json' },
  { value: 'jsonl', label: 'jsonl' }
]);

const jsonStyleDict = ref([
  { value: 'Alpaca', label: 'Alpaca' },
  { value: 'ShareGPT', label: 'ShareGPT' },
  { value: 'Multilingual Thinking', label: 'Multilingual Thinking' }
]);

const exampleDemo = {
  'Alpaca+json': `[
    {
        "instruction": "问题字段",
        "input": "",
        "output": "答案字段",
        "system": ""
    },
    {
        "instruction": "问题字段",
        "input": "",
        "output": "答案字段",
        "system": ""
    }
]`,
  'Alpaca+jsonl': `{"instruction": "问题字段","input": "","output": "答案字段","system": ""}
{"instruction": "问题字段","input": "","output": "答案字段","system": ""}`,
  'ShareGPT+json': `[
  {
    "messages": [
      { "role": "user", "content": "问题" },
      { "role": "assistant", "content": "答案" }
    ]
  }
]`,
  'ShareGPT+jsonl': `{"messages":[{"role":"user","content":"问题"},{"role":"assistant","content":"答案"}]}
{"messages":[{"role":"user","content":"问题"},{"role":"assistant","content":"答案"}]}
`,
  'Multilingual Thinking+json': `[
  {
    "reasoning_language": "English",
    "developer": "",
    "user": "问题",
    "analysis": "thinking",
    "final": "答案",
    "messages": [
      { "content": "", "role": "system", "thinking": null },
      { "content": "问题", "role": "user", "thinking": null },
      { "content": "答案", "role": "assistant", "thinking": "thinking" }
    ]
  }
]`,
  'Multilingual Thinking+jsonl': ` {"reasoning_language": "English","developer": "","user": "问题","analysis": "thinking","final": "答案","messages": [{"content": "","role": "system","thinking": null},{"content": "问题","role": "user","thinking": null},{"content": "答案","role": "assistant","thinking": "thinking"}]}
 {"reasoning_language": "English","developer": "","user": "问题","analysis": "thinking","final": "答案","messages": [{"content": "","role": "system","thinking": null},{"content": "问题","role": "user","thinking": null},{"content": "答案","role": "assistant","thinking": "thinking"}]}`
};

function open() {
  reset();
  openExport.value = true;
}

function cancel() {
  openExport.value = false;
}

function reset() {
  form.value = { fileType: '', jsonStyle: '', exampleData: '' };
  proxy.resetForm('exportFormRef');
}

function handleDownload() {
  proxy.$refs.exportFormRef.validate((valid) => {
    if (valid) {
      const originForm = form.value ?? {};
      const data = {
        fileType: originForm.fileType,
        jsonStyle: originForm.jsonStyle,
        documentIds: props.documentIdList.join(','),
        idType: props.idType
      };
      genJsonFile(data).then((res) => {
        const url = `${import.meta.env.VITE_APP_BASE_API}/docs/kmcDocument/streamDownload?fileId=${encodeURIComponent(res.data)}`;
        const a = document.createElement('a');
        a.href = url;
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);
      });
      cancel();
      proxy.$modal.msgWarning('文件已下载，请注意浏览器下载进度');
    }
  });
}

function handleChange() {
  if (!form.value.fileType || !form.value.jsonStyle) {
    form.value.exampleData = '';
    return;
  }
  const key = form.value.jsonStyle + '+' + form.value.fileType;
  form.value.exampleData = exampleDemo[key];
}

defineExpose({ open });
</script>

<style scoped></style>
