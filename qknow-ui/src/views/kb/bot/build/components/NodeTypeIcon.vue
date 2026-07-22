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
    <span class="workflow-node-type-icon" :style="wrapperStyle">
        <LlmNodeIcon
            v-if="normalizedType === 'llm'"
            class="workflow-node-type-icon__svg workflow-node-type-icon__svg--llm"
        />
        <svg
            v-else-if="normalizedType === 'reply'"
            viewBox="0 0 24 24"
            fill="currentColor"
            class="workflow-node-type-icon__svg"
        >
            <path
                d="M20 2H4c-1.1 0-2 .9-2 2v18l4-4h14c1.1 0 2-.9 2-2V4c0-1.1-.9-2-2-2zm0 14H6l-2 2V4h16v12z"
            />
        </svg>
        <svg
            v-else-if="normalizedType === 'condition'"
            viewBox="0 0 24 24"
            fill="currentColor"
            class="workflow-node-type-icon__svg"
        >
            <path
                d="M19 3H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2zm-7 9h-2V5h2v7zM7 9h2v7H7V9zm10 7h-2V9h2v7z"
            />
        </svg>
        <span v-else-if="normalizedType === 'loop'" class="workflow-node-type-icon__glyph"
            >&#8734;</span
        >
        <span
            v-else-if="normalizedType === 'tool' && normalizedFallback"
            class="workflow-node-type-icon__fallback"
        >
            {{ normalizedFallback }}
        </span>
        <svg
            v-else-if="normalizedType === 'tool'"
            viewBox="0 0 24 24"
            fill="currentColor"
            class="workflow-node-type-icon__svg"
        >
            <path
                d="M20.7 7.3a5.99 5.99 0 0 1-7.95 7.95l-5.02 5.02a1.5 1.5 0 1 1-2.12-2.12l5.02-5.02A6 6 0 0 1 16.7 3.3l-3.06 3.06 1 3 3 1 3.06-3.06z"
            />
        </svg>
        <span v-else class="workflow-node-type-icon__fallback">
            {{ fallback }}
        </span>
    </span>
</template>

<script setup>
    import { computed } from 'vue';
    import LlmNodeIcon from './LlmNodeIcon.vue';

    const props = defineProps({
        type: {
            type: String,
            default: ''
        },
        fallback: {
            type: [String, Number],
            default: ''
        }
    });

    const normalizedType = computed(() => `${props.type || ''}`.trim());
    const normalizedFallback = computed(() => `${props.fallback || ''}`.trim());

    const wrapperStyle = computed(() => {
        if (normalizedType.value === 'llm') {
            return {
                backgroundColor: '#dbeafe'
            };
        }

        if (normalizedType.value === 'reply') {
            return {
                backgroundColor: '#ffedd5',
                color: '#f97316'
            };
        }

        if (normalizedType.value === 'condition') {
            return {
                backgroundColor: '#fef3c7',
                color: '#f59e0b'
            };
        }

        if (normalizedType.value === 'loop') {
            return {
                background: 'linear-gradient(180deg, #14b8a6 0%, #06b6d4 100%)',
                color: '#fff',
                boxShadow: '0 6px 14px rgba(6, 182, 212, 0.18)'
            };
        }

        if (normalizedType.value === 'tool') {
            return {
                backgroundColor: '#ede9fe',
                color: '#7c3aed'
            };
        }

        return {
            backgroundColor: '#f9fafb',
            color: '#111827'
        };
    });
</script>

<style scoped>
    .workflow-node-type-icon {
        display: inline-flex;
        align-items: center;
        justify-content: center;
    }

    .workflow-node-type-icon__svg {
        width: 18px;
        height: 18px;
    }

    .workflow-node-type-icon__svg--llm {
        width: 20px;
        height: 20px;
    }

    .workflow-node-type-icon__glyph {
        font-size: 18px;
        font-weight: 700;
        line-height: 1;
    }

    .workflow-node-type-icon__fallback {
        font-size: 20px;
        line-height: 1;
    }
</style>
