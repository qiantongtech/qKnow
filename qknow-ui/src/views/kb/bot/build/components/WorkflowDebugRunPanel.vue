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
    <div class="workflow-debug-run-panel">
        <div class="workflow-debug-run-panel__toolbar">
            <div class="workflow-debug-run-panel__title">
                <span class="blue-bar"></span>输入参数
            </div>
            <el-button type="primary" :loading="running" @click="handleRun"> 执行 </el-button>
        </div>

        <el-form class="workflow-debug-run-panel__form" @submit.prevent label-width="100px">
            <el-row :gutter="20">
                <el-col :span="24">
                    <template v-if="fields.length">
                        <el-form-item
                            v-for="field in fields"
                            :key="getFieldKey(field)"
                            :label="getFieldLabel(field)"
                        >
                            <template #label>
                                <DebugOverflowTooltipLabel :text="getFieldLabel(field)" />
                            </template>
                            <el-input
                                v-model="formData[getFieldKey(field)]"
                                :placeholder="`请输入`"
                            />
                        </el-form-item>
                    </template>
                    <div v-else class="workflow-debug-run-panel__empty-hint">
                        当前没有输入变量，请添加。
                    </div>
                </el-col>
            </el-row>
        </el-form>

        <div class="workflow-debug-run-panel__result">
            <div class="workflow-debug-run-panel__result-title">
                <span class="blue-bar"></span>输出结果
            </div>
            <div class="workflow-debug-run-panel__result-content" v-html="resultTextMd"></div>
        </div>
    </div>
</template>

<script setup>
    import { computed, onBeforeUnmount, ref, watch } from 'vue';
    import { ProcessFlow } from '@/api/kb/bot/flow.js';
    import MarkdownIt from 'markdown-it';
    import hljs from 'highlight.js';
    import 'highlight.js/styles/xcode.min.css';
    import DebugOverflowTooltipLabel from './DebugOverflowTooltipLabel.vue';
    const conversationInAbortController = ref(); // 对话进行中 abort 控制器(控制 stream 对话)

    const md = new MarkdownIt({
        html: true, // 允许解析 HTML（可选）
        highlight: function (str, lang) {
            if (lang && hljs.getLanguage(lang)) {
                try {
                    const copyHtml = `<div id="copy" data-copy='${str}' style="position: absolute; right: 10px; top: 5px; color: #fff;cursor: pointer;">复制</div>`;
                    return `<pre style="position: relative;">${copyHtml}<code class="hljs">${
                        hljs.highlight(lang, str, true).value
                    }</code></pre>`;
                } catch (__) {}
            }
            return ``;
        }
    });
    const resultTextMd = computed(() => {
        const startTag = '<think>';
        const endTag = '</think>';
        const content = resultText.value;

        // 如果没有 <think> 标签，直接渲染全部内容
        const startIndex = content.indexOf(startTag);
        if (startIndex === -1) {
            return md.render(content);
        }

        // 有 <think>，则提取 </think> 之后的内容
        const afterStart = content.substring(startIndex + startTag.length);
        const endIndex = afterStart.indexOf(endTag);

        let remainingContent = '';
        if (endIndex !== -1) {
            // 提取 </think> 之后的部分
            remainingContent = afterStart.substring(endIndex + endTag.length);
        } else {
            // 没有闭合标签，可能还在思考中？可以返回空或原内容，按需处理
            remainingContent = ''; // 或者保留 afterStart，看产品需求
        }
        return md.render(remainingContent);
    });
    const props = defineProps({
        fields: {
            type: Array,
            default: () => []
        },
        workflowData: {
            type: Object,
            default: () => ({
                nodes: [],
                edges: []
            })
        },
        beforeRun: {
            type: Function,
            default: null
        }
    });

    const emit = defineEmits(['run']);

    const formData = ref({});
    const resultText = ref('');
    const running = ref(false);

    watch(
        () => props.fields,
        (fields) => {
            const previousValues = formData.value || {};
            const nextValues = {};

            (Array.isArray(fields) ? fields : []).forEach((field) => {
                const fieldKey = getFieldKey(field);

                if (!fieldKey) {
                    return;
                }

                nextValues[fieldKey] = Object.prototype.hasOwnProperty.call(
                    previousValues,
                    fieldKey
                )
                    ? previousValues[fieldKey]
                    : getInitialFieldValue(field);
            });

            formData.value = nextValues;
        },
        {
            immediate: true,
            deep: true
        }
    );

    function getFieldKey(field = {}) {
        return `${field?.name || field?.id || ''}`.trim();
    }

    function getFieldLabel(field = {}) {
        return `${field?.label || field?.name || '未命名变量'}`.trim();
    }

    function getInitialFieldValue(field = {}) {
        return field?.defaultValue === undefined || field?.defaultValue === null
            ? ''
            : `${field.defaultValue}`;
    }

    function buildInputValues() {
        return (Array.isArray(props.fields) ? props.fields : []).reduce((result, field) => {
            const fieldKey = getFieldKey(field);

            if (!fieldKey) {
                return result;
            }

            result[fieldKey] = formData.value[fieldKey] ?? '';
            return result;
        }, {});
    }

    async function handleRun() {
        resultText.value = '';
        const payload = {
            input: buildInputValues(),
            flow: props.workflowData
        };

        if (typeof props.beforeRun === 'function') {
            const canRun = await Promise.resolve(props.beforeRun(payload));

            if (canRun === false) {
                return;
            }
        }
        // 创建 AbortController 实例，以便中止请求
        conversationInAbortController.value = new AbortController();

        running.value = true;

        try {
            emit('run', payload);
            console.log(payload);
            // executeFlow(payload);

            await ProcessFlow.executeFlowStream(
                payload.flow,
                payload.input,
                conversationInAbortController.value,
                async (res) => {
                    // const { code, msg, data } = JSON.parse(res.data);

                    const outer = JSON.parse(res.data.replace(/^data:/, ''));
                    const inner = JSON.parse(outer.data);
                    console.log(outer.code);

                    if (outer.code !== 200) {
                        message.alert(`对话异常! ${msg}`);
                        return;
                    }
                    if (inner.text) {
                        resultText.value += inner.text; // 实时追加
                    }
                },
                (error) => {
                    stopStream();
                    throw error;
                },
                () => {
                    stopStream();
                }
            );
        } finally {
            running.value = false;
        }
    }
    /** 停止 stream 流式调用 */
    const stopStream = async () => {
        // tip：如果 stream 进行中的 message，就需要调用 controller 结束
        if (conversationInAbortController.value) {
            conversationInAbortController.value.abort();
        }
    };

    onBeforeUnmount(() => {
        stopStream();
    });
