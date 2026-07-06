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
    import { ref, onMounted, onUnmounted } from 'vue';
    import * as monaco from 'monaco-editor';

    const editorContainer = ref(null);
    let editor = null;

    const props = defineProps({
        code: String
    });
    // 监听 code 变化，自动设置到编辑器
    watchEffect(() => {
        if (editor && props.code) {
            editor.setValue(props.code);
        }
    });

    // 初始化编辑器
    onMounted(() => {
        editor = monaco.editor.create(editorContainer.value, {
            value: props.code,
            language: 'java', // JAVA 高亮
            theme: 'vs-dark', // 深色主题
            lineNumbers: 'on',
            minimap: { enabled: true },
            fontSize: 14,
            automaticLayout: true
        });
    });

    const getCurrentCode = () => {
        return editor.getValue().trim();
    };
    onUnmounted(() => {
        editor?.dispose();
    });

    // 暴露给父组件
    defineExpose({
        getCurrentCode
    });
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
