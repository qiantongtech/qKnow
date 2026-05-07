<template>
  <div ref="editorContainer" class="editor"></div>
</template>

<script setup>
import {ref, onMounted, onUnmounted} from 'vue'
import * as monaco from 'monaco-editor'

const editorContainer = ref(null)
let editor = null

const props = defineProps({
  code: String
})
// 监听 code 变化，自动设置到编辑器
watchEffect(() => {
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
    minimap: {enabled: true},
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