</script>

<style scoped lang="scss">
    .workflow-debug-run-panel {
        display: flex;
        flex-direction: column;
        gap: 20px;
    }

    .workflow-debug-run-panel__toolbar {
        display: flex;
        align-items: center;
        justify-content: space-between;
        gap: 12px;
    }

    .workflow-debug-run-panel__title,
    .workflow-debug-run-panel__result-title {
        display: inline-flex;
        align-items: center;
        gap: 10px;
        font-size: 16px;
        font-weight: 400;
        color: #111827;
        .blue-bar {
            background-color: #2666fb;
            width: 6px;
            height: 16px;
            // margin-right: 10px;
            border-radius: 10px;
        }

        &::before {
            content: none;
        }
    }

    .workflow-debug-run-panel__form {
        :deep(.el-form-item:last-child) {
            margin-bottom: 0;
        }

        :deep(.el-form-item__label) {
            overflow: hidden;
            color: #111827;
            font-size: 14px;
            font-weight: 600;
        }
    }

    .workflow-debug-run-panel__result {
        display: flex;
        flex-direction: column;
        gap: 12px;
    }

    .workflow-debug-run-panel__result-content {
        min-height: 524px;
        padding: 0px 17px;
        overflow: auto;
        line-height: 1.6;
        color: #111827;
        background-color: #fff;
        border: 1px solid #dcdfe6;
        border-radius: 4px;
    }

    .workflow-debug-run-panel__empty-hint {
        padding: 12px 0;
        font-size: 13px;
        line-height: 1.7;
        color: #6b7280;
    }
</style>
