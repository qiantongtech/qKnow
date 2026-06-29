<!--
  /*
 * Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
 *
 * This file is part of qKnow Intelligent Agent Building Platform (Open Source Edition).
 *
 * qKnow is licensed under Apache License 2.0 with additional qKnow terms.
 * You may use qKnow for commercial purposes, but you may not remove, hide,
 * modify, or replace the qKnow logo, copyright notices, license notices,
 * or attribution information without a separate commercial license.
 *
 * White-label use, OEM distribution, rebranding, or presenting qKnow as
 * another product requires separate commercial authorization from
 * Jiangsu Qiantong Technology Co., Ltd.
 *
 * Business License: https://community.qknow.ai/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */
-->
<template>
    <el-tooltip :content="text" placement="top" :disabled="!isOverflowing || !text">
        <span ref="labelRef" class="debug-overflow-tooltip-label">
            {{ text }}
        </span>
    </el-tooltip>
</template>

<script setup>
    import { nextTick, onBeforeUnmount, onMounted, ref, watch } from 'vue';

    const props = defineProps({
        text: {
            type: String,
            default: ''
        }
    });

    const labelRef = ref(null);
    const isOverflowing = ref(false);
    let resizeObserver = null;

    function updateOverflowState() {
        const labelElement = labelRef.value;

        isOverflowing.value = Boolean(
            labelElement && labelElement.scrollWidth > labelElement.clientWidth
        );
    }

    function observeLabelSize() {
        if (resizeObserver || typeof ResizeObserver === 'undefined') {
            return;
        }

        resizeObserver = new ResizeObserver(() => {
            updateOverflowState();
        });

        if (labelRef.value) {
            resizeObserver.observe(labelRef.value);
        }
    }

    onMounted(() => {
        observeLabelSize();
        nextTick(() => {
            updateOverflowState();
        });
    });

    watch(
        () => props.text,
        () => {
            nextTick(() => {
                updateOverflowState();
            });
        },
        {
            immediate: true
        }
    );

    onBeforeUnmount(() => {
        resizeObserver?.disconnect();
        resizeObserver = null;
    });
</script>

<style scoped lang="scss">
    .debug-overflow-tooltip-label {
        display: block;
        width: 100%;
        min-width: 0;
        overflow: hidden;
        white-space: nowrap;
        text-overflow: ellipsis;
        text-align: right;
    }
</style>
