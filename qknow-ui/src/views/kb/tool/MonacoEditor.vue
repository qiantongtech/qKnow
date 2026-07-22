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
  <div ref="editorContainer" class="editor"></div>
</template>

<script setup>
import {ref, onMounted, onUnmounted} from 'vue'
import * as monaco from 'monaco-editor'

const editorContainer = ref(null)

// 模拟：后端返回的 Java 类元数据（你可以换成动态获取）
let classMeta = {};

let editor = null

let variableMap = {}; // 变量名映射 变量名 → 类名
const lineToVariables = {};// 行号 → 该行定义的变量
const VAR_REGEX = /^\s*([A-Z][a-zA-Z0-9_]*)\s+([a-z][a-zA-Z0-9_]*)\s*=/;// 正则：提取 类名 变量名 =

const props = defineProps({
  code: String
})
// 监听 code 变化，自动设置到编辑器
watchEffect(() => {
  console.log('code change11111111111111111111111111111:', props.code)
  if (editor && props.code) {
    editor.setValue(props.code)
  }
})

// 初始化编辑器
onMounted(() => {
  editor = monaco.editor.create(editorContainer.value, {
    value: props.code,
    language: 'java',       // JAVA 高亮
    theme: 'vs-dark',       // 深色主题
    lineNumbers: 'on',
    minimap: {enabled: false},
    fontSize: 14,
    automaticLayout: true,
  })

})

const getCurrentCode = () => {
  return editor.getValue().trim();
}
onUnmounted(() => {
  editor?.dispose()
})

/**
 * 解析单行代码，提取变量
 */
function parseSingleLine(lineNum, lineText) {
  const match = lineText.match(VAR_REGEX);

  if (!match) return;

  const className = match[1];
  const varName = match[2];

  variableMap[varName] = className;

  // 记录：这一行定义了这个变量
  if (!lineToVariables[lineNum]) lineToVariables[lineNum] = [];
  lineToVariables[lineNum].push(varName);
}

/**
 * 删除某一行对应的所有变量
 */
function clearVariablesByLine(lineNum) {
  const vars = lineToVariables[lineNum];

  if (!vars || vars.length === 0) return;
  // 从内存映射中删除
  vars.forEach(v => delete variableMap[v]);
  // 清空行记录
  lineToVariables[lineNum] = [];
}

/**
 * 从代码中解析所有变量类型
 */
function buildVariableMap(code) {
  const map = {};
  const lines = code.split('\n');

  lines.forEach(line => {
    let match;
    if ((match = VAR_REGEX.exec(line)) !== null) {
      const className = match[1];
      const varName = match[2];
      map[varName] = className;
    }
  });
  variableMap = map; // 覆盖更新
}

/**
 * 从代码中解析所有变量类型
 */
function queryClassMeta() {
  getClassMeta().then(res => {
    classMeta = res.data;
  })

}

// 暴露给父组件
defineExpose({
  getCurrentCode
})
</script>

<style scoped>

.editor {
  width: 100%;
  height: 100%;
  border: 1px solid #ddd;
  border-radius: 6px;
  overflow: hidden;
}
</style>