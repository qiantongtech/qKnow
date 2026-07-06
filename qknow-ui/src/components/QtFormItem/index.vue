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
    <el-form-item v-bind="$attrs">
        <template #default="scope">
            <div class="default-wrap">
                <slot name="default" v-bind="scope || {}" />
                <div class="tip-content" v-if="isString && props.tip">
                    <el-icon> <InfoFilled /> </el-icon>
                    <span v-html="props.tip"></span>
                </div>
            </div>
        </template>

        <template #label="scope">
            <div class="label-wrap">
                <slot name="label" v-bind="scope">
                    {{ scope.label }}
                </slot>
                <el-tooltip
                    v-if="!isString"
                    v-bind="props.tip"
                    :effect="props.tip.effect || 'light'"
                    :placement="props.tip.placement || 'right-start'"
                >
                    <el-icon class="tip-icon"> <InfoFilled /> </el-icon>
                    <template #content v-if="props.tip.custom">
                        <div class="tip-content" v-html="props.tip.content"></div>
                    </template>
                </el-tooltip>
            </div>
        </template>
    </el-form-item>
</template>

<script setup name="QtFromItem">
    import { computed } from 'vue';

    const props = defineProps({
        tip: {
            type: [String, Object],
            default: ''
        }
    });

    const isString = computed(() => {
        return typeof props.tip === 'string';
    });
</script>

<style lang="scss" scoped>
    .default-wrap {
        width: 100%;
        position: relative;

        .tip-content {
            display: flex;
            align-items: center;
            gap: 2px;
            color: #888;
            font-size: 12px;
            line-height: 1.5;
            padding-top: 4px;
            white-space: wrap;
        }
    }

    .label-wrap {
        display: flex;
        align-items: center;
        gap: 2px;
        .el-icon {
            color: #888;
        }
    }

    ::v-deep(.el-form-item__error) {
        padding-top: 6px;
    }

    .el-form-item.is-error {
        padding-bottom: 16px;
        .tip-content {
            display: none;
        }
    }
</style>